
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
  implementation(group: 'com.shuttl', name: 'locationsyncservice', version: '0.14', ext: 'aar', classifier: '')
}
```

To start using Wherro, just put in `LocationConfigs` through `LocationsHelper.initLocationsModule` method:

```kotlin
LocationsHelper.initLocationsModule(app = application, locationConfigs = LocationConfigs())
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
LocationConfigs(val minTimeInterval: Int = 10000, // min Time Interval for Location Fetching
  val minDistanceInterval: Int = 100, // min Distance Interval for Location Fetching
  val minSyncInterval: Int = 10000, // min Time Interval for Location Syncing
  val xApiKey: String? = "", // xApiKey Auth Key for the URL to function, if you have one
  val syncUrl: String? = "" // PUTs the location parameters on this URL
  )
```


## FAQ ❓

* **Why is it not working for me?** - dependency issue maybe, create an issue if it doesn't work
* **Why is Notification not showing up for me?** - some phones have an issue with running services, create an issue if you find such issues

## Contributing 🤝

**We're looking for contributors! Don't be shy.** 😁 Feel free to open issues/pull requests to help me improve this project.