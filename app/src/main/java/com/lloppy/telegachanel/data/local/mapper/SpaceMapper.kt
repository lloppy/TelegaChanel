package com.lloppy.telegachanel.data.local.mapper

import com.lloppy.telegachanel.data.local.entity.SpaceEntity
import com.lloppy.telegachanel.domain.model.Space
import com.lloppy.telegachanel.domain.model.SpaceType

fun SpaceEntity.toDomain(
    coverUri: String? = null,
    itemCount: Int = 0,
    lastNoteText: String? = null,
    lastActivityAt: Long? = null
): Space = Space(
    id = id,
    name = name,
    type = SpaceType.valueOf(type),
    createdAt = createdAt,
    coverUri = coverUri,
    itemCount = itemCount,
    lastNoteText = lastNoteText,
    lastActivityAt = lastActivityAt
)
