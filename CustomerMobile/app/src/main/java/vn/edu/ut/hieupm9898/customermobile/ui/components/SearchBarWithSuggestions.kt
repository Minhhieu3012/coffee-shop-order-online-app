package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.edu.ut.hieupm9898.customermobile.data.model.Product
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosSubTitle
import vn.edu.ut.hieupm9898.customermobile.utils.StringUtils.normalizeForSearch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun SearchBarWithSuggestions(
    query: String,
    onQueryChange: (String) -> Unit,
    suggestions: List<Product>,
    showSuggestions: Boolean,
    onSuggestionClick: (Product) -> Unit,
    onClearClick: () -> Unit,
    isSearching: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Search Input
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = {
                Text(
                    text = "Tìm kiếm món ăn, đồ uống...",
                    color = BrosSubTitle
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = BrosSubTitle
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClearClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = BrosSubTitle
                        )
                    }
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.LightGray,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        // Suggestions Dropdown
        AnimatedVisibility(
            visible = showSuggestions && suggestions.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    if (isSearching) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                    } else {
                        suggestions.forEach { product ->
                            SearchSuggestionItem(
                                product = product,
                                query = query,
                                onClick = { onSuggestionClick(product) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchSuggestionItem(
    product: Product,
    query: String,
    onClick: () -> Unit
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Product Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(product.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = product.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Product Info
        Column(modifier = Modifier.weight(1f)) {
            // Highlight matching text
            HighlightedText(
                text = product.name,
                query = query,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Price
        Text(
            text = formatter.format(product.price),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun HighlightedText(
    text: String,
    query: String,
    style: androidx.compose.ui.text.TextStyle
) {
    val normalizedText = text.normalizeForSearch()
    val normalizedQuery = query.normalizeForSearch()

    val startIndex = normalizedText.indexOf(normalizedQuery)

    if (startIndex == -1) {
        Text(text = text, style = style)
        return
    }

    val endIndex = startIndex + query.length

    Row {
        // Text before match
        if (startIndex > 0) {
            Text(
                text = text.substring(0, startIndex),
                style = style
            )
        }

        // Matched text (highlighted)
        Text(
            text = text.substring(startIndex, endIndex.coerceAtMost(text.length)),
            style = style.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )

        // Text after match
        if (endIndex < text.length) {
            Text(
                text = text.substring(endIndex),
                style = style
            )
        }
    }
}