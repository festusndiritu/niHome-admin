package com.alphazit.nihomeadmin.utilities

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

val auth = FirebaseAuth.getInstance()

fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            // Show success message
            onResult(true, null)
        } else {
            // Handle the error
            onResult(false, task.exception?.message)
        }
    }
}

fun register(name: String, email: String, password: String, onResult: (Boolean, String?) -> Unit) {
    val profileUpdates = userProfileChangeRequest {
        displayName = name
    }
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { registerTask ->
            if (registerTask.isSuccessful) {
                val user = auth.currentUser
                user!!.updateProfile(profileUpdates)
                    .addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            // Show Success with callback
                            onResult(true, null)
                        } else {
                            // Handle the error
                            onResult(false, updateTask.exception?.message)
                        }
                    }

            } else {
                // Handle the error
                onResult(false, registerTask.exception?.message)
            }
        }

}

fun signOut(onResult: (Boolean, String?) -> Unit) {
    try {
        auth.signOut()
        // Notify the calling screen of success
        onResult(true, null)
    } catch (e: Exception) {
        // Notify the calling screen of the error
        onResult(false, e.message)
    }
}

fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit) {
    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Show success
                onResult(true, null)
            } else {
                // Handle the error
                onResult(false, task.exception?.message)
            }
        }
}