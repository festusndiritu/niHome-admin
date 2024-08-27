package com.alphazit.nihomeadmin.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alphazit.nihomeadmin.R
import com.alphazit.nihomeadmin.components.Logo
import com.alphazit.nihomeadmin.utilities.login

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var showPassword by remember { mutableStateOf(false) }

        val context = LocalContext.current
        var isLoading = false

        // App Logo
        Logo()

        Spacer(modifier = Modifier.height(40.dp))

        Text(text = "Welcome Back. You've been missed!", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(24.dp))

        // Email textField
        TextField(
            value = email,
            onValueChange = { email = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(.9f),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            label = {
                Text(text = "Email")
            }
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Password textField
        TextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    val icon = if (!showPassword) R.drawable.ic_showpass else R.drawable.ic_hidepass
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = {
                Text(text = "Password")
            },
            modifier = Modifier.fillMaxWidth(.9f),
            visualTransformation = if (showPassword)
                VisualTransformation.None else PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    isLoading = true
                    login(email, password) { success, errorMessage ->
                        isLoading = false
                        if (success) {
                            navController.navigate("home")
                        } else {
                            Toast.makeText(
                                context,
                                "Login failed: $errorMessage",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Enter email and password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth(.7f)
                .height(53.dp),
            shape = ShapeDefaults.Medium
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    trackColor = Color.White
                )
            } else {
                Text(text = "Login", fontSize = 18.sp)
            }
        }
        Spacer(modifier = Modifier.height(100.dp))

        TextButton(
            onClick = { navController.navigate("register") }
        ) {
            Text(text = "Don't have an account? Register", fontSize = 18.sp)
        }
    }
}