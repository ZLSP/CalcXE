package com.zlsp.calcxe.domain

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zlsp.calcxe.R

enum class TypeCategory(@StringRes val nameId: Int, @DrawableRes val imageId: Int) {
    USER(nameId = R.string.categoryUser, imageId = R.drawable.ic_add),
    FAVORITE(nameId = R.string.categoryFavorite, imageId = R.drawable.ic_favorite),
    ALL(nameId = R.string.categoryAll, imageId = R.drawable.ic_all),
    GREEN(nameId = R.string.categoryGreen, imageId = R.drawable.ic_green),
    FRUIT(nameId = R.string.categoryFruit, imageId = R.drawable.ic_fruit),
    SWEET(nameId = R.string.categorySweet, imageId = R.drawable.ic_sweet),
    FLOUR(nameId = R.string.categoryFlour, imageId = R.drawable.ic_flour),
    GROATS(nameId = R.string.categoryGroats, imageId = R.drawable.ic_groats),
    MEAT(nameId = R.string.categoryMeat, imageId = R.drawable.ic_meat),
    FISH(nameId = R.string.categoryFish, imageId = R.drawable.ic_fish),
    CHEESE(nameId = R.string.categoryCheese, imageId = R.drawable.ic_cheese),
    DAIRY(nameId = R.string.categoryDairy, imageId = R.drawable.ic_milk),
    NUTS(nameId = R.string.categoryNuts, imageId = R.drawable.ic_nuts),
    SPICES(nameId = R.string.categorySpices, imageId = R.drawable.ic_salt),
    ALCOHOL(nameId = R.string.categoryAlcohol, imageId = R.drawable.ic_alcohol),
    OTHER(nameId = R.string.categoryOther, imageId = R.drawable.ic_other),
}