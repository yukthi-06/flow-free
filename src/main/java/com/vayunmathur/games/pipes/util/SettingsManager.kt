package com.vayunmathur.games.pipes.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class AppSettings(
    val colorblind: Boolean = false,
    val levelsPath: String = "/sdcard/Vypeensoft/Flow_Free/game/levels/"
)

object SettingsManager {
    private val settingsFile = File("/sdcard/Vypeensoft/Flow_Free/settings/settings.json")
    
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
    
    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    init {
        loadSettings()
    }

    fun loadSettings() {
        try {
            if (settingsFile.exists()) {
                val content = settingsFile.readText()
                val parsed = json.decodeFromString<AppSettings>(content)
                _settings.value = parsed
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveSettings(newSettings: AppSettings) {
        _settings.value = newSettings
        try {
            settingsFile.parentFile?.mkdirs()
            settingsFile.writeText(json.encodeToString(newSettings))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setColorblind(value: Boolean) {
        saveSettings(_settings.value.copy(colorblind = value))
    }

    fun setLevelsPath(path: String) {
        saveSettings(_settings.value.copy(levelsPath = path))
    }
}
