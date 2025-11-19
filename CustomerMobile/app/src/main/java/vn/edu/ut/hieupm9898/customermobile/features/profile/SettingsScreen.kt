package vn.edu.ut.hieupm9898.customermobile.features.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.edu.ut.hieupm9898.customermobile.ui.theme.CustomerMobileTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onDeleteAccountClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Settings", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineMedium)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                .padding(24.dp)
        ) {
            // Nhóm 1: Preferences
            Text("Preferences", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(icon = Icons.Default.Language, title = "Language", value = "English", onClick = onLanguageClick)
            SettingItem(icon = Icons.Default.Notifications, title = "Push Notifications", value = "On", onClick = {})
            SettingItem(icon = Icons.Default.DarkMode, title = "Dark Mode", value = "Off", onClick = {})

            Spacer(modifier = Modifier.height(32.dp))

            // Nhóm 2: Account
            Text("Account", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            SettingItem(icon = Icons.Default.Lock, title = "Change Password", onClick = {})
            SettingItem(icon = Icons.Default.Security, title = "Privacy Policy", onClick = {})

            Spacer(modifier = Modifier.height(24.dp))

            // Delete Account (Màu đỏ cảnh báo)
            SettingItem(
                icon = Icons.Default.DeleteForever,
                title = "Delete Account",
                iconTint = MaterialTheme.colorScheme.error,
                textColor = MaterialTheme.colorScheme.error,
                onClick = onDeleteAccountClick
            )
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    value: String? = null,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = iconTint)
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, style = MaterialTheme.typography.bodyLarge, color = textColor, modifier = Modifier.weight(1f))

            if (value != null) {
                Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.width(8.dp))
            }

            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        }
        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    CustomerMobileTheme { SettingsScreen() }
}