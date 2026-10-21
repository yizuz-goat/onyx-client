package com.onyx.client.core

import android.content.Context
import androidx.core.content.edit
import java.util.LinkedHashMap

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
            description = "Displays current measured FPS with configurable panel settings.",
            enabled = true,
            availabilityStatus = "available"
        ),
        ModuleDefinition(
            id = "coordinates",
            displayName = "Coordinates",
            category = "Visual HUD",
            description = "Shows X, Y, and Z values when player state is available.",
            enabled = true,
            availabilityStatus = "conditional"
        ),
        ModuleDefinition(
            id = "brightness",
            displayName = "Brightness",
            category = "Visuals",
            description = "Applies safe visual brightness adjustments when supported.",
            enabled = false,
            availabilityStatus = "experimental"
        ),
        ModuleDefinition(
            id = "performance_dashboard",
            displayName = "Performance Dashboard",
            category = "Performance",
            description = "Tracks runtime diagnostics, FPS and frame-time health.",
            enabled = true,
            availabilityStatus = "available"
        ),
        ModuleDefinition(
            id = "resource_pack_manager",
            displayName = "Resource Packs",
            category = "Resource Packs",
            description = "Lists and validates Bedrock-compatible packs."
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
        val states = LinkedHashMap<String, Boolean>()
        moduleList.forEach { states[it.id] = it.enabled }
        prefs.edit {
            clear()
            states.forEach { (key, value) -> putBoolean(key, value) }
        }
    }
}
