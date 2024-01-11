package com.zlsp.calcxe.ui.screens.list

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zlsp.calcxe.R
import com.zlsp.calcxe.base.ext.shadow
import com.zlsp.calcxe.domain.ProductUi
import com.zlsp.calcxe.domain.TypeCategory
import com.zlsp.calcxe.ui.theme.AppTheme
import com.zlsp.calcxe.ui.theme.models.ThemeMode

@Composable
fun ListScreen() {
    Row(Modifier.fillMaxWidth()) {
        LazyColumn(Modifier.weight(1f)){
            items(ProductUi.getPreviewList()) {
                Spacer(modifier = Modifier.height(10.dp))
                ListItem(it)
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
        }
        Spacer(modifier = Modifier.width(10.dp))
        LazyColumn{
            items(TypeCategory.entries) {
                Spacer(modifier = Modifier.height(10.dp))
                ItemCategoryList(
                    categoryItem = it,
                    isActive = true,
                    onClick = {}
                )
            }
            item { Spacer(modifier = Modifier.height(10.dp)) }
        }
    }
}

@Composable
private fun ListItem(product: ProductUi) {
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
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = product.name,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W700
            )
            Divider(
                modifier = Modifier.padding(vertical = 10.dp),
                color = MaterialTheme.colorScheme.primary,
            )
            Row(Modifier.fillMaxWidth()) {
                TableItem(R.string.columnCaloriesMin, product.calories)
                TableItem(R.string.columnProteinMin, product.protein)
                TableItem(R.string.columnFatsMin, product.fats)
                TableItem(R.string.columnCarbohydratesMin, product.carbohydrates)
                TableItem(R.string.columnGi, product.gi)
            }
        }
    }
}

@Stable
@Composable
private fun ItemCategoryList(
    categoryItem: TypeCategory,
    isActive: Boolean,
    onClick: (TypeCategory) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.primary
        ),
        border = if (isActive) BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary) else null,
        modifier = Modifier
//            .defaultMinSize(100.dp, 45.dp)
            .width(100.dp)
            .height(80.dp)
            .padding(start = 1.dp, end = 1.dp, top = 2.dp, bottom = 2.dp)
            .clickable { if (isActive.not()) onClick(categoryItem) }
    ) {
//        Image(
//            painter = painterResource(categoryItem.typeCategory.imageId),
//            contentDescription = "",
//            alignment = Alignment.BottomCenter,
//            modifier = Modifier.padding(top = 30.dp)
//        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(categoryItem.nameId),
                modifier = Modifier.padding(5.dp),
                maxLines = 3,
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .padding(5.dp),
                painter = painterResource(categoryItem.imageId),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun RowScope.TableItem(
    @StringRes titleRes: Int,
    value: Double
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
            text = value.toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W500
        )
    }

}

@Preview
@Composable
private fun PreviewLight() = AppTheme(themeMode = ThemeMode.LIGHT) {
    Box(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
    ) {
        ListScreen()
    }
}

@Preview
@Composable
private fun PreviewDark() = AppTheme(themeMode = ThemeMode.DARK) {
    Box(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
    ) {
        ListScreen()
    }
}

@Preview
@Composable
private fun PreviewAmoled() = AppTheme(themeMode = ThemeMode.AMOLED) {
    Box(
        Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
    ) {
        ListScreen()
    }
}