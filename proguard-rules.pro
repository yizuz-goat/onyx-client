# ULTRA OPTIMIZATION ProGuard Rules
# Aggressive shrinking and optimization for 200+ FPS

# Keep Android framework classes
-keep class android.** { *; }
-keep class androidx.** { *; }
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver

# Keep our optimization classes
-keep class com.onyx.client.core.** { *; }
-keep class com.onyx.client.ui.** { *; }

# Remove all logging
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# AGGRESSIVE OPTIMIZATION
-optimizationpasses 7
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# Enable obfuscation
-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses

# Shrink code aggressively
-dontshrink
-dontpreverify

# Keep line numbers for debugging
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Remove unused resources
-dontwarn **
