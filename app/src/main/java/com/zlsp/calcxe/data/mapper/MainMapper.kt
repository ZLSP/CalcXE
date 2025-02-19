package com.zlsp.calcxe.data.mapper

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.zlsp.calcxe.data.models.CategoryResponse
import com.zlsp.calcxe.data.models.CategoryUi
import com.zlsp.calcxe.data.models.ProductResponse
import com.zlsp.calcxe.data.models.ProductUi

class MainMapper {

    fun categoriesResponseToUi(
        userList: List<ProductResponse>,
        responseList: List<CategoryResponse>,
        favoriteIds: List<Int>,
    ): SnapshotStateList<CategoryUi> {
        val userProductsUi = userList
            .map {
                val isFavorite = favoriteIds.contains(it.id)
                it.toUi(isFavorite)
            }.sortedBy {
                it.name
            }.toMutableStateList()

        val userCategory = CategoryUi.createUserCategory(userProductsUi)
        val list = responseList.map {
            it.toUi(favoriteIds)
        }

        return mutableStateListOf<CategoryUi>().apply {
            add(userCategory)
            addAll(list)
        }
    }

    private fun CategoryResponse.toUi(favoriteIds: List<Int>): CategoryUi {
        val productsUi = products.map {
            val isFavorite = favoriteIds.contains(it.id)
            it.toUi(isFavorite)
        }.toMutableStateList()
        return CategoryUi(
            id = id,
            name = name,
            iconUrl = iconUrl,
            products = productsUi,
        )
    }

    private fun ProductResponse.toUi(isFavorite: Boolean): ProductUi {
        return ProductUi(
            id = id,
            categoryId = categoryId,
            name = name,
            calories = calories,
            proteins = proteins,
            fats = fats,
            carbohydrates = carbohydrates,
            gi = gi,
            isFavorite = mutableStateOf(isFavorite)
        )
    }

}