package com.zlsp.calcxe.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.zlsp.calcxe.data.models.CategoryUi
import com.zlsp.calcxe.ui.screens.products.ProductsScreenState

interface MainContract {
    data class State(
        val categories: SnapshotStateList<CategoryUi> = mutableStateListOf(),
        val productsScreenState: ProductsScreenState = ProductsScreenState(),
        val searchHomeProducts: MutableState<String> = mutableStateOf(""),
    ) {
        val productsScreenList: SnapshotStateList<CategoryUi>
            get() = productsScreenState.getActiveList(categories)
    }

    sealed interface Effect{

    }

    sealed interface Action {

        data class ChangeProductFavoriteState(
            val productId: Int,
            val categoryId: Int
        ): Action

        sealed interface ProductsScreenActions: Action {
            data object ChangeStateFilterFavorite: ProductsScreenActions
            data class ChangeCategoryOpenState(val id: Int): ProductsScreenActions
        }

    }

}