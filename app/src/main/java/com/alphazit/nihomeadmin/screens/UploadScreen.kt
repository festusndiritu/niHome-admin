package com.alphazit.nihomeadmin.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.alphazit.nihomeadmin.models.HouseData
import com.alphazit.nihomeadmin.utilities.MediaPicker
import com.alphazit.nihomeadmin.utilities.PhoneNumberVisualTransformation
import com.alphazit.nihomeadmin.utilities.logUserAction
import com.alphazit.nihomeadmin.utilities.requestPermissions
import com.alphazit.nihomeadmin.utilities.saveHouseData
import com.alphazit.nihomeadmin.utilities.uploadImagesToFirebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun UploadScreen(navController: NavController, modifier: Modifier = Modifier) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var showStatus by remember { mutableStateOf(false) }
    var showType by remember { mutableStateOf(false) }
    val amenities = remember { mutableStateListOf<String>() }
    var newAmenity by remember { mutableStateOf("") }

    var isUploading by remember { mutableStateOf(false) }
    var showAmenityDialog by remember { mutableStateOf(false) }
    var imageProgress by remember { mutableStateOf(mapOf<Uri, Float>()) }

    val permissionGranted = requestPermissions()
    val context = LocalContext.current

    var selectedImages by remember { mutableStateOf(listOf<Uri>()) }
    var downloadUrls by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Upload house details",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Name input
            item {
                OutlinedTextField(
                    value = name,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { name = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = "Apartment Name",
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                )
            }

            // Phone number input
            item {
                OutlinedTextField(
                    value = phone,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    onValueChange = {
                        if (it.length <= 10) {
                            phone = it
                        }
                    },
                    singleLine = true,
                    label = {
                        Text(
                            text = "Phone number",
                            color = MaterialTheme.colorScheme.outline
                        )
                    },
                    placeholder = {
                        Text(text = "071 234 5678")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    visualTransformation = PhoneNumberVisualTransformation()
                )
            }

            // Location input
            item {
                OutlinedTextField(
                    value = location,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    onValueChange = { location = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = "House Location",
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                )
            }

            // Price and size inputs
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = price,
                        modifier = Modifier.weight(1f),
                        onValueChange = { price = it },
                        singleLine = true,
                        prefix = { Text(text = "Kes. ") },
                        label = {
                            Text(
                                text = "Monthly Price",
                                color = MaterialTheme.colorScheme.outline
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    OutlinedTextField(
                        value = size,
                        modifier = Modifier.weight(1f),
                        onValueChange = { size = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        suffix = { Text(text = "Square ft.") },
                        label = {
                            Text(
                                text = "House Size",
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    )
                }
            }

            // Status and Type inputs
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "House Status")
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = {
                                showStatus = !showStatus
                            },
                            shape = ShapeDefaults.Small,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = status.ifBlank { "Choose Status" },
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        DropdownMenu(
                            expanded = showStatus,
                            onDismissRequest = { showStatus = false },
                            modifier = Modifier.width(140.dp)
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    status = "Vacant"
                                    showStatus = false
                                },
                                text = { Text(text = "Vacant") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    status = "Rented"
                                    showStatus = false
                                },
                                text = { Text(text = "Rented") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "House Type")
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = {
                                showType = !showType
                            },
                            shape = ShapeDefaults.Small,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = type.ifBlank { "Choose type" },
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        DropdownMenu(
                            expanded = showType,
                            onDismissRequest = { showType = false },
                            modifier = Modifier.width(140.dp)
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    type = "Shop"
                                    showType = false
                                },
                                text = { Text(text = "Shop") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    type = "Single Room"
                                    showType = false
                                },
                                text = { Text(text = "Single Room") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    type = "Double Room"
                                    showType = false
                                },
                                text = { Text(text = "Double Room") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    type = "Bedsitter"
                                    showType = false
                                },
                                text = { Text(text = "Bedsitter") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    type = "One Bedroom"
                                    showType = false
                                },
                                text = { Text(text = "One Bedroom") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    type = "Two Bedroom"
                                    showType = false
                                },
                                text = { Text(text = "Two Bedroom") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    type = "Three Bedroom"
                                    showType = false
                                },
                                text = { Text(text = "Three Bedroom") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    type = "Own Compound"
                                    showType = false
                                },
                                text = { Text(text = "Own Compound") }
                            )
                        }
                    }
                }
            }

            // Amenities picker
            item {
                Text(
                    text = "House Amenities",
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 4.dp)
                        .fillMaxWidth()
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    amenities.forEach { amenity ->
                        FilterChip(
                            selected = true,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            onClick = { amenities.remove(amenity) },
                            label = { Text(amenity) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Remove $amenity"
                                )
                            },
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = true
                            )
                        )
                    }
                    FilterChip(
                        selected = false,
                        onClick = { showAmenityDialog = true },
                        label = { Text("Add Amenity") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Amenity"
                            )
                        },
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = false
                        )
                    )
                }

                // Dialog for adding a new amenity
                if (showAmenityDialog) {
                    AlertDialog(
                        onDismissRequest = { showAmenityDialog = false },
                        title = { Text(text = "Add Amenity") },
                        text = {
                            Column {
                                OutlinedTextField(
                                    value = newAmenity,
                                    onValueChange = { newAmenity = it },
                                    label = { Text("Amenity Name") },
                                    singleLine = true,
                                    placeholder = { Text("e.g., Swimming Pool") }
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    if (newAmenity.isNotBlank()) {
                                        amenities.add(newAmenity.trim())
                                        newAmenity = ""
                                    }
                                }
                            ) {
                                Text("Add")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showAmenityDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Images picker and upload
            item {
                if (permissionGranted) {
                    MediaPicker { uris -> selectedImages = uris }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Display images with progress bars
                Text(text = "Selected Images", modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(selectedImages) { uri ->
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(70.dp)
                            )
                            if (imageProgress[uri] != null) {
                                LinearProgressIndicator(
                                    progress = { imageProgress[uri]!! / 100f },
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .fillMaxWidth()
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isBlank() || location.isBlank() || price.isBlank() ||
                            size.isBlank() || status.isBlank() || type.isBlank() || phone.isBlank()
                        ) {
                            Toast.makeText(
                                context,
                                "Please fill in all details",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            isUploading = true
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    uploadImagesToFirebase(selectedImages, { urls ->
                                        downloadUrls = urls
                                    }, { uri, progress ->
                                        imageProgress = imageProgress + (uri to progress)
                                    })
                                    // Save the house data after upload
                                    withContext(Dispatchers.Main) {
                                        saveHouseData(
                                            HouseData(
                                                name = name,
                                                location = location,
                                                price = "Kes. $price",
                                                size = "$size sqft",
                                                status = status,
                                                phoneNumber = phone,
                                                type = type,
                                                amenities = amenities,
                                                imageUrls = downloadUrls
                                            )
                                        )
                                        logUserAction("Added", houseName = name)

                                        // Clear fields and images on success
                                        isUploading = false
                                        Toast.makeText(
                                            context,
                                            "Data successfully uploaded!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.popBackStack()
                                    }
                                } catch (e: Exception) {
                                    // Handle exceptions
                                    withContext(Dispatchers.Main) {
                                        isUploading = false
                                        Toast.makeText(
                                            context,
                                            "Upload failed. Please try again.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(.6f)
                        .height(53.dp),
                    shape = ShapeDefaults.Medium
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Upload House Data")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}