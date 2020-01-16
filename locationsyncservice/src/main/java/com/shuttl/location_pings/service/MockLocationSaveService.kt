package com.shuttl.location_pings.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import com.shuttl.location_pings.config.components.LocationsDB
import com.shuttl.location_pings.custom.notification
import com.shuttl.location_pings.data.model.entity.GPSLocation
import com.shuttl.location_pings.data.repo.LocationRepo
import com.shuttl.location_pings.mockLocation.MockLocationProvider
import com.shuttl.locations_sync.BuildConfig

class MockLocationSaveService : Service() {

    private val locManager by lazy { applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    private val repo by lazy { LocationRepo(LocationsDB.create(applicationContext)?.locationsDao()) }

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        Log.d("MockLocationSaveService", "onCreate")
        startForeground(1, notification(this, "Updating mock location details..."))
        work()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun work(){
        val mMockLocationProvider: MockLocationProvider = MockLocationProvider(applicationContext)
        mMockLocationProvider.addMockLocationProvider(applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager, applicationContext)
    }

    fun saveMockLocationInDB(location: Location?){
        if (location != null) {
            Log.d("LocationSave ", " latitude = " + location.getLatitude())
            Log.d("LocationSave ", " lng = " + location.getLongitude())
        }
        repo.addLocation(GPSLocation.create(location))
    }



}
