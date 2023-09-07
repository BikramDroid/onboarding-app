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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
        contentPadding = PaddingValues(10.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 5.dp, end = 15.dp, top = 50.dp)
            ) {
                CustomSpacer()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.popular_products),
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

                //Popular products
                LazyRow(
                    state = rememberLazyListState(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    items(products) {
                        val product = it

                        Card(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 10.dp
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White,
                            ),
                            modifier = Modifier
                                .height(248.dp)
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .size(150.dp)
                                        .padding(10.dp)
                                        .background(Color.White, shape = RoundedCornerShape(10.dp))
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            //todo handle
                                        },
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
        }

        item {
            CustomSpacer(25.dp)
        }

        item {
            Card(
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

        item {
            CustomSpacer(20.dp)
        }

        //to be followed by more stuff
    }
}

//get a random banner everytime
private fun getRandomBanner(): Int {
    val res = intArrayOf(R.drawable.banner_1, R.drawable.banner_2, R.drawable.banner_3)
    return res.random()
}