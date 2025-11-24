package vn.edu.ut.hieupm9898.customermobile.features.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Thêm địa chỉ", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(60.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(24.dp).fillMaxSize()
        ) {
            BrosTextField(value = name, onValueChange = { name = it }, label = "Tên địa chỉ (ví dụ: Nhà riêng, Cơ quan)")
            Spacer(modifier = Modifier.height(16.dp))
            BrosTextField(value = detail, onValueChange = { detail = it }, label = "Địa chỉ đầy đủ")
            Spacer(modifier = Modifier.height(16.dp))
            BrosTextField(value = note, onValueChange = { note = it }, label = "Lưu ý cho tài xế (Tùy chọn)")

            Spacer(modifier = Modifier.height(400.dp))

            BrosButton(text = "Lưu địa chỉ", onClick = onSaveClick, modifier = Modifier.fillMaxWidth().height(60.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddAddressPreview() {
    CustomerMobileTheme {
        AddAddressScreen()
    }
}