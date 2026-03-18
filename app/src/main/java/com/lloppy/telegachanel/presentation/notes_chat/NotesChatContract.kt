package com.lloppy.telegachanel.presentation.notes_chat

import com.lloppy.telegachanel.domain.model.Note
import com.lloppy.telegachanel.domain.model.SpaceType

object NotesChatContract {
    data class State(
        val spaceId: Long = 0,
        val spaceName: String = "",
        val spaceType: SpaceType = SpaceType.TEXT,
        val notes: List<Note> = emptyList(),
        val groupedNotes: Map<String, List<Note>> = emptyMap(),
        val inputText: String = "",
        val attachedImageUri: String? = null,
        val isLoading: Boolean = true
    )

    sealed interface Event {
        data class OnInputChanged(val text: String) : Event
        data object OnSendClicked : Event
        data class OnDeleteNote(val noteId: Long) : Event
        data object OnAttachImageClicked : Event
        data class OnImagePicked(val uri: String) : Event
        data object OnRemoveImage : Event
        data object OnBackPressed : Event
    }

    sealed interface Effect {
        data object ScrollToBottom : Effect
        data object OpenImagePicker : Effect
        data object NavigateBack : Effect
        data class ShowError(val message: String) : Effect
    }
}
