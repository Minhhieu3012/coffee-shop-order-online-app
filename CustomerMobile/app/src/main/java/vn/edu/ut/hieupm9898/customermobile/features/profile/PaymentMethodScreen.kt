package vn.edu.ut.hieupm9898.customermobile.features.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.edu.ut.hieupm9898.customermobile.ui.components.BrosButton
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme
import androidx.compose.foundation.border

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen(
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Payment Methods", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(24.dp)) {
                BrosButton(text = "Add New Card", onClick = { /* Add Logic */ }, modifier = Modifier.fillMaxWidth())
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Credit Card Visualization (Visa style)
            CreditCardView()

            Spacer(modifier = Modifier.height(32.dp))

            // List Methods
            Text("Other Methods", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            PaymentMethodItem(name = "MoMo E-Wallet", isSelected = true)
            Spacer(modifier = Modifier.height(12.dp))
            PaymentMethodItem(name = "Cash on Delivery", isSelected = false)
        }
    }
}

@Composable
fun CreditCardView() {
    // Thẻ tín dụng giả lập với Gradient màu Nâu
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF6B4226), Color(0xFF8D6E63)) // Gradient nâu
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Credit Card", color = Color.White.copy(alpha = 0.8f))
                Icon(Icons.Default.CreditCard, contentDescription = null, tint = Color.White)
            }

            Text(
                "3829 4820 4629 5025",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                letterSpacing = 2.sp
            )

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("Card Holder", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                    Text("Hieu PM", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Expiry Date", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                    Text("09/28", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun PaymentMethodItem(name: String, isSelected: Boolean) {
    val containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor, RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Radio Button giả
        RadioButton(selected = isSelected, onClick = { /* Select logic */ })
        Spacer(modifier = Modifier.width(8.dp))
        Text(name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.weight(1f))
        if(!isSelected) {
            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Preview
@Composable
fun PaymentPreview() {
    CustomerMobileTheme { PaymentMethodScreen() }
}