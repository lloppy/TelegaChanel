package com.lloppy.telegachanel.presentation.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lloppy.telegachanel.domain.model.Space
import com.lloppy.telegachanel.domain.model.SpaceType
import com.lloppy.telegachanel.domain.model.ThemeMode
import com.lloppy.telegachanel.ui.theme.SpaceTheme
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSpacesList: (String) -> Unit,
    onNavigateToPhotoFolders: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val spaceColors = SpaceTheme.colors

    val textSpaces = state.spaces.filter { it.type == SpaceType.TEXT }
    val eventSpaces = state.spaces.filter { it.type == SpaceType.EVENT }
    val photoSpaces = state.spaces.filter { it.type == SpaceType.PHOTO }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Единое Пространство",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ДУША  \u00B7  ОПЫТ  \u00B7  ТЕЛО",
                        style = MaterialTheme.typography.labelMedium,
                        color = spaceColors.textHint
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ThemeToggle(
                        themeMode = state.themeMode,
                        onThemeModeChanged = {
                            viewModel.onEvent(HomeContract.Event.OnThemeModeChanged(it))
                        }
                    )
                }
            }

            // Dusha (inner)
            item {
                FacetCard(
                    emoji = "\uD83D\uDC9C",
                    label = "ДУША",
                    title = "Убеждения",
                    spaces = textSpaces,
                    primaryColor = spaceColors.razumPrimary,
                    containerColor = spaceColors.razumContainer,
                    onClick = { onNavigateToSpacesList(SpaceType.TEXT.name) }
                )
            }

            // Opyt (external)
            item {
                FacetCard(
                    emoji = "\uD83E\uDDE9",
                    label = "ОПЫТ",
                    title = "Рассуждения",
                    spaces = eventSpaces,
                    primaryColor = spaceColors.dushaPrimary,
                    containerColor = spaceColors.dushaContainer,
                    onClick = { onNavigateToSpacesList(SpaceType.EVENT.name) }
                )
            }

            // Telo
            item {
                TeloCard(
                    photoSpaces = photoSpaces,
                    onClick = onNavigateToPhotoFolders
                )
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
private fun FacetCard(
    emoji: String,
    label: String,
    title: String,
    spaces: List<Space>,
    primaryColor: Color,
    containerColor: Color,
    onClick: () -> Unit
) {
    val latestSpace = spaces.maxByOrNull { it.lastActivityAt ?: 0L }
    val lastText = latestSpace?.lastNoteText
    val lastTime = latestSpace?.lastActivityAt

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(primaryColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emoji, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = label, style = MaterialTheme.typography.labelMedium, color = primaryColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (!lastText.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "\u00AB$lastText\u00BB",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light
                    ),
                    color = SpaceTheme.colors.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Text(
                    text = "${spaces.size} пространств",
                    style = MaterialTheme.typography.bodySmall,
                    color = SpaceTheme.colors.textHint
                )
                if (lastTime != null) {
                    Text(
                        text = "  \u00B7  ${formatRelativeTime(lastTime)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SpaceTheme.colors.textHint
                    )
                }
            }
        }
    }
}

@Composable
private fun TeloCard(
    photoSpaces: List<Space>,
    onClick: () -> Unit
) {
    val spaceColors = SpaceTheme.colors
    val totalPhotos = photoSpaces.sumOf { it.itemCount }
    val lastTime = photoSpaces.maxByOrNull { it.lastActivityAt ?: 0L }?.lastActivityAt

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = spaceColors.teloContainer),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(spaceColors.teloPrimary.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "\uD83D\uDC41", fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "ТЕЛО", style = MaterialTheme.typography.labelMedium, color = spaceColors.teloPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Фотографии",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (photoSpaces.any { it.coverUri != null }) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    photoSpaces.filter { it.coverUri != null }.take(5).forEach { space ->
                        AsyncImage(
                            model = space.coverUri,
                            contentDescription = null,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row {
                Text(
                    text = "${photoSpaces.size} папок  \u00B7  $totalPhotos фото",
                    style = MaterialTheme.typography.bodySmall,
                    color = spaceColors.textHint
                )
                if (lastTime != null) {
                    Text(
                        text = "  \u00B7  ${formatRelativeTime(lastTime)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = spaceColors.textHint
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeToggle(
    themeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit
) {
    val modes = listOf(
        ThemeMode.LIGHT to "\u2600\uFE0F Светлая",
        ThemeMode.DARK to "\uD83C\uDF19 Тёмная"
    )

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        modes.forEach { (mode, label) ->
            val isSelected = themeMode == mode
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                label = "themeBg"
            )

            Box(
                modifier = Modifier
                    .background(bgColor, RoundedCornerShape(10.dp))
                    .clickable { onThemeModeChanged(mode) }
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onSurface
                    else SpaceTheme.colors.textHint
                )
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
                now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR) ->
            "Сегодня, ${timeFormat.format(Date(timestamp))}"
        now.get(Calendar.YEAR) == then.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) - then.get(Calendar.DAY_OF_YEAR) == 1 ->
            "Вчера, ${timeFormat.format(Date(timestamp))}"
        else -> {
            val dateFormat = SimpleDateFormat("d MMM", Locale("ru"))
            dateFormat.format(Date(timestamp))
        }
    }
}
