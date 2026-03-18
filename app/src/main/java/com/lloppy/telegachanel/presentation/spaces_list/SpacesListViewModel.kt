package com.lloppy.telegachanel.presentation.spaces_list

import androidx.lifecycle.SavedStateHandle
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
class SpacesListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAllSpaces: GetAllSpacesUseCase,
    private val createSpace: CreateSpaceUseCase,
    private val deleteSpace: DeleteSpaceUseCase
) : MviViewModel<SpacesListContract.State, SpacesListContract.Event, SpacesListContract.Effect>(
    SpacesListContract.State(
        facetType = SpaceType.valueOf(savedStateHandle.get<String>("facetType") ?: "TEXT")
    )
) {

    init {
        viewModelScope.launch {
            getAllSpaces().collect { allSpaces ->
                val filtered = allSpaces.filter { it.type == state.value.facetType }
                setState { copy(spaces = filtered, isLoading = false) }
            }
        }
    }

    override fun onEvent(event: SpacesListContract.Event) {
        when (event) {
            is SpacesListContract.Event.OnCreateClicked -> {
                setState { copy(showCreateDialog = true, newSpaceName = "") }
            }
            is SpacesListContract.Event.OnNameChanged -> {
                setState { copy(newSpaceName = event.name) }
            }
            is SpacesListContract.Event.OnConfirmCreate -> {
                val name = state.value.newSpaceName.trim()
                if (name.isNotEmpty()) {
                    viewModelScope.launch {
                        createSpace(name, state.value.facetType)
                        setState { copy(showCreateDialog = false) }
                    }
                }
            }
            is SpacesListContract.Event.OnDismissDialog -> {
                setState { copy(showCreateDialog = false) }
            }
            is SpacesListContract.Event.OnSpaceClicked -> {
                setEffect(SpacesListContract.Effect.NavigateToChat(event.space.id))
            }
            is SpacesListContract.Event.OnDeleteSpace -> {
                viewModelScope.launch { deleteSpace(event.spaceId) }
            }
            is SpacesListContract.Event.OnBackPressed -> {
                setEffect(SpacesListContract.Effect.NavigateBack)
            }
        }
    }
}
