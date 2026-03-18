package com.lloppy.telegachanel.domain.usecase.note

import com.lloppy.telegachanel.domain.model.Note
import com.lloppy.telegachanel.domain.repository.NoteRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) = repository.addNote(note)
}
