package com.lloppy.telegachanel.di

import com.lloppy.telegachanel.data.repository.NoteRepositoryImpl
import com.lloppy.telegachanel.data.repository.PhotoRepositoryImpl
import com.lloppy.telegachanel.data.repository.SpaceRepositoryImpl
import com.lloppy.telegachanel.domain.repository.NoteRepository
import com.lloppy.telegachanel.domain.repository.PhotoRepository
import com.lloppy.telegachanel.domain.repository.SpaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSpaceRepository(impl: SpaceRepositoryImpl): SpaceRepository

    @Binds
    @Singleton
    abstract fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository

    @Binds
    @Singleton
    abstract fun bindPhotoRepository(impl: PhotoRepositoryImpl): PhotoRepository
}
