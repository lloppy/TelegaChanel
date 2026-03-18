package com.lloppy.telegachanel.domain.usecase.space

import com.lloppy.telegachanel.domain.model.Space
import com.lloppy.telegachanel.domain.repository.SpaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSpacesUseCase @Inject constructor(
    private val repository: SpaceRepository
) {
    operator fun invoke(): Flow<List<Space>> = repository.getAllSpaces()
}
