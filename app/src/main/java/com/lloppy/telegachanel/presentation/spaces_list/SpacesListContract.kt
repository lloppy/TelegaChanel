package com.lloppy.telegachanel.presentation.spaces_list

import com.lloppy.telegachanel.domain.model.Space
import com.lloppy.telegachanel.domain.model.SpaceType

object SpacesListContract {
    data class State(
        val facetType: SpaceType = SpaceType.TEXT,
        val spaces: List<Space> = emptyList(),
        val isLoading: Boolean = true,
        val showCreateDialog: Boolean = false,
        val newSpaceName: String = ""
    )

    sealed interface Event {
        data object OnCreateClicked : Event
        data class OnNameChanged(val name: String) : Event
        data object OnConfirmCreate : Event
        data object OnDismissDialog : Event
        data class OnSpaceClicked(val space: Space) : Event
        data class OnDeleteSpace(val spaceId: Long) : Event
        data object OnBackPressed : Event
    }

    sealed interface Effect {
        data class NavigateToChat(val spaceId: Long) : Effect
        data object NavigateBack : Effect
    }
}
