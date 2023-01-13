package com.example.bookstore.ui.screens.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun MainTabRow(
    allScreens: List<VolumesDestination>,
    onTabSelected: (VolumesDestination) -> Unit,
    currentScreen: VolumesDestination
) {
    Surface(
        Modifier
            .height(TabHeight)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .selectableGroup()
                .background(MaterialTheme.colors.primary)
            ) {
            allScreens.forEach { screen ->
                VolumeTab(
                    text = screen.route,
                    icon = screen.icon,
                    onSelected = { onTabSelected(screen) },
                    selected = currentScreen == screen
                )
            }
        }
    }
}

@Composable
private fun VolumeTab(
    text: String,
    icon: ImageVector?,
    onSelected: () -> Unit,
    selected: Boolean
) {
    val color = MaterialTheme.colors.onPrimary
    val tabTintColor = if (selected) color else color.copy(alpha = 0.60f)

    Column(
        modifier = Modifier
            .padding(top = 5.dp)
            .animateContentSize()
            .height(TabHeight)
            .selectable(
                selected = selected,
                onClick = onSelected,
                role = Role.Tab,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = false,
                    radius = Dp.Unspecified,
                    color = Color.Unspecified
                )
            )
            .clearAndSetSemantics { contentDescription = text }
    ) {
        Icon(
            imageVector = icon ?: Icons.Filled.QuestionMark,
            contentDescription = text, tint = tabTintColor,
            modifier = Modifier.align(CenterHorizontally)

        )
        Text(text.uppercase(Locale.getDefault()), color = tabTintColor)
    }
}

@Preview
@Composable
fun MainBarPreview(){
    MainTabRow(volumesTabRowScreens, {}, Home)
}

private val TabHeight = 56.dp

