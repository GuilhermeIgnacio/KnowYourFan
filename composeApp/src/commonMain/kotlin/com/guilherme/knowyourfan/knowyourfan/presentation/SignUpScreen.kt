package com.guilherme.knowyourfan.knowyourfan.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.guilherme.knowyourfan.core.presentation.CpfVisualTransformation
import com.guilherme.knowyourfan.knowyourfan.presentation.composables.LoadingIcon
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import knowyourfan.composeapp.generated.resources.Res
import knowyourfan.composeapp.generated.resources.address_card_regular
import knowyourfan.composeapp.generated.resources.calendar_regular
import knowyourfan.composeapp.generated.resources.confirm_email_placeholder
import knowyourfan.composeapp.generated.resources.confirm_password_placeholder
import knowyourfan.composeapp.generated.resources.cpf_placeholder
import knowyourfan.composeapp.generated.resources.data_consent
import knowyourfan.composeapp.generated.resources.email_textfield
import knowyourfan.composeapp.generated.resources.furia_logo
import knowyourfan.composeapp.generated.resources.image_solid
import knowyourfan.composeapp.generated.resources.interest_games_label
import knowyourfan.composeapp.generated.resources.list_events
import knowyourfan.composeapp.generated.resources.list_purchases
import knowyourfan.composeapp.generated.resources.password_textfield
import knowyourfan.composeapp.generated.resources.register_button
import knowyourfan.composeapp.generated.resources.submit_button
import knowyourfan.composeapp.generated.resources.upload_id_photo
import knowyourfan.composeapp.generated.resources.username_placeholder
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onAccountCreated: () -> Unit,
    onReturnButtonClicked: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

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

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            val result = snackBarHostState.showSnackbar(message = getString(it))
            when (result) {
                SnackbarResult.Dismissed -> {
                    viewModel.clearSnackBar()
                }

                SnackbarResult.ActionPerformed -> {}
            }

        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            IconButton(onClick = { onReturnButtonClicked() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = ""
                )
            }

            Image(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(48.dp),
                bitmap = imageResource(Res.drawable.furia_logo),
                contentDescription = ""
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextFields(
                iconsColor = iconsColor,
                outlinedTextFieldColors = outlinedTextFieldColors,
                state = state,
                onEvent = onEvent
            )

            IdSection(
                placeholderTextColor = placeholderTextColor,
                outlinedTextFieldContainerColor = outlinedTextFieldContainerColor,
                state = state,
                onEvent = onEvent
            )

            InterestGamesSection(
                placeholderTextColor = placeholderTextColor,
                onChipClicked = {
                    onEvent(SignUpEvents.OnGameChipClicked(it))
                },
                interestGamesList = state.interestGamesList
            )

            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
            val cpfRegex = Regex("""^(\d{11}|\d{3}\.\d{3}\.\d{3}-\d{2})$""")
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                onClick = { onEvent(SignUpEvents.OnRegisterButtonClicked) },

                enabled = !state.usernameTextField.isNullOrEmpty()
                        && !state.emailTextField.isNullOrEmpty()
                        && state.emailTextField == state.confirmEmailTextField
                        && state.emailTextField!!.matches(emailRegex)
                        && !state.idTextField.isNullOrEmpty()
                        && state.idTextField!!.matches(cpfRegex)
                        && state.selectedImageByteArray != null
                        && !state.passwordTextField.isNullOrEmpty()
                        && state.passwordTextField == state.confirmPasswordTextField
                        && state.interestGamesList.isNotEmpty()
                ,

                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors().copy(
                    contentColor = Color.Black,
                    containerColor = Color.White,
                )
            ) {
                Text(text = stringResource(Res.string.register_button))
            }


        }
    }

    if (state.isLoading) {
        LoadingIcon()
    }

    if (state.isAuthenticated) {
        onAccountCreated()
    }

}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun InterestGamesSection(
    placeholderTextColor: Color,
    onChipClicked: (ChipItem) -> Unit,
    interestGamesList: List<ChipItem>,
) {
    val games = listOf(
        ChipItem(
            game = "Counter-Strike",
            onClick = {},
            icon = null
        ),

        ChipItem(
            game = "Valorant",
            onClick = {},
            icon = null
        ),
        ChipItem(
            game = "Rainbow Six",
            onClick = {},
            icon = null
        ),
        ChipItem(
            game = "Apex Legends",
            onClick = {},
            icon = null
        ),
        ChipItem(
            game = "League of Legends",
            onClick = {},
            icon = null
        ),
        ChipItem(
            game = "Fighting Games",
            onClick = {},
            icon = null
        ),
    )

    Text(
        text = stringResource(Res.string.interest_games_label),
        color = placeholderTextColor
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        games.forEach {
            FilterChip(
                onClick = { onChipClicked(it) },
                label = {
                    Text(text = it.game)
                },
                shape = RoundedCornerShape(100),
                selected = it in interestGamesList,
                leadingIcon = if (it.icon != null) {
                    {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = "",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )
        }

    }
}

@Composable
private fun IdSection(
    placeholderTextColor: Color,
    outlinedTextFieldContainerColor: Color,
    state: SignUpState,
    onEvent: (SignUpEvents) -> Unit,
) {

    val scope = rememberCoroutineScope()


    val singleImagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let {
                // Process the selected images' ByteArrays.
                onEvent(SignUpEvents.OnImageSelected(it))
            }
        }
    )

    Text(
        text = stringResource(Res.string.upload_id_photo),
        color = placeholderTextColor
    )


    if (state.selectedImageByteArray == null) {

        Column(
            modifier = Modifier
                .border(1.dp, placeholderTextColor, shape = RoundedCornerShape(12.dp))
                .padding(all = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(color = outlinedTextFieldContainerColor, shape = CircleShape)
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.image_solid),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(12.dp)
                        .size(24.dp)
                )
            }

            Text(
                text = stringResource(Res.string.data_consent),
                textAlign = TextAlign.Center,
                color = placeholderTextColor
            )

            OutlinedButton(
                onClick = { singleImagePicker.launch() },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = stringResource(Res.string.submit_button))
            }
        }

    } else {
        Column(
            modifier = Modifier
                .border(1.dp, placeholderTextColor, shape = RoundedCornerShape(12.dp))
                .padding(all = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = state.selectedImageByteArray,
                contentDescription = "",
                contentScale = ContentScale.Fit
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
            Row {
                IconButton(onClick = { onEvent(SignUpEvents.OnRemoveImageSelected) }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "",
                        tint = placeholderTextColor
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TextFields(
    iconsColor: Color,
    outlinedTextFieldColors: TextFieldColors,
    state: SignUpState,
    onEvent: (SignUpEvents) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.usernameTextField ?: "",
        onValueChange = { onEvent(SignUpEvents.OnUsernameTextFieldValueChanged(it)) },
        placeholder = { Text(text = stringResource(Res.string.username_placeholder)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "",
                tint = iconsColor
            )
        },
        maxLines = 1,
        shape = RoundedCornerShape(12.dp),
        colors = outlinedTextFieldColors,
    )

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = state.emailTextField ?: "",
        onValueChange = { onEvent(SignUpEvents.OnEmailTextFieldValueChanged(it)) },
        placeholder = { Text(text = stringResource(Res.string.email_textfield)) },
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
        modifier = Modifier.fillMaxWidth(),
        value = state.confirmEmailTextField ?: "",
        onValueChange = { onEvent(SignUpEvents.OnConfirmEmailTextFieldValueChanged(it)) },
        placeholder = { Text(text = stringResource(Res.string.confirm_email_placeholder)) },
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
        modifier = Modifier.fillMaxWidth(),
        value = state.idTextField ?: "",
        onValueChange = { onEvent(SignUpEvents.OnIdTextFieldValueChanged(it)) },
        placeholder = { Text(text = stringResource(Res.string.cpf_placeholder)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = CpfVisualTransformation(),
        leadingIcon = {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = vectorResource(Res.drawable.address_card_regular),
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
        onValueChange = { onEvent(SignUpEvents.OnPasswordTextFieldValueChanged(it)) },
        placeholder = { Text(text = stringResource(Res.string.password_textfield)) },
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

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = state.confirmPasswordTextField ?: "",
        onValueChange = { onEvent(SignUpEvents.OnConfirmPasswordTextFieldValueChanged(it)) },
        placeholder = { Text(text = stringResource(Res.string.confirm_password_placeholder)) },
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

    //Last year purchases section

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = state.lastYearPurchasesTextField ?: "",
        onValueChange = { onEvent(SignUpEvents.OnLastYearPurchasesTextFieldValueChanged(it)) },
        placeholder = { Text(text = stringResource(Res.string.list_purchases)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = "",
                tint = iconsColor
            )
        },
        maxLines = 2,
        minLines = 2,
        shape = RoundedCornerShape(12.dp),
        colors = outlinedTextFieldColors,
    )

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        state.lastYearPurchasesList.forEach {
            FilterChip(
                onClick = { onEvent(SignUpEvents.OnLastYearPurchaseChipClicked(it)) },
                label = {
                    Text(text = it.game)
                },
                shape = RoundedCornerShape(100),
                selected = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear Icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
        }

    }

    //Last year purchases section


    //Events Section

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = state.eventsTextField ?: "",
        onValueChange = { onEvent(SignUpEvents.OnEventsTextFieldValueChanged(it)) },
        placeholder = { Text(text = stringResource(Res.string.list_events)) },
        leadingIcon = {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = vectorResource(Res.drawable.calendar_regular),
                contentDescription = "",
                tint = iconsColor
            )
        },
        maxLines = 2,
        minLines = 2,
        shape = RoundedCornerShape(12.dp),
        colors = outlinedTextFieldColors,
    )

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        state.eventsList.forEach {
            FilterChip(
                onClick = { onEvent(SignUpEvents.OnEventChipClicked(it)) },
                label = {
                    Text(text = it.game)
                },
                shape = RoundedCornerShape(100),
                selected = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear Icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
        }

    }

    //Events Section

}

data class ChipItem(
    val game: String,
    val onClick: () -> Unit,
    val icon: ImageVector?,
)