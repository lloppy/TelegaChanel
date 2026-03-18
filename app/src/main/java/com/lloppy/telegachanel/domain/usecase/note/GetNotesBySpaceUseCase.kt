package com.lloppy.telegachanel.domain.usecase.note

import com.lloppy.telegachanel.domain.model.Note
import com.lloppy.telegachanel.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesBySpaceUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(spaceId: Long): Flow<List<Note>> =
        repository.getNotesBySpace(spaceId)
}
