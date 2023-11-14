package com.bikram.onboardingapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bikram.onboardingapp.R
import com.bikram.onboardingapp.domain.model.Product
import com.bikram.onboardingapp.ui.components.CustomSpacer
import com.bikram.onboardingapp.ui.components.LoadingScreen
import kotlinx.coroutines.delay

@Composable
fun AllProductsScreen(
    productsUiState: List<Product>,
    productsCategory: String,
    onDetailsButtonClicked: (Int) -> Unit
) {
    var show by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = Unit) {
        delay(300)
        show = false
    }

    if (show)
        LoadingScreen()
    else
        ProductsColumn(
            productsUiState, productsCategory, onDetailsButtonClicked
        )
}

@Composable
private fun ProductsColumn(
    products: List<Product>,
    productsCategory: String,
    onDetailsButtonClicked: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp)
    ) {
        LazyColumn {
            //if products category is available from last screen then take it
            //otherwise shuffle products for new/popular etc
            var filteredProducts = products.filter {
                it.category == productsCategory.replace("wear", "clothing").lowercase()
            }

            if (filteredProducts.isEmpty())
                filteredProducts = products.shuffled()

            items(filteredProducts) {
                ProductItem(
                    product = it, onDetailsButtonClicked, modifier = Modifier
                        .fillMaxWidth()
                        .height(135.dp)
                        .padding(8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(
    product: Product,
    onDetailsButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        onClick = {
            onDetailsButtonClicked(product.id)
        }
    ) {
        Row {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(product.image)
                    .crossfade(true)
                    .build(),
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)
                    .fillMaxHeight(),
                error = painterResource(R.drawable.loading_img),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = stringResource(R.string.product_image),
                contentScale = ContentScale.Inside
            )
            CustomSpacer(8.dp)
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(5.dp)
            ) {
                Text(
                    text = product.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                CustomSpacer(5.dp)
                Text(
                    text = "kr ${product.priceKr}",
                    color = Color.Black,
                    fontSize = 22.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AllProductsScreenPreview() {
    AllProductsScreen(emptyList(), productsCategory = "", onDetailsButtonClicked = {})
}