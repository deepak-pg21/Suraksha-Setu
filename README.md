

# 🚨 Suraksha-Setu
### AI-Powered Hyper-Local Emergency Safety Network

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-purple?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-blue?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Database-Room-orange?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/Firebase-FCM-yellow?style=for-the-badge"/>
  <img src="https://img.shields.io/badge/AI-Gemini-red?style=for-the-badge"/>
</p>

---

# 📌 Overview

Suraksha-Setu is an Android-based emergency response application designed to improve personal safety using AI, sensor automation, emergency communication, and hyper-local alert systems.


The application helps users quickly alert trusted contacts during emergency situations using:

- 📳 Shake gesture detection
- 🚨 SOS emergency alerts
- 🎤 Emergency audio recording
- 👥 Volunteer responder systems
- 🤖 AI-powered safety assistance

The project was developed using Kotlin, Jetpack Compose, Firebase, and Google AI APIs as part of an Android + GenAI infrastructure solution.

---

# 🎯 Problem Statement

In many rural and semi-urban areas, women and elderly citizens walking alone at night often feel unsafe. Traditional emergency applications mainly depend on police response systems, which may not always provide immediate local assistance.

Suraksha-Setu introduces a Hyper-Local Safety Network where trusted contacts, nearby volunteers, and community responders can be alerted instantly during emergency situations.

---

# ✨ Key Features

## 🚨 Emergency SOS System

- Large high-visibility SOS button
- One-tap emergency activation
- Fast emergency response workflow
- Background emergency execution

---

## 📳 Shake Gesture Detection

- Detects emergency phone shaking
- Uses Android motion sensors
- Runs using foreground emergency services
- Can trigger alerts even when app is minimized

---

## 📩 Emergency SMS Alerts

- Sends SOS SMS messages instantly
- Supports multiple emergency contacts
- Emergency alerts work without internet

### Example Alert

```text
SOS ALERT! I may be in danger. Please help immediately.
````

---

## 🎤 Emergency Audio Recording

* Automatically records emergency audio
* Stores evidence locally on device
* Starts during SOS activation
* Uses Android MediaRecorder APIs

---

## 🤖 AI Safety Assistant

Powered using Google Gemini AI.

### Features

* AI-generated safety tips
* Smart emergency awareness assistance
* Dynamic safety guidance

---

## 👥 Safe Circle Contacts

* Add trusted emergency contacts
* Store contacts locally using Room Database
* Manage emergency response network

---

## 🧑‍🤝‍🧑 Volunteer Responder Mode

* Users can register as volunteers
* Supports community-based emergency assistance
* Designed for future hyper-local responder systems

---

## ☁ Firebase Integration

Integrated with Firebase for:

* Firebase Cloud Messaging (FCM)
* Notification infrastructure
* Future real-time emergency expansion

---

# 🛠 Tech Stack

| Technology                    | Purpose                        |
| ----------------------------- | ------------------------------ |
| Kotlin                        | Core Android Development       |
| Jetpack Compose               | Modern Android UI              |
| Room Database                 | Local Data Storage             |
| Firebase Cloud Messaging      | Notification Infrastructure    |
| Gemini AI API                 | AI-generated Safety Tips       |
| Android Sensors               | Shake Detection                |
| Foreground Services           | Background Emergency Execution |
| MediaRecorder                 | Emergency Audio Recording      |
| Google Play Services Location | Location Support               |

---

# 🧠 System Workflow

```text
User shakes phone / presses SOS
            ↓
Emergency Service Activated
            ↓
Alarm Sound Starts
            ↓
Emergency Audio Recording Begins
            ↓
SOS SMS Alerts Sent
            ↓
Trusted Contacts Receive Alert
```

---

# 📂 Project Structure

```text
app/
│
├── ai/                 → Gemini AI integration
├── data/               → Room database & DAO
├── fcm/                → Firebase messaging
├── service/            → Background emergency services
├── ui/                 → Compose UI screens
├── utils/              → Audio recorder & utilities
│
├── MainActivity.kt
├── MainViewModel.kt
└── AndroidManifest.xml
```

---

# ✅ Implemented Success Criteria

✔ Background shake gesture detection
✔ High-visibility SOS emergency UI
✔ Emergency communication workflow
✔ Emergency audio recording
✔ Firebase integration structure
✔ AI-powered safety assistance

---

# 🔒 Permissions Used

The application uses the following Android permissions:

* ACCESS_FINE_LOCATION
* ACCESS_COARSE_LOCATION
* RECORD_AUDIO
* SEND_SMS
* FOREGROUND_SERVICE
* POST_NOTIFICATIONS

---

# 📈 Future Improvements

Planned future enhancements:

* 🌍 Live GPS location sharing
* 📡 Real-time volunteer notifications
* ☁ Cloud audio upload
* 🗺 Emergency location dashboard
* 📍 Advanced geofencing
* 🔔 Smart AI risk prediction
* 🧠 AI emergency classification

---

# 📱 Screens Included

* Permission Screen
* SOS Home Screen
* Safe Circle Contacts Screen
* Volunteer Mode Screen

---

# 🚀 How to Run the Project

## Prerequisites

* Android Studio
* Android SDK 26+
* Firebase Project
* Internet Connection

---

## Setup Instructions

### 1. Clone Repository

```bash
git clone https://github.com/deepak-pg21/Suraksha-Setu.git
```

### 2. Open in Android Studio

Open the cloned project folder inside Android Studio.

### 3. Add Firebase Configuration

Place:

```text
google-services.json
```

inside:

```text
app/
```

### 4. Sync Gradle

Allow Android Studio to download dependencies and complete Gradle Sync.

### 5. Run Application

Connect Android device or emulator and run the project.

---

# 👨‍💻 Developed By

## P G Deepak Chiranjeevi


Final Year Engineering Student
Android + GenAI Developer + Future CEO

---

# 📜 License

This project was developed for educational and academic purposes.

---

# ⭐ Project Highlights

✅ AI + Android Integration
✅ Real-time Emergency Workflow
✅ Sensor-Based Automation
✅ Firebase Infrastructure
✅ Modern Jetpack Compose UI
✅ Practical Safety-Oriented Solution

---

<p align="center">
  <b>Suraksha-Setu</b><br>
  Building safer communities using Android + AI 🚨
</p>
