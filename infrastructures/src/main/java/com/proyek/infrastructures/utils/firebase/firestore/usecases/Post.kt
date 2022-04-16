package com.proyek.infrastructures.utils.firebase.firestore.usecases

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.ListenerRegistration
import com.proyek.infrastructures.utils.firebase.firestore.entities.Post
import com.proyek.infrastructures.utils.firebase.firestore.Firestore.firestoreInstance
import com.proyek.infrastructures.utils.firebase.firestore.Firestore.id
import com.proyek.infrastructures.utils.firebase.firestore.Firestore.postCollectionRef

object Post {
    fun createPost(post: Post, onComplete: (channelId: String) -> Unit) {

        val userDoc = firestoreInstance.document("users/$id")

        val newPost = postCollectionRef.document("${post.creatorId}${post.createdAt?.time}")
        newPost.set(post)

        userDoc
            .collection("post")
            .document(newPost.id)
            .set(mapOf("postId" to newPost.id))

        onComplete(newPost.id)
    }

    fun PostListener(context: Context,
                     onListen: (ArrayList<Post>) -> Unit): ListenerRegistration {
        return postCollectionRef
            .orderBy("createdAt")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "PostListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = ArrayList<Post>()
                querySnapshot!!.documents.forEach {
                    items.add(it.toObject(Post::class.java)!!)
                }
                onListen(items)
            }
    }

}