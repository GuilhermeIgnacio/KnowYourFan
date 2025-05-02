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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import knowyourfan.composeapp.generated.resources.Res
import knowyourfan.composeapp.generated.resources.connected
import knowyourfan.composeapp.generated.resources.connected_accounts
import knowyourfan.composeapp.generated.resources.furia_logo
import knowyourfan.composeapp.generated.resources.link_icon
import knowyourfan.composeapp.generated.resources.not_connected
import knowyourfan.composeapp.generated.resources.sign_out
import knowyourfan.composeapp.generated.resources.x_twitter_brands
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onReturnButtonClicked: () -> Unit,
    onSignOutButtonClicked: () -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            val result = snackBarHostState.showSnackbar(message = getString(it))
            when (result) {
                SnackbarResult.Dismissed -> {
                    viewModel.clearErrorMessage()
                }

                SnackbarResult.ActionPerformed -> {}
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) {
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
                text = stringResource(Res.string.connected_accounts),
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
                            text = if (state.isLinkedWithX) stringResource(Res.string.connected) else stringResource(
                                Res.string.not_connected
                            ),
                            color = if (state.isLinkedWithX) Color.Green else Color.Unspecified,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = { onEvent(ProfileEvents.LinkXAccount) }) {
                        Icon(
                            imageVector = if (state.isLinkedWithX) Icons.Default.Check else vectorResource(
                                Res.drawable.link_icon
                            ),
                            contentDescription = "",
                            tint = if (state.isLinkedWithX) Color.Green else Color.White
                        )
                    }

                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)
                    .padding(horizontal = padding),
                onClick = {
                    onEvent(ProfileEvents.OnSignOutButtonClicked)
                    onSignOutButtonClicked()
                },
                shape = RoundedCornerShape(30f),
                colors = ButtonDefaults.buttonColors()
                    .copy(containerColor = Color.Red, contentColor = Color.White)
            ) {
                Text(text = stringResource(Res.string.sign_out), fontWeight = FontWeight.Bold)
            }

        }
    }

}