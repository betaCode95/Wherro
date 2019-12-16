package com.shuttl.location_pings.data.model.entity

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gps_locations")
data class GPSLocation(
                       @ColumnInfo(name = "lat") val latitude: Double = 0.0,
                       @ColumnInfo(name = "long") val longitude: Double = 0.0,
                       @ColumnInfo(name = "accuracy") val accuracy: Float = 0.0f,
                       @ColumnInfo(name = "provider") val provider: String = "",
                       @PrimaryKey @ColumnInfo(name = "timestamp") val timestamp: String = "") {

    companion object {
        fun create(location: Location?): GPSLocation = GPSLocation(
                latitude = location?.latitude ?: 0.0,
                longitude = location?.longitude ?: 0.0,
                accuracy = location?.accuracy ?: 0f,
                provider = location?.provider ?: "gps",
                timestamp = (location?.time ?: 0).toString())
    }
}