package vn.edu.ut.hieupm9898.customermobile.features.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosTextField
import vn.edu.ut.hieupm9898.customermobile.ui.components.StatusScreen
import vn.edu.ut.hieupm9898.customermobile.ui.theme.*

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast

@Composable
fun LoginScreen(
    navController: NavHostController,
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoginSuccess by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account.idToken?.let { token ->
                viewModel.onGoogleSignInClicked(token)
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google Sign-In lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("743926532574-tg8hnq21k7qfdcgicftqh03c76gutc02.apps.googleusercontent.com")
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    LaunchedEffect(key1 = isLoginSuccess) {
        if (isLoginSuccess) {
            delay(2000)
            isLoginSuccess = false
            navController.navigate(AppRoutes.HOME) {
                popUpTo(AppRoutes.LOGIN) { inclusive = true }
            }
            onLoginSuccess()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is AuthNavEvent.NavigateToHome -> {
                    isLoginSuccess = true
                }
                else -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BrosBackground)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(180.dp)
                )
            }

            Card(
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Đăng nhập", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = BrosTitle)

                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text("Bạn không có tài khoản? ", color = BrosSubTitle, fontSize = 16.sp)
                        Text(
                            "Đăng ký", color = BrosBrown, fontWeight = FontWeight.Bold, fontSize = 16.sp,
                            modifier = Modifier.clickable { navController.navigate(AppRoutes.REGISTER) }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    BrosTextField(
                        value = email, onValueChange = { email = it },
                        label = "Email", keyboardType = KeyboardType.Email
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    BrosTextField(
                        value = password, onValueChange = { password = it },
                        label = "Password", isPassword = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe, onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(checkedColor = BrosBrown)
                            )
                            Text("Luôn ghi nhớ", fontSize = 12.sp, color = BrosSubTitle)
                        }
                        Text(
                            "Quên mật khẩu?", fontSize = 14.sp, color = BrosBrown,
                            modifier = Modifier.clickable { navController.navigate(AppRoutes.FORGOT_PASSWORD) }
                        )
                    }

                    uiState.errorMessage?.let {
                        Text(text = it, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
                    }

                    BrosButton(
                        text = if (uiState.isLoading) "Đang xử lý..." else "Đăng nhập",
                        onClick = {
                            viewModel.updateField("email", email)
                            viewModel.updateField("password", password)
                            viewModel.onLoginClicked(isRememberMe = rememberMe)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        HorizontalDivider(Modifier.weight(1f), color = Color.LightGray)
                        Text(" Hoặc ", modifier = Modifier.padding(horizontal = 8.dp), color = BrosSubTitle)
                        HorizontalDivider(Modifier.weight(1f), color = Color.LightGray)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    BrosButton(
                        text = "Đăng ký",
                        onClick = { navController.navigate(AppRoutes.REGISTER) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            googleSignInLauncher.launch(googleSignInClient.signInIntent)
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BrosSubTitle),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = null, modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Tiếp tục với Google", fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)).clickable(enabled = false){}, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        if (isLoginSuccess) {
            StatusScreen(
                icon = Icons.Default.Person,
                title = "Đăng nhập",
                statusText = "thành công !",
                subtitle = "Đang chuyển hướng...",
                onTimeout = {}
            )
        }
    }
}