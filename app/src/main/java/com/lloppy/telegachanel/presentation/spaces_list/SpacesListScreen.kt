package com.lloppy.telegachanel.presentation.spaces_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lloppy.telegachanel.domain.model.Space
import com.lloppy.telegachanel.domain.model.SpaceType
import com.lloppy.telegachanel.presentation.common.components.DeleteConfirmDialog
import com.lloppy.telegachanel.presentation.common.components.EmptyStateView
import com.lloppy.telegachanel.ui.theme.SpaceTheme
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpacesListScreen(
    onNavigateToChat: (Long) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SpacesListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spaceColors = SpaceTheme.colors
    var deleteSpaceId by remember { mutableStateOf<Long?>(null) }

    val isEvent = state.facetType == SpaceType.EVENT
    val accentColor = if (isEvent) spaceColors.dushaPrimary else spaceColors.razumPrimary
    val cardBgColor = if (isEvent) spaceColors.dushaContainer else spaceColors.razumContainer
    val emoji = if (isEvent) "\uD83E\uDDE9" else "\uD83D\uDC9C"
    val label = if (isEvent) "ОПЫТ" else "ДУША"
    val title = if (isEvent) "Рассуждения" else "Убеждения"

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SpacesListContract.Effect.NavigateToChat -> onNavigateToChat(effect.spaceId)
                is SpacesListContract.Effect.NavigateBack -> onNavigateBack()
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
                            text = "$emoji $label",
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor
                        )
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.onEvent(SpacesListContract.Event.OnBackPressed) },
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(SpacesListContract.Event.OnCreateClicked) },
                containerColor = accentColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Создать")
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
                    .background(
                        accentColor,
                        RoundedCornerShape(2.dp)
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.spaces.isEmpty() && !state.isLoading) {
                EmptyStateView(
                    icon = Icons.Default.ChatBubble,
                    title = "Пока пусто",
                    subtitle = "Создайте первое пространство"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 16.dp,
                        vertical = 4.dp
                    )
                ) {
                    items(state.spaces, key = { it.id }) { space ->
                        SpaceCard(
                            space = space,
                            containerColor = cardBgColor,
                            onClick = { viewModel.onEvent(SpacesListContract.Event.OnSpaceClicked(space)) },
                            onLongClick = { deleteSpaceId = space.id }
                        )
                    }
                }
            }
        }
    }

    if (state.showCreateDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(SpacesListContract.Event.OnDismissDialog) },
            title = { Text("Новое пространство") },
            text = {
                OutlinedTextField(
                    value = state.newSpaceName,
                    onValueChange = { viewModel.onEvent(SpacesListContract.Event.OnNameChanged(it)) },
                    placeholder = { Text("Название") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onEvent(SpacesListContract.Event.OnConfirmCreate) },
                    enabled = state.newSpaceName.isNotBlank()
                ) {
                    Text("Создать")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(SpacesListContract.Event.OnDismissDialog) }) {
                    Text("Отмена")
                }
            }
        )
    }

    deleteSpaceId?.let { id ->
        val spaceName = state.spaces.find { it.id == id }?.name ?: ""
        DeleteConfirmDialog(
            title = "Удалить \u00AB$spaceName\u00BB?",
            message = "Все заметки и прикреплённые фото в этом пространстве будут удалены безвозвратно",
            onConfirm = {
                viewModel.onEvent(SpacesListContract.Event.OnDeleteSpace(id))
                deleteSpaceId = null
            },
            onDismiss = { deleteSpaceId = null }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SpaceCard(
    space: Space,
    containerColor: Color,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = space.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (!space.lastNoteText.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\u00AB${space.lastNoteText}\u00BB",
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic),
                    color = SpaceTheme.colors.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row {
                val countLabel = if (space.itemCount == 1) "заметка" else "заметок"
                Text(
                    text = "${space.itemCount} $countLabel",
                    style = MaterialTheme.typography.labelSmall,
                    color = SpaceTheme.colors.textHint
                )
                if (space.lastActivityAt != null) {
                    Text(
                        text = "  \u00B7  ${formatRelativeTime(space.lastActivityAt)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SpaceTheme.colors.textHint
                    )
                }
            }
        }
    }
}

private fun formatRelativeTime(timestamp: Long): String {
    val now = Calendar.getInstance()
    val then = Calendar.getInstance().apply { timeInMillis = timestamp }
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    return when {
        now.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR) -> {
            "Сегодня, ${timeFormat.format(Date(timestamp))}"
        }
        now.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) - then.get(Calendar.DAY_OF_YEAR) == 1 -> {
            "Вчера, ${timeFormat.format(Date(timestamp))}"
        }
        else -> {
            val dateFormat = SimpleDateFormat("d MMM", Locale("ru"))
            dateFormat.format(Date(timestamp))
        }
    }
}
