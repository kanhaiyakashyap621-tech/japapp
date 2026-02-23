# Shant Jap

Minimal spiritual Jap counter app built with Kotlin, Jetpack Compose, MVVM, Room, and Firebase Firestore.

**Features**
- Dual mode (Radha Naam, Ram Naam) with separate daily and lifetime counts
- Silent Jap fullscreen mode with session summary
- Daily quote from local JSON (Hindi + English)
- 108 Day Journey tracking with unlocks
- Personal Sankalp saved locally
- Leaderboard with Firestore sync
- FCM setup ready

**Setup**
1. Open the project root in Android Studio.
2. Add your Firebase `google-services.json` to `app/`.
3. Sync Gradle and run on a device or emulator.

**Structure**
- UI: `app/src/main/java/com/shantjap/app/presentation`
- Data: `app/src/main/java/com/shantjap/app/data`
- Domain: `app/src/main/java/com/shantjap/app/domain`
