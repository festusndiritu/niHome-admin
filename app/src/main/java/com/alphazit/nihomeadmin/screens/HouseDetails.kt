package com.alphazit.nihomeadmin.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.alphazit.nihomeadmin.models.HouseData
import com.alphazit.nihomeadmin.utilities.logUserAction
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HouseDetailsScreen(houseId: String, navController: NavController) {
    var house by remember { mutableStateOf<HouseData?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(houseId) {
        scope.launch {
            val firestore = Firebase.firestore
            firestore.collection("houses").document(houseId)
                .get()
                .addOnSuccessListener { document ->
                    house = document.toObject(HouseData::class.java)?.copy(id = document.id)
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "House Details",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Feature coming soon", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "edit",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "delete",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
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
        },
        content = { padding ->
            house?.let { houseData ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Text(text = "Name: ${houseData.name}")
                    Text(text = "Phone: ${houseData.phoneNumber}")
                    Text(text = "Location: ${houseData.location}")
                    Text(text = "Price: ${houseData.price}")
                    Text(text = "Size: ${houseData.size}")
                    Text(text = "Status: ${houseData.status}")
                    Text(text = "Type: ${houseData.type}")
                    Text(text = "Amenities: ${houseData.amenities.joinToString(", ")}")

                    Spacer(modifier = Modifier.height(16.dp))

                    if (houseData.imageUrls.isNotEmpty()) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(houseData.imageUrls) { imageUrl ->
                                Image(
                                    painter = rememberAsyncImagePainter(imageUrl),
                                    contentDescription = houseData.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            val index = houseData.imageUrls.indexOf(imageUrl)
                                            navController.navigate("pager/$houseId/$index")
                                        },
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    )

    // Show a Confirmation Dialog Before Deleting
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this house?") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        house?.let { houseData ->
                            // Delete images from Firebase Storage
                            deleteImagesFromStorage(houseData.imageUrls) {
                                // After images are deleted, delete the document from FireStore
                                val firestore = Firebase.firestore
                                firestore.collection("houses").document(houseId)
                                    .delete()
                                    .addOnSuccessListener {
                                        logUserAction("Deleted", houseName = houseData.name)
                                        showDeleteDialog = false
                                        navController.popBackStack()
                                    }
                            }
                        }
                    }
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun deleteImagesFromStorage(imageUrls: List<String>, onComplete: () -> Unit) {
    val storage = Firebase.storage
    var deletedCount = 0

    imageUrls.forEach { imageUrl ->
        val imageRef = storage.getReferenceFromUrl(imageUrl)
        imageRef.delete().addOnSuccessListener {
            deletedCount++
            if (deletedCount == imageUrls.size) {
                onComplete()
            }
        }.addOnFailureListener {
            // Handle failure to delete image (e.g., log the error or notify the user)
        }
    }
}