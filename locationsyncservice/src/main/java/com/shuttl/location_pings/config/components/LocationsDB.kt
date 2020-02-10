package com.shuttl.location_pings.config.components

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shuttl.location_pings.data.dao.GPSLocationsDao
import com.shuttl.location_pings.data.model.entity.GPSLocation

@Database(entities = [GPSLocation::class], version = 2, exportSchema = false)
abstract class LocationsDB : RoomDatabase() {

    abstract fun locationsDao(): GPSLocationsDao

    companion object {
        private const val DB_NAME = "locations.db"

        private var roomDB: LocationsDB? = null

        fun create(context: Context): LocationsDB? {
            if (roomDB == null) {
                roomDB = Room.databaseBuilder(context, LocationsDB::class.java, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return roomDB
        }
    }

}