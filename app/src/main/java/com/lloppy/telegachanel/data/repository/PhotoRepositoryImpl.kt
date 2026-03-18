package com.lloppy.telegachanel.data.repository

import com.lloppy.telegachanel.data.local.db.PhotoDao
import com.lloppy.telegachanel.data.local.entity.PhotoEntity
import com.lloppy.telegachanel.data.local.mapper.toDomain
import com.lloppy.telegachanel.data.storage.PhotoFileStorage
import com.lloppy.telegachanel.domain.model.PhotoItem
import com.lloppy.telegachanel.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val photoDao: PhotoDao,
    private val photoFileStorage: PhotoFileStorage
) : PhotoRepository {

    override fun getPhotosBySpace(spaceId: Long): Flow<List<PhotoItem>> =
        photoDao.getPhotosBySpace(spaceId).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getAllPhotos(): Flow<List<PhotoItem>> =
        photoDao.getAllPhotos().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun savePhoto(uri: String, spaceId: Long, width: Int, height: Int): PhotoItem {
        val savedUri = photoFileStorage.copyPhotoToFolder(uri, spaceId)
        val entity = PhotoEntity(
            uri = savedUri,
            spaceId = spaceId,
            takenAt = System.currentTimeMillis(),
            width = width,
            height = height
        )
        val id = photoDao.insert(entity)
        return entity.copy(id = id).toDomain()
    }

    override suspend fun deletePhotos(photoIds: List<Long>) {
        val photos = photoDao.getByIds(photoIds)
        photos.forEach { photoFileStorage.deletePhoto(it.uri) }
        photoDao.deleteByIds(photoIds)
    }
}
