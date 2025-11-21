package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    var phoneNumber by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrosBrown)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrosBackground)
            )
        },
        containerColor = BrosBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text("Forgot Your Password?", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = BrosTitle)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Please enter your registered Phone Number so we can send an OTP Verification code to reset your password.",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Dùng BrosTextField cho nhập SĐT
            Text("Phone Number", fontWeight = FontWeight.Bold, color = BrosTitle)
            Spacer(modifier = Modifier.height(8.dp))
            BrosTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = "Enter Phone Number",
                icon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.weight(1f))

            // Dùng BrosButton
            BrosButton(
                text = "Proceed",
                onClick = {
                    // Chuyển sang màn OTP
                    navController.navigate(AppRoutes.OTP_VERIFICATION)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun ForgotPasswordPreview() {
    ForgotPasswordScreen(navController = rememberNavController())
}