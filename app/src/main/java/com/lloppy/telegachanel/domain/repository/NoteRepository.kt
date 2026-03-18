package com.lloppy.telegachanel.domain.repository

import com.lloppy.telegachanel.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotesBySpace(spaceId: Long): Flow<List<Note>>
    suspend fun addNote(note: Note)
    suspend fun deleteNote(noteId: Long)
}
