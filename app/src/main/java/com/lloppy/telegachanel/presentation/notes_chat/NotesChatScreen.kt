package com.lloppy.telegachanel.presentation.notes_chat

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lloppy.telegachanel.domain.model.SpaceType
import com.lloppy.telegachanel.presentation.common.components.ChatBubble
import com.lloppy.telegachanel.presentation.common.components.DateHeader
import com.lloppy.telegachanel.presentation.common.components.DeleteConfirmDialog
import com.lloppy.telegachanel.presentation.common.components.EmptyStateView
import com.lloppy.telegachanel.presentation.common.components.InputBar
import com.lloppy.telegachanel.ui.theme.SpaceTheme
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesChatScreen(
    onNavigateBack: () -> Unit,
    viewModel: NotesChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()
    val spaceColors = SpaceTheme.colors
    var deleteNoteId by remember { mutableStateOf<Long?>(null) }

    val isEvent = state.spaceType == SpaceType.EVENT
    val accentColor = if (isEvent) spaceColors.dushaPrimary else spaceColors.razumPrimary
    val bubbleColor = if (isEvent) spaceColors.dushaBubble else spaceColors.razumBubble
    val facetLabel = if (isEvent) "\uD83D\uDC9C Душа \u00B7 События" else "\uD83E\uDDE0 Разум \u00B7 Философия"
    val placeholder = if (isEvent) "Что произошло..." else "Напиши мысль..."

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.onEvent(NotesChatContract.Event.OnImagePicked(it.toString())) }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is NotesChatContract.Effect.ScrollToBottom -> {
                    val totalItems = state.groupedNotes.entries.sumOf { 1 + it.value.size }
                    if (totalItems > 0) listState.animateScrollToItem(totalItems - 1)
                }
                is NotesChatContract.Effect.OpenImagePicker -> {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
                is NotesChatContract.Effect.NavigateBack -> onNavigateBack()
                is NotesChatContract.Effect.ShowError -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = facetLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor
                    )
                    Text(
                        text = state.spaceName,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = { viewModel.onEvent(NotesChatContract.Event.OnBackPressed) },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = accentColor.copy(alpha = 0.1f)
                    )
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = accentColor
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(accentColor.copy(alpha = 0.08f))
        )

        if (state.notes.isEmpty() && !state.isLoading) {
            EmptyStateView(
                icon = Icons.Default.ChatBubble,
                title = "Пока пусто",
                subtitle = if (isEvent) "Запишите первое событие" else "Напишите первую заметку",
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                state.groupedNotes.forEach { (date, notes) ->
                    item(key = "date_$date") {
                        DateHeader(date = date)
                    }
                    items(notes, key = { it.id }) { note ->
                        ChatBubble(
                            text = note.text,
                            timestamp = note.timestamp,
                            imageUri = note.imageUri,
                            bubbleColor = bubbleColor,
                            onLongClick = { deleteNoteId = note.id },
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        InputBar(
            text = state.inputText,
            onTextChanged = { viewModel.onEvent(NotesChatContract.Event.OnInputChanged(it)) },
            onSendClicked = { viewModel.onEvent(NotesChatContract.Event.OnSendClicked) },
            placeholder = placeholder,
            accentColor = accentColor,
            attachedImageUri = state.attachedImageUri,
            onAttachImageClicked = { viewModel.onEvent(NotesChatContract.Event.OnAttachImageClicked) },
            onRemoveImage = { viewModel.onEvent(NotesChatContract.Event.OnRemoveImage) }
        )
    }

    deleteNoteId?.let { id ->
        DeleteConfirmDialog(
            title = "Удалить заметку?",
            message = "Заметка и прикреплённое фото будут удалены безвозвратно",
            onConfirm = {
                viewModel.onEvent(NotesChatContract.Event.OnDeleteNote(id))
                deleteNoteId = null
            },
            onDismiss = { deleteNoteId = null }
        )
    }
}
