package com.shuttl.location_pings.data.model.entity

import android.content.Context
import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "gps_locations")
data class GPSLocation(
    @ColumnInfo(name = "latitude") val latitude: Double = 0.0,
    @ColumnInfo(name = "longitude") val longitude: Double = 0.0,
    @ColumnInfo(name = "accuracy") val accuracy: Float = 0.0f,
    @ColumnInfo(name = "provider") val provider: String = "",
    @PrimaryKey @ColumnInfo(name = "time") val time: String = ""
) {

    companion object {
        fun create(location: Location?): GPSLocation =
            GPSLocation(
                latitude = location?.latitude ?: 0.0,
                longitude = location?.longitude ?: 0.0,
                time = (((location?.time ?: 0)) / 1000).toString(),
                accuracy = location?.accuracy ?: 0.0f,
                provider = location?.provider ?: ""
            )

        fun getLastLocation(context: Context): GPSLocation? {
            val prefs = context.getSharedPreferences("sdk_location_last", 0)
            return Gson().fromJson<GPSLocation>(
                prefs.getString("config", ""),
                GPSLocation::class.java
            )
        }

        fun removeFromSharedPref(context: Context) {
            val prefs = context.getSharedPreferences("sdk_location_last", 0)
            val editor = prefs.edit()
            editor.putString("last_location", "")
            editor.apply()
        }

    }

    fun saveToSharedPref(context: Context) {
        val prefs = context.getSharedPreferences("sdk_location_last", 0)
        val editor = prefs.edit()
        editor.putString("last_location", Gson().toJson(this))
        editor.apply()
    }

}