package com.lloppy.telegachanel.domain.usecase.space

import com.lloppy.telegachanel.domain.model.SpaceType
import com.lloppy.telegachanel.domain.repository.SpaceRepository
import javax.inject.Inject

class CreateSpaceUseCase @Inject constructor(
    private val repository: SpaceRepository
) {
    suspend operator fun invoke(name: String, type: SpaceType): Long =
        repository.createSpace(name, type)
}
