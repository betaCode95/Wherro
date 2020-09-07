

# Wherro

![Build Status](https://travis-ci.org/ChuckerTeam/chucker.svg?branch=master) ![License](https://img.shields.io/github/license/ChuckerTeam/Chucker.svg) [![PRs Welcome](https://img.shields.io/badge/PRs-welcome-orange.svg)](http://makeapullrequest.com)

A module that helps in syncing locations

* [Getting Started](#getting-started-)
* [Features](#features-)
* [Configure](#configure-)
* [FAQ](#faq-)
* [Contributing](#contributing-)

Wherro simplifies the location syncing to the server.

Apps using Wherro will run 2 services responsible for Location Syncing

## Getting Started 👣

Wherro is distributed through [Bintray](https://bintray.com/deeptolat/LocationSyncService/com.shuttl.locations_sync). To use it you need to add the following **Gradle dependency** to your `build.gradle` file of your android app module (NOT the root file).

```groovy
repositories {
    maven { url "https://jitpack.io" }
}
```

```groovy
dependencies {
  implementation 'com.shuttl:locationsyncservice:0.15'
}
```

To start using Wherro, just put in `LocationConfigs` through `LocationsHelper.initLocationsModule` method:

```kotlin
LocationsHelper.initLocationsModule(app = application, locationConfigs = LocationConfigs(), callback = callback, intent = intent)
```
Following need to be added in the app's `AndroidManifest` root

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

```

Add this to your app's `AndroidManifest` application

```xml
<service android:name="com.shuttl.location_pings.service.LocationPingService" />
<service android:name="com.shuttl.location_pings.service.LocationSaveService" />
```



**That's it!** 🎉 Wherro will now start 2 services corresponding to Location Saving and Location Syncing simultaneously and show 1 notification for the same

## Features 🧰

Don't forget to check the [changelog](https://bintray.com/deeptolat/LocationSyncService/com.shuttl.locations_sync) to have a look at all the changes in the latest version of Wherro.

* Compatible with **OkHTTP 4** | **Retrofit**
* **API >= 16** compatible
* Easy to integrate (just a 2 gradle implementation line)
* Highly Configurable

## Configure 🎨
```kotlin
LocationConfigs(
     val minTimeInterval: Int = 10000, // min Time Interval for Location Fetching
     val minDistanceInterval: Int = 100, // min Distance Interval for Location Fetching
     val minSyncInterval: Int = 10000, // min Time Interval for Location Syncing
     val accuracy: Int = 3, // accuracy of Lat-Long in meters, 3 means 110 meter
     val bufferSize: Int = 100, // number of entries at max can be stored in the Database
     val batchSize: Int = 10, // number of location entries sent at a time while polling
     val timeout: Int = 1800000, // time in milliseconds after which we stop the services
     val xApiKey: String? = "", // xApiKey Auth Key for the URL to function
     val syncUrl: String? = "", // PUTS the location parameters on this URL
     val smallIcon: Int = R.drawable.ic_loc // Notification icon
  )
```
Callbacks
```kotlin
private val callback = object : LocationPingServiceCallback<GPSLocation> {
        override fun afterSyncLocations(locations: List<GPSLocation>?) {
            Log.i(TAG, "afterSyncLocations, number of locations synced: " + locations?.size)
        }

        override fun errorWhileSyncLocations(error: Exception?) {
            Log.i(TAG, "errorWhileSyncLocations" + error?.toString())
        }

        override fun serviceStarted() {
            Log.i(TAG, "serviceStarted")
        }

        override fun serviceStopped() {
            Log.i(TAG, "serviceStopped")
        }

        override fun serviceStoppedManually() {
            Log.i(TAG, "serviceStoppedManually")
            LocationsHelper.stopAndClearAll(application)
        }

        override fun beforeSyncLocations(locations: List<GPSLocation>?): List<GPSLocation> {
            return locations?: emptyList()
        }
    }
```

Request Body JSON in the PUT network call, along with `xApiKey` as Authorization
```json
[
	{
	   //Its Configurable from beforeSyncLocations
	},
	{
	  //Its Configurable from beforeSyncLocations
	}
]
```


## FAQ ❓

* **Why is it not working for me?** - dependency issue maybe, create an issue if it doesn't work
* **Why is Notification not showing up for me?** - some phones have an issue with running services, create an issue if you find such issues
* **Can I modify JSON Request/Params?** - in works, will be updated soon

## Contributing 🤝

**We're looking for contributors! Don't be shy.** 😁 Feel free to open issues/pull requests to help me improve this project.