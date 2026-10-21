package com.onyx.client.core

import android.content.Context
import androidx.core.content.edit

class ClientConfiguration(private val context: Context) {
    private val prefs = context.getSharedPreferences("gooner_client_config", Context.MODE_PRIVATE)

    var activeProfile: String
        get() = prefs.getString("active_profile", "Default") ?: "Default"
        set(value) = prefs.edit { putString("active_profile", value) }

    var stretchEnabled: Boolean
        get() = prefs.getBoolean("stretch_enabled", false)
        set(value) = prefs.edit { putBoolean("stretch_enabled", value) }

    var goonModeEnabled: Boolean
        get() = prefs.getBoolean("goon_mode_enabled", false)
        set(value) = prefs.edit { putBoolean("goon_mode_enabled", value) }

    var performancePreset: String
        get() = prefs.getString("performance_preset", "BALANCED") ?: "BALANCED"
        set(value) = prefs.edit { putString("performance_preset", value) }

    var accentColor: String
        get() = prefs.getString("accent_color", "#8B5CF6") ?: "#8B5CF6"
        set(value) = prefs.edit { putString("accent_color", value) }

    fun saveModuleState(id: String, enabled: Boolean) {
        prefs.edit { putBoolean("module_$id", enabled) }
    }

    fun loadModuleState(id: String, default: Boolean): Boolean {
        return prefs.getBoolean("module_$id", default)
    }
}
