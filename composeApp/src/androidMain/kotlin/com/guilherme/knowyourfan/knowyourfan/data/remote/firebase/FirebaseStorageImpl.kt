package com.guilherme.knowyourfan.knowyourfan.data.remote.firebase

import com.guilherme.knowyourfan.core.domain.StorageError
import com.guilherme.knowyourfan.domain.Error
import com.guilherme.knowyourfan.domain.Result
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirebaseStorageImpl(
    private val storage: com.google.firebase.storage.FirebaseStorage,
) : FirebaseStorage {
    override suspend fun uploadToStorage(imageBytes: ByteArray): Result<String, StorageError.Storage> {
        val storageRef = storage.reference
        val imageRef = storageRef.child(UUID.randomUUID().toString() + ".jpg")

        try {
            imageRef.putBytes(imageBytes).await()
            val foo = imageRef.downloadUrl.await()

            return Result.Success(foo.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error")
            return Result.Error(StorageError.Storage.UNKNOWN)
        }

    }
}