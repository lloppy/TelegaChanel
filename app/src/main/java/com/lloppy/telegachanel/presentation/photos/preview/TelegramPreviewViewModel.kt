package com.lloppy.telegachanel.presentation.photos.preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lloppy.telegachanel.domain.model.PhotoItem
import com.lloppy.telegachanel.domain.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelegramPreviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val photoRepository: PhotoRepository
) : ViewModel() {

    data class State(
        val photos: List<PhotoItem> = emptyList()
    )

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        val photoIdsStr = savedStateHandle.get<String>("photoIds") ?: ""
        val photoIds = photoIdsStr.split(",").mapNotNull { it.toLongOrNull() }.toSet()

        viewModelScope.launch {
            val allPhotos = photoRepository.getAllPhotos().first()
            val selected = allPhotos.filter { it.id in photoIds }
            _state.value = State(photos = selected)
        }
    }
}
