package com.crty.ams

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import com.crty.ams.core.ui.navigation.AppNavigation
import com.crty.ams.core.ui.theme.AmsTheme
import dagger.hilt.android.AndroidEntryPoint
import com.crty.ams.core.data.repository.AppParameterRepository
import com.crty.ams.core.data.datastore.di.AppParameterDataStore
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appParameterDataStore: AppParameterDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // lifecycleScope for random AppParameter only
        // will replace with scanner sdk initialization
        lifecycleScope.launch {
            AppParameterRepository(appParameterDataStore).setRandomAppParameter()
        }


        enableEdgeToEdge()
        setContent {
            AmsTheme {
                AppNavigation()


            }
        }
    }
}