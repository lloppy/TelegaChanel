package com.lloppy.telegachanel.domain.model

data class Note(
    val id: Long = 0,
    val text: String,
    val timestamp: Long,
    val spaceId: Long,
    val imageUri: String? = null
)
