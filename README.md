<div align="center">
    <h2>molo Android app</h2>
    <p>Local news and events, collected and curated in one place</p>

[![Project Status: Active](https://www.repostatus.org/badges/latest/active.svg)](https://www.repostatus.org/#active)
![This is a research software](https://img.shields.io/badge/research-software-blue?style=plastic)
![License: AGPL-3.0](https://img.shields.io/badge/license-AGPL--3.0-blue)
![Kotlin 2.3](https://img.shields.io/badge/kotlin-2.3-purple)
![Jetpack Compose](https://img.shields.io/badge/jetpack-compose-green)
![Android 9+](https://img.shields.io/badge/android-9%2B%20(API%2028)-green)
</div>

### Quick links

- 🚀 Build and run the app: [Getting started](#getting-started)
- ⚙️ Configuration reference: [Configuration](#configuration)
- 🧩 Project structure: [Project structure](#project-structure)
- 🖥️ Backend (Django, REST API): [zemki/molonews-django-backend](https://github.com/zemki/molonews-django-backend)
- 🤝 Contributing: [CONTRIBUTING.md](CONTRIBUTING.md)

## What is molo?

molo is a free/libre Open Source platform for local information. It collects news
and events from many local sources – newspapers, municipalities, cultural venues,
clubs and initiatives – and makes them available in one place, sorted by region and
topic. Organizations and citizens can also publish their own articles and events.

It's developed by the [ZeMKI (Centre for Media, Communication and Information Research)](https://zemki.uni-bremen.de/)
at the [University of Bremen](https://www.uni-bremen.de/).

This repository contains the **Android app**: a native Kotlin / Jetpack Compose client
for the molo backend. It provides

- a **personalised feed** of local articles and events, filtered by area and topics
- **area selection** (postal codes / towns) and **topic selection** (tags and subtags)
- **search** across articles and events, **bookmarks** and a **source overview**
- **event details** with location, dates and map
- **user accounts** with registration, login and password reset
- **publishing** of articles and events by registered users and organizations
- optional **push notifications** via Firebase Cloud Messaging
- **onboarding**, **profile** and an info section (imprint, privacy policy, terms)

The app talks to the molo backend through its REST API (v4). By default it is
configured for the public instance at `redaktion.molo.news`; see
[Configuration](#configuration) to point it at your own backend.

## Getting started

### Requirements

- Android Studio (current stable release) with Android SDK 36
- JDK 21 (set the Gradle JDK in Android Studio or `JAVA_HOME` for command-line builds)
- a Firebase project with a valid `google-services.json` for this Android app
  (required for the device ID used by core API requests, even when push notifications are disabled)
- access to the `forge` library packages (see [Dependencies](#dependencies))

### Build

```bash
git clone <this repository> molo-android && cd molo-android
cp local.properties.example local.properties   # set sdk.dir and the values described below
# Download google-services.json for news.molo.android from your Firebase project
# and save it as app/google-services.json before building.
./gradlew :app:assembleDebug
```

The debug APK is written to `app/build/outputs/apk/debug/`. A release build
(`./gradlew :app:assembleRelease`) additionally needs a signing keystore, see
[Configuration](#configuration).

## Configuration

**No secrets are part of this repository.** Everything that differs between
installations is read from `local.properties` in the project root (not committed,
see `local.properties.example`) or from environment variables, which is useful in CI.

| `local.properties` key          | Environment variable           | Purpose |
| ------------------------------- | ------------------------------ | ------- |
| `sdk.dir`                       | `ANDROID_HOME`                 | Android SDK location |
| `gpr.user` / `gpr.key`          | `GITHUB_ACTOR` / `GITHUB_TOKEN`| GitHub Packages access for the `forge` libraries (token scope `read:packages`) |
| `molo.maps.apiKey`              | `MOLO_MAPS_API_KEY`            | Google Maps API key for the event map. Restrict it to the package name and signing certificate in the Google Cloud console. Without a key the app still builds, the map stays empty. |
| `molo.keystore.file`            | `MOLO_KEYSTORE_FILE`           | Path to the release keystore (absolute or relative to the project root). If unset, release builds are unsigned. |
| `molo.keystore.storePassword`   | `MOLO_KEYSTORE_STORE_PASSWORD` | Keystore password |
| `molo.keystore.keyAlias`        | `MOLO_KEYSTORE_KEY_ALIAS`      | Key alias |
| `molo.keystore.keyPassword`     | `MOLO_KEYSTORE_KEY_PASSWORD`   | Key password |

**Firebase / device ID / push notifications.** Register an Android app with the
package name `news.molo.android` in your Firebase project, download its
`google-services.json` and place it in `app/`. The file is ignored by Git.
Firebase Installations provides the device ID required by core API requests, so a
valid Firebase configuration is also required for development without push
notifications. `app/google-services.json.example` only illustrates the expected
structure; its placeholder values do not provide a working configuration.
Push notifications are optional and additionally require the backend to be
configured with the matching Firebase credentials.

**Backend URL.** The API base URL is defined as `BASE_URL` in `data/build.gradle.kts`
(per build type). Change it there to connect the app to your own molo backend.

## Project structure

The app follows a clean-architecture layout with one Gradle module per layer and
feature (about 55 modules):

```
app/             Application entry point, navigation host, dependency injection (Koin), push service
build-logic/     Gradle convention plugins shared by all modules
core/
  designsystem/  Theme, colours, typography, reusable Compose components
  navigation/    Screen definitions and navigation graph
  event/         App-wide events
  viewmodel/     Base classes for presentation
  resource/      Strings, drawables and other Android resources
  util/          Helpers
data/            Ktor HTTP client, REST API implementation, encrypted DataStore, repository implementations
domain/
  model/         Domain models (Article, Event, Organization, User, ...)
  repository/    Repository interfaces
  usecase/       Use cases
  provider/      Providers for device data (location, ...)
feature/         One folder per feature, each split into `presentation` (view models, state) and `ui` (Compose screens):
                 feed, article, event, search, bookmark, tags, area, source, organization, profile,
                 auth (login, registration, password reset), onboarding, location, info, posting, picker
```

## Dependencies

The app is built with Kotlin 2.3, Jetpack Compose (BOM 2026.01), Koin, Ktor,
kotlinx.serialization, AndroidX DataStore, Google Play Services (Maps, Location)
and Firebase Cloud Messaging.

Several infrastructure modules (networking, use cases, view models, paging,
navigation helpers) come from the internal library **`forge`**
(`de.convelop.forge:*`), which is distributed through GitHub Packages
(`maven.pkg.github.com/CONVELOP-GbR/forge`). GitHub Packages always requires an
authenticated token, even for public packages. Configure `gpr.user` / `gpr.key` as
described above. If you do not have access to these packages, please get in touch
(see [Contact](#contact)).

## Development

If you have reached this section, chances are high you want to

- build molo for your own region and backend,
- understand the app or hack on a local version,
- improve molo.

Start with [Getting started](#getting-started) and [CONTRIBUTING.md](CONTRIBUTING.md).
Please report security issues privately to [kontakt@molo.news](mailto:kontakt@molo.news)
or via GitHub's private vulnerability reporting, not as public issues.

## Contact

Questions, feedback or interest in running molo in your region:
[kontakt@molo.news](mailto:kontakt@molo.news)

## Licenses

### Software

molo is a free/libre Open Source Software for local news and events.
Copyright (C) 2026 ZeMKI, Universität Bremen

The software is released under the AGPL-3.0 license, which you can read in our
[license file](./LICENSE).

### Third-party components

Third-party libraries are pulled in as dependencies via Gradle and keep their own
licenses. Google Play Services and Firebase SDKs are subject to the
[Google APIs Terms of Service](https://developers.google.com/terms).
