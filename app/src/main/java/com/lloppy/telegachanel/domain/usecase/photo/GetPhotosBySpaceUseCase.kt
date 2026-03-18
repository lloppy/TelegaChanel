package com.lloppy.telegachanel.domain.usecase.photo

import com.lloppy.telegachanel.domain.model.PhotoItem
import com.lloppy.telegachanel.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosBySpaceUseCase @Inject constructor(
    private val repository: PhotoRepository
) {
    operator fun invoke(spaceId: Long): Flow<List<PhotoItem>> =
        repository.getPhotosBySpace(spaceId)
}
