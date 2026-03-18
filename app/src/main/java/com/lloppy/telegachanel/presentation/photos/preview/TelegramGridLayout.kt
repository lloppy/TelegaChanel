package com.lloppy.telegachanel.presentation.photos.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lloppy.telegachanel.domain.model.PhotoItem
import com.lloppy.telegachanel.domain.model.PhotoOrientation

private val spacing = 2.dp

@Composable
fun TelegramPhotoGrid(
    photos: List<PhotoItem>,
    modifier: Modifier = Modifier
) {
    when (photos.size) {
        0 -> {}
        1 -> SinglePhoto(photos[0], modifier)
        2 -> TwoPhotos(photos, modifier)
        3 -> ThreePhotos(photos, modifier)
        4 -> FourPhotos(photos, modifier)
        5 -> FivePhotos(photos, modifier)
        6 -> SixPhotos(photos, modifier)
        else -> MultiplePhotos(photos, modifier)
    }
}

@Composable
private fun SinglePhoto(photo: PhotoItem, modifier: Modifier = Modifier) {
    val ratio = if (photo.height > 0) (photo.width.toFloat() / photo.height).coerceIn(0.5f, 1.5f) else 1f
    AsyncImage(
        model = photo.uri,
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(ratio)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun TwoPhotos(photos: List<PhotoItem>, modifier: Modifier = Modifier) {
    val allVertical = photos.all { it.orientation == PhotoOrientation.VERTICAL }
    val allHorizontal = photos.all { it.orientation == PhotoOrientation.HORIZONTAL }

    if (allHorizontal) {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(spacing)) {
            photos.forEach { photo ->
                AsyncImage(
                    model = photo.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.8f)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    } else {
        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing)) {
            photos.forEach { photo ->
                AsyncImage(
                    model = photo.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(0.75f)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun ThreePhotos(photos: List<PhotoItem>, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing)) {
        AsyncImage(
            model = photos[0].uri,
            contentDescription = null,
            modifier = Modifier
                .weight(2f)
                .aspectRatio(0.6f)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            photos.drop(1).forEach { photo ->
                AsyncImage(
                    model = photo.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun FourPhotos(photos: List<PhotoItem>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(spacing)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            photos.take(2).forEach { photo ->
                AsyncImage(
                    model = photo.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            photos.drop(2).forEach { photo ->
                AsyncImage(
                    model = photo.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun FivePhotos(photos: List<PhotoItem>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(spacing)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            photos.take(2).forEach { photo ->
                AsyncImage(
                    model = photo.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1.2f)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            photos.drop(2).forEach { photo ->
                AsyncImage(
                    model = photo.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1.2f)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun SixPhotos(photos: List<PhotoItem>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(spacing)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            photos.take(3).forEach { photo ->
                AsyncImage(
                    model = photo.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            photos.drop(3).forEach { photo ->
                AsyncImage(
                    model = photo.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun MultiplePhotos(photos: List<PhotoItem>, modifier: Modifier = Modifier) {
    val rows = photos.chunked(3)
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(spacing)) {
        rows.forEach { rowPhotos ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing)
            ) {
                rowPhotos.forEach { photo ->
                    AsyncImage(
                        model = photo.uri,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                repeat(3 - rowPhotos.size) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun Box(modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Box(modifier = modifier)
}
