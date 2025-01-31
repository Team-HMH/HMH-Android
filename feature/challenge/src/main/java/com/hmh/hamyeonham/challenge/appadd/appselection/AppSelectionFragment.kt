@file:OptIn(ExperimentalMaterial3Api::class)

package com.hmh.hamyeonham.challenge.appadd.appselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hmh.hamyeonham.challenge.appadd.AppAddState
import com.hmh.hamyeonham.challenge.appadd.AppAddViewModel
import com.hmh.hamyeonham.challenge.model.AppInfo
import com.hmh.hamyeonham.common.context.getAppIconFromPackageName
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.core.designsystem.compose.theme.HMHAndroidTheme
import com.hmh.hamyeonham.core.designsystem.compose.theme.HmhTypography
import com.hmh.hamyeonham.core.designsystem.compose.theme.hmhColors
import com.hmh.hamyeonham.core.designsystem.compose.ui.component.HmhCheckBox
import com.hmh.hamyeonham.feature.challenge.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppSelectionFragment : Fragment() {

    private val viewModel by activityViewModels<AppAddViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                HMHAndroidTheme {
                    AppSelectionScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun AppSelectionScreen(
    viewModel: AppAddViewModel,
    modifier: Modifier = Modifier
) {
    val appAddState by viewModel.state.collectAsState()
    val query by viewModel.query.collectAsState()
    val installedApps by viewModel.installedApps.collectAsState()

    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            AppSelectionTextField(
                modifier = Modifier.padding(horizontal = 20.dp),
                query = query,
                onQueryChanged = viewModel::onQueryChanged,
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    horizontal = 20.dp
                ),
            ) {
                items(
                    items = installedApps,
                    key = { it.packageName }
                ) { app ->
                    AppSelectionItem(
                        appAddState = appAddState,
                        app = app,
                        onAppChecked = viewModel::appCheckChanged
                    )
                }
            }
        }
    }
}

@Composable
private fun AppSelectionItem(
    appAddState: AppAddState,
    app: AppInfo,
    onAppChecked: (packageName: String, isCheck: Boolean) -> Unit,
) {
    val context = LocalContext.current
    val packageName = app.packageName
    val appName = remember { context.getAppNameFromPackageName(packageName) }
    val image = remember {
        context.getAppIconFromPackageName(packageName)?.toBitmap()?.asImageBitmap()
    }
    val isChecked = appAddState.selectedApps.contains(packageName)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        image?.let {
            Image(
                bitmap = image,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = appName)
        Spacer(modifier = Modifier.weight(1f))
        HmhCheckBox(
            isChecked = isChecked,
            onCheckedChange = { onAppChecked(packageName, it) }
        )
    }
}

@Composable
private fun AppSelectionTextField(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier.fillMaxWidth(),
        textStyle = HmhTypography.titleSmall.copy(
            color = hmhColors.gray1
        ),
        singleLine = true,
        cursorBrush = SolidColor(hmhColors.gray1),
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = query,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = remember { MutableInteractionSource() },
                placeholder = {
                    Text(
                        text = stringResource(R.string.appselection_searchbar),
                        color = hmhColors.gray1,
                        style = HmhTypography.titleSmall
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = hmhColors.gray1,
                    unfocusedTextColor = hmhColors.gray1,
                    disabledTextColor = hmhColors.gray1,
                    focusedContainerColor = hmhColors.gray6,
                    unfocusedContainerColor = hmhColors.gray6,
                    errorContainerColor = hmhColors.gray6,
                    disabledContainerColor = hmhColors.gray6,
                    cursorColor = hmhColors.gray1,
                    focusedPlaceholderColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_search_24),
                        contentDescription = stringResource(R.string.appselection_searchbar),
                        tint = hmhColors.gray1
                    )
                },
                shape = RoundedCornerShape(40.dp),
            )
        }
    )
}


@Preview
@Composable
fun TextFieldPreview() {
    var query by remember { mutableStateOf("") }

    HMHAndroidTheme {
        AppSelectionTextField(query,
            onQueryChanged = { query = it })
    }
}
