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
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.guilherme.knowyourfan.core.presentation.dashedBorder
import knowyourfan.composeapp.generated.resources.Res
import knowyourfan.composeapp.generated.resources.address_card_regular
import knowyourfan.composeapp.generated.resources.furia_logo
import knowyourfan.composeapp.generated.resources.image_solid
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignUpScreen() {

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp)
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(48.dp),
            bitmap = imageResource(Res.drawable.furia_logo),
            contentDescription = ""
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextFields(iconsColor, outlinedTextFieldColors)

        IdSection(placeholderTextColor, outlinedTextFieldContainerColor)

        InterestGamesSection(placeholderTextColor)

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            onClick = {/*Todo Sign In*/ },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors().copy(
                contentColor = Color.Black,
                containerColor = Color.White,
            )
        ) {
            Text(text = "Register")
        }


    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun InterestGamesSection(placeholderTextColor: Color) {
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
        text = "Select games you`re interested in",
        color = placeholderTextColor
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        games.forEach {
            FilterChip(
                onClick = { },
                label = {
                    Text(text = it.game)
                },
                shape = RoundedCornerShape(100),
                selected = false,
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
) {
    Text(
        text = "Upload your ID photo",
        color = placeholderTextColor
    )

    Column(
        modifier = Modifier
            .dashedBorder(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        placeholderTextColor,
                        placeholderTextColor
                    )
                ),
                shape = RoundedCornerShape(12.dp), on = 8.dp, off = 8.dp
            )
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
            text = "Don't worry, your data won't be stored. It only will be used for verification",
            textAlign = TextAlign.Center,
            color = placeholderTextColor
        )

        OutlinedButton(
            onClick = {/*Todo: pick photo*/ },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Submit")
        }

    }
}

@Composable
private fun TextFields(
    iconsColor: Color,
    outlinedTextFieldColors: TextFieldColors,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = "",
        onValueChange = { },
        placeholder = { Text(text = "Username") },
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
        value = "",
        onValueChange = { },
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
        modifier = Modifier.fillMaxWidth(),
        value = "",
        onValueChange = { },
        placeholder = { Text(text = "Confirm Email") },
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
        value = "",
        onValueChange = { },
        placeholder = { Text(text = "CPF") },
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
        value = "",
        onValueChange = {},
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

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = "",
        onValueChange = {},
        placeholder = { Text(text = "Confirm Password") },
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

data class ChipItem(
    val game: String,
    val onClick: () -> Unit,
    val icon: ImageVector?,
)