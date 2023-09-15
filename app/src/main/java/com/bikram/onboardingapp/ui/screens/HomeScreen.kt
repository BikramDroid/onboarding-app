package com.bikram.onboardingapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bikram.onboardingapp.R
import com.bikram.onboardingapp.model.Product
import com.bikram.onboardingapp.ui.components.CustomSpacer
import com.bikram.onboardingapp.ui.viewmodels.ProductsUiState

@Composable
fun HomeScreen(
    productsUiState: ProductsUiState, modifier: Modifier = Modifier
) {
    when (productsUiState) {
        is ProductsUiState.Loading -> InfoScreen(modifier = modifier.fillMaxSize())
        is ProductsUiState.Success -> ResultScreen(
            productsUiState.products, modifier = modifier.fillMaxWidth()
        )

        is ProductsUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
    }
}

//The home screen displaying the loading message.
@Composable
fun InfoScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

//The home screen displaying error message.
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

//ResultScreen displaying the products retrieved.
@Composable
fun ResultScreen(products: List<Product>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(10.dp),
        modifier = Modifier.padding(top = 55.dp, bottom = 55.dp)
    ) {
        item {
            BannerCard()
        }

        item {
            CategoryRow()
        }

        item {
            ProductsRow(stringResource(id = R.string.popular_products), products)
        }

        item {
            BannerCard()
        }

        item {
            ProductsRow(stringResource(id = R.string.newly_arrived), products)
        }

//        Add more?
//        item {
//            ProductsRow(stringResource(id = R.string.only_for_you), products)
//        }
    }
}

@Composable
private fun ProductsRow(rowDescription: String, products: List<Product>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        CustomSpacer(20.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = rowDescription,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.see_more),
                color = MaterialTheme.colorScheme.secondary
            )
        }

        CustomSpacer()
        ProductsCard(products)

        CustomSpacer(25.dp)
    }
}

@Composable
private fun ProductsCard(products: List<Product>) {
    LazyRow(
        state = rememberLazyListState(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(products.shuffled().take(8)) {
            val product = it

            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                modifier = Modifier
                    .height(248.dp)
            ) {
                Column(modifier = Modifier
                    .clickable {
                        //todo handle
                    }) {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .padding(10.dp)
                            .background(Color.White, shape = RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(product.imgSrc)
                                .crossfade(true)
                                .build(),
                            error = painterResource(R.drawable.loading_img),
                            placeholder = painterResource(R.drawable.loading_img),
                            contentDescription = stringResource(R.string.product_image),
                            contentScale = ContentScale.Inside
                        )
                    }

                    CustomSpacer()

                    Text(
                        text = product.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .width(150.dp)
                    )

                    CustomSpacer(6.dp)

                    Row(
                        modifier = Modifier
                            .width(150.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "kr ${product.priceKr}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        CategoryCard(Icons.Filled.AccountCircle, "men's wear")
        CategoryCard(Icons.Filled.Phone, "electronics")
        CategoryCard(Icons.Filled.ShoppingCart, "jewelery")
        CategoryCard(Icons.Filled.Face, "women's wear")
    }
}

@Composable
private fun CategoryCard(imageVector: ImageVector, categoryTitle: String) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(5.dp)
            .clickable {
                //todo handle
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector, categoryTitle, tint = Color.White,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .padding(10.dp)
        )

        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = categoryTitle,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BannerCard() {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(getRandomBanner())
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.loading_img),
            placeholder = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.product_image),
            contentScale = ContentScale.Crop
        )
    }
}

//get a random banner everytime
private fun getRandomBanner(): Int {
    val res = intArrayOf(
        R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3, R.drawable.banner_4,
        R.drawable.banner_5, R.drawable.banner_6, R.drawable.banner_7, R.drawable.banner_8
    )

    return res.random()
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(ProductsUiState.Loading)
}