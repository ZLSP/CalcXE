package com.zlsp.calcxe.data.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class CategoryUi(
    val id: Int,
    val name: String,
    val iconUrl: String,
    val products: SnapshotStateList<ProductUi>,
    val isSelected: MutableState<Boolean> = mutableStateOf(false)
) {
    companion object {
        private const val USER_CATEGORY_ID = 1
        private const val USER_CATEGORY_NAME = "Добавленные"
        private const val USER_CATEGORY_ICON_URL = ""

        fun createUserCategory(products: SnapshotStateList<ProductUi>) = CategoryUi(
            id = USER_CATEGORY_ID,
            name = USER_CATEGORY_NAME,
            iconUrl = USER_CATEGORY_ICON_URL,
            products = products,
        )
    }

    val isFavorite: Boolean
        get() = products.find { it.isFavorite.value } != null

    fun hasSearch(text: String): Boolean {
        return products.find { it.containsByName(text) } != null
    }

    fun changeOpenState() {
        isSelected.value = !isSelected.value
    }

    val favoritesProducts: List<ProductUi>
        get() = products.filter { it.isFavorite.value }

    fun getProductById(id: Int): ProductUi? {
        return products.find { it.id == id }
    }

    fun changeFavoriteStateProduct(id: Int) {
        getProductById(id)?.let {
            it.isFavorite.value = !it.isFavorite.value
        }
    }
}