package com.onyx.client.core

import android.content.Context
import androidx.core.content.edit

data class ModuleDefinition(
    val id: String,
    val displayName: String,
    val category: String,
    val description: String,
    var enabled: Boolean = false,
    val availabilityStatus: String = "available"
)

class ModuleRegistry(private val context: Context) {
    private val moduleList = listOf(
        ModuleDefinition(
            id = "fps_counter",
            displayName = "FPS Counter",
            category = "Visual HUD",
            description = "Measures current render performance and shows live FPS.",
            enabled = true,
            availabilityStatus = "available"
        ),
        ModuleDefinition(
            id = "coordinates",
            displayName = "Coordinates",
            category = "Visual HUD",
            description = "Displays X, Y, and Z values when player state is available.",
            enabled = true,
            availabilityStatus = "conditional"
        ),
        ModuleDefinition(
            id = "brightness",
            displayName = "Brightness",
            category = "Visuals",
            description = "Applies safe brightness adjustments when supported by the runtime.",
            enabled = false,
            availabilityStatus = "experimental"
        ),
        ModuleDefinition(
            id = "performance_dashboard",
            displayName = "Performance Dashboard",
            category = "Performance",
            description = "Tracks FPS, frame time, and client diagnostics.",
            enabled = true,
            availabilityStatus = "available"
        ),
        ModuleDefinition(
            id = "resource_pack_manager",
            displayName = "Resource Packs",
            category = "Resource Packs",
            description = "Validates and manages local Bedrock-compatible resource packs.",
            enabled = true,
            availabilityStatus = "available"
        ),
        ModuleDefinition(
            id = "module_registry",
            displayName = "Module Registry",
            category = "Client Settings",
            description = "Central registry for module lifecycle and persisted configuration.",
            enabled = true,
            availabilityStatus = "available"
        )
    )

    fun getModules(): List<ModuleDefinition> = moduleList

    fun toggle(id: String, enabled: Boolean) {
        val module = moduleList.firstOrNull { it.id == id } ?: return
        module.enabled = enabled
        persistState()
    }

    private fun persistState() {
        val prefs = context.getSharedPreferences("module_registry", Context.MODE_PRIVATE)
        prefs.edit {
            moduleList.forEach { module ->
                putBoolean(module.id, module.enabled)
            }
        }
    }
}

