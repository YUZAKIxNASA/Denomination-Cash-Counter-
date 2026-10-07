# Denomination (Cash Counter)

A modern, high-speed, and intuitive Indian Currency Denomination Calculator and Cash Management Android application built with **Jetpack Compose**, **Kotlin**, and **Room Database**.

**Credits:** `@yuzaki_x_nasa` | `@ঔৣ፝ N4!`

---

## 📱 Overview

**Denomination (Cash Counter)** is designed for cashiers, shop owners, accountants, bank staff, and daily retail businesses to quickly tally, verify, save, and share physical cash denominations in Indian Rupees (`₹`).

The app features a high-contrast, minimalist black-and-white interface matching traditional cash tally slips, with zero latency, live calculations, and full offline persistence.

---

## ✨ Features

### 1. Active Denominations (RBI Aligned)
- Supports exactly **10 denominations** in sequential order:
  - **₹500**
  - **₹200**
  - **₹100**
  - **₹50**
  - **₹20**
  - **₹10**
  - **₹5**
  - **₹2**
  - **₹1**
  - **Coin** *(Counts total coin units without monetary value addition, displayed as `-`)*
- **₹2000 Note Removed:** In accordance with Reserve Bank of India (RBI) withdrawal guidelines, ₹2000 is completely excluded.

### 2. Live Calculations & Summary
- **Instant Updates:** As soon as you enter a note count, row totals, total pieces, and the grand total update instantly.
- **Grand Total in Words:** Automatically converts numbers into Indian numbering words format (`Crore`, `Lakh`, `Thousand`, `Hundred`, `Rupees Only`).
- **Live Device Date & Time:** Displays the exact transaction timestamp in real-time.
- **Formatted Currency:** Standard Indian comma separation and format (`₹ X /-`).

### 3. Data Persistence (Room Database)
- **100% Offline & Private:** All data is stored locally on the device using SQLite via Android Jetpack Room. No account or internet connection required.
- **Save Calculations:** Save any count session with an optional custom label/note.
- **History Viewer:**
  - View full breakdown of past saved counts.
  - One-tap reload of any historical record back into the active counter.
  - Individual record deletion or complete history clearing with confirmation dialogs.

### 4. Sharing & Exporting
- **Receipt Sharing:** Share clean, formatted cash breakdown receipts directly via WhatsApp, Telegram, SMS, Email, or clipboard.
- **Formatted Text Breakdown:** Produces a clean slip format displaying denomination counts, sub-amounts, total pieces, and grand total in words.

### 5. Custom App Branding & Logo
- **Official Rupee Emblem:** Launcher icon features the crisp, official Indian Rupee (`₹`) symbol in high contrast.
- **Adaptive Icon Support:** Full Android adaptive icon with dynamic background, foreground layers, and legacy density fallbacks (`mdpi`, `hdpi`, `xhdpi`, `xxhdpi`, `xxxhdpi`).

---

## 🛠 Tech Stack & Architecture

- **Language:** 100% Kotlin
- **UI Toolkit:** Jetpack Compose (Material Design 3)
- **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture
- **State Management:** Kotlin Coroutines & `MutableStateFlow` with lifecycle awareness
- **Database:** Android Jetpack Room with Kotlin Symbol Processing (KSP)
- **Asynchronous Operations:** Kotlin Coroutines & Flow
- **Build System:** Gradle (Kotlin DSL - `.gradle.kts`)

---

## 📂 Project Structure

```text
├── app/
│   ├── build.gradle.kts                # App-level build dependencies & configurations
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml     # App manifest & permissions
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt     # Single Activity host with edge-to-edge support
│   │   │   │   ├── data/
│   │   │   │   │   ├── AppDatabase.kt          # Room Database instance
│   │   │   │   │   ├── CashRecordDao.kt        # Data Access Object for records
│   │   │   │   │   └── CashRecordEntity.kt     # Database Entity definition
│   │   │   │   ├── model/
│   │   │   │   │   ├── Denomination.kt         # Enum of 10 supported denominations
│   │   │   │   │   └── NumberToWordsConverter.kt # Indian currency words engine
│   │   │   │   ├── ui/
│   │   │   │   │   ├── HomeScreen.kt           # Main denomination calculator interface
│   │   │   │   │   ├── HistoryScreen.kt        # Saved records list & detail dialog
│   │   │   │   │   ├── theme/                  # Material 3 colors, typography, theme
│   │   │   │   │   └── components/             # Reusable UI widgets & table rows
│   │   │   │   └── viewmodel/
│   │   │   │       └── CashCounterViewModel.kt # Business logic, totals, DB operations
│   │   │   └── res/
│   │   │       ├── drawable/           # Launcher icon vector & background layers
│   │   │       ├── mipmap-*/           # Density-specific launcher icons
│   │   │       └── values/             # strings.xml, colors.xml
│   │   └── test/                       # Unit tests & calculation validation
├── build.gradle.kts                    # Root build configuration
├── settings.gradle.kts                 # Project & repository settings
└── README.md                           # Documentation file
```

---

## 🚀 How to Build & Run

### Prerequisites
- **Android Studio** Hedgehog (2023.1.1) or newer / **Google AI Studio Build**
- **JDK:** OpenJDK 17 or newer
- **Android SDK:** Compile SDK 34, Min SDK 24

### Gradle Commands

1. **Assemble Debug APK:**
   ```bash
   gradle assembleDebug
   ```
   The generated APK will be located at:
   `app/build/outputs/apk/debug/app-debug.apk`

2. **Run Local JVM Unit Tests:**
   ```bash
   gradle :app:testDebugUnitTest
   ```

3. **Install to Connected Device or Emulator via ADB:**
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 📖 User Guide

### 1. Counting Cash
1. Tap on the count underline next to any denomination (e.g., `₹500`).
2. Type the number of notes using the numeric keypad.
3. The row amount on the right and the top summary values (`Total Notes / Coins`, `Total Amount`, `Grand Total in Words`) update in real time.

### 2. Clearing Values
- Tap the **Clear (Trash)** icon in the header.
- Confirm the dialog to reset all denomination inputs back to zero.

### 3. Saving a Calculation
1. Tap the **Save (Disk)** icon in the top header.
2. Enter an optional remark or title (e.g., *"Morning Cash In Hand"* or *"Counter 1 Closing"*).
3. Tap **Save**. Your entry is permanently saved in the local database.

### 4. Viewing History
1. Tap the **History (Clock)** icon in the header.
2. Scroll through previously saved calculation cards with dates, total amounts, and note breakdowns.
3. Tap **Load** on any record to reload those exact values into the active counter.
4. Tap **Share** on any record to export its text breakdown.

### 5. Sharing Cash Slips
- Tap the **Share** icon in the header to share the active calculation breakdown to WhatsApp, SMS, or any messaging app.

---

## 🔒 Privacy & Permissions

- **Zero Network Required:** Operates 100% offline. No internet connection is required or used for calculations or database storage.
- **Zero Sensitive Permissions:** No camera, location, contacts, or external storage permissions required.
- **Local SQLite Storage:** All saved history remains on the user's device.

---

## 📄 License & Credits

- **Authors / Credits:** `@yuzaki_x_nasa` | `@ঔৣ፝ N4!`
- Built for fast and reliable daily retail cash accounting.
