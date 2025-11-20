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

package com.example.inventory.ui.item

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.CreationMethod
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import java.text.NumberFormat

/**
 * ViewModel to validate and insert items in the Room database.
 */
class ItemEntryViewModel (private val itemsRepository: ItemsRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(
                itemDetails = itemDetails, isEntryValid = validateInput(itemDetails),
                isValidFiled = validateFields(itemDetails)
            )
    }

    private fun validateFields(uiState: ItemDetails = itemUiState.itemDetails): MutableMap<String, Boolean>{
        val isValidFiled_map: MutableMap<String, Boolean> = mutableMapOf()

        if (uiState.price.isNotBlank()) {
            isValidFiled_map["price"] = !uiState.price.matches(regex_price)
        } else {
            isValidFiled_map["price"] = true
        }

        if (uiState.quantity.isNotBlank()) {
            isValidFiled_map["quantity"] = !uiState.quantity.matches(regex_quantity)
        } else {
            isValidFiled_map["quantity"] = true
        }

        if (uiState.suppliers_email.isNotBlank()) {
            isValidFiled_map["suppliers_email"] = !uiState.suppliers_email.matches(regex_suppliers_email)
        } else {
            isValidFiled_map["suppliers_email"] = true
        }

        if (uiState.suppliers_mobile_phone.isNotBlank()) {
            isValidFiled_map["suppliers_mobile_phone"] = !uiState.suppliers_mobile_phone.matches(regex_suppliers_mobile_phone)
        } else {
            isValidFiled_map["suppliers_mobile_phone"] = true
        }

        return isValidFiled_map
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        var flag = true
        for ((_, value) in validateFields(uiState)) {
            if (value) {
                flag = false
            }
        }
        return flag
        /*
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
                    && suppliers_name.isNotBlank() && suppliers_email.isNotBlank() && suppliers_mobile_phone.isNotBlank()
        }*/
    }

    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false,
    val isValidFiled: MutableMap<String, Boolean> = mutableMapOf("price" to false, "quantity" to false,
        "suppliers_email" to false, "suppliers_mobile_phone" to false)
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    var quantity: String = "",
    val suppliers_name: String = "",
    val suppliers_email: String = "",
    val suppliers_mobile_phone: String = "",
    val creation_method: CreationMethod = CreationMethod.manual,
)

/**
 * Extension function to convert [ItemDetails] to [Item]. If the value of [ItemDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemDetails.quantity] is not a valid [Int], then the quantity will be set to 0
 */
fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toIntOrNull() ?: 0,
    suppliers_name = suppliers_name,
    suppliers_email = suppliers_email,
    suppliers_mobile_phone = suppliers_mobile_phone,
    creation_method = creation_method
)

fun Item.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(price)
}

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid,
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString(),
    suppliers_name = suppliers_name,
    suppliers_email = suppliers_email,
    suppliers_mobile_phone = suppliers_mobile_phone,
    creation_method = creation_method
)

val regex_price = Regex("[0-9]+(\\.[0-9]+)?$") // скатал у deepSeek
val regex_quantity = Regex("[0-9]+")
val regex_suppliers_email = Regex(
    ("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"))
val regex_suppliers_mobile_phone= Regex( // sdd = space, dot, or dash
    ("(\\+[0-9]+[\\- \\.]*)?" // +<digits><sdd>*
    + "(\\([0-9]+\\)[\\- \\.]*)?" // (<digits>)<sdd>*
    + "([0-9][0-9\\- \\.]+[0-9])"))



