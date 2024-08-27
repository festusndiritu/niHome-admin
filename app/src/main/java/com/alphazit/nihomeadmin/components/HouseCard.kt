package com.alphazit.nihomeadmin.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.alphazit.nihomeadmin.models.HouseData

@Composable
fun HouseCard(house: HouseData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            if (house.imageUrls.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(house.imageUrls[0]),
                    contentDescription = house.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = house.name,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
