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
import com.lloppy.telegachanel.presentation.photos.photo_folders.PhotoFoldersScreen
import com.lloppy.telegachanel.presentation.photos.preview.TelegramPreviewScreen
import com.lloppy.telegachanel.presentation.spaces_list.SpacesListScreen

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
                onNavigateToSpacesList = { facetType ->
                    navController.navigate(Screen.SpacesList(facetType))
                },
                onNavigateToPhotoFolders = {
                    navController.navigate(Screen.PhotoFolders)
                }
            )
        }

        composable<Screen.SpacesList> {
            SpacesListScreen(
                onNavigateToChat = { spaceId ->
                    navController.navigate(Screen.NotesChat(spaceId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<Screen.PhotoFolders> {
            PhotoFoldersScreen(
                onNavigateToFolder = { spaceId ->
                    navController.navigate(Screen.PhotoSpace(spaceId))
                },
                onNavigateToAllPhotos = {
                    navController.navigate(Screen.AllPhotos)
                },
                onNavigateBack = { navController.popBackStack() }
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
