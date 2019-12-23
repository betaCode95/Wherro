package com.shuttl.location_pings.config.components

import android.os.Parcel
import android.os.Parcelable

data class LocationConfigs(val minTimeInterval: Int = 10000, // min Time Interval for Location Fetching
                           val minDistanceInterval: Int = 100, // min Distance Interval for Location Fetching
                           val minSyncInterval: Int = 10000, // min Time Interval for Location Syncing
                           val xApiKey: String? = "", // xApiKey Auth Key for the URL to function
                           val syncUrl: String? = "" // PUTS the location parameters on this URL
                                   ) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(minTimeInterval)
        parcel.writeInt(minDistanceInterval)
        parcel.writeInt(minSyncInterval)
        parcel.writeString(xApiKey)
        parcel.writeString(syncUrl)
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