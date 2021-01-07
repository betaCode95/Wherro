package com.shuttl.location_pings.config.components

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.shuttl.locations_sync.R

data class LocationConfigs(
    val minTimeInterval: Int = 10000, // min Time Interval for Location Fetching
    val minDistanceInterval: Int = 100, // min Distance Interval for Location Fetching
    val minSyncInterval: Int = 10000, // min Time Interval for Location Syncing
    val accuracy: Int = 3, // accuracy of Lat-Long in meters, 3 means 110 meter
    val bufferSize: Int = 100, // number of entries at max can be stored in the Database
    val batchSize: Int = 10, // number of location entries sent at a time while polling
    val timeout: Int = 1800000, // time in milliseconds after which we stop the services
    val xApiKey: String? = "", // xApiKey Auth Key for the URL to function
    val syncUrl: String? = "", // PUTS the location parameters on this URL
    val wakeLock: Boolean? = true, // WakeLocks are enabled on service if made true
    val alarm: Boolean? = true, // Alarm Manager
    val canReuseLastLocation: Boolean? = true, // Last Location gets reused for the Sync on every interval, This will make sure that we ping every on every interval
    val smallIcon: Int = R.drawable.ic_loc // Notification icon
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(minTimeInterval)
        parcel.writeInt(minDistanceInterval)
        parcel.writeInt(minSyncInterval)
        parcel.writeInt(accuracy)
        parcel.writeInt(bufferSize)
        parcel.writeInt(batchSize)
        parcel.writeInt(timeout)
        parcel.writeString(xApiKey)
        parcel.writeString(syncUrl)
        parcel.writeValue(wakeLock)
        parcel.writeValue(alarm)
        parcel.writeValue(canReuseLastLocation)
        parcel.writeInt(smallIcon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocationConfigs> {
        override fun createFromParcel(parcel: Parcel): LocationConfigs {
            return LocationConfigs(parcel)
        }

        override fun newArray(size: Int): Array<LocationConfigs?> {
            return arrayOfNulls(size)
        }

        fun getFromLocal(context: Context): LocationConfigs? {
            val prefs = context.getSharedPreferences("sdk_location_configs", 0)
            return Gson().fromJson<LocationConfigs>(prefs.getString("config", ""), LocationConfigs::class.java)
        }
    }

    fun saveToSharedPref(context: Context) {
        val prefs = context.getSharedPreferences("sdk_location_configs", 0)
        val editor = prefs.edit()
        editor.putString("config", Gson().toJson(this))
        editor.apply()
    }

}