package com.lloppy.telegachanel.data.local.mapper

import com.lloppy.telegachanel.data.local.entity.PhotoEntity
import com.lloppy.telegachanel.domain.model.PhotoItem
import com.lloppy.telegachanel.domain.model.PhotoOrientation

fun PhotoEntity.toDomain(): PhotoItem = PhotoItem(
    id = id,
    uri = uri,
    spaceId = spaceId,
    takenAt = takenAt,
    width = width,
    height = height,
    orientation = when {
        width > height -> PhotoOrientation.HORIZONTAL
        height > width -> PhotoOrientation.VERTICAL
        else -> PhotoOrientation.SQUARE
    }
)
