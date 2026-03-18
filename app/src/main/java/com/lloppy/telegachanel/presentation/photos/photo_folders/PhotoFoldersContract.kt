package com.lloppy.telegachanel.presentation.photos.photo_folders

import com.lloppy.telegachanel.domain.model.Space

object PhotoFoldersContract {
    data class State(
        val folders: List<Space> = emptyList(),
        val isLoading: Boolean = true,
        val showCreateDialog: Boolean = false,
        val newFolderName: String = ""
    )

    sealed interface Event {
        data object OnCreateClicked : Event
        data class OnFolderNameChanged(val name: String) : Event
        data object OnConfirmCreate : Event
        data object OnDismissDialog : Event
        data class OnFolderClicked(val space: Space) : Event
        data class OnDeleteFolder(val spaceId: Long) : Event
        data object OnAllPhotosClicked : Event
        data object OnBackPressed : Event
    }

    sealed interface Effect {
        data class NavigateToFolder(val spaceId: Long) : Effect
        data object NavigateToAllPhotos : Effect
        data object NavigateBack : Effect
    }
}
