package com.lloppy.telegachanel.domain.repository

import com.lloppy.telegachanel.domain.model.PhotoItem
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    fun getPhotosBySpace(spaceId: Long): Flow<List<PhotoItem>>
    fun getAllPhotos(): Flow<List<PhotoItem>>
    suspend fun savePhoto(uri: String, spaceId: Long, width: Int, height: Int): PhotoItem
    suspend fun deletePhotos(photoIds: List<Long>)
}
