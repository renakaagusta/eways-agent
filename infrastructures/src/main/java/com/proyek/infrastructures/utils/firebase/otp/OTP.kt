package com.proyek.infrastructures.utils.firebase.otp

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


object OTP {

    lateinit var activity: AppCompatActivity
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var verificationId: String
    lateinit var verificationNumber: String
    lateinit var phoneNumber: String


    fun verificationCallbacks () {
        mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("otp", "verificationCompleted")
                signIn(
                    credential
                )
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("otp", "verificationFailed")
                Log.d("exception", p0.toString())
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                Log.d("otp", "codeSent")
                verificationId = p0.toString()
            }

        }
    }

    fun verify () {
        Log.d("otp", "verify")
        verificationCallbacks()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
                60,
                TimeUnit.SECONDS,
            activity,
            mCallbacks
        )
    }

    fun signIn (credential: PhoneAuthCredential): Int {
        var signInSuccess = 0
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener {
                    task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        signInSuccess = 1
                    }
                }
        return signInSuccess
    }

    fun authenticate () {
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
            verificationId,
            verificationNumber
        )
        signIn(credential)
    }
}
