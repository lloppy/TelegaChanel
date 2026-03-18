package com.lloppy.telegachanel.domain.usecase.photo

import com.lloppy.telegachanel.domain.repository.PhotoRepository
import javax.inject.Inject

class DeletePhotosUseCase @Inject constructor(
    private val repository: PhotoRepository
) {
    suspend operator fun invoke(photoIds: List<Long>) = repository.deletePhotos(photoIds)
}
