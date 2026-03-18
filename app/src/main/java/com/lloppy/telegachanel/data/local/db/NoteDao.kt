package com.lloppy.telegachanel.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lloppy.telegachanel.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes WHERE space_id = :spaceId ORDER BY timestamp ASC")
    fun getNotesBySpace(spaceId: Long): Flow<List<NoteEntity>>

    @Query("SELECT COUNT(*) FROM notes WHERE space_id = :spaceId")
    suspend fun getNoteCountBySpace(spaceId: Long): Int

    @Query("SELECT * FROM notes WHERE space_id = :spaceId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestNoteBySpace(spaceId: Long): NoteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getById(noteId: Long): NoteEntity?

    @Query("SELECT * FROM notes WHERE space_id = :spaceId")
    suspend fun getAllBySpace(spaceId: Long): List<NoteEntity>

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun delete(noteId: Long)
}
