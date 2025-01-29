package com.hmh.hamyeonham.challenge.appadd.appselection

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hmh.hamyeonham.challenge.appadd.AppAddState
import com.hmh.hamyeonham.challenge.appadd.AppAddViewModel
import com.hmh.hamyeonham.challenge.model.AppInfo
import com.hmh.hamyeonham.common.context.getAppIconFromPackageName
import com.hmh.hamyeonham.common.context.getAppNameFromPackageName
import com.hmh.hamyeonham.core.designsystem.ui.theme.HMHAndroidTheme
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
                    AppSelectionScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    private fun getInstalledAppList(context: Context): List<AppSelectionModel> {
        val installApps = viewModel.installedApps.value
        val selectedApps = viewModel.state.value.selectedApps
        return installApps.map {
            if (it.packageName.contains(context.packageName)) {
                return@map null
            }
            AppSelectionModel(it.packageName, selectedApps.contains(it.packageName))
        }.sortedBy { context.getAppNameFromPackageName(it?.packageName ?: "") }.filterNotNull()
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
                .padding(innerPadding)
        ) {
            TextField(
                value = query,
                onValueChange = viewModel::onQueryChanged,
                label = { },
                placeholder = { stringResource(R.string.appselection_searchbar) },
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(installedApps) { app ->
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
    val isChecked by remember { derivedStateOf { appAddState.selectedApps.contains(packageName) } }

    Row(modifier = Modifier.fillMaxWidth()) {
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
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onAppChecked(packageName, it) }
        )
    }
}