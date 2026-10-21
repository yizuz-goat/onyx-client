package com.onyx.client

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.onyx.client.core.ClientConfiguration
import com.onyx.client.core.ModuleRegistry

class MainActivity : AppCompatActivity() {

    private lateinit var clientConfig: ClientConfiguration
    private lateinit var moduleRegistry: ModuleRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clientConfig = ClientConfiguration(this)
        moduleRegistry = ModuleRegistry(this)
        renderHome()
    }

    private fun renderHome() {
        setContentView(R.layout.activity_main)

        val launchButton = findViewById<TextView>(R.id.launchButton)
        val modulesNav = findViewById<TextView>(R.id.modulesNav)
        val hudsNav = findViewById<TextView>(R.id.hudsNav)
        val visualsNav = findViewById<TextView>(R.id.visualsNav)
        val settingsNav = findViewById<TextView>(R.id.settingsNav)
        val accountNav = findViewById<TextView>(R.id.accountNav)

        launchButton.setOnClickListener { launchOfficialMinecraft() }
        modulesNav.setOnClickListener { renderModules() }
        hudsNav.setOnClickListener { renderHud() }
        visualsNav.setOnClickListener { renderVisuals() }
        settingsNav.setOnClickListener { renderSettings() }
        accountNav.setOnClickListener { renderAccount() }
    }

    private fun renderModules() {
        setContentView(R.layout.activity_modules)

        val container = findViewById<LinearLayout>(R.id.moduleListContainer)
        val searchField = findViewById<TextInputEditText>(R.id.moduleSearch)
        val backButton = findViewById<TextView>(R.id.backButton)
        val modules = moduleRegistry.getModules()

        fun renderList() {
            val query = searchField.text?.toString()?.trim().orEmpty()
            val filtered = modules.filter { module ->
                query.isEmpty() || module.displayName.contains(query, true) ||
                    module.category.contains(query, true) ||
                    module.description.contains(query, true)
            }

            container.removeAllViews()
            if (filtered.isEmpty()) {
                val empty = TextView(this).apply {
                    text = "No modules match this filter."
                    setTextColor(getColor(android.R.color.white))
                    textSize = 16f
                    setPadding(0, 24, 0, 0)
                }
                container.addView(empty)
                return
            }

            filtered.forEach { module ->
                val cardView = layoutInflater.inflate(R.layout.module_card_item, container, false)
                val title = cardView.findViewById<TextView>(R.id.moduleTitle)
                val subtitle = cardView.findViewById<TextView>(R.id.moduleSubtitle)
                val status = cardView.findViewById<TextView>(R.id.moduleStatus)
                val toggle = cardView.findViewById<SwitchMaterial>(R.id.moduleToggle)

                title.text = module.displayName
                subtitle.text = module.description
                status.text = "${module.category} • ${module.availabilityStatus}"
                toggle.isChecked = module.enabled
                toggle.setOnCheckedChangeListener { _, isChecked ->
                    moduleRegistry.toggle(module.id, isChecked)
                    status.text = "${module.category} • ${if (isChecked) "enabled" else "disabled"}"
                }
                container.addView(cardView)
            }
        }

        backButton.setOnClickListener { renderHome() }
        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = renderList()
            override fun afterTextChanged(s: Editable?) = Unit
        })

        renderList()
    }

    private fun renderHud() {
        setContentView(R.layout.activity_hud)
        val backButton = findViewById<TextView>(R.id.backButton)
        val hudToggle = findViewById<TextView>(R.id.hudToggle)
        backButton.setOnClickListener { renderHome() }
        hudToggle.setOnClickListener {
            Toast.makeText(this, "HUD editor ready.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderVisuals() {
        setContentView(R.layout.activity_visuals)
        val backButton = findViewById<TextView>(R.id.backButton)
        val visualPreset = findViewById<TextView>(R.id.visualPreset)
        backButton.setOnClickListener { renderHome() }
        visualPreset.setOnClickListener {
            Toast.makeText(this, "Vanilla preset applied.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderSettings() {
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<TextView>(R.id.backButton)
        val saveButton = findViewById<TextView>(R.id.saveButton)
        val stretchToggle = findViewById<SwitchMaterial>(R.id.stretchToggle)
        val goonModeToggle = findViewById<SwitchMaterial>(R.id.goonModeToggle)

        stretchToggle.isChecked = clientConfig.stretchEnabled
        goonModeToggle.isChecked = clientConfig.goonModeEnabled

        stretchToggle.setOnCheckedChangeListener { _, checked ->
            clientConfig.stretchEnabled = checked
        }

        goonModeToggle.setOnCheckedChangeListener { _, checked ->
            clientConfig.goonModeEnabled = checked
            Toast.makeText(this, if (checked) "Goon Mode enabled." else "Goon Mode disabled.", Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener { renderHome() }
        saveButton.setOnClickListener {
            Toast.makeText(this, "Settings saved.", Toast.LENGTH_SHORT).show()
            renderHome()
        }
    }

    private fun renderAccount() {
        setContentView(R.layout.activity_account)
        val backButton = findViewById<TextView>(R.id.backButton)
        val actionButton = findViewById<TextView>(R.id.accountPrimaryAction)
        backButton.setOnClickListener { renderHome() }
        actionButton.setOnClickListener {
            Toast.makeText(this, "Account sync is local-only for this launcher.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchOfficialMinecraft() {
        val packageName = "com.mojang.minecraftpe"
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)

        if (launchIntent == null) {
            Toast.makeText(
                this,
                "Minecraft Bedrock is not installed on this device.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        try {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(launchIntent)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Minecraft could not be opened. Check the app installation and Android launcher settings.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
