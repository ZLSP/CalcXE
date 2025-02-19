package com.zlsp.calcxe.ui.screens.products

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.zlsp.calcxe.R
import com.zlsp.calcxe.base.ext.shadow
import com.zlsp.calcxe.base.ext.toProducts
import com.zlsp.calcxe.data.models.CategoryUi
import com.zlsp.calcxe.data.models.ProductUi
import com.zlsp.calcxe.main.MainContract

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductsScreen(
    categories: SnapshotStateList<CategoryUi>,
    selectedFilterCategories: Boolean,
    selectedFilterFavorite: Boolean,
    sendAction: (MainContract.Action) -> Unit
) {
//    val notCategoryList = remember {
//        derivedStateOf {
//            mutableListOf<ProductUi>().apply {
//                categories.forEach {
//                    addAll(it.products)
//                }
//            }.sortedBy {
//                it.name
//            }.toList()
//        }
//    }
    val listState = rememberLazyListState()
    LaunchedEffect(selectedFilterCategories, selectedFilterFavorite) {
        listState.animateScrollToItem(0)

    }
    LazyColumn(
        state = listState
    ) {
        item { }
        if (selectedFilterCategories) {
            categories.forEach {
                if (it.products.isNotEmpty()) {
                    stickyHeader {
                        CategoryItem(
                            name = it.name,
                            iconUrl = it.iconUrl,
                            isOpen = it.isSelected.value,
                            onClick = {
                                sendAction(MainContract.Action.ProductsScreenActions.ChangeCategoryOpenState(it.id))
                            }
                        )
                    }
                }
                if (it.isSelected.value) {
                    items(it.products) { product ->
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            PrductItem(product) {
                                sendAction(
                                    MainContract.Action.ChangeProductFavoriteState(
                                        productId = product.id,
                                        categoryId = product.categoryId
                                    )
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(10.dp)) }
                }

            }
        } else {
            items(categories.toProducts()) { product ->
                Spacer(modifier = Modifier.height(10.dp))
                PrductItem(product) {
                    sendAction(
                        MainContract.Action.ChangeProductFavoriteState(
                            productId = product.id,
                            categoryId = product.categoryId
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PrductItem(
    product: ProductUi,
    onClickFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .shadow(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                blurRadius = 24.dp,
            ),
        colors = CardDefaults.elevatedCardColors(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(width = 1.dp, MaterialTheme.colorScheme.primary),
    ) {
        Column(Modifier.padding(10.dp)) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    text = product.name,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W700
                )
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(35.dp),
                    onClick = onClickFavorite
                ) {
                    Crossfade(
                        modifier = Modifier,
                        targetState = product.isFavorite.value
                    ) {
                        val iconRes = if (it) {
                            R.drawable.ic_favorite
                        } else {
                            R.drawable.ic_not_favorite
                        }
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(iconRes),
                            contentDescription = "favorite icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }


            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = MaterialTheme.colorScheme.primary,
            )
            Row(Modifier.fillMaxWidth()) {
                TableItem(R.string.columnCaloriesMin, product.calories.toString())
                TableItem(R.string.columnProteinMin, product.proteins.toString())
                TableItem(R.string.columnFatsMin, product.fats.toString())
                TableItem(R.string.columnCarbohydratesMin, product.carbohydrates.toString())
                TableItem(R.string.columnGi, product.gi.toString())
            }
        }
    }
}

@Stable
@Composable
private fun CategoryItem(
    name: String,
    iconUrl: String,
    isOpen: Boolean,
    onClick: () -> Unit,
) {
    val rotateAnimate = animateFloatAsState(
        targetValue = if (isOpen) 90f else -90f
    )
    Button(
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(bottom = 5.dp)
            .fillMaxWidth(),
        onClick = onClick,
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            modifier = Modifier
                .size(50.dp)
                .padding(5.dp),
            painter = rememberAsyncImagePainter(
                model = iconUrl
            ),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = name,
            modifier = Modifier
                .padding(5.dp)
                .weight(1f),
            maxLines = 3,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Icon(
            modifier = Modifier
                .padding(5.dp)
                .size(30.dp)
                .rotate(rotateAnimate.value),
            painter = painterResource(R.drawable.ic_arrow),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun RowScope.TableItem(
    @StringRes titleRes: Int,
    value: String
) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(titleRes),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W700
        )
        Text(
            text = value,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W500
        )
    }
}

//@Preview
//@Composable
//private fun PreviewLight() = AppTheme(themeMode = ThemeMode.LIGHT) {
//    Box(
//        Modifier
//            .background(MaterialTheme.colorScheme.background)
//            .padding(10.dp)
//    ) {
//        ListScreen(
//            categories = emptyList(),
//            selectedFilterCategories = true,
//            selectedFilterFavorite = false
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun PreviewDark() = AppTheme(themeMode = ThemeMode.DARK) {
//    Box(
//        Modifier
//            .background(MaterialTheme.colorScheme.background)
//            .padding(10.dp)
//    ) {
//        ListScreen(
//            categories = emptyList(),
//            selectedFilterCategories = true,
//            selectedFilterFavorite = false
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun PreviewAmoled() = AppTheme(themeMode = ThemeMode.AMOLED) {
//    Box(
//        Modifier
//            .background(MaterialTheme.colorScheme.background)
//            .padding(10.dp)
//    ) {
//        ListScreen(
//            categories = emptyList(),
//            selectedFilterCategories = true,
//            selectedFilterFavorite = false
//        )
//    }
//}