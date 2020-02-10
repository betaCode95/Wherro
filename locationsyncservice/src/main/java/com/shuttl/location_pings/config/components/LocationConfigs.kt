package com.shuttl.location_pings.config.components

import android.os.Parcel
import android.os.Parcelable
import com.shuttl.locations_sync.R

data class LocationConfigs(val minTimeInterval: Int = 10000, // min Time Interval for Location Fetching
                           val minDistanceInterval: Int = 100, // min Distance Interval for Location Fetching
                           val minSyncInterval: Int = 10000, // min Time Interval for Location Syncing
                           val accuracy: Int = 3, // accuracy of Lat-Long in meters, 3 means 110 meter
                           val bufferSize: Int = 100, // number of entries at max can be stored in the Database
                           val batchSize: Int = 10, // number of location entries sent at a time while polling
                           val timeout: Int = 1800000, // time in milliseconds after which we stop the services
                           val xApiKey: String? = "", // xApiKey Auth Key for the URL to function
                           val syncUrl: String? = "", // PUTS the location parameters on this URL
                           val userId: String? = "", // to uniquely identify the user
                           val vehicleNumber: String? = "", // to uniquely identify the trip
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
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())


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
        parcel.writeString(userId)
        parcel.writeString(vehicleNumber)
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
    }

}