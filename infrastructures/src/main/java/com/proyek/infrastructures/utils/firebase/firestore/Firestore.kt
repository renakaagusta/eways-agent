package com.proyek.infrastructures.utils.firebase.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object Firestore {
    val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().currentUser?.uid
                ?: throw NullPointerException("UID is null.")}")

    val chatChannelsCollectionRef = firestoreInstance.collection("chatChannel")

    val postCollectionRef = firestoreInstance.collection("posts")

    var id = ""
    var username = ""
    var password = ""
    var phoneNumber = ""
    var token = ""
}