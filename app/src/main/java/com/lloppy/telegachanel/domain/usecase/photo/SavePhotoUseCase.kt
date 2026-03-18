package com.lloppy.telegachanel.domain.usecase.photo

import com.lloppy.telegachanel.domain.model.PhotoItem
import com.lloppy.telegachanel.domain.repository.PhotoRepository
import javax.inject.Inject

class SavePhotoUseCase @Inject constructor(
    private val repository: PhotoRepository
) {
    suspend operator fun invoke(uri: String, spaceId: Long, width: Int, height: Int): PhotoItem =
        repository.savePhoto(uri, spaceId, width, height)
}
