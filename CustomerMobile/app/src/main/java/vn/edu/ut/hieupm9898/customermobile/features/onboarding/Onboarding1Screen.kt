package vn.edu.ut.hieupm9898.customermobile.features.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape // Cần thiết cho Dots
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import vn.edu.ut.hieupm9898.customermobile.R
import vn.edu.ut.hieupm9898.customermobile.navigation.AppRoutes
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosCream
import vn.edu.ut.hieupm9898.customermobile.ui.theme.BrosBrown

@Composable
fun Onboarding1Screen(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BrosCream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(64.dp))

                Image(
                    painter = painterResource(id = R.drawable.image_17 ),
                    contentDescription = "Order Made Simple",
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Order Made Simple",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = BrosBrown
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Browse and order your favorite drinks. Receive easy and fast delivery.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(BrosBrown)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Box(modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(BrosCream.copy(alpha = 0.5f))
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Box(modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(BrosCream.copy(alpha = 0.5f))
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))


                Button(
                    onClick = {
                        navController.navigate(AppRoutes.ONBOARDING_2)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = BrosBrown)
                ) {
                    Text(
                        text = "Next",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}