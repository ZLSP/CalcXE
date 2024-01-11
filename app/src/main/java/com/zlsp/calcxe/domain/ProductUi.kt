package com.zlsp.calcxe.domain

data class ProductUi(
    val id: String,
    val category: TypeCategory,
    val calories: Double,
    val protein: Double,
    val fats: Double,
    val carbohydrates: Double,
    val gi: Double,
    val weight: Double = DEFAULT_WEIGHT,
    val isFavorite: Boolean = DEFAULT_IS_FAVORITE,
    val name: String = DEFAULT_NAME
) {
    val numCategory = category.ordinal

    companion object {
        const val DEFAULT_WEIGHT = 100.0
        const val DEFAULT_IS_FAVORITE = false
        const val DEFAULT_NAME_ID = -1
        const val DEFAULT_NAME = ""

        fun getDefault() = ProductUi(
            id = "",
            category = TypeCategory.ALL,
            calories = 0.0,
            protein = 0.0,
            fats = 0.0,
            carbohydrates = 0.0,
            gi = 0.0
        )

        fun getPreviewItem(
            id: String,
            name: String = "Апельсин",
            category: TypeCategory = TypeCategory.ALL
        ) = ProductUi(
            id = id,
            category = category,
            calories = 150.0,
            protein = 150.0,
            fats = 150.0,
            carbohydrates = 150.0,
            gi = 0.0,
            name = name
        )

        fun getPreviewList() = listOf(
            getPreviewItem("1"),
            getPreviewItem("2"),
            getPreviewItem("3"),
            getPreviewItem("4"),
            getPreviewItem("5"),
            getPreviewItem("6"),
            getPreviewItem("7"),
            getPreviewItem("8"),
        )
    }
}
