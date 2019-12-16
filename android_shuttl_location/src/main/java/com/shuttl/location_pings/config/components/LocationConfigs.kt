package com.shuttl.location_pings.config.components

import android.os.Parcel
import android.os.Parcelable

data class LocationConfigs(val minTimeInterval: Int = 10000,
                           val minDistanceInterval: Int = 100,
                           val minSyncInterval: Int = 10000,
                           val xApiKey: String? = "",
                           val syncUrl: String? = "") : Parcelable {

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