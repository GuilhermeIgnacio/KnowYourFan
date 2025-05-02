package com.guilherme.knowyourfan.knowyourfan.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import knowyourfan.composeapp.generated.resources.Res
import knowyourfan.composeapp.generated.resources.furia_logo
import knowyourfan.composeapp.generated.resources.link_icon
import knowyourfan.composeapp.generated.resources.x_twitter_brands
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onReturnButtonClicked: () -> Unit,
) {

    Column(
        modifier = Modifier.statusBarsPadding().navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        IconButton(onClick = { onReturnButtonClicked() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = ""
            )
        }

        Image(
            modifier = Modifier.size(64.dp).align(Alignment.CenterHorizontally),
            bitmap = imageResource(Res.drawable.furia_logo),
            contentDescription = ""
        )

        val padding = 16.dp

        Text(
            modifier = Modifier.padding(horizontal = padding),
            text = "Connected Accounts",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )

        Surface(
            modifier = Modifier.padding(horizontal = padding),
            shape = RoundedCornerShape(30f),
            color = Color(0xFF1f2937)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = vectorResource(Res.drawable.x_twitter_brands),
                    contentDescription = ""
                )

                Column {
                    Text(
                        text = "X (Twitter)",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "Not Connected",
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = {}) {
                    Icon(
                        vectorResource(Res.drawable.link_icon),
                        contentDescription = "",
                        tint = Color.White
                    )
                }

            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max).padding(horizontal = padding),
            onClick = { },
            shape = RoundedCornerShape(30f),
            colors = ButtonDefaults.buttonColors()
                .copy(containerColor = Color.Red, contentColor = Color.White)
        ) {
            Text(text = "Sign Out", fontWeight = FontWeight.Bold)
        }

    }

}