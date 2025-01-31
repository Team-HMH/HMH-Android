package com.hmh.hamyeonham.core.designsystem.compose.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hmh.hamyeonham.core.designsystem.compose.theme.hmhColors

@Composable
fun HmhCheckBox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    colors: CheckboxColors = CheckboxDefaults.colors(
        checkmarkColor = hmhColors.whiteText,
        checkedColor = hmhColors.bluePurpleLine,
        uncheckedColor = hmhColors.gray3,
    ),
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
) {
    Checkbox(
        checked = isChecked,
        colors = colors,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource
    )
}

@Preview
@Composable
fun HmhCheckBoxPreview() {
    HmhCheckBox(isChecked = true, onCheckedChange = {})
}

@Preview
@Composable
fun HmhCheckBoxUncheckedPreview() {
    HmhCheckBox(isChecked = false, onCheckedChange = {})
}