package ru.kpfu.itis.debugmenu.feature.debugmenu.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class EnvironmentPreferencesStorage(
    private val sharedPreferences: SharedPreferences,
    private val encryptedPreferences: SharedPreferences,
    private val serverUrls: Array<String>
) {
    var selectedUrl: String
        get() {
            val default = serverUrls[0]
            return sharedPreferences.getString(KEY_SELECTED_URL, default) ?: default
        }
        set(value) {
            sharedPreferences.edit(true) { putString(KEY_SELECTED_URL, value) }
            encryptedPreferences.edit().remove(DEVICE_TOKEN).apply()
        }


    companion object {
        private const val SHRED_PREFS_NAME = "environment_preferences"
        private const val KEY_SELECTED_URL = "key_selected_url"
        private const val DEVICE_TOKEN = "DEVICE_TOKEN"

        fun create(
            context: Context,
            serverUrls: Array<String>,
        ): EnvironmentPreferencesStorage =
            EnvironmentPreferencesStorage(
                context.getSharedPreferences(SHRED_PREFS_NAME, Context.MODE_PRIVATE),
                EncryptedSharedPreferences.create(
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    context.packageName + "preferences",
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                ),
                serverUrls
            )
    }
}