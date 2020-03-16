package com.shuttl.location_pings.data.model.entity

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gps_locations")
data class GPSLocation(
                       @ColumnInfo(name = "latitude") val latitude: Double = 0.0,
                       @ColumnInfo(name = "longitude") val longitude: Double = 0.0,
                       @ColumnInfo(name = "user_id") val user_id: String = "",
                       @ColumnInfo(name = "vehicle_number") val vehicle_number: String = "",
                       @PrimaryKey @ColumnInfo(name = "time") val time: String = "") {

    companion object {
        fun create(location: Location?, user_id: String, vehicle_number: String): GPSLocation = GPSLocation(
                latitude = location?.latitude ?: 0.0,
                longitude = location?.longitude ?: 0.0,
                time = (location?.time ?: 0).toString(),
                user_id = user_id,
                vehicle_number = vehicle_number)
    }
}