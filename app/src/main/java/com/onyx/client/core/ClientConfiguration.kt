package com.onyx.client

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.onyx.client.core.ClientConfiguration
import com.onyx.client.core.ModuleDefinition
import com.onyx.client.core.ModuleRegistry

class MainActivity : AppCompatActivity() {

    private lateinit var moduleRegistry: ModuleRegistry
    private lateinit var appConfig: ClientConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moduleRegistry = ModuleRegistry(this)
        appConfig = ClientConfiguration(this)
        renderHome()
    }

    private fun renderHome() {
        setContentView(R.layout.activity_main)
        applyStretchIfNeeded(findViewById(R.id.homeRoot))

        findViewById<TextView>(R.id.launchButton).setOnClickListener {
            Toast.makeText(this, "Launching GOONER Client", Toast.LENGTH_SHORT).show()
        }

        findViewById<TextView>(R.id.modulesNav).setOnClickListener { renderModules() }
        findViewById<TextView>(R.id.hudsNav).setOnClickListener { renderHud() }
        findViewById<TextView>(R.id.visualsNav).setOnClickListener { renderVisuals() }
        findViewById<TextView>(R.id.settingsNav).setOnClickListener { renderSettings() }
        findViewById<TextView>(R.id.accountNav).setOnClickListener {
            Toast.makeText(this, "Account screen ready for next pass", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderModules() {
        setContentView(R.layout.activity_modules)
        applyStretchIfNeeded(findViewById(R.id.modulesRoot))

        val container = findViewById<LinearLayout>(R.id.moduleListContainer)
        val search = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.moduleSearch)
        val allFilter = findViewById<TextView>(R.id.categoryAll)
        val hudFilter = findViewById<TextView>(R.id.categoryVisualHud)
        val performanceFilter = findViewById<TextView>(R.id.categoryPerformance)
        val packsFilter = findViewById<TextView>(R.id.categoryPacks)
        val backButton = findViewById<TextView>(R.id.backButton)

        var currentCategory = "All"

        fun renderList() {
            val query = search.text?.toString()?.trim().orEmpty()
            val filtered = moduleRegistry.getModules().filter { module ->
                val matchesCategory = currentCategory == "All" || module.category == currentCategory
                val matchesQuery = query.isEmpty() ||
                    module.displayName.contains(query, true) ||
                    module.description.contains(query, true)
                matchesCategory && matchesQuery
            }

            container.removeAllViews()
            if (filtered.isEmpty()) {
                val empty = TextView(this).apply {
                    text = "No modules match this filter."
                    setTextColor(getColor(android.R.color.white))
                    textSize = 16f
                    setPadding(16, 32, 16, 0)
                }
                container.addView(empty)
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

        fun applyFilter(selected: String) {
            currentCategory = selected
            val selectedView = when (selected) {
                "All" -> allFilter
                "Visual HUD" -> hudFilter
                "Performance" -> performanceFilter
                "Resource Packs" -> packsFilter
                else -> allFilter
            }
            listOf(allFilter, hudFilter, performanceFilter, packsFilter).forEach { it.isSelected = it == selectedView }
            renderList()
        }

        allFilter.setOnClickListener { applyFilter("All") }
        hudFilter.setOnClickListener { applyFilter("Visual HUD") }
        performanceFilter.setOnClickListener { applyFilter("Performance") }
        packsFilter.setOnClickListener { applyFilter("Resource Packs") }
        backButton.setOnClickListener { renderHome() }
        search.setOnEditorActionListener { _, _, _ ->
            renderList()
            true
        }
        search.setOnKeyListener { _, _, _ ->
            renderList()
            true
        }

        applyFilter(currentCategory)
    }

    private fun renderHud() {
        setContentView(R.layout.activity_hud)
        applyStretchIfNeeded(findViewById(R.id.hudRoot))
        findViewById<TextView>(R.id.backButton).setOnClickListener { renderHome() }
        findViewById<TextView>(R.id.hudToggle).setOnClickListener {
            Toast.makeText(this, "HUD editor ready for widget tuning", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderVisuals() {
        setContentView(R.layout.activity_visuals)
        applyStretchIfNeeded(findViewById(R.id.visualsRoot))
        findViewById<TextView>(R.id.backButton).setOnClickListener { renderHome() }
        findViewById<TextView>(R.id.visualPreset).setOnClickListener {
            Toast.makeText(this, "Vanilla preset applied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderSettings() {
        setContentView(R.layout.activity_settings)
        applyStretchIfNeeded(findViewById(R.id.settingsRoot))

        val stretchToggle = findViewById<SwitchMaterial>(R.id.stretchToggle)
        stretchToggle.isChecked = appConfig.stretchEnabled
        stretchToggle.setOnCheckedChangeListener { _, isChecked ->
            appConfig.stretchEnabled = isChecked
            applyStretchIfNeeded(findViewById(R.id.settingsRoot))
        }

        findViewById<TextView>(R.id.backButton).setOnClickListener { renderHome() }
        findViewById<TextView>(R.id.saveButton).setOnClickListener {
            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun applyStretchIfNeeded(target: View?) {
        val stretched = appConfig.stretchEnabled
        if (target == null) return
        target.scaleX = if (stretched) 1.12f else 1f
        target.translationX = if (stretched) 36f else 0f
    }
}

