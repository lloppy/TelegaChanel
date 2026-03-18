package com.lloppy.telegachanel.data.repository

import com.lloppy.telegachanel.data.local.db.NoteDao
import com.lloppy.telegachanel.data.local.mapper.toDomain
import com.lloppy.telegachanel.data.local.mapper.toEntity
import com.lloppy.telegachanel.domain.model.Note
import com.lloppy.telegachanel.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getNotesBySpace(spaceId: Long): Flow<List<Note>> =
        noteDao.getNotesBySpace(spaceId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun addNote(note: Note) {
        noteDao.insert(note.toEntity())
    }

    override suspend fun deleteNote(noteId: Long) {
        noteDao.delete(noteId)
    }
}
