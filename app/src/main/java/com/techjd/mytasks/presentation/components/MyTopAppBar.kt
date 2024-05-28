package com.techjd.mytasks.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyTopAppBar(
  title: String,
  leadingIcon: @Composable (() -> Unit)? = null
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.background)
      .padding(12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (leadingIcon != null) {
      leadingIcon()
      Spacer(modifier = Modifier.width(8.dp))
    }
    Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
  }
}