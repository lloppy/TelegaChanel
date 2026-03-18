package com.lloppy.telegachanel.presentation.photos.camera

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.lloppy.telegachanel.domain.usecase.photo.SavePhotoUseCase
import com.lloppy.telegachanel.presentation.common.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val savePhoto: SavePhotoUseCase
) : MviViewModel<CameraContract.State, CameraContract.Event, CameraContract.Effect>(
    CameraContract.State(spaceId = savedStateHandle.get<Long>("spaceId") ?: 0)
) {

    override fun onEvent(event: CameraContract.Event) {
        when (event) {
            is CameraContract.Event.OnCaptureClicked -> {
                setEffect(CameraContract.Effect.TriggerCapture)
            }
            is CameraContract.Event.OnToggleFlash -> {
                setState { copy(flashEnabled = !flashEnabled) }
            }
            is CameraContract.Event.OnFlipCamera -> {
                setState { copy(isFrontCamera = !isFrontCamera) }
            }
            is CameraContract.Event.OnPhotoCaptured -> {
                viewModelScope.launch {
                    try {
                        savePhoto(event.uri, state.value.spaceId, event.width, event.height)
                        setEffect(CameraContract.Effect.NavigateBack)
                    } catch (e: Exception) {
                        setEffect(CameraContract.Effect.ShowError(e.message ?: "Ошибка сохранения"))
                    }
                }
            }
            is CameraContract.Event.OnBackPressed -> {
                setEffect(CameraContract.Effect.NavigateBack)
            }
        }
    }
}
