package com.lloppy.telegachanel.presentation.home

import com.lloppy.telegachanel.domain.model.Space
import com.lloppy.telegachanel.domain.model.SpaceType
import com.lloppy.telegachanel.domain.model.ThemeMode

object HomeContract {
    data class State(
        val spaces: List<Space> = emptyList(),
        val isLoading: Boolean = true,
        val showCreateDialog: Boolean = false,
        val newSpaceName: String = "",
        val newSpaceType: SpaceType = SpaceType.TEXT,
        val themeMode: ThemeMode = ThemeMode.DARK
    )

    sealed interface Event {
        data object OnCreateSpaceClicked : Event
        data class OnSpaceNameChanged(val name: String) : Event
        data class OnSpaceTypeChanged(val type: SpaceType) : Event
        data object OnConfirmCreate : Event
        data object OnDismissDialog : Event
        data class OnSpaceClicked(val space: Space) : Event
        data class OnDeleteSpace(val spaceId: Long) : Event
        data object OnAllPhotosClicked : Event
        data class OnThemeModeChanged(val mode: ThemeMode) : Event
    }

    sealed interface Effect {
        data class NavigateToTextSpace(val spaceId: Long) : Effect
        data class NavigateToPhotoSpace(val spaceId: Long) : Effect
        data object NavigateToAllPhotos : Effect
        data class ShowError(val message: String) : Effect
    }
}
