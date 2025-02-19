package com.zlsp.calcxe.data.models

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf

data class ProductUi(
    val id: Int,
    val categoryId: Int,
    val name: String,
    val calories: Int,
    val proteins: Double,
    val fats: Double,
    val carbohydrates: Double,
    val gi: Int,
    val weight: MutableIntState = mutableIntStateOf(100),
    val isFavorite: MutableState<Boolean> = mutableStateOf(false)
) {
    fun containsByName(text: String): Boolean {
        val split = text.split(" ")
        println(split)
        val listHasContains = mutableListOf<Boolean>().apply {
            split.forEach {
                add(name.contains(it, true))
            }
        }
        return listHasContains.all { it }
    }
}