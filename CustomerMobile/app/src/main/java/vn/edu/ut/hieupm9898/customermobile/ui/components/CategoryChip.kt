package vn.edu.ut.hieupm9898.customermobile.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

/**
 * Chip (thẻ) để lọc danh mục (ví dụ: "Tất cả", "Cà phê", "Trà").
 * Tự động đổi màu khi được chọn (selected).
 * @param text Tên danh mục.
 * @param isSelected Trạng thái (đã được chọn hay chưa).
 * @param onClick Hàm (lambda) được gọi khi nhấn vào.
 */
@OptIn(ExperimentalMaterial3Api::class) // Cần có để dùng FilterChip
@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(text, style = MaterialTheme.typography.bodyMedium) },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryChipPreview() {
    CustomerMobileTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            // Test 2 trạng thái
            Row {
                CategoryChip(text = "Cà phê", isSelected = true, onClick = {})
                Spacer(modifier = Modifier.width(8.dp))
                CategoryChip(text = "Trà", isSelected = false, onClick = {})
            }
        }
    }
}