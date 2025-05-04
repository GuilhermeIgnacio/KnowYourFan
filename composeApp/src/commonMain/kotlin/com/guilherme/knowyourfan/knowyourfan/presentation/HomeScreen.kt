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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guilherme.knowyourfan.knowyourfan.presentation.composables.LoadingIcon
import knowyourfan.composeapp.generated.resources.Res
import knowyourfan.composeapp.generated.resources.continue_with_x
import knowyourfan.composeapp.generated.resources.continue_without_x
import knowyourfan.composeapp.generated.resources.furia_logo
import knowyourfan.composeapp.generated.resources.link_x_account
import knowyourfan.composeapp.generated.resources.link_x_account_benefits
import knowyourfan.composeapp.generated.resources.try_again_button_label
import knowyourfan.composeapp.generated.resources.x_twitter_brands
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onProfileButtonClicked: () -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    val snackBarHostState = remember { SnackbarHostState() }

    Spacer(Modifier.height(16.dp))

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            val foo = snackBarHostState.showSnackbar(message = getString(it))
            when (foo) {
                SnackbarResult.Dismissed -> {
                    viewModel.clearErrorMessage()
                }

                SnackbarResult.ActionPerformed -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        if (!state.isLinkedWithX) {
            LinkAccount(
                onEvent = onEvent
            )
        } else {
            LaunchedEffect(Unit) { onEvent(HomeEvents.OnHomeScreenLoaded) }
            LazyColumn(
                modifier = Modifier.fillMaxWidth().statusBarsPadding().navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        IconButton(onClick = {}) {
                            Image(
                                modifier = Modifier.size(26.dp),
                                bitmap = imageResource(Res.drawable.furia_logo),
                                contentDescription = ""
                            )
                        }

                        IconButton(onClick = { onProfileButtonClicked() }) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = ""
                            )
                        }
                    }
                }

                if (state.isError) {
                    item {
                        Button(
                            modifier = Modifier,
                            onClick = { onEvent(HomeEvents.OnTryAgainButtonClicked) }
                        ) {
                            Text(stringResource(Res.string.try_again_button_label))
                        }
                    }
                }

                items(state.recommendations) {

                    if (it.title.isNotEmpty() && it.link.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                            onClick = { onEvent(HomeEvents.OnCardClicked(it.link)) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors()
                                .copy(
                                    containerColor = Color(0xFF1f2937),
                                    contentColor = Color.White
                                )
                        ) {
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = it.title,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }
            }
        }
    }

    if (state.isLoading) {
        LoadingIcon()
    }

}

@Composable
private fun LinkAccount(
    onEvent: (HomeEvents) -> Unit,
) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.link_x_account),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.link_x_account_benefits),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(1f))

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

                Text(text = stringResource(Res.string.continue_with_x))
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                onClick = { onEvent(HomeEvents.OnContinueWithoutXButtonClicked) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    contentColor = Color.White,
                    containerColor = Color(0xFF1f2937),
                )
            ) {
                Text(text = stringResource(Res.string.continue_without_x))
            }
        }
    }
}