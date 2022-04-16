package com.proyek.infrastructures.utils.firebase.firestore.usecases

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.iid.FirebaseInstanceId
import com.proyek.infrastructures.utils.firebase.firestore.Firestore
import com.proyek.infrastructures.utils.firebase.firestore.entities.User

object User {

    fun signIn(id: String, uid: String,token: String) {
        Firestore.firestoreInstance.collection("users")
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener {
                it.forEach{
                    val user = it.toObject(User::class.java)
                    Firestore.id = user.id!!
                    Firestore.token = user.token!!
                    Log.d("user", user.toString())
                }
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                return@addOnFailureListener
            }
    }

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        Firestore.currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                Firestore.id = FirebaseAuth.getInstance().currentUser?.uid!!
                FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                    Firestore.token = it.token
                }
                val newUser =
                    User(
                        Firestore.id, FirebaseAuth.getInstance().currentUser?.uid!!,
                        Firestore.token
                    )
                Firestore.currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            }
            else
                onComplete()
        }
    }

    fun updateCurrentUser(id: String, uid: String, token: String) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (id.isNotBlank()) userFieldMap["id"] = id
        if (uid.isNotBlank()) userFieldMap["uid"] = uid
        if (token.isNotBlank()) userFieldMap["token"] = token
        Firestore.currentUserDocRef.update(userFieldMap)
    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        Firestore.currentUserDocRef.get()
            .addOnSuccessListener {
                onComplete(it.toObject(User::class.java)!!)
            }
    }

    fun addUsersListener(context: Context, onListen: (ArrayList<User>) -> Unit): ListenerRegistration {
        return Firestore.firestoreInstance.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                var users = ArrayList<User>()
                querySnapshot!!.documents.forEach {
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                        users.add(it.toObject(User::class.java)!!)
                }
                onListen(users)
            }
    }
}