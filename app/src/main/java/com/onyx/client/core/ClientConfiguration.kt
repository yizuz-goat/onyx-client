package com.onyx.client

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.onyx.client.core.ClientConfiguration
import com.onyx.client.core.ModuleDefinition
import com.onyx.client.core.ModuleRegistry

class MainActivity : AppCompatActivity() {

    private lateinit var moduleRegistry: ModuleRegistry
    private lateinit var clientConfig: ClientConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moduleRegistry = ModuleRegistry(this)
        clientConfig = ClientConfiguration(this)
        renderHome()
    }

    private fun renderHome() {
        setContentView(R.layout.activity_main)
        applyStretch(findViewById(R.id.homeRoot))

        findViewById<TextView>(R.id.launchButton).setOnClickListener {
            Toast.makeText(this, "Launching GOONER Client", Toast.LENGTH_SHORT).show()
        }

        findViewById<TextView>(R.id.modulesNav).setOnClickListener { renderModules() }
        findViewById<TextView>(R.id.hudsNav).setOnClickListener { renderHud() }
        findViewById<TextView>(R.id.visualsNav).setOnClickListener { renderVisuals() }
        findViewById<TextView>(R.id.settingsNav).setOnClickListener { renderSettings() }
        findViewById<TextView>(R.id.accountNav).setOnClickListener { renderAccount() }
    }

    private fun renderModules() {
        setContentView(R.layout.activity_modules)
        applyStretch(findViewById(R.id.modulesRoot))

        val container = findViewById<LinearLayout>(R.id.moduleListContainer)
        val searchField = findViewById<TextInputEditText>(R.id.moduleSearch)
        val backButton = findViewById<TextView>(R.id.backButton)
        val allChip = findViewById<TextView>(R.id.categoryAll)
        val visualChip = findViewById<TextView>(R.id.categoryVisualHud)
        val performanceChip = findViewById<TextView>(R.id.categoryPerformance)
        val packsChip = findViewById<TextView>(R.id.categoryPacks)

        var currentCategory = "All"

        fun renderList() {
            val query = searchField.text?.toString()?.trim().orEmpty()
            val filtered = moduleRegistry.getModules().filter { module ->
                val matchesCategory = currentCategory == "All" || module.category == currentCategory
                val matchesQuery = query.isEmpty() ||
                    module.displayName.contains(query, true) ||
                    module.description.contains(query, true)
                matchesCategory && matchesQuery
            }

            container.removeAllViews()
            if (filtered.isEmpty()) {
                container.addView(buildEmptyState("No modules match this filter."))
                return
            }

            filtered.forEach { module ->
                val card = LayoutInflater.from(this).inflate(R.layout.module_card_item, container, false)
                val title = card.findViewById<TextView>(R.id.moduleTitle)
                val description = card.findViewById<TextView>(R.id.moduleSubtitle)
                val badge = card.findViewById<TextView>(R.id.moduleStatus)
                val toggle = card.findViewById<SwitchMaterial>(R.id.moduleToggle)

                title.text = module.displayName
                description.text = module.description
                badge.text = "${module.category} • ${module.availabilityStatus}"
                toggle.isChecked = module.enabled
                toggle.setOnCheckedChangeListener { _, isChecked ->
                    moduleRegistry.toggle(module.id, isChecked)
                    badge.text = "${module.category} • ${if (isChecked) "enabled" else "disabled"}"
                }
                container.addView(card)
            }
        }

        fun selectChip(selected: TextView) {
            val chips = listOf(allChip, visualChip, performanceChip, packsChip)
            chips.forEach { chip ->
                chip.setBackgroundResource(if (chip == selected) R.drawable.chip_selected else R.drawable.chip_unselected)
                chip.setTextColor(if (chip == selected) getColor(android.R.color.white) else getColor(R.color.text_secondary))
            }
        }

        allChip.setOnClickListener {
            currentCategory = "All"
            selectChip(allChip)
            renderList()
        }
        visualChip.setOnClickListener {
            currentCategory = "Visual HUD"
            selectChip(visualChip)
            renderList()
        }
        performanceChip.setOnClickListener {
            currentCategory = "Performance"
            selectChip(performanceChip)
            renderList()
        }
        packsChip.setOnClickListener {
            currentCategory = "Resource Packs"
            selectChip(packsChip)
            renderList()
        }

        backButton.setOnClickListener { renderHome() }
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = renderList()
            override fun afterTextChanged(s: Editable?) = Unit
        })

        selectChip(allChip)
        renderList()
    }

    private fun renderHud() {
        setContentView(R.layout.activity_hud)
        applyStretch(findViewById(R.id.hudRoot))
        findViewById<TextView>(R.id.backButton).setOnClickListener { renderHome() }
        findViewById<TextView>(R.id.hudToggle).setOnClickListener {
            Toast.makeText(this, "HUD editor ready for layered widgets", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderVisuals() {
        setContentView(R.layout.activity_visuals)
        applyStretch(findViewById(R.id.visualsRoot))
        findViewById<TextView>(R.id.backButton).setOnClickListener { renderHome() }
        findViewById<TextView>(R.id.visualPreset).setOnClickListener {
            Toast.makeText(this, "Vanilla preset applied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderSettings() {
        setContentView(R.layout.activity_settings)
        applyStretch(findViewById(R.id.settingsRoot))

        val stretchToggle = findViewById<SwitchMaterial>(R.id.stretchToggle)
        stretchToggle.isChecked = clientConfig.stretchEnabled
        stretchToggle.setOnCheckedChangeListener { _, checked ->
            clientConfig.stretchEnabled = checked
            applyStretch(findViewById(R.id.settingsRoot))
        }

        findViewById<TextView>(R.id.backButton).setOnClickListener { renderHome() }
        findViewById<TextView>(R.id.saveButton).setOnClickListener {
            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderAccount() {
        setContentView(R.layout.activity_account)
        applyStretch(findViewById(R.id.accountRoot))
        findViewById<TextView>(R.id.backButton).setOnClickListener { renderHome() }
        findViewById<TextView>(R.id.accountPrimaryAction).setOnClickListener {
            Toast.makeText(this, "Account sync ready", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildEmptyState(message: String): TextView {
        return TextView(this).apply {
            text = message
            setTextColor(getColor(android.R.color.white))
            textSize = 16f
            setPadding(12, 24, 12, 0)
        }
    }

    private fun applyStretch(target: View?) {
        if (target == null) return
        target.scaleX = if (clientConfig.stretchEnabled) 1.12f else 1f
        target.translationX = if (clientConfig.stretchEnabled) 22f else 0f
    }
}

