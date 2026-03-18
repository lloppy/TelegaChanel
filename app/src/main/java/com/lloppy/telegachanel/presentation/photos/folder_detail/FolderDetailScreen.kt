package com.lloppy.telegachanel.presentation.photos.folder_detail

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lloppy.telegachanel.presentation.common.components.EmptyStateView
import com.lloppy.telegachanel.ui.theme.SpaceTheme
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderDetailScreen(
    onNavigateToCamera: (Long) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: FolderDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val spaceColors = SpaceTheme.colors

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val (w, h) = getImageDimensions(context, it)
            viewModel.onEvent(
                FolderDetailContract.Event.OnGalleryPhotoPicked(
                    uri = it.toString(),
                    width = w,
                    height = h
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is FolderDetailContract.Effect.OpenCamera -> onNavigateToCamera(effect.spaceId)
                is FolderDetailContract.Effect.OpenGalleryPicker -> {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
                is FolderDetailContract.Effect.NavigateBack -> onNavigateBack()
                is FolderDetailContract.Effect.ShowError -> {}
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
                            text = "\uD83D\uDC41 Тело \u00B7 Фотографии",
                            style = MaterialTheme.typography.labelSmall,
                            color = spaceColors.teloPrimary
                        )
                        Text(
                            text = state.spaceName,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.onEvent(FolderDetailContract.Event.OnBackPressed) },
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
                actions = {
                    Text(
                        text = "${state.photos.size} фото",
                        style = MaterialTheme.typography.bodySmall,
                        color = spaceColors.textHint,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = { viewModel.onEvent(FolderDetailContract.Event.OnGalleryClicked) },
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = spaceColors.teloPrimary
                ) {
                    Icon(Icons.Default.Image, contentDescription = "Из галереи")
                }
                FloatingActionButton(
                    onClick = { viewModel.onEvent(FolderDetailContract.Event.OnCameraClicked) },
                    containerColor = spaceColors.teloPrimary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Камера")
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Accent line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(spaceColors.teloPrimary.copy(alpha = 0.08f))
            )

            if (state.photos.isEmpty() && !state.isLoading) {
                EmptyStateView(
                    icon = Icons.Default.Photo,
                    title = "Пока пусто",
                    subtitle = "Сделайте фото камерой или выберите из галереи"
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    items(state.photos, key = { it.id }) { photo ->
                        AsyncImage(
                            model = photo.uri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

private fun getImageDimensions(context: android.content.Context, uri: Uri): Pair<Int, Int> {
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    context.contentResolver.openInputStream(uri)?.use { input ->
        BitmapFactory.decodeStream(input, null, options)
    }
    return Pair(options.outWidth.coerceAtLeast(1), options.outHeight.coerceAtLeast(1))
}
