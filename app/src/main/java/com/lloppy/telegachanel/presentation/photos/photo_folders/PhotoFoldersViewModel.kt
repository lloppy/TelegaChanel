package com.lloppy.telegachanel.presentation.photos.photo_folders

import androidx.lifecycle.viewModelScope
import com.lloppy.telegachanel.domain.model.SpaceType
import com.lloppy.telegachanel.domain.usecase.space.CreateSpaceUseCase
import com.lloppy.telegachanel.domain.usecase.space.DeleteSpaceUseCase
import com.lloppy.telegachanel.domain.usecase.space.GetAllSpacesUseCase
import com.lloppy.telegachanel.presentation.common.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoFoldersViewModel @Inject constructor(
    private val getAllSpaces: GetAllSpacesUseCase,
    private val createSpace: CreateSpaceUseCase,
    private val deleteSpace: DeleteSpaceUseCase
) : MviViewModel<PhotoFoldersContract.State, PhotoFoldersContract.Event, PhotoFoldersContract.Effect>(
    PhotoFoldersContract.State()
) {

    init {
        viewModelScope.launch {
            getAllSpaces().collect { allSpaces ->
                val photoSpaces = allSpaces.filter { it.type == SpaceType.PHOTO }
                setState { copy(folders = photoSpaces, isLoading = false) }
            }
        }
    }

    override fun onEvent(event: PhotoFoldersContract.Event) {
        when (event) {
            is PhotoFoldersContract.Event.OnCreateClicked -> {
                setState { copy(showCreateDialog = true, newFolderName = "") }
            }
            is PhotoFoldersContract.Event.OnFolderNameChanged -> {
                setState { copy(newFolderName = event.name) }
            }
            is PhotoFoldersContract.Event.OnConfirmCreate -> {
                val name = state.value.newFolderName.trim()
                if (name.isNotEmpty()) {
                    viewModelScope.launch {
                        createSpace(name, SpaceType.PHOTO)
                        setState { copy(showCreateDialog = false) }
                    }
                }
            }
            is PhotoFoldersContract.Event.OnDismissDialog -> {
                setState { copy(showCreateDialog = false) }
            }
            is PhotoFoldersContract.Event.OnFolderClicked -> {
                setEffect(PhotoFoldersContract.Effect.NavigateToFolder(event.space.id))
            }
            is PhotoFoldersContract.Event.OnDeleteFolder -> {
                viewModelScope.launch { deleteSpace(event.spaceId) }
            }
            is PhotoFoldersContract.Event.OnAllPhotosClicked -> {
                setEffect(PhotoFoldersContract.Effect.NavigateToAllPhotos)
            }
            is PhotoFoldersContract.Event.OnBackPressed -> {
                setEffect(PhotoFoldersContract.Effect.NavigateBack)
            }
        }
    }
}
