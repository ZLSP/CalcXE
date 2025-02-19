package com.zlsp.calcxe.data.models
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class ResponseAppData(
    @SerialName("categories")
    val categories: List<CategoryResponse>
)

@Serializable
data class CategoryResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("iconUrl")
    val iconUrl: String,
    @SerialName("products")
    val products: List<ProductResponse>
)

@Serializable
data class ProductResponse(
    @SerialName("calories")
    val calories: Int,
    @SerialName("carbohydrates")
    val carbohydrates: Double,
    @SerialName("category_id")
    val categoryId: Int,
    @SerialName("fats")
    val fats: Double,
    @SerialName("gi")
    val gi: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("proteins")
    val proteins: Double
)


