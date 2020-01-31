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
                       @ColumnInfo(name = "user_id") val user_id: String = "",
                       @ColumnInfo(name = "booking_id") val booking_id: String = "",
                       @PrimaryKey @ColumnInfo(name = "timestamp") val timestamp: String = "") {

    companion object {
        fun create(location: Location?, user_id: String, booking_id: String): GPSLocation = GPSLocation(
                latitude = location?.latitude ?: 0.0,
                longitude = location?.longitude ?: 0.0,
                accuracy = location?.accuracy ?: 0f,
                provider = location?.provider ?: "gps",
                timestamp = (location?.time ?: 0).toString(),
                user_id = user_id,
                booking_id = booking_id)
    }
}