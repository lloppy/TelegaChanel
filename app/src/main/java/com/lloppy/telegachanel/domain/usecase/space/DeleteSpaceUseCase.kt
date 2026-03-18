package com.lloppy.telegachanel.domain.usecase.space

import com.lloppy.telegachanel.domain.repository.SpaceRepository
import javax.inject.Inject

class DeleteSpaceUseCase @Inject constructor(
    private val repository: SpaceRepository
) {
    suspend operator fun invoke(spaceId: Long) = repository.deleteSpace(spaceId)
}
