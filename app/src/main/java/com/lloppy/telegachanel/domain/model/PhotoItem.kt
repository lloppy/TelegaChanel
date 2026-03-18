package com.lloppy.telegachanel.domain.model

data class PhotoItem(
    val id: Long = 0,
    val uri: String,
    val spaceId: Long,
    val takenAt: Long,
    val width: Int,
    val height: Int,
    val orientation: PhotoOrientation
)
