package com.lloppy.telegachanel.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable data object Home : Screen
    @Serializable data class NotesChat(val spaceId: Long) : Screen
    @Serializable data class PhotoSpace(val spaceId: Long) : Screen
    @Serializable data object AllPhotos : Screen
    @Serializable data class TelegramPreview(val photoIds: String) : Screen
    @Serializable data class Camera(val spaceId: Long) : Screen
}
