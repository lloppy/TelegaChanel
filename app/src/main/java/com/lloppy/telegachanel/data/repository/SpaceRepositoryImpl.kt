package com.lloppy.telegachanel.data.repository

import com.lloppy.telegachanel.data.local.db.NoteDao
import com.lloppy.telegachanel.data.local.db.PhotoDao
import com.lloppy.telegachanel.data.local.db.SpaceDao
import com.lloppy.telegachanel.data.local.entity.SpaceEntity
import com.lloppy.telegachanel.data.local.mapper.toDomain
import com.lloppy.telegachanel.data.storage.PhotoFileStorage
import com.lloppy.telegachanel.domain.model.Space
import com.lloppy.telegachanel.domain.model.SpaceType
import com.lloppy.telegachanel.domain.repository.SpaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SpaceRepositoryImpl @Inject constructor(
    private val spaceDao: SpaceDao,
    private val noteDao: NoteDao,
    private val photoDao: PhotoDao,
    private val photoFileStorage: PhotoFileStorage
) : SpaceRepository {

    override fun getAllSpaces(): Flow<List<Space>> =
        spaceDao.getAllSpaces().map { spaces ->
            spaces.map { entity ->
                val type = SpaceType.valueOf(entity.type)
                when (type) {
                    SpaceType.TEXT, SpaceType.EVENT -> {
                        val count = noteDao.getNoteCountBySpace(entity.id)
                        val latest = noteDao.getLatestNoteBySpace(entity.id)
                        entity.toDomain(
                            coverUri = latest?.imageUri,
                            itemCount = count,
                            lastNoteText = latest?.text,
                            lastActivityAt = latest?.timestamp
                        )
                    }
                    SpaceType.PHOTO -> {
                        val count = photoDao.getPhotoCountBySpace(entity.id)
                        val latest = photoDao.getLatestPhotoBySpace(entity.id)
                        entity.toDomain(
                            coverUri = latest?.uri,
                            itemCount = count,
                            lastActivityAt = latest?.takenAt
                        )
                    }
                }
            }
        }

    override suspend fun getSpaceById(spaceId: Long): Space? {
        val entity = spaceDao.getSpaceById(spaceId) ?: return null
        val type = SpaceType.valueOf(entity.type)
        return when (type) {
            SpaceType.TEXT, SpaceType.EVENT -> {
                val count = noteDao.getNoteCountBySpace(entity.id)
                val latest = noteDao.getLatestNoteBySpace(entity.id)
                entity.toDomain(
                    itemCount = count,
                    lastNoteText = latest?.text,
                    lastActivityAt = latest?.timestamp
                )
            }
            SpaceType.PHOTO -> {
                val count = photoDao.getPhotoCountBySpace(entity.id)
                val latest = photoDao.getLatestPhotoBySpace(entity.id)
                entity.toDomain(
                    coverUri = latest?.uri,
                    itemCount = count,
                    lastActivityAt = latest?.takenAt
                )
            }
        }
    }

    override suspend fun createSpace(name: String, type: SpaceType): Long =
        spaceDao.insert(
            SpaceEntity(name = name, type = type.name, createdAt = System.currentTimeMillis())
        )

    override suspend fun deleteSpace(spaceId: Long) {
        // Clean up note image files for TEXT/EVENT spaces
        val notes = noteDao.getAllBySpace(spaceId)
        notes.forEach { note ->
            note.imageUri?.let { photoFileStorage.deletePhoto(it) }
        }
        // Clean up photo folder files for PHOTO spaces
        photoFileStorage.deleteFolderPhotos(spaceId)
        // CASCADE delete handles DB rows
        spaceDao.delete(spaceId)
    }
}
