package com.lloppy.telegachanel.presentation.photos.folder_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.lloppy.telegachanel.domain.repository.SpaceRepository
import com.lloppy.telegachanel.domain.usecase.photo.GetPhotosBySpaceUseCase
import com.lloppy.telegachanel.domain.usecase.photo.SavePhotoUseCase
import com.lloppy.telegachanel.presentation.common.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPhotosBySpace: GetPhotosBySpaceUseCase,
    private val savePhoto: SavePhotoUseCase,
    private val spaceRepository: SpaceRepository
) : MviViewModel<FolderDetailContract.State, FolderDetailContract.Event, FolderDetailContract.Effect>(
    FolderDetailContract.State(spaceId = savedStateHandle.get<Long>("spaceId") ?: 0)
) {

    init {
        val spaceId = state.value.spaceId
        viewModelScope.launch {
            val space = spaceRepository.getSpaceById(spaceId)
            setState { copy(spaceName = space?.name ?: "") }
        }
        viewModelScope.launch {
            getPhotosBySpace(spaceId).collect { photos ->
                setState { copy(photos = photos, isLoading = false) }
            }
        }
    }

    override fun onEvent(event: FolderDetailContract.Event) {
        when (event) {
            is FolderDetailContract.Event.OnCameraClicked -> {
                setEffect(FolderDetailContract.Effect.OpenCamera(state.value.spaceId))
            }
            is FolderDetailContract.Event.OnGalleryClicked -> {
                setEffect(FolderDetailContract.Effect.OpenGalleryPicker)
            }
            is FolderDetailContract.Event.OnGalleryPhotoPicked -> {
                viewModelScope.launch {
                    try {
                        savePhoto(event.uri, state.value.spaceId, event.width, event.height)
                    } catch (e: Exception) {
                        setEffect(FolderDetailContract.Effect.ShowError(e.message ?: "Ошибка сохранения"))
                    }
                }
            }
            is FolderDetailContract.Event.OnBackPressed -> {
                setEffect(FolderDetailContract.Effect.NavigateBack)
            }
        }
    }
}
