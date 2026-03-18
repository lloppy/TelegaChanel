package com.lloppy.telegachanel.data.storage

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoFileStorage @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val photosDir: File
        get() = context.getExternalFilesDir("photos") ?: context.filesDir

    fun copyPhotoToFolder(sourceUri: String, folderId: Long): String {
        val folderDir = File(photosDir, folderId.toString()).apply { mkdirs() }
        val destFile = File(folderDir, "${UUID.randomUUID()}.jpg")

        context.contentResolver.openInputStream(Uri.parse(sourceUri))?.use { input ->
            destFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return Uri.fromFile(destFile).toString()
    }

    fun deletePhoto(uri: String) {
        try {
            val file = File(Uri.parse(uri).path ?: return)
            file.delete()
        } catch (_: Exception) {
        }
    }

    fun deleteFolderPhotos(folderId: Long) {
        val folderDir = File(photosDir, folderId.toString())
        folderDir.deleteRecursively()
    }
}
