package com.lloppy.telegachanel.domain.usecase.note

import com.lloppy.telegachanel.domain.repository.NoteRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long) = repository.deleteNote(noteId)
}
