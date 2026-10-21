package com.onyx.client

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.switchmaterial.SwitchMaterial
import com.onyx.client.core.ModuleDefinition
import com.onyx.client.core.ModuleRegistry

class MainActivity : AppCompatActivity() {

    private lateinit var moduleRegistry: ModuleRegistry
    private var currentCategory: String = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moduleRegistry = ModuleRegistry(this)
        renderHomeScreen()
    }

    private fun renderHomeScreen() {
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.launchButton).setOnClickListener {
            Toast.makeText(this, "Launch flow ready for Bedrock integration.", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.modulesNav).setOnClickListener { renderModuleBrowser() }
        findViewById<View>(R.id.hudsNav).setOnClickListener {
            Toast.makeText(this, "HUD browser in progress.", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.visualsNav).setOnClickListener {
            Toast.makeText(this, "Visuals screen in progress.", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.settingsNav).setOnClickListener {
            Toast.makeText(this, "Settings screen in progress.", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.accountNav).setOnClickListener {
            Toast.makeText(this, "Account screen in progress.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderModuleBrowser() {
        setContentView(R.layout.activity_modules)

        val searchField = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.moduleSearch)
        val container = findViewById<LinearLayout>(R.id.moduleListContainer)
        val allFilter = findViewById<TextView>(R.id.categoryAll)
        val visualFilter = findViewById<TextView>(R.id.categoryVisualHud)
        val performanceFilter = findViewById<TextView>(R.id.categoryPerformance)
        val packsFilter = findViewById<TextView>(R.id.categoryPacks)
        val backButton = findViewById<TextView>(R.id.backButton)

        backButton.setOnClickListener { renderHomeScreen() }

        fun applyCategoryFilter() {
            val query = searchField.text?.toString()?.trim() ?: ""
            val filtered = moduleRegistry.getModules().filter { module ->
                val matchesCategory = currentCategory == "All" || module.category == currentCategory
                val matchesQuery = query.isEmpty() || module.displayName.contains(query, ignoreCase = true) || module.description.contains(query, ignoreCase = true)
                matchesCategory && matchesQuery
            }

            container.removeAllViews()
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

            if (filtered.isEmpty()) {
                val emptyView = TextView(this).apply {
                    text = "No modules match the current filter."
                    setTextColor(getColor(android.R.color.white))
                    textSize = 16f
                    setPadding(16, 32, 16, 0)
                }
                container.addView(emptyView)
            }
        }

        fun setCategory(selected: String) {
            currentCategory = selected
            listOf(allFilter, visualFilter, performanceFilter, packsFilter).forEach { view ->
                view.isSelected = (view.id == when (selected) {
                    "All" -> R.id.categoryAll
                    "Visual HUD" -> R.id.categoryVisualHud
                    "Performance" -> R.id.categoryPerformance
                    "Resource Packs" -> R.id.categoryPacks
                    else -> R.id.categoryAll
                })
            }
            applyCategoryFilter()
        }

        allFilter.setOnClickListener { setCategory("All") }
        visualFilter.setOnClickListener { setCategory("Visual HUD") }
        performanceFilter.setOnClickListener { setCategory("Performance") }
        packsFilter.setOnClickListener { setCategory("Resource Packs") }

        searchField.setOnEditorActionListener { _, _, _ ->
            applyCategoryFilter()
            true
        }
        searchField.setOnKeyListener { _, _, _ ->
            applyCategoryFilter()
            true
        }
        searchField.setOnFocusChangeListener { _, _ -> applyCategoryFilter() }

        setCategory(currentCategory)
    }
}

