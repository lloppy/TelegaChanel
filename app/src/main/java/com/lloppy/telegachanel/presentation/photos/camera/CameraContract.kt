package com.lloppy.telegachanel.presentation.photos.camera

object CameraContract {
    data class State(
        val spaceId: Long = 0,
        val isCameraReady: Boolean = false,
        val flashEnabled: Boolean = false,
        val isFrontCamera: Boolean = false
    )

    sealed interface Event {
        data object OnCaptureClicked : Event
        data object OnToggleFlash : Event
        data object OnFlipCamera : Event
        data class OnPhotoCaptured(val uri: String, val width: Int, val height: Int) : Event
        data object OnBackPressed : Event
    }

    sealed interface Effect {
        data object TriggerCapture : Effect
        data object NavigateBack : Effect
        data class ShowError(val message: String) : Effect
    }
}
