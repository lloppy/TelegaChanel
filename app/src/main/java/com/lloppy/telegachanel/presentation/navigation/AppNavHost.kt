package com.lloppy.telegachanel.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lloppy.telegachanel.presentation.home.HomeScreen
import com.lloppy.telegachanel.presentation.notes_chat.NotesChatScreen
import com.lloppy.telegachanel.presentation.photos.all_photos.AllPhotosScreen
import com.lloppy.telegachanel.presentation.photos.camera.CameraScreen
import com.lloppy.telegachanel.presentation.photos.folder_detail.FolderDetailScreen
import com.lloppy.telegachanel.presentation.photos.preview.TelegramPreviewScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onNavigateToTextSpace = { spaceId ->
                    navController.navigate(Screen.NotesChat(spaceId))
                },
                onNavigateToPhotoSpace = { spaceId ->
                    navController.navigate(Screen.PhotoSpace(spaceId))
                },
                onNavigateToAllPhotos = {
                    navController.navigate(Screen.AllPhotos)
                }
            )
        }

        composable<Screen.NotesChat> {
            NotesChatScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Screen.PhotoSpace> {
            FolderDetailScreen(
                onNavigateToCamera = { spaceId ->
                    navController.navigate(Screen.Camera(spaceId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Screen.AllPhotos> {
            AllPhotosScreen(
                onNavigateToPreview = { photoIds ->
                    navController.navigate(Screen.TelegramPreview(photoIds))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Screen.TelegramPreview> {
            TelegramPreviewScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Screen.Camera> {
            CameraScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
