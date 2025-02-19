package com.zlsp.calcxe.data.helper

import android.content.Context
import com.zlsp.calcxe.data.models.ResponseAppData
import com.zlsp.calcxe.data.models.CategoryResponse
import kotlinx.serialization.json.Json

class AssetJsonHelper(context: Context) {
    private val assetManager = context.assets

    private fun readJsonStringFromAsset(fileName: String): String {
        return assetManager
            .open(fileName)
            .bufferedReader()
            .use { it.readText() }
    }

    fun getListCategories(): List<CategoryResponse> {
        return try {
            val jsonString = readJsonStringFromAsset("app_data.json")
            Json.decodeFromString<ResponseAppData>(jsonString).categories.map { category ->
                category.copy(
                    products = category.products.sortedBy { it.name }
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}