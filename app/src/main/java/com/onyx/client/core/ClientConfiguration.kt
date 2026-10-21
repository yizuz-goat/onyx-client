package com.onyx.client

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.onyx.client.core.ClientConfiguration
import com.onyx.client.core.ModuleRegistry

class MainActivity : AppCompatActivity() {

    private lateinit var moduleRegistry: ModuleRegistry
    private lateinit var settings: ClientConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moduleRegistry = ModuleRegistry(this)
        settings = ClientConfiguration(this)
        renderHomeScreen()
    }

    private fun renderHomeScreen() {
        setContentView(R.layout.activity_main)
        applyStretchIfNeeded(findViewById(R.id.homeRoot))

        findViewById<View>(R.id.launchButton).setOnClickListener {
            Toast.makeText(this, "Launch flow ready for Bedrock integration.", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.modulesNav).setOnClickListener { renderModuleBrowser() }
        findViewById<View>(R.id.hudsNav).setOnClickListener { renderHudEditor() }
        findViewById<View>(R.id.visualsNav).setOnClickListener { renderVisuals() }
        findViewById<View>(R.id.settingsNav).setOnClickListener { renderSettings() }
        findViewById<View>(R.id.accountNav).setOnClickListener {
            Toast.makeText(this, "Account profile screen is ready for the next pass.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderModuleBrowser() {
        setContentView(R.layout.activity_modules)
        applyStretchIfNeeded(findViewById(R.id.modulesRoot))

        val searchField = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.moduleSearch)
        val container = findViewById<LinearLayout>(R.id.moduleListContainer)
        val filters = mapOf(
            R.id.categoryAll to "All",
            R.id.categoryVisualHud to "Visual HUD",
            R.id.categoryPerformance to "Performance",
            R.id.categoryPacks to "Resource Packs"
        )

        var currentCategory = "All"

        fun renderList() {
            val query = searchField.text?.toString()?.trim().orEmpty()
            val filtered = moduleRegistry.getModules().filter { module ->
                val byCategory = currentCategory == "All" || module.category == currentCategory
                val byQuery = query.isEmpty() ||
                    module.displayName.contains(query, true) ||
                    module.description.contains(query, true)
                byCategory && byQuery
            }

            container.removeAllViews()

            if (filtered.isEmpty()) {
                val emptyState = TextView(this).apply {
                    text = "No modules match this filter."
                    setTextColor(getColor(android.R.color.white))
                    textSize = 16f
                    setPadding(16, 32, 16, 0)
                }
                container.addView(emptyState)
                return
            }

            filtered.forEach { module ->
                val card = layoutInflater.inflate(R.layout.module_card_item, container, false)
                val title = card.findViewById<TextView>(R.id.moduleTitle)
                val subtitle = card.findViewById<TextView>(R.id.moduleSubtitle)
                val status = card.findViewById<TextView>(R.id.moduleStatus)
                val toggle = card.findViewById<SwitchMaterial>(R.id.moduleToggle)

                title.text = module.displayName
                subtitle.text = module.description
                status.text = "${module.category} • ${module.availabilityStatus}"
                toggle.isChecked = module.enabled
                toggle.setOnCheckedChangeListener { _, isChecked ->
                    moduleRegistry.toggle(module.id, isChecked)
                    status.text = "${module.category} • ${if (isChecked) "enabled" else "disabled"}"
                }

                container.addView(card)
            }
        }

        fun applyCategorySelection(selected: String) {
            currentCategory = selected
            filters.forEach { (id, value) ->
                val view = findViewById<TextView>(id)
                view.isSelected = value == selected
            }
            renderList()
        }

        filters.forEach { (id, value) ->
            findViewById<TextView>(id).setOnClickListener { applyCategorySelection(value) }
        }

        searchField.setOnEditorActionListener { _, _, _ ->
            renderList()
            true
        }
        searchField.setOnKeyListener { _, _, _ ->
            renderList()
            true
        }

        findViewById<TextView>(R.id.backButton).setOnClickListener { renderHomeScreen() }
        applyCategorySelection(currentCategory)
    }

    private fun renderHudEditor() {
        setContentView(R.layout.activity_hud)
        applyStretchIfNeeded(findViewById(R.id.hudRoot))
        findViewById<TextView>(R.id.backButton).setOnClickListener { renderHomeScreen() }
        findViewById<TextView>(R.id.hudToggle).setOnClickListener {
            Toast.makeText(this, "HUD editor ready for widget calibration.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderVisuals() {
        setContentView(R.layout.activity_visuals)
        applyStretchIfNeeded(findViewById(R.id.visualsRoot))
        findViewById<TextView>(R.id.backButton).setOnClickListener { renderHomeScreen() }
        findViewById<TextView>(R.id.visualPreset).setOnClickListener {
            Toast.makeText(this, "Vanilla preset applied to the client config.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderSettings() {
        setContentView(R.layout.activity_settings)
        applyStretchIfNeeded(findViewById(R.id.settingsRoot))

        val stretchToggle = findViewById<SwitchMaterial>(R.id.stretchToggle)
        stretchToggle.isChecked = settings.stretchEnabled
        stretchToggle.setOnCheckedChangeListener { _, isChecked ->
            settings.stretchEnabled = isChecked
            applyStretchIfNeeded(findViewById(R.id.settingsRoot))
        }

        findViewById<TextView>(R.id.backButton).setOnClickListener { renderHomeScreen() }
        findViewById<TextView>(R.id.saveButton).setOnClickListener {
            Toast.makeText(this, "Settings saved to the persistent client config.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun applyStretchIfNeeded(target: View?) {
        val enabled = settings.stretchEnabled
        if (target == null) return
        target.scaleX = if (enabled) 1.12f else 1f
        target.scaleY = if (enabled) 1f else 1f
        target.translationX = if (enabled) 30f else 0f
    }
}
