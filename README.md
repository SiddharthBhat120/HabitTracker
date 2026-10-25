# 🧘 Habit Tracker — Digital Sanctuary

A complete Android habit tracking app built with Kotlin + Jetpack Compose.

## 📱 Screens Included
- Splash Screen (animated logo)
- Onboarding (3-page walkthrough)
- Login & Register (with validation)
- Forgot Password
- Home Dashboard (habits list + daily progress)
- Create Habit
- Habit Detail (streak, consistency, weekly overview)
- Journal (add/delete entries with mood)
- Settings (dark mode, notifications, profile, logout)

## 🚀 Setup Instructions

### Step 1 — Open in Android Studio
1. Unzip `HabitTracker.zip`
2. Open **Android Studio** (Hedgehog 2023.3 or newer)
3. Click **File → Open** → select the `HabitTracker` folder
4. Wait for Gradle sync to complete (first time downloads dependencies ~2 min)

### Step 2 — Fix local.properties
Android Studio will auto-create `local.properties` with your SDK path.
If it doesn't, add this line manually:
```
sdk.dir=C\:\\Users\\YourName\\AppData\\Local\\Android\\Sdk   (Windows)
sdk.dir=/Users/YourName/Library/Android/sdk                  (Mac)
sdk.dir=/home/YourName/Android/Sdk                           (Linux)
```

### Step 3 — Run
- Connect an Android device (API 28+) or start an emulator
- Click the ▶ **Run** button

## 🏗️ Architecture
```
com.digitalsanctuary.habittracker/
├── data/
│   ├── local/
│   │   ├── dao/          — Room DAOs
│   │   ├── entity/       — Room entities
│   │   └── HabitDatabase — Room database
│   └── repository/       — Data access layer
├── navigation/           — NavGraph + Screen routes
├── ui/
│   ├── screens/          — All Compose screens
│   └── theme/            — Colors, Typography, Theme
├── utils/                — PreferencesManager, DateUtils
├── viewmodel/            — MVVM ViewModels
└── MainActivity.kt
```

## 🎨 Design System
- **Primary:** #2F6B8A (Soft Blue)
- **Secondary:** #5BAD8F (Accent Green)
- **Background:** #F2F5F8 (Light Grey)
- **Cards:** 20-24dp rounded corners
- **Buttons:** Pill-shaped (50dp radius)

## 📦 Dependencies
- Jetpack Compose BOM 2024.10
- Navigation Compose 2.8.3
- Room 2.6.1
- DataStore Preferences 1.1.1
- Material Icons Extended
- Kotlin Coroutines + Flow
