package com.zlsp.calcxe.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zlsp.calcxe.data.mapper.MainMapper
import com.zlsp.calcxe.data.repository.MainRepository
import com.zlsp.calcxe.main.MainContract.*
import com.zlsp.calcxe.ui.screens.products.ProductsScreenInteractor
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class MainViewModel(
    private val repository: MainRepository,
    private val mapper: MainMapper,
) : ContainerHost<State, Effect>, ViewModel() {
    override val container: Container<State, Effect> = container(
        State()
    ) {
        getProducts()
    }

    private val localState = container.stateFlow.value

    private val productsScreenInteractor = ProductsScreenInteractor { newState ->
        intent { reduce { newState } }
    }

    fun sendAction(action: Action) {
        when (action) {
            is Action.ProductsScreenActions ->
                productsScreenInteractor.sendAction(localState, action)


            is Action.ChangeProductFavoriteState ->
                changeProductFavoriteState(
                    productId = action.productId,
                    categoryId = action.categoryId
                )
        }
    }

    private fun changeProductFavoriteState(
        productId: Int,
        categoryId: Int
    ) = intent {
        reduce {
            state.apply {
                categories.find { it.id == categoryId }?.changeFavoriteStateProduct(productId)
            }
        }
    }

    private fun changeCategoryOpenState(id: Int) = intent {
        reduce {
            state.apply {
                categories.find { it.id == id }?.changeOpenState()
            }
        }
    }

    private fun getProducts() {
        repository.getData()
            .map { (responseList, userList, favoriteIds) ->
                mapper.categoriesResponseToUi(
                    userList = userList,
                    responseList = responseList,
                    favoriteIds = favoriteIds
                )
            }
            .onEach {
                intent {
                    reduce {
                        state.apply {
                            categories.apply {
                                clear()
                                addAll(it)
                            }
                        }
                    }
                }
            }.catch {
                println(it.message)
            }.launchIn(viewModelScope)
    }
}