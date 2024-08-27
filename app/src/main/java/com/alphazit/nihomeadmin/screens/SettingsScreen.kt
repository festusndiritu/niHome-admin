package com.alphazit.nihomeadmin.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alphazit.nihomeadmin.BuildConfig
import com.alphazit.nihomeadmin.ui.theme.LocalThemeToggle
import com.alphazit.nihomeadmin.ui.theme.isDarkTheme
import com.alphazit.nihomeadmin.utilities.signOut
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, modifier: Modifier = Modifier) {
    val versionNumber = BuildConfig.VERSION_CODE
    val versionName = BuildConfig.VERSION_NAME

    val showChangeTheme = rememberModalBottomSheetState()
    val context = LocalContext.current
    val toggleTheme = LocalThemeToggle.current
    val isDarkMode = isDarkTheme(context)
    val currentEmail = FirebaseAuth.getInstance().currentUser?.email

    val scope = rememberCoroutineScope()
    val themeText = if (isDarkMode) "Light theme" else "Dark theme"
    val themeIcon = if (isDarkMode) Icons.Default.WbSunny else Icons.Default.NightsStay


    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                            contentDescription = "back",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults
                    .topAppBarColors()
                    .copy(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
            )
        }
    ) { innerPadding ->
        if (showChangeTheme.isVisible) {
            ModalBottomSheet(onDismissRequest = {
                scope.launch {
                    showChangeTheme.hide()
                }
            }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = themeIcon,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        text = "Switch to $themeText?"
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    showChangeTheme.hide()
                                }
                            },
                            shape = ShapeDefaults.Medium
                        ) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                toggleTheme()
                                scope.launch {
                                    showChangeTheme.hide()
                                }
                            },
                            shape = ShapeDefaults.Medium
                        ) {
                            Text(text = "Confirm")
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(text = "Appearance")
            Spacer(modifier = Modifier.height(8.dp))
            Card {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(text = "Dark Mode")
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = {
                            scope.launch {
                                showChangeTheme.show()
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "About")
            Spacer(modifier = Modifier.height(8.dp))
            Card {
                Row(
                    modifier = Modifier
                        .clickable { }
                ) {
                    Text(
                        text = "Terms and Conditions",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 12.dp)
                    )
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .clickable { }
                ) {
                    Text(
                        text = "Privacy Policy",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 12.dp)
                    )
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .clickable { }
                ) {
                    Text(
                        text = "See how your data is managed",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 12.dp)
                    )
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .clickable { }
                ) {
                    Text(
                        text = "Open Source Licenses",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 12.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            Text(text = "Account")
            Spacer(modifier = Modifier.height(8.dp))
            Card {
                Row(
                    modifier = Modifier
                        .clickable { }
                ) {
                    Text(
                        text = "Change password",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 12.dp)
                    )
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier.clickable {
                        signOut { success, errorMessage ->
                            if (!success) {
                                Toast.makeText(
                                    context,
                                    "Failed to signOut: $errorMessage",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Text(
                        text = "Sign Out",
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                onClick = {

                },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Delete Account")
                        if (currentEmail != null) {
                            Text(text = currentEmail, color = MaterialTheme.colorScheme.outline)
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "App Version $versionNumber ($versionName)",
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}