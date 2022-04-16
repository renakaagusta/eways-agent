package com.proyek.infrastructures.utils.firebase.firestore.usecases

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.ListenerRegistration
import com.proyek.infrastructures.utils.firebase.firestore.Firestore
import com.proyek.infrastructures.utils.firebase.firestore.entities.Chat
import com.proyek.infrastructures.utils.firebase.firestore.entities.ChatChannel

object Chat {
    fun getOrCreateChatChannel(otherUserId: String,
                               onComplete: (channelId: String) -> Unit) {
        Firestore.currentUserDocRef.collection("engagedChatChannels")
            .document(otherUserId).get().addOnSuccessListener {
                if (it.exists()) {
                    onComplete(it["channelId"] as String)
                    return@addOnSuccessListener
                }

                val currentUserId = Firestore.id

                val newChannel = Firestore.chatChannelsCollectionRef.document()
                newChannel.set(
                    ChatChannel(
                        mutableListOf(currentUserId, otherUserId)
                    )
                )

                Firestore.currentUserDocRef
                    .collection("engagedChatChannels")
                    .document(otherUserId)
                    .set(mapOf("channelId" to newChannel.id))

                Firestore.firestoreInstance.collection("users").document(otherUserId)
                    .collection("engagedChatChannels")
                    .document(currentUserId)
                    .set(mapOf("channelId" to newChannel.id))

                onComplete(newChannel.id)
            }
    }

    fun ChatListener(channelId: String, context: Context,
                                onListen: (ArrayList<Chat>) -> Unit): ListenerRegistration {
        return Firestore.chatChannelsCollectionRef.document(channelId).collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = ArrayList<Chat>()
                querySnapshot!!.documents.forEach {
                    if (it["type"] == "TEXT")
                        items.add(it.toObject(Chat::class.java)!!)
                    else
                        TODO("Add image message.")
                }
                onListen(items)
            }
    }

    fun createChat(message: Chat, channelId: String) {
        Firestore.chatChannelsCollectionRef.document(channelId)
            .collection("messages")
            .add(message)
    }
}