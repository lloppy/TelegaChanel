package com.lloppy.telegachanel.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lloppy.telegachanel.data.local.entity.SpaceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpaceDao {
    @Query("SELECT * FROM spaces ORDER BY created_at DESC")
    fun getAllSpaces(): Flow<List<SpaceEntity>>

    @Query("SELECT * FROM spaces WHERE id = :spaceId")
    suspend fun getSpaceById(spaceId: Long): SpaceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(space: SpaceEntity): Long

    @Query("DELETE FROM spaces WHERE id = :spaceId")
    suspend fun delete(spaceId: Long)
}
