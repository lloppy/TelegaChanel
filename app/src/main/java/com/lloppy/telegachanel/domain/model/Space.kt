package com.lloppy.telegachanel.domain.model

data class Space(
    val id: Long = 0,
    val name: String,
    val type: SpaceType,
    val createdAt: Long,
    val coverUri: String? = null,
    val itemCount: Int = 0,
    val lastNoteText: String? = null,
    val lastActivityAt: Long? = null
)
