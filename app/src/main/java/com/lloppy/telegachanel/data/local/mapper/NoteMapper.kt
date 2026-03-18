package com.lloppy.telegachanel.data.local.mapper

import com.lloppy.telegachanel.data.local.entity.NoteEntity
import com.lloppy.telegachanel.domain.model.Note

fun NoteEntity.toDomain(): Note = Note(
    id = id,
    text = text,
    timestamp = timestamp,
    spaceId = spaceId,
    imageUri = imageUri
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id,
    text = text,
    timestamp = timestamp,
    spaceId = spaceId,
    imageUri = imageUri
)
