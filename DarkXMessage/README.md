# DarkX Message

Native Android SMS/MMS client (Kotlin, Compose, Material 3). No server, no internet requirement for SMS.

## Status: Part 1 of 5 (Phases 1-3)
Done: Gradle, manifest (default-SMS role components), theme, navigation, onboarding, default-SMS role request.

WARNING: `SmsDeliverReceiver`, `MmsDeliverReceiver` and `RespondViaMessageService` are empty stubs until Part 2.
Do not set this build as your default SMS app on a phone you rely on - incoming texts would not be stored.

## Build
Open in Android Studio (Ladybug or newer), let Gradle sync, run on a device/emulator with telephony.
If `gradlew` is missing, Android Studio will use its bundled Gradle, or run `gradle wrapper --gradle-version 8.9`.
