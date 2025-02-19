package com.zlsp.calcxe.ui.screens.products

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.zlsp.calcxe.data.models.CategoryUi
import com.zlsp.calcxe.data.models.ProductUi

data class ProductsScreenState(
    val isOnlyFavorite: MutableState<Boolean> = mutableStateOf(false),
    val searchList: MutableState<String> = mutableStateOf("")
) {
    private val searchIsActive: Boolean
        get() = searchList.value.length > 2

    fun getActiveList(categories: SnapshotStateList<CategoryUi>): SnapshotStateList<CategoryUi> {
        return if (isOnlyFavorite.value || searchIsActive) {
            getFilteredList(categories.toList())
        } else {
            categories
        }
    }

    private fun getFilterCondition(product: ProductUi): Boolean {
        return when {
            isOnlyFavorite.value && searchIsActive -> {
                product.isFavorite.value && product.containsByName(searchList.value)
            }

            isOnlyFavorite.value -> {
                product.isFavorite.value
            }

            searchIsActive -> {
                product.containsByName(searchList.value)
            }

            else -> true
        }
    }

    private fun getFilteredList(categories: List<CategoryUi>): SnapshotStateList<CategoryUi> {
        return mutableStateListOf<CategoryUi>().apply {
            categories.forEach { category ->
                if (category.isFavorite || searchIsActive) {
                    add(
                        CategoryUi(
                            id = category.id,
                            name = category.name,
                            iconUrl = category.iconUrl,
                            products = mutableStateListOf<ProductUi>().apply {
                                category.products.forEach { product ->
                                    if(getFilterCondition(product)) {
                                        add(product)
                                    }
                                }
                            },
                            isSelected = mutableStateOf(category.isSelected.value)
                        )
                    )
                }
            }
        }
//        return categories.filter { category ->
//            category.isFavorite
//        }.map { category ->
//            category.apply {
//                products.removeIf {
//                    getFilterCondition(it)
//                }
//            }
//        }.toMutableStateList()
    }
}