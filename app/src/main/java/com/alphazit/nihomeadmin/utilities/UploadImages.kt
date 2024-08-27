package com.alphazit.nihomeadmin.utilities

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID

suspend fun uploadImagesToFirebase(
    uris: List<Uri>,
    onUploadComplete: (List<String>) -> Unit,
    onProgressUpdate: (Uri, Float) -> Unit
) {
    val storage = Firebase.storage
    val urls = mutableListOf<String>()

    for (uri in uris) {
        val ref = storage.reference.child("images/${UUID.randomUUID()}.jpg")
        val uploadTask = ref.putFile(uri)

        uploadTask.addOnProgressListener { snapshot ->
            val progress =
                (snapshot.bytesTransferred.toFloat() / snapshot.totalByteCount.toFloat()) * 100
            onProgressUpdate(uri, progress)
        }.await()

        val downloadUrl = ref.downloadUrl.await()
        urls.add(downloadUrl.toString())
    }

    onUploadComplete(urls)
}
