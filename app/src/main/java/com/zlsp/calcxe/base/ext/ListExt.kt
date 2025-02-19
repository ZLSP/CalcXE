package com.zlsp.calcxe.base.ext

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.zlsp.calcxe.data.models.CategoryUi
import com.zlsp.calcxe.data.models.ProductUi

fun SnapshotStateList<CategoryUi>.toProducts(): List<ProductUi> {
    return mutableListOf<ProductUi>().apply {
        this@toProducts.forEach {
            addAll(it.products)
        }
    }.sortedBy {
        it.name
    }
}