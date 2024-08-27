package com.alphazit.nihomeadmin.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.alphazit.nihomeadmin.R
import com.alphazit.nihomeadmin.components.Logo
import com.alphazit.nihomeadmin.utilities.register

@Composable
fun RegisterScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TextFields states
        var email by remember { mutableStateOf("") }
        var firstName by remember { mutableStateOf("") }
        var lastName by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var showPassword by remember { mutableStateOf(false) }
        var showConfirmPassword by remember { mutableStateOf(false) }

        var isLoading = false

        val context = LocalContext.current

        // Logo
        Logo()
        Spacer(modifier = Modifier.height(40.dp))

        // Welcome message
        Text(text = "Let's create an account for you!", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth(.9f)
        ) {
            // Name field
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                label = {
                    Text(text = "First Name")
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                modifier = Modifier.weight(1f),
                singleLine = true,
                label = {
                    Text(text = "Last Name")
                }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Email textField
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(.9f),
            singleLine = true,
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
        Spacer(modifier = Modifier.height(12.dp))

        // Confirm Password textField
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                    val icon =
                        if (!showConfirmPassword) R.drawable.ic_showpass else R.drawable.ic_hidepass
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            label = {
                Text(text = "Confirm password")
            },
            modifier = Modifier.fillMaxWidth(.9f),
            visualTransformation = if (showConfirmPassword)
                VisualTransformation.None else PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Register Button
        Button(
            onClick = {
                if (password != confirmPassword) {
                    Toast.makeText(
                        context,
                        "Passwords do not match",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if ((firstName.isNotEmpty() || lastName.isNotEmpty()) && email.isNotEmpty() && password.isNotEmpty()) {
                    isLoading = true
                    val fullName = "$firstName $lastName"
                    register(fullName, email, password) { success, errorMessage ->
                        isLoading = false
                        if (success) {
                            navController.navigate("login")
                        } else {
                            Toast.makeText(
                                context,
                                "Account registration failed: $errorMessage",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Please fill in all fields",
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
                CircularProgressIndicator(modifier = Modifier.size(30.dp))
            } else {
                Text(text = "Register", fontSize = 18.sp)
            }
        }
        Spacer(modifier = Modifier.height(100.dp))

        TextButton(
            onClick = { navController.popBackStack() }
        ) {
            Text(text = "Already have an account? Login", fontSize = 18.sp)
        }
    }
}