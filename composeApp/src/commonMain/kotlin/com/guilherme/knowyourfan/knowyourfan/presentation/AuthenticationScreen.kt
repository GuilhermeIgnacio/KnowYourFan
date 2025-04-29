package com.guilherme.knowyourfan.knowyourfan.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guilherme.knowyourfan.knowyourfan.presentation.composables.LoadingIcon
import knowyourfan.composeapp.generated.resources.Res
import knowyourfan.composeapp.generated.resources.apple_brands
import knowyourfan.composeapp.generated.resources.furia_logo
import knowyourfan.composeapp.generated.resources.google_brands
import knowyourfan.composeapp.generated.resources.x_twitter_brands
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthenticationScreen(
    onAuth: () -> Unit,
    onSignUpClick: () -> Unit,
) {

    val viewModel = koinViewModel<AuthenticationViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            val snackBar = snackBarHostState.showSnackbar(
                message = getString(it)
            )

            when (snackBar) {
                SnackbarResult.Dismissed -> {
                    viewModel.clearErrorMessage()
                }

                SnackbarResult.ActionPerformed -> {}
            }
        }
    }

    val interactionSource = remember { MutableInteractionSource() }

    val outlinedTextFieldContainerColor = Color(0xFF1E1E1E)
    val indicatorColor = Color.Transparent
    val iconsColor = Color(0xFF606060)
    val placeholderTextColor = Color(0xFF515151)

    val outlinedTextFieldColors = OutlinedTextFieldDefaults.colors().copy(
        unfocusedContainerColor = outlinedTextFieldContainerColor,
        focusedContainerColor = outlinedTextFieldContainerColor,
        unfocusedIndicatorColor = indicatorColor,
        focusedIndicatorColor = indicatorColor,
        focusedPlaceholderColor = placeholderTextColor,
        unfocusedPlaceholderColor = placeholderTextColor,
        cursorColor = placeholderTextColor,
        errorCursorColor = placeholderTextColor,
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(48.dp),
                bitmap = imageResource(Res.drawable.furia_logo),
                contentDescription = ""
            )

            Headers(placeholderTextColor)

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.emailTextField ?: "",
                    onValueChange = { onEvent(AuthenticationEvents.OnEmailTextFieldValueChanged(it)) },
                    placeholder = { Text(text = "Email") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "",
                            tint = iconsColor
                        )
                    },
                    maxLines = 1,
                    shape = RoundedCornerShape(12.dp),
                    colors = outlinedTextFieldColors,
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = state.passwordTextField ?: "",
                    onValueChange = {
                        onEvent(
                            AuthenticationEvents.OnPasswordTextFieldValueChanged(
                                it
                            )
                        )
                    },
                    placeholder = { Text(text = "Password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "",
                            tint = iconsColor
                        )
                    },
                    trailingIcon = {},
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(12.dp),
                    colors = outlinedTextFieldColors,
                )
            }


            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                onClick = { onEvent(AuthenticationEvents.OnSignInButtonClicked) },

                enabled = !state.emailTextField.isNullOrEmpty() && !state.passwordTextField.isNullOrEmpty(),

                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    contentColor = Color.Black,
                    containerColor = Color.White,
                )
            ) {
                Text(text = "Sign In")
            }

            CreateAccountText(
                placeholderTextColor = placeholderTextColor,
                interactionSource = interactionSource,
                onSignUpClick = { onSignUpClick() }
            )

        }
    }

    if (state.isLoading) {
        LoadingIcon()
    }

    if (state.isAuthenticated) {
        onAuth()
    }

}

@Composable
private fun CreateAccountText(
    placeholderTextColor: Color,
    interactionSource: MutableInteractionSource,
    onSignUpClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Don't have an account? ",
            color = placeholderTextColor
        )

        Text(
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onSignUpClick() },
            text = "Sign Up",
            color = Color.White
        )
    }
}

@Composable
private fun GoogleAndAppleSignInMethods(
    outlinedTextFieldHeight: Dp,
    onSignInWithGoogleButtonClicked: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Button(
            modifier = Modifier
                .weight(1f)
                .height(outlinedTextFieldHeight),
            onClick = { onSignInWithGoogleButtonClicked() },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors().copy(
                contentColor = Color.White,
                containerColor = Color(0xFF1E1E1E),
            )
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = vectorResource(Res.drawable.google_brands),
                contentDescription = "",
                tint = Color.White
            )
            Spacer(Modifier.width(12.dp))
            Text(text = "Google")
        }

        Button(
            modifier = Modifier
                .weight(1f)
                .height(outlinedTextFieldHeight),
            onClick = {/*Todo: Sign in with apple*/ },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors().copy(
                contentColor = Color.White,
                containerColor = Color(0xFF1E1E1E),
            )
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = vectorResource(Res.drawable.apple_brands),
                contentDescription = "",
                tint = Color.White
            )
            Spacer(Modifier.width(12.dp))
            Text(text = "Apple")
        }

    }
}

@Composable
private fun OrSection(placeholderTextColor: Color) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

        HorizontalDivider(Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = "OR",
            color = placeholderTextColor
        )
        HorizontalDivider(Modifier.weight(1f))
    }
}

@Composable
private fun Headers(placeholderTextColor: Color) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Welcome Back",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Sign In to Continue",
            color = placeholderTextColor
        )
    }
}