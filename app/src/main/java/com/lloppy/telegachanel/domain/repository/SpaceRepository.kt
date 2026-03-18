package com.lloppy.telegachanel.domain.repository

import com.lloppy.telegachanel.domain.model.Space
import com.lloppy.telegachanel.domain.model.SpaceType
import kotlinx.coroutines.flow.Flow

interface SpaceRepository {
    fun getAllSpaces(): Flow<List<Space>>
    suspend fun getSpaceById(spaceId: Long): Space?
    suspend fun createSpace(name: String, type: SpaceType): Long
    suspend fun deleteSpace(spaceId: Long)
}
