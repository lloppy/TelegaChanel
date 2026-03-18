package com.lloppy.telegachanel.presentation.home

import androidx.lifecycle.viewModelScope
import com.lloppy.telegachanel.data.local.ThemePreferences
import com.lloppy.telegachanel.domain.model.SpaceType
import com.lloppy.telegachanel.domain.model.ThemeMode
import com.lloppy.telegachanel.domain.usecase.space.CreateSpaceUseCase
import com.lloppy.telegachanel.domain.usecase.space.DeleteSpaceUseCase
import com.lloppy.telegachanel.domain.usecase.space.GetAllSpacesUseCase
import com.lloppy.telegachanel.presentation.common.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllSpaces: GetAllSpacesUseCase,
    private val createSpace: CreateSpaceUseCase,
    private val deleteSpace: DeleteSpaceUseCase,
    private val themePreferences: ThemePreferences
) : MviViewModel<HomeContract.State, HomeContract.Event, HomeContract.Effect>(
    HomeContract.State()
) {

    val themeMode = themePreferences.themeMode.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ThemeMode.DARK
    )

    init {
        viewModelScope.launch {
            getAllSpaces().collect { spaces ->
                setState { copy(spaces = spaces, isLoading = false) }
            }
        }
        viewModelScope.launch {
            themePreferences.themeMode.collect { mode ->
                setState { copy(themeMode = mode) }
            }
        }
    }

    override fun onEvent(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.OnCreateSpaceClicked -> {
                setState { copy(showCreateDialog = true, newSpaceName = "", newSpaceType = SpaceType.TEXT) }
            }
            is HomeContract.Event.OnSpaceNameChanged -> {
                setState { copy(newSpaceName = event.name) }
            }
            is HomeContract.Event.OnSpaceTypeChanged -> {
                setState { copy(newSpaceType = event.type) }
            }
            is HomeContract.Event.OnConfirmCreate -> {
                val name = state.value.newSpaceName.trim()
                if (name.isNotEmpty()) {
                    viewModelScope.launch {
                        createSpace(name, state.value.newSpaceType)
                        setState { copy(showCreateDialog = false) }
                    }
                }
            }
            is HomeContract.Event.OnDismissDialog -> {
                setState { copy(showCreateDialog = false) }
            }
            is HomeContract.Event.OnSpaceClicked -> {
                when (event.space.type) {
                    SpaceType.TEXT, SpaceType.EVENT -> setEffect(HomeContract.Effect.NavigateToTextSpace(event.space.id))
                    SpaceType.PHOTO -> setEffect(HomeContract.Effect.NavigateToPhotoSpace(event.space.id))
                }
            }
            is HomeContract.Event.OnDeleteSpace -> {
                viewModelScope.launch { deleteSpace(event.spaceId) }
            }
            is HomeContract.Event.OnAllPhotosClicked -> {
                setEffect(HomeContract.Effect.NavigateToAllPhotos)
            }
            is HomeContract.Event.OnThemeModeChanged -> {
                viewModelScope.launch {
                    themePreferences.setThemeMode(event.mode)
                }
            }
        }
    }
}
