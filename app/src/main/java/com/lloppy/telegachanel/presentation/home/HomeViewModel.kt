package com.lloppy.telegachanel.presentation.home

import androidx.lifecycle.viewModelScope
import com.lloppy.telegachanel.data.local.ThemePreferences
import com.lloppy.telegachanel.domain.model.ThemeMode
import com.lloppy.telegachanel.domain.usecase.space.GetAllSpacesUseCase
import com.lloppy.telegachanel.presentation.common.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllSpaces: GetAllSpacesUseCase,
    private val themePreferences: ThemePreferences
) : MviViewModel<HomeContract.State, HomeContract.Event, HomeContract.Effect>(
    HomeContract.State()
) {

    val themeMode = themePreferences.themeMode

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
            is HomeContract.Event.OnThemeModeChanged -> {
                viewModelScope.launch {
                    themePreferences.setThemeMode(event.mode)
                }
            }
        }
    }
}
