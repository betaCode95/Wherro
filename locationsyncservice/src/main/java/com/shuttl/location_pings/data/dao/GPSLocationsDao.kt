package com.shuttl.location_pings.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shuttl.location_pings.data.model.entity.GPSLocation

@Dao
interface GPSLocationsDao {

    @Insert
    fun addLocation(location: GPSLocation)

    @Query("SELECT * FROM gps_locations")
    fun locations(): List<GPSLocation>

    @Query("DELETE FROM gps_locations")
    fun clearLocations()

    @Query("SELECT COUNT(*) FROM gps_locations")
    fun getRowsCount(): Int

    @Query("DELETE FROM gps_locations WHERE timestamp < :lastTimestamp ")
    fun deleteEntries(lastTimestamp: String)

}