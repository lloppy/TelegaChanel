package com.lloppy.telegachanel.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lloppy.telegachanel.data.local.entity.NoteEntity
import com.lloppy.telegachanel.data.local.entity.PhotoEntity
import com.lloppy.telegachanel.data.local.entity.SpaceEntity

@Database(
    entities = [SpaceEntity::class, NoteEntity::class, PhotoEntity::class],
    version = 3,
    exportSchema = false
)
abstract class TelegaChanelDatabase : RoomDatabase() {
    abstract fun spaceDao(): SpaceDao
    abstract fun noteDao(): NoteDao
    abstract fun photoDao(): PhotoDao
}
