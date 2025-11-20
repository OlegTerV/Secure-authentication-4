package com.example.inventory.ui.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.io.File
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.os.Build

object PreferencesService {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    var sharedPreferences: SharedPreferences? = null


    // Custom Advanced Master Key
    val advancedSpec = KeyGenParameterSpec.Builder("master_key", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    ).apply {
        setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        setKeySize(256)
        setUserAuthenticationRequired(false)
        setUserAuthenticationValidityDurationSeconds (15) // must be larger than @
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            setUnlockedDeviceRequired(true)
            setIsStrongBoxBacked(false)
        } }.build()
    val advancedKeyAlias = MasterKeys.getOrCreate(advancedSpec)


    @Composable
    fun InitPreferences () {
         sharedPreferences = EncryptedSharedPreferences.create(
            "PreferencesTestFile_8",
            masterKeyAlias,
             LocalContext.current,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}