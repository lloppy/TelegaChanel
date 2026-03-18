package com.lloppy.telegachanel.presentation.photos.all_photos

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lloppy.telegachanel.presentation.common.components.DateHeader
import com.lloppy.telegachanel.presentation.common.components.DeleteConfirmDialog
import com.lloppy.telegachanel.presentation.common.components.EmptyStateView
import com.lloppy.telegachanel.ui.theme.SpaceTheme
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun AllPhotosScreen(
    onNavigateToPreview: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: AllPhotosViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spaceColors = SpaceTheme.colors
    var showDeleteConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AllPhotosContract.Effect.NavigateToPreview -> onNavigateToPreview(effect.photoIds)
                is AllPhotosContract.Effect.NavigateBack -> onNavigateBack()
                is AllPhotosContract.Effect.ShowError -> {}
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    if (state.isSelectionMode) {
                        Text(
                            "Выбрано: ${state.selectedPhotoIds.size}",
                            color = spaceColors.teloPrimary
                        )
                    } else {
                        Text("Все фотографии")
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.onEvent(AllPhotosContract.Event.OnBackPressed) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = spaceColors.teloPrimary.copy(alpha = 0.1f)
                        )
                    ) {
                        Icon(
                            if (state.isSelectionMode) Icons.Default.Close
                            else Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = spaceColors.teloPrimary
                        )
                    }
                },
                actions = {
                    if (!state.isSelectionMode) {
                        TextButton(
                            onClick = {
                                // Enter selection mode via long press on any photo
                            }
                        ) {
                            Text(
                                "Выбрать",
                                color = spaceColors.teloPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (state.isSelectionMode)
                        MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            if (state.isSelectionMode && state.selectedPhotoIds.isNotEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { showDeleteConfirm = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Удалить")
                        }
                        Button(
                            onClick = { viewModel.onEvent(AllPhotosContract.Event.OnPreviewSelected) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = spaceColors.teloPrimary,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Preview, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Превью")
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (state.groupedPhotos.isEmpty() && !state.isLoading) {
            EmptyStateView(
                icon = Icons.Default.Photo,
                title = "Нет фотографий",
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                modifier = Modifier.padding(padding)
            ) {
                state.groupedPhotos.forEach { (date, photos) ->
                    item(key = "date_$date") {
                        DateHeader(date = date)
                    }
                    item(key = "photos_$date") {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            verticalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            photos.forEach { photo ->
                                val isSelected = photo.id in state.selectedPhotoIds
                                Box(
                                    modifier = Modifier
                                        .width(115.dp)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(3.dp))
                                        .then(
                                            if (isSelected) Modifier.border(
                                                2.dp,
                                                spaceColors.teloPrimary,
                                                RoundedCornerShape(3.dp)
                                            ) else Modifier
                                        )
                                        .combinedClickable(
                                            onClick = {
                                                if (state.isSelectionMode) {
                                                    viewModel.onEvent(
                                                        AllPhotosContract.Event.OnPhotoToggleSelect(photo.id)
                                                    )
                                                }
                                            },
                                            onLongClick = {
                                                viewModel.onEvent(
                                                    AllPhotosContract.Event.OnPhotoLongPress(photo.id)
                                                )
                                            }
                                        )
                                ) {
                                    AsyncImage(
                                        model = photo.uri,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    spaceColors.teloPrimary.copy(alpha = 0.25f)
                                                )
                                        )
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = spaceColors.teloPrimary,
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .padding(4.dp)
                                                .size(24.dp)
                                                .background(
                                                    MaterialTheme.colorScheme.surface,
                                                    CircleShape
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteConfirm) {
        val count = state.selectedPhotoIds.size
        DeleteConfirmDialog(
            title = "Удалить $count фото?",
            message = "Выбранные фотографии будут удалены безвозвратно",
            onConfirm = {
                viewModel.onEvent(AllPhotosContract.Event.OnDeleteSelected)
                showDeleteConfirm = false
            },
            onDismiss = { showDeleteConfirm = false }
        )
    }
}
