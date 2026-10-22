package com.onyx.client.core

import android.content.Context
import androidx.core.content.edit

data class ModuleDefinition(
    val id: String,
    val displayName: String,
    val category: String,
    val description: String,
    val enabled: Boolean = true,
    val availabilityStatus: String = "supported",
    val compatibilityNotes: String = ""
)

class ModuleRegistry(private val context: Context) {
    private val prefs = context.getSharedPreferences("gooner_client_modules", Context.MODE_PRIVATE)

    private val defaultModules = listOf(
        ModuleDefinition(
            id = "launch",
            displayName = "Launch",
            category = "Launcher",
            description = "Opens the official Minecraft Bedrock app when installed.",
            enabled = true,
            availabilityStatus = "supported",
            compatibilityNotes = "Requires the canonical Bedrock package."
        ),
        ModuleDefinition(
            id = "goon_mode",
            displayName = "Goon Mode",
            category = "Performance",
            description = "Valid launcher-side optimization mode: lighter UI, reduced motion, battery-aware settings, and performance presets.",
            enabled = false,
            availabilityStatus = "supported",
            compatibilityNotes = "Launcher-side optimization only. No game cheating and no hidden-game data access."
        ),
        ModuleDefinition(
            id = "hud_fps",
            displayName = "FPS Counter",
            category = "HUD",
            description = "Displays FPS only when a supported integration exposes it.",
            enabled = true,
            availabilityStatus = "conditional",
            compatibilityNotes = "Needs a supported data source."
        ),
        ModuleDefinition(
            id = "visual_crosshair",
            displayName = "Crosshair",
            category = "Visuals",
            description = "Local-only crosshair styling for supported interfaces.",
            enabled = true,
            availabilityStatus = "supported",
            compatibilityNotes = "Depends on the host UI or resource-pack surface."
        ),
        ModuleDefinition(
            id = "performance_balanced",
            displayName = "Balanced Performance",
            category = "Performance",
            description = "Uses conservative optimization and lighter idle work for smoother everyday use.",
            enabled = true,
            availabilityStatus = "supported",
            compatibilityNotes = "Applies to the launcher, not the game executable."
        ),
        ModuleDefinition(
            id = "resource_packs",
            displayName = "Resource Packs",
            category = "Resource Packs",
            description = "Import and manage local resource packs with safe compatibility checks.",
            enabled = true,
            availabilityStatus = "supported",
            compatibilityNotes = "Requires storage access and valid files."
        )
    )

    fun getModules(): List<ModuleDefinition> {
        return defaultModules.map { module ->
            module.copy(enabled = prefs.getBoolean("module_${module.id}", module.enabled))
        }
    }

    fun toggle(id: String, enabled: Boolean) {
        prefs.edit { putBoolean("module_$id", enabled) }
    }
}
