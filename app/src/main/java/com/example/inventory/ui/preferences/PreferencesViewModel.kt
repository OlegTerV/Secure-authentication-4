/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.preferences

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.core.content.edit

/**
 * ViewModel to retrieve all items in the Room database.
 */
class PreferencesViewModel(applicationContext: Context) : ViewModel() {
    fun saveDataBool(filedName: String, value: Boolean) {
        PreferencesService.sharedPreferences!!.edit() { putBoolean(filedName, value).apply() }
    }

    fun saveDataInt(filedName: String, value: Int) {
        PreferencesService.sharedPreferences!!.edit() { putInt(filedName, value).apply() }
    }

    fun readDataBool(filedName: String): Boolean {
        return PreferencesService.sharedPreferences!!.getBoolean(filedName, false)
    }

    fun readDataInt(filedName: String): Int {
        return PreferencesService.sharedPreferences!!.getInt(filedName, 0)
    }

    fun validInput(value: String): Boolean {
        return value.matches(Regex("[0-9]+"))
    }


}

