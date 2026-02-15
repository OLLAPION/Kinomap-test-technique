# Kinomap – Android Technical Test

Android application developed as part of a technical test for Kinomap.

The app displays a list of badges, allows filtering by category, and includes a master/detail UI on tablets.
Authentication is handled via Firebase (Google Sign-In).

---

## Tech stack

- **Kotlin**
- **Jetpack Compose**
- **MVVM + Clean Architecture**
- **Hilt (Dependency Injection)**
- **Coroutines & Flow**
- **Room**
- **Firebase Authentication (Google Sign-In)**
- **Glide (images)**

---

## Features

- Google authentication
- Badge list with category filtering
- Badge details screen
- Master/Detail layout on tablets
- Offline-first data (Room)
- Reactive UI using `StateFlow`

---

## Getting started

### Prerequisites

- Android Studio (Giraffe or newer recommended)
- Android SDK 26+
- A Firebase project

---

## Firebase configuration (required)

This project uses **Firebase Authentication with Google Sign-In**.

Because this repository is public, the `google-services.json` file is **not committed**.

To run the project:

1. Create a Firebase project at https://console.firebase.google.com
2. Add an **Android app** with package name:
