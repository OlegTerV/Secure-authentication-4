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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.home.HomeViewModel
import com.example.inventory.ui.navigation.NavigationDestination
import java.util.Currency
import java.util.Locale

object PreferencesDestination : NavigationDestination {
    override val route = "preferences"
    override val titleRes: Int = R.string.app_preferences
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val viewModel: PreferencesViewModel = PreferencesViewModel(LocalContext.current)

    val hideData = remember { mutableStateOf(viewModel.readDataBool("hideData")) }
    val blockSending = remember { mutableStateOf(viewModel.readDataBool("blockSending")) }
    val defaultCountBoolean = remember { mutableStateOf(viewModel.readDataBool("defaultCountBoolean")) }
    val defaultCountString = remember { mutableStateOf(viewModel.readDataInt("defaultCount").toString()) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            InventoryTopAppBar(
                title = stringResource(PreferencesDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior,
                canOpenSettings = false
            )
        }
    ) { innerPadding ->

        HomeBody(
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
            viewModel = viewModel,
            hideData = hideData,
            blockSending = blockSending,
            defaultCountBoolean = defaultCountBoolean,
            defaultCountString = defaultCountString
            //readData = { viewModel.saveData("hideData", false) }, КАК???

        )
    }
}

@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: PreferencesViewModel,
    hideData: MutableState<Boolean>,
    blockSending: MutableState<Boolean>,
    defaultCountBoolean: MutableState<Boolean>,
    defaultCountString: MutableState<String>,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(350.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(contentPadding)
        ) {
            Text("Скрыть \"чувствительные\" данные", fontSize = 20.sp)
            Checkbox(
                checked = hideData.value,
                onCheckedChange = {
                    hideData.value = it
                    viewModel.saveDataBool("hideData", it)
                },
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Запретить отправку данных из приложения", fontSize = 20.sp)
            Checkbox(
                checked = blockSending.value,
                onCheckedChange = {
                    blockSending.value = it
                    viewModel.saveDataBool("blockSending", it)
                },
            )
        }
        Spacer(
            modifier = Modifier.padding(12.dp)
        )
        Column {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Использовать количество товара по умолчанию", fontSize = 20.sp)
                Checkbox(
                    checked = defaultCountBoolean.value,
                    onCheckedChange = {
                        defaultCountBoolean.value = it
                        viewModel.saveDataBool("defaultCountBoolean", it)
                    },
                )
            }
            OutlinedTextField(
                value = defaultCountString.value,
                onValueChange = {
                    defaultCountString.value = it
                    if (viewModel.validInput(defaultCountString.value)) {
                        viewModel.saveDataInt("defaultCount", defaultCountString.value.toInt())

                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = defaultCountBoolean.value,
                singleLine = true,
                isError = !viewModel.validInput(defaultCountString.value),
                supportingText = {
                    if (!viewModel.validInput(defaultCountString.value)) {
                        Text("Incorrect quantity format.")
                    }
                }
            )
        }
    }
}

fun onValueChange() {

}
