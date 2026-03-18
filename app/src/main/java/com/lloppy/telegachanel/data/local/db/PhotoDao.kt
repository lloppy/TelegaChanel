package com.lloppy.telegachanel.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lloppy.telegachanel.data.local.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos WHERE space_id = :spaceId ORDER BY taken_at DESC")
    fun getPhotosBySpace(spaceId: Long): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM photos ORDER BY taken_at DESC")
    fun getAllPhotos(): Flow<List<PhotoEntity>>

    @Query("SELECT COUNT(*) FROM photos WHERE space_id = :spaceId")
    suspend fun getPhotoCountBySpace(spaceId: Long): Int

    @Query("SELECT * FROM photos WHERE space_id = :spaceId ORDER BY taken_at DESC LIMIT 1")
    suspend fun getLatestPhotoBySpace(spaceId: Long): PhotoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: PhotoEntity): Long

    @Query("SELECT * FROM photos WHERE id IN (:photoIds)")
    suspend fun getByIds(photoIds: List<Long>): List<PhotoEntity>

    @Query("DELETE FROM photos WHERE id IN (:photoIds)")
    suspend fun deleteByIds(photoIds: List<Long>)
}
