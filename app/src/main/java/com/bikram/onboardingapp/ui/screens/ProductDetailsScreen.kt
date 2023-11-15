package com.bikram.onboardingapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.DownloadForOffline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bikram.onboardingapp.R
import com.bikram.onboardingapp.domain.model.Product
import com.bikram.onboardingapp.ui.components.CustomSpacer
import com.bikram.onboardingapp.ui.components.CustomToast
import com.bikram.onboardingapp.ui.viewmodels.ProductState

@Composable
fun ProductDetailsScreen(
    productsUiState: ProductState,
    productId: Int,
    onDismiss: () -> Unit,
    onARView: () -> Unit,
    onMoreButtonClicked: (String) -> Unit,
    onDetailsButtonClicked: (Int) -> Unit
) {
    if (productId > productsUiState.products.size) {
        CustomToast(stringResource(id = R.string.not_found), LocalContext.current)
        onDismiss()
    } else {
        DetailsScreenContent(
            product = productsUiState.products[productId - 1],
            modifier = Modifier
                .fillMaxSize(),
            onARView, productsUiState, onMoreButtonClicked, onDetailsButtonClicked
        )
    }
}

@Composable
fun DetailsScreenContent(
    product: Product,
    modifier: Modifier = Modifier,
    onARView: () -> Unit,
    productsUiState: ProductState,
    onMoreButtonClicked: (String) -> Unit,
    onDetailsButtonClicked: (Int) -> Unit
) {
    LazyColumn {
        item {
            Box(
                modifier = modifier
                    .padding(top = 60.dp)
                    .height(240.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(product.image)
                        .crossfade(true)
                        .build(),
                    modifier = modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .align(Alignment.Center),
                    error = painterResource(R.drawable.loading_img),
                    placeholder = painterResource(R.drawable.loading_img),
                    contentDescription = stringResource(R.string.product_image),
                    contentScale = ContentScale.Inside
                )

                Icon(
                    painter = painterResource(id = R.drawable.view_ar),
                    contentDescription = null, tint = Color.Black,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 10.dp)
                        .clickable {
                            onARView()
                        }
                )
            }
        }

        item {
            CustomSpacer()
            MakeProductCard(modifier, product)
        }

        item {
            CustomSpacer()
            MakeProductInfoCard(modifier, product.description)
        }

        item {
            CustomSpacer()
            MakeAboutCard(modifier, product.id)
        }

        item {
            CustomSpacer()
            MakeDocumentationCard(modifier)
        }

        item {
            CustomSpacer()

            val filteredProducts = productsUiState.products.filter {
                it.category == product.category.replace("wear", "clothing").lowercase() &&
                        it.id != product.id
            }

            MakeMoreLikeThisCard(
                filteredProducts,
                false,
                onDetailsButtonClicked
            )
        }

        item {
            CustomSpacer()
            SeeMoreInfoCard(modifier)
        }
    }
}

@Composable
private fun MakeProductCard(modifier: Modifier = Modifier, product: Product) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = product.title,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
            )

            CustomSpacer()

            Text(
                text = "kr ${product.priceKr}",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MakeProductInfoCard(modifier: Modifier = Modifier, productDesc: String) {
    var isExpanded by remember { mutableStateOf(false) }
    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
    var isClickable by remember { mutableStateOf(false) }
    var adjustedText by remember { mutableStateOf(productDesc) }
    var isOverFlow by remember { mutableStateOf(false) }
    val maxLines = 3

    val textLayoutResult = textLayoutResultState.value
    LaunchedEffect(textLayoutResult) {
        if (textLayoutResult == null) return@LaunchedEffect

        when {
            isExpanded -> {
                adjustedText = productDesc
            }

            !isExpanded && textLayoutResult.hasVisualOverflow -> {
                val lastCharIndex = textLayoutResult.getLineEnd(maxLines - 1)
                val showMoreString = "... Read more"
                adjustedText = productDesc
                    .substring(startIndex = 0, endIndex = lastCharIndex)
                    .dropLast(showMoreString.length)
                    .dropLastWhile { it == ' ' || it == '.' }

                isClickable = true
                isOverFlow = true
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.prod_info),
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            CustomSpacer()

            Text(
                text = buildAnnotatedString {
                    if (!isExpanded) {
                        withStyle(style = SpanStyle(color = Color.Black)) {
                            append(adjustedText)
                        }
                        if (isOverFlow)
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("... Read more")
                            }
                    } else {
                        withStyle(style = SpanStyle(color = Color.Black)) {
                            append(adjustedText)
                        }
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(" Show Less")
                        }
                    }
                },
                maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
                onTextLayout = { textLayoutResultState.value = it },
                color = Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                modifier = modifier
                    .clickable(enabled = isClickable) { isExpanded = !isExpanded }
                    .animateContentSize(),
            )
        }
    }
}

@Composable
private fun MakeAboutCard(modifier: Modifier = Modifier, product: Int) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.about_product),
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
            )

            CustomSpacer()

            MakeAboutRows("Product Number", product.toString())
            MakeAboutRows("Height (cm)", (50..150).shuffled().last().toString())
            MakeAboutRows("Width (cm)", (20..100).shuffled().last().toString())
            MakeAboutRows("Material", "N/A")
        }
    }
}

@Composable
private fun MakeAboutRows(headerText: String, infoText: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = headerText,
            color = Color.Black,
            modifier = Modifier.weight(1f),
            fontSize = 15.sp,
            fontWeight = FontWeight.Light
        )

        Text(
            text = infoText,
            color = Color.Black,
            modifier = Modifier.weight(1f),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun MakeDocumentationCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.documentation),
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            CustomSpacer()

            MakeDownloadRows("Manual_1.pdf")
            MakeDownloadRows("Manual_2.pdf")
        }
    }
}

@Composable
private fun MakeDownloadRows(headerText: String) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://file.io/wdRjnncxv4Kn")
                    context.startActivity(intent)
                }
        ) {
            Icon(
                imageVector = Icons.Rounded.AttachFile,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .weight(1f)
            )

            Text(
                text = headerText,
                color = Color.Black,
                modifier = Modifier.weight(5f),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )

            CustomSpacer()

            Icon(
                imageVector = Icons.Rounded.DownloadForOffline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun SeeMoreInfoCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(
            text = stringResource(R.string.see_more_web),
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun MakeMoreLikeThisCard(
    products: List<Product>,
    reversed: Boolean,
    onDetailsButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.more_like_this),
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            CustomSpacer()

            ProductsCard(products, reversed, onDetailsButtonClicked)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProductDetailsScreenPreview() {
    ProductDetailsScreen(
        ProductState(),
        productId = 0,
        onDismiss = {},
        onARView = {},
        onMoreButtonClicked = {},
        onDetailsButtonClicked = {})
}