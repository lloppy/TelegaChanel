package com.lloppy.telegachanel.presentation.photos.all_photos

import com.lloppy.telegachanel.domain.model.PhotoItem

object AllPhotosContract {
    data class State(
        val groupedPhotos: Map<String, List<PhotoItem>> = emptyMap(),
        val isLoading: Boolean = true,
        val isSelectionMode: Boolean = false,
        val selectedPhotoIds: Set<Long> = emptySet()
    )

    sealed interface Event {
        data class OnPhotoLongPress(val photoId: Long) : Event
        data class OnPhotoToggleSelect(val photoId: Long) : Event
        data object OnCancelSelection : Event
        data object OnPreviewSelected : Event
        data object OnDeleteSelected : Event
        data object OnBackPressed : Event
    }

    sealed interface Effect {
        data class NavigateToPreview(val photoIds: String) : Effect
        data object NavigateBack : Effect
        data class ShowError(val message: String) : Effect
    }
}
