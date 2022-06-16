package com.joaquincollazoruiz.spacex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joaquincollazoruiz.spacex.data.local.model.LaunchEntity

@Database(entities = [LaunchEntity::class], version = 4)
abstract class AppDB : RoomDatabase() {
    abstract fun launchesDao(): LaunchesDao
}