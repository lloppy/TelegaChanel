package com.lloppy.telegachanel.presentation.photos.photo_folders

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lloppy.telegachanel.domain.model.Space
import com.lloppy.telegachanel.presentation.common.components.DeleteConfirmDialog
import com.lloppy.telegachanel.presentation.common.components.EmptyStateView
import com.lloppy.telegachanel.ui.theme.SpaceTheme
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PhotoFoldersScreen(
    onNavigateToFolder: (Long) -> Unit,
    onNavigateToAllPhotos: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: PhotoFoldersViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spaceColors = SpaceTheme.colors
    var deleteFolderId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is PhotoFoldersContract.Effect.NavigateToFolder -> onNavigateToFolder(effect.spaceId)
                is PhotoFoldersContract.Effect.NavigateToAllPhotos -> onNavigateToAllPhotos()
                is PhotoFoldersContract.Effect.NavigateBack -> onNavigateBack()
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "\uD83D\uDC41 ТЕЛО",
                            style = MaterialTheme.typography.labelSmall,
                            color = spaceColors.teloPrimary
                        )
                        Text(
                            text = "Фотографии",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.onEvent(PhotoFoldersContract.Event.OnBackPressed) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = spaceColors.teloPrimary.copy(alpha = 0.1f)
                        )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = spaceColors.teloPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(PhotoFoldersContract.Event.OnCreateClicked) },
                containerColor = spaceColors.teloPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Создать папку")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Accent line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .padding(horizontal = 16.dp)
                    .background(spaceColors.teloPrimary, RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.folders.isEmpty() && !state.isLoading) {
                EmptyStateView(
                    icon = Icons.Default.Photo,
                    title = "Нет папок",
                    subtitle = "Создайте первую папку для фотографий"
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.folders, key = { it.id }) { folder ->
                        FolderCard(
                            folder = folder,
                            onClick = { viewModel.onEvent(PhotoFoldersContract.Event.OnFolderClicked(folder)) },
                            onLongClick = { deleteFolderId = folder.id }
                        )
                    }

                    // "All photos" card — full width
                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(4.dp))
                        AllPhotosCard(
                            totalPhotos = state.folders.sumOf { it.itemCount },
                            onClick = { viewModel.onEvent(PhotoFoldersContract.Event.OnAllPhotosClicked) }
                        )
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    if (state.showCreateDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(PhotoFoldersContract.Event.OnDismissDialog) },
            title = { Text("Новая папка") },
            text = {
                OutlinedTextField(
                    value = state.newFolderName,
                    onValueChange = { viewModel.onEvent(PhotoFoldersContract.Event.OnFolderNameChanged(it)) },
                    placeholder = { Text("Название папки") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onEvent(PhotoFoldersContract.Event.OnConfirmCreate) },
                    enabled = state.newFolderName.isNotBlank()
                ) {
                    Text("Создать")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(PhotoFoldersContract.Event.OnDismissDialog) }) {
                    Text("Отмена")
                }
            }
        )
    }

    deleteFolderId?.let { id ->
        val folderName = state.folders.find { it.id == id }?.name ?: ""
        DeleteConfirmDialog(
            title = "Удалить папку \u00AB$folderName\u00BB?",
            message = "Все фотографии в этой папке будут удалены безвозвратно",
            onConfirm = {
                viewModel.onEvent(PhotoFoldersContract.Event.OnDeleteFolder(id))
                deleteFolderId = null
            },
            onDismiss = { deleteFolderId = null }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FolderCard(
    folder: Space,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
    ) {
        Column {
            // Cover image
            if (folder.coverUri != null) {
                AsyncImage(
                    model = folder.coverUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.2f)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.2f)
                        .background(
                            SpaceTheme.colors.teloContainer,
                            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Photo,
                        contentDescription = null,
                        tint = SpaceTheme.colors.textHint,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = folder.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${folder.itemCount} фото",
                    style = MaterialTheme.typography.labelSmall,
                    color = SpaceTheme.colors.textHint
                )
            }
        }
    }
}

@Composable
private fun AllPhotosCard(
    totalPhotos: Int,
    onClick: () -> Unit
) {
    val spaceColors = SpaceTheme.colors

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = spaceColors.teloContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        spaceColors.teloPrimary.copy(alpha = 0.15f),
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PhotoLibrary,
                    contentDescription = null,
                    tint = spaceColors.teloPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Все фотографии",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$totalPhotos фото \u00B7 по датам",
                    style = MaterialTheme.typography.bodySmall,
                    color = spaceColors.textSecondary
                )
            }
            Text(
                text = "\u2192",
                style = MaterialTheme.typography.titleMedium,
                color = spaceColors.textHint
            )
        }
    }
}
