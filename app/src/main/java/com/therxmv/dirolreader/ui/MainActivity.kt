package com.therxmv.dirolreader.ui

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.therxmv.dirolreader.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.setDecorFitsSystemWindows(false)
//            window.navigationBarColor = Color.TRANSPARENT
//        }
    }
}