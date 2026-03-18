package com.lloppy.telegachanel.presentation.photos.all_photos

import androidx.lifecycle.viewModelScope
import com.lloppy.telegachanel.domain.usecase.photo.DeletePhotosUseCase
import com.lloppy.telegachanel.domain.usecase.photo.GetAllPhotosGroupedByDateUseCase
import com.lloppy.telegachanel.presentation.common.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllPhotosViewModel @Inject constructor(
    private val getAllPhotosGroupedByDate: GetAllPhotosGroupedByDateUseCase,
    private val deletePhotos: DeletePhotosUseCase
) : MviViewModel<AllPhotosContract.State, AllPhotosContract.Event, AllPhotosContract.Effect>(
    AllPhotosContract.State()
) {

    init {
        viewModelScope.launch {
            getAllPhotosGroupedByDate().collect { grouped ->
                setState { copy(groupedPhotos = grouped, isLoading = false) }
            }
        }
    }

    override fun onEvent(event: AllPhotosContract.Event) {
        when (event) {
            is AllPhotosContract.Event.OnPhotoLongPress -> {
                setState {
                    copy(
                        isSelectionMode = true,
                        selectedPhotoIds = setOf(event.photoId)
                    )
                }
            }
            is AllPhotosContract.Event.OnPhotoToggleSelect -> {
                setState {
                    val newSelection = if (event.photoId in selectedPhotoIds) {
                        selectedPhotoIds - event.photoId
                    } else {
                        selectedPhotoIds + event.photoId
                    }
                    copy(
                        selectedPhotoIds = newSelection,
                        isSelectionMode = newSelection.isNotEmpty()
                    )
                }
            }
            is AllPhotosContract.Event.OnCancelSelection -> {
                setState { copy(isSelectionMode = false, selectedPhotoIds = emptySet()) }
            }
            is AllPhotosContract.Event.OnPreviewSelected -> {
                val ids = state.value.selectedPhotoIds.joinToString(",")
                if (ids.isNotEmpty()) {
                    setEffect(AllPhotosContract.Effect.NavigateToPreview(ids))
                }
            }
            is AllPhotosContract.Event.OnDeleteSelected -> {
                viewModelScope.launch {
                    deletePhotos(state.value.selectedPhotoIds.toList())
                    setState { copy(isSelectionMode = false, selectedPhotoIds = emptySet()) }
                }
            }
            is AllPhotosContract.Event.OnBackPressed -> {
                if (state.value.isSelectionMode) {
                    setState { copy(isSelectionMode = false, selectedPhotoIds = emptySet()) }
                } else {
                    setEffect(AllPhotosContract.Effect.NavigateBack)
                }
            }
        }
    }
}
