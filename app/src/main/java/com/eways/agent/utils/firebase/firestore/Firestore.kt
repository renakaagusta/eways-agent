package com.eways.agent.utils.firebase.firestore

import android.util.Log
import android.view.View
import com.eways.agent.kabarcluster.viewdto.KabarClusterCommentViewDTO
import com.eways.agent.kabarcluster.viewdto.KabarClusterPostViewDTO
import com.eways.agent.utils.date.SLDate
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Source
import com.proyek.infrastructures.kabarcluster.entities.Comment
import com.proyek.infrastructures.kabarcluster.entities.Post
import com.proyek.infrastructures.order.chat.entities.Chat
import java.text.SimpleDateFormat

object Firestore {
    val firestoreInstance =
        FirebaseFirestore.getInstance()

    val chatChannelRef = firestoreInstance.collection("chats")
    val postChannelRef = firestoreInstance.collection("posts")

    var token = ""

    fun ChatListener(orderId: String, onListen: (ArrayList<Chat>) -> Unit): ListenerRegistration {

        return chatChannelRef
            .document(orderId)
            .collection("messages")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = ArrayList<Chat>()
                querySnapshot!!.documents.forEach {
                    items.add(it.toObject(Chat::class.java)!!)
                }

                Log.d("firestorechat", items.toString())

                onListen(items)
            }
    }

    fun PostListener(
        clusterId: String,
        view: View?,
        onListen: (View?, ArrayList<KabarClusterPostViewDTO>) -> Unit
    ): ListenerRegistration {
        firestoreInstance.clearPersistence()
        return postChannelRef
            .document(clusterId)
            .collection("post")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "PostMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = ArrayList<Post>()
                querySnapshot!!.documents.forEach {
                    items.add(it.toObject(Post::class.java)!!)
                }

                val listKabarClusterPostViewDTO = ArrayList<KabarClusterPostViewDTO>()
                items.forEach {
                    val SLDate = SLDate()
                    SLDate.date = SimpleDateFormat("yyyy-MM-dd").parse(it.created_at)
                    var dto = KabarClusterPostViewDTO(
                        it.id!!,
                        it.user_id!!,
                        it.pinned == 1,
                        "",
                        it.content!!,
                        "",
                        SLDate,
                        0
                    )

                    listKabarClusterPostViewDTO.add(
                        dto
                    )
                }
                onListen(view, listKabarClusterPostViewDTO)
            }
    }

    fun CommentListener(
        clusterId: String,
        postId: String,
        onListen: (ArrayList<KabarClusterCommentViewDTO>) -> Unit
    ): ListenerRegistration {
        return postChannelRef
            .document(clusterId)
            .collection("post")
            .document(postId)
            .collection("comments")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "PostMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = ArrayList<Comment>()
                querySnapshot!!.documents.forEach {
                    items.add(it.toObject(Comment::class.java)!!)
                }

                val listKabarClusterCommentViewDTO = ArrayList<KabarClusterCommentViewDTO>()

                items.forEach {
                    Log.d("listCOmment", it.toString())
                    val SLDate = SLDate()
                    SLDate.date = SimpleDateFormat("yyyy-MM-dd").parse(it.created_at)

                    listKabarClusterCommentViewDTO.add(
                        KabarClusterCommentViewDTO(
                            it.id!!,
                            it.user_id!!,
                            it.content!!,
                            SLDate
                        )
                    )
                }
                onListen(listKabarClusterCommentViewDTO)
            }
    }

}