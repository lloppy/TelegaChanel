package com.lloppy.telegachanel.presentation.home

import com.lloppy.telegachanel.domain.model.Space
import com.lloppy.telegachanel.domain.model.ThemeMode

object HomeContract {
    data class State(
        val spaces: List<Space> = emptyList(),
        val isLoading: Boolean = true,
        val themeMode: ThemeMode = ThemeMode.DARK
    )

    sealed interface Event {
        data class OnThemeModeChanged(val mode: ThemeMode) : Event
    }

    sealed interface Effect
}
