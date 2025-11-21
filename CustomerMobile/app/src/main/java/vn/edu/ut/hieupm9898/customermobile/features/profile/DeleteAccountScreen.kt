package vn.edu.ut.hieupm9898.customermobile.features.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountScreen(
    onBackClick: () -> Unit = {},
    onConfirmDelete: () -> Unit = {}
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Xóa tài khoản", fontWeight = FontWeight.Bold, fontSize = 30.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(60.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Text(
                "Bạn sắp xóa tài khoản của mình!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error, // Màu đỏ cảnh báo
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Không thể hoàn tác hành động này. Toàn bộ dữ liệu của bạn, bao gồm lịch sử đơn hàng, phần thưởng và thông tin cá nhân, sẽ bị xóa vĩnh viễn.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(280.dp))

            // Nút Xóa
            BrosButton(
                text = "Delete Account",
                onClick = onConfirmDelete,
                modifier = Modifier.fillMaxWidth().height(60.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nút Hủy
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteAccountPreview() {
    CustomerMobileTheme { DeleteAccountScreen() }
}