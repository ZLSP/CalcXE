package com.zlsp.calcxe.ui.screens.products

import com.zlsp.calcxe.base.BaseInteractor
import com.zlsp.calcxe.main.MainContract
import com.zlsp.calcxe.main.MainContract.Action.ProductsScreenActions.*

class ProductsScreenInteractor(
    private val updateState: (MainContract.State) -> Unit
): BaseInteractor {

    override fun sendAction(state: MainContract.State, action: MainContract.Action) {
        if (action is MainContract.Action.ProductsScreenActions) {
            when(action) {
                is ChangeCategoryOpenState -> changeCategoryOpenState(state, action.id)
                ChangeStateFilterFavorite -> changeStateFilterFavorite(state)
            }
        }
    }

    private fun changeCategoryOpenState(state: MainContract.State, id: Int) {
        updateState(
            state.apply {
                categories.find { it.id == id }?.changeOpenState()
            }
        )
    }

    private fun changeStateFilterFavorite(state: MainContract.State) {
        updateState(
            state.apply {
                productsScreenState.isOnlyFavorite.value = !productsScreenState.isOnlyFavorite.value
            }
        )
    }

}