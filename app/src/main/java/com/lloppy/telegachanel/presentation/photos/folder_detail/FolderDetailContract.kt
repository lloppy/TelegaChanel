package com.lloppy.telegachanel.presentation.photos.folder_detail

import com.lloppy.telegachanel.domain.model.PhotoItem

object FolderDetailContract {
    data class State(
        val spaceId: Long = 0,
        val spaceName: String = "",
        val photos: List<PhotoItem> = emptyList(),
        val isLoading: Boolean = true
    )

    sealed interface Event {
        data object OnCameraClicked : Event
        data object OnGalleryClicked : Event
        data class OnGalleryPhotoPicked(val uri: String, val width: Int, val height: Int) : Event
        data object OnBackPressed : Event
    }

    sealed interface Effect {
        data class OpenCamera(val spaceId: Long) : Effect
        data object OpenGalleryPicker : Effect
        data object NavigateBack : Effect
        data class ShowError(val message: String) : Effect
    }
}
