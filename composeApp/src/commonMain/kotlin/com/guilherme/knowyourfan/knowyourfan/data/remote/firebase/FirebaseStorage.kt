package com.guilherme.knowyourfan.knowyourfan.data.remote.firebase

import com.guilherme.knowyourfan.core.domain.StorageError
import com.guilherme.knowyourfan.domain.Result

interface FirebaseStorage {

    suspend fun uploadToStorage(imageBytes: ByteArray): Result<String, StorageError.Storage>

}