package com.lloppy.telegachanel.presentation.notes_chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.lloppy.telegachanel.domain.model.Note
import com.lloppy.telegachanel.domain.repository.SpaceRepository
import com.lloppy.telegachanel.domain.usecase.note.AddNoteUseCase
import com.lloppy.telegachanel.domain.usecase.note.DeleteNoteUseCase
import com.lloppy.telegachanel.domain.usecase.note.GetNotesBySpaceUseCase
import com.lloppy.telegachanel.presentation.common.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NotesChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getNotesBySpace: GetNotesBySpaceUseCase,
    private val addNote: AddNoteUseCase,
    private val deleteNote: DeleteNoteUseCase,
    private val spaceRepository: SpaceRepository
) : MviViewModel<NotesChatContract.State, NotesChatContract.Event, NotesChatContract.Effect>(
    NotesChatContract.State(spaceId = savedStateHandle.get<Long>("spaceId") ?: 0)
) {

    private val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))

    init {
        val spaceId = state.value.spaceId
        viewModelScope.launch {
            val space = spaceRepository.getSpaceById(spaceId)
            setState { copy(spaceName = space?.name ?: "", spaceType = space?.type ?: com.lloppy.telegachanel.domain.model.SpaceType.TEXT) }
        }
        viewModelScope.launch {
            getNotesBySpace(spaceId).collect { notes ->
                val grouped = notes.groupBy { dateFormat.format(Date(it.timestamp)) }
                setState { copy(notes = notes, groupedNotes = grouped, isLoading = false) }
                if (notes.isNotEmpty()) {
                    setEffect(NotesChatContract.Effect.ScrollToBottom)
                }
            }
        }
    }

    override fun onEvent(event: NotesChatContract.Event) {
        when (event) {
            is NotesChatContract.Event.OnInputChanged -> setState { copy(inputText = event.text) }
            is NotesChatContract.Event.OnSendClicked -> sendNote()
            is NotesChatContract.Event.OnDeleteNote -> {
                viewModelScope.launch { deleteNote(event.noteId) }
            }
            is NotesChatContract.Event.OnAttachImageClicked -> {
                setEffect(NotesChatContract.Effect.OpenImagePicker)
            }
            is NotesChatContract.Event.OnImagePicked -> {
                setState { copy(attachedImageUri = event.uri) }
            }
            is NotesChatContract.Event.OnRemoveImage -> {
                setState { copy(attachedImageUri = null) }
            }
            is NotesChatContract.Event.OnBackPressed -> {
                setEffect(NotesChatContract.Effect.NavigateBack)
            }
        }
    }

    private fun sendNote() {
        val text = state.value.inputText.trim()
        val imageUri = state.value.attachedImageUri
        if (text.isEmpty() && imageUri == null) return

        viewModelScope.launch {
            addNote(
                Note(
                    text = text,
                    timestamp = System.currentTimeMillis(),
                    spaceId = state.value.spaceId,
                    imageUri = imageUri
                )
            )
            setState { copy(inputText = "", attachedImageUri = null) }
        }
    }
}
