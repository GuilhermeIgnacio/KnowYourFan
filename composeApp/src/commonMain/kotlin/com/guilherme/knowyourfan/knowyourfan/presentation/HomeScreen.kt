package com.guilherme.knowyourfan.knowyourfan.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import knowyourfan.composeapp.generated.resources.Res
import knowyourfan.composeapp.generated.resources.x_twitter_brands
import org.jetbrains.compose.resources.vectorResource

@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {

        Spacer(Modifier.height(16.dp))

        if (!state.isLinkedWithX) {
            LinkAccount(
                modifier = Modifier.weight(1f),
                onEvent = onEvent
            )
        } else {

            Button(onClick = { viewModel.getRecommendations() }) {
                Text("Generate Recommendations")
            }

        }

    }
}

@Composable
private fun LinkAccount(
    modifier: Modifier = Modifier,
    onEvent: (HomeEvents) -> Unit,
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Link your X account",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(Modifier.height(8.dp))

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Get better personalized information and recommendations",
        textAlign = TextAlign.Center
    )

    Spacer(modifier)

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            onClick = { onEvent(HomeEvents.OnLinkWithXButtonClicked) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors().copy(
                contentColor = Color.Black,
                containerColor = Color.White,
            )
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = vectorResource(Res.drawable.x_twitter_brands),
                contentDescription = ""
            )

            Spacer(Modifier.width(8.dp))

            Text(text = "Continue With X")
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            onClick = { onEvent(HomeEvents.OnLinkWithXButtonClicked) },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors().copy(
                contentColor = Color.White,
                containerColor = Color(0xFF1f2937),
            )
        ) {
            Text(text = "Continue Without X")
        }
    }
}