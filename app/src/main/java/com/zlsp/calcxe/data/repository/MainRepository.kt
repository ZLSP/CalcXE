package com.zlsp.calcxe.data.repository

import com.zlsp.calcxe.data.helper.AssetJsonHelper
import com.zlsp.calcxe.data.models.CategoryResponse
import com.zlsp.calcxe.data.models.ProductResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class MainRepository(
    private val assetJsonHelper: AssetJsonHelper
) {
    fun getData(): Flow<Triple<List<CategoryResponse>, List<ProductResponse>, List<Int>>> {
        return combine(
            getResponseCategory(),
            flowOf(emptyList<ProductResponse>()),
            flowOf(emptyList<Int>())
        ) { response, user, favorite ->
            Triple(response, user, favorite)
        }
    }

    private fun getResponseCategory(): Flow<List<CategoryResponse>> {
        return flowOf(assetJsonHelper.getListCategories())
            .flowOn(Dispatchers.IO)
    }
}