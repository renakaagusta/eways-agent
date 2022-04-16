package com.proyek.infrastructures.utils.firebase.firestore.usecases

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.ListenerRegistration
import com.proyek.infrastructures.utils.firebase.firestore.Firestore.postCollectionRef
import java.util.*
import kotlin.collections.ArrayList

object Comment {
    fun createComment(creatorId: String, createdAt: Date, comment: Comment, onComplete: (commentId: String) -> Unit) {
        val addComment = postCollectionRef.document("$creatorId$createdAt").collection("comments").document()
        addComment.set(comment)
        onComplete(addComment.id)
    }

    fun CommentListener(creatorId: String, createdAt: Date, context: Context,
                        onListen: (ArrayList<Comment>) -> Unit): ListenerRegistration {
        return postCollectionRef.document("$creatorId$createdAt").collection("comments")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "PostListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = ArrayList<Comment>()
                querySnapshot!!.forEach {
                    items.add(it.toObject(Comment::class.java))
                }
                onListen(items)
            }
    }

}