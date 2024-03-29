package com.therxmv.dirolreader.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.therxmv.dirolreader.data.repository.AppSharedPrefsRepository
import com.therxmv.dirolreader.ui.navigation.DirolNavHost
import com.therxmv.dirolreader.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appSharedPrefsRepository: AppSharedPrefsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDynamic = remember { mutableStateOf(appSharedPrefsRepository.isDynamic) }

            AppTheme(
                dynamicColor = isDynamic.value
            ) {
                DirolNavHost {
                    isDynamic.value = it
                    appSharedPrefsRepository.isDynamic = it
                }
            }
        }
    }
}