package vn.edu.ut.hieupm9898.customermobile.features.auth

// --- PHẦN IMPORT QUAN TRỌNG (Đừng xóa) ---
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.theme.*

@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isTermAccepted by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showSuccessDialog) {
        if (showSuccessDialog) {
            delay(2000)
            showSuccessDialog = false
            navController.navigate(AppRoutes.PROFILE) {
                popUpTo(AppRoutes.REGISTER) { inclusive = true }
            }
        }
    }

    SignUpSuccessDialog(showDialog = showSuccessDialog, onDismiss = {})

    Surface(color = BrosBackground, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp)
                )
            }

            Card(
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = BrosTitle)

                    Row(modifier = Modifier.padding(vertical = 16.dp)) {
                        Text("Already have an account? ", color = BrosSubTitle)
                        Text(
                            "Log In",
                            color = BrosBrown,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                navController.popBackStack()
                            }
                        )
                    }

                    BrosTextField(value = name, onValueChange = { name = it }, label = "Full Name")
                    Spacer(modifier = Modifier.height(16.dp))
                    BrosTextField(value = email, onValueChange = { email = it }, label = "Email", keyboardType = KeyboardType.Email)
                    Spacer(modifier = Modifier.height(16.dp))
                    BrosTextField(value = phone, onValueChange = { phone = it }, label = "Phone Number", keyboardType = KeyboardType.Phone)
                    Spacer(modifier = Modifier.height(16.dp))
                    BrosTextField(value = password, onValueChange = { password = it }, label = "Password", isPassword = true)

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isTermAccepted,
                            onCheckedChange = { isTermAccepted = it },
                            colors = CheckboxDefaults.colors(checkedColor = BrosBrown)
                        )
                        Text("I accept the Terms & Conditions", fontSize = 12.sp, color = BrosSubTitle)
                    }

                    BrosButton(
                        text = "Sign Up",
                        onClick = { showSuccessDialog = true },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    )

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RegisterPreview() {
    RegisterScreen(navController = rememberNavController())
}