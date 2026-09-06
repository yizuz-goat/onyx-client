# Onyx Client - Revolutionary Minecraft Bedrock Launcher

<div align="center">
  <h2>🎮 The Ultimate Bedrock Experience</h2>
  <p><strong>Optimized • Fast • Revolutionary</strong></p>
</div>

---

## 📱 Features

✅ **Instant Launch** - One-tap Minecraft Bedrock launching  
✅ **Texture Management** - Import and manage custom textures  
✅ **Resource Packs** - Easy resource pack installation  
✅ **Cache Optimization** - Automated cache clearing  
✅ **Dark UI** - Eye-friendly modern interface  
✅ **Minimal APK Size** - Highly optimized build  

---

## 🚀 Getting Started

### Prerequisites
- Android 7.0+ (API 24+)
- Java 11 or higher
- Gradle 8.0.0+
- Kotlin 1.9.0+

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yizuz-goat/onyx-client.git
   cd onyx-client
   ```

2. **Build the APK:**
   ```bash
   gradle build
   ```

3. **Release APK:**
   ```bash
   gradle assembleRelease
   ```

---

## 📁 Project Structure

```
onyx-client/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/onyx/client/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── LaunchFragment.kt
│   │   │   │   │   └── SettingsFragment.kt
│   │   │   │   └── core/
│   │   │   │       ├── BedrockLauncher.kt
│   │   │   │       └── TextureManager.kt
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   ├── drawable/
│   │   │   │   └── values/
│   │   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md
```

---

## 🎨 Interface

### Main Screen
- **LAUNCH Button** - Start Minecraft Bedrock instantly
- **SETTINGS Button** - Access texture and resource pack options

### Launch Fragment
- Real-time game status
- Quick launch button
- Status monitoring

### Settings Fragment
- Import textures
- Import resource packs
- Clear cache
- Optimization settings

---

## ⚙️ Building & Optimization

### ProGuard Optimization
- Code shrinking enabled
- Resource shrinking enabled
- Removes logging statements
- Optimizes multiple passes

### Dependencies
- **Kotlin** - Modern Android development
- **Coroutines** - Async operations
- **AndroidX** - Modern Android libraries
- **Material Design** - Beautiful UI components

---

## 📊 Performance

- **APK Size:** ~5-8MB (optimized)
- **Memory Usage:** ~50-100MB
- **Launch Time:** <2 seconds
- **Target Devices:** Android 7.0+

---

## 🔐 Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

---

## 🛠️ Development

### Running on Emulator
```bash
gradle installDebug
adb shell am start -n com.onyx.client/.MainActivity
```

### Creating Release APK
```bash
gradle assembleRelease
# APK location: app/build/outputs/apk/release/app-release.apk
```

---

## 📝 License

MIT License - See LICENSE file for details

---

## 👤 Author

**yizuz-goat** - Onyx Client Developer

---

<div align="center">
  <p><strong>Made with ❤️ for Minecraft Bedrock Players</strong></p>
  <p>⭐ Star this repo if you like it!</p>
</div>
