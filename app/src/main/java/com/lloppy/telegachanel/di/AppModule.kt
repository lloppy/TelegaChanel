package com.lloppy.telegachanel.di

import android.content.Context
import androidx.room.Room
import com.lloppy.telegachanel.data.local.db.NoteDao
import com.lloppy.telegachanel.data.local.db.PhotoDao
import com.lloppy.telegachanel.data.local.db.SpaceDao
import com.lloppy.telegachanel.data.local.db.TelegaChanelDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TelegaChanelDatabase =
        Room.databaseBuilder(
            context,
            TelegaChanelDatabase::class.java,
            "telega_chanel.db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideSpaceDao(db: TelegaChanelDatabase): SpaceDao = db.spaceDao()

    @Provides
    fun provideNoteDao(db: TelegaChanelDatabase): NoteDao = db.noteDao()

    @Provides
    fun providePhotoDao(db: TelegaChanelDatabase): PhotoDao = db.photoDao()
}
