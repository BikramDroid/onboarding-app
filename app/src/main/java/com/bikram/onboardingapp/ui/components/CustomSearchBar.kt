package com.bikram.onboardingapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bikram.onboardingapp.R
import com.bikram.onboardingapp.domain.model.Product

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
fun CustomSearchBar(
    scanButton: () -> Unit,
    onDismiss: () -> Unit,
    lensButton: () -> Unit,
    onCategoryButtonClicked: (String) -> Unit,
    onDetailsButtonClicked: (Int) -> Unit,
    onClearSearchText: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    searchText: String,
    products: List<Product>,
    isSearching: Boolean,
    productsUiState: List<Product>
) {
    var active by rememberSaveable { mutableStateOf(true) }
    val searchHistory = remember { mutableStateListOf("") }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        onClearSearchText()
    }

    Box(Modifier.fillMaxSize()) {
        DockedSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        keyboardController?.show()
                    }
                },
            query = searchText,
            onQueryChange = onSearchTextChange,
            onSearch = {
                searchHistory.add(it)
            },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text(stringResource(id = R.string.search_products)) },
            leadingIcon = {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.clickable {
                        onDismiss()
                    }
                )
            },
            trailingIcon = {
                if (searchText.isEmpty())
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.barcode_scanner),
                            contentDescription = null, tint = Color.Black,
                            modifier = Modifier.clickable {
                                scanButton()
                            }
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.lens),
                            contentDescription = null, tint = Color.Black,
                            modifier = Modifier
                                .padding(start = 5.dp, end = 5.dp)
                                .clickable {
                                    lensButton()
                                }
                        )
                    }
                else
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = null, tint = Color.Black,
                        modifier = Modifier.clickable {
                            onClearSearchText()
                            active = true
                            focusRequester.requestFocus()
                        }
                    )
            },
        ) {
            if (searchText.isEmpty() || isSearching) {
                DefaultScreen(
                    onCategoryButtonClicked,
                    onDetailsButtonClicked,
                    productsUiState.shuffled().take(3)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp)
                        .weight(1f)
                ) {
                    items(products) { person ->
                        Row(
                            modifier = Modifier
                                .padding(start = 15.dp)
                                .height(40.dp)
                                .clickable {
                                    onDetailsButtonClicked(person.id)
                                }) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_outward),
                                tint = Color.Black,
                                contentDescription = null
                            )

                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                text = person.title,
                                style = MaterialTheme.typography.titleMedium.copy(Color.Black)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DefaultScreen(
    onCategoryButtonClicked: (String) -> Unit,
    onDetailsButtonClicked: (Int) -> Unit,
    products: List<Product>
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val list = listOf("Men's wear", "Electronics", "Jewelery", "Women's wear")
        items(count = list.size) {
            Row(
                modifier = Modifier
                    .padding(start = 15.dp)
                    .height(30.dp)
                    .clickable {
                        onCategoryButtonClicked(list[it])
                    }) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_outward),
                    tint = Color.Black,
                    contentDescription = null
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = list[it],
                    style = MaterialTheme.typography.titleMedium.copy(Color.Black)
                )
            }
        }

        items(products.size) { product ->
            ListItem(
                modifier = Modifier.clickable {
                    onDetailsButtonClicked(products[product].id)
                },
                headlineContent = {
                    Text(
                        text = products[product].title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                supportingContent = {
                    Text(
                        text = "kr ${products[product].priceKr}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                },
                leadingContent = {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(products[product].image)
                            .crossfade(true)
                            .build(),
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        error = painterResource(R.drawable.loading_img),
                        placeholder = painterResource(R.drawable.loading_img),
                        contentDescription = stringResource(R.string.product_image),
                        contentScale = ContentScale.Inside
                    )
                },
            )
        }

//                todo need history?
//                items(count = searchHistory.size) {
//                    searchHistory.forEach {
//                        if (it.isNotEmpty()) {
//                            Row(modifier = Modifier.padding(all = 14.dp)) {
//                                Icon(imageVector = Icons.Default.Info, contentDescription = null)
//                                Spacer(modifier = Modifier.width(10.dp))
//                                Text(text = it)
//                            }
//                        }
//                    }
//
//                    Divider()
//                    Text(
//                        modifier = Modifier
//                            .padding(all = 14.dp)
//                            .fillMaxWidth()
//                            .clickable {
//                                searchHistory.clear()
//                            },
//                        textAlign = TextAlign.Center,
//                        fontWeight = FontWeight.Bold,
//                        text = "Clear history"
//                    )
//                }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CustomSearchBarPreview() {
    CustomSearchBar(
        scanButton = {},
        onDismiss = {},
        lensButton = {},
        onCategoryButtonClicked = {},
        onDetailsButtonClicked = {},
        onClearSearchText = {},
        onSearchTextChange = {},
        "", emptyList(), false, emptyList()
    )
}