package com.example.kinomaptest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.kinomaptest.ui.app.App
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = this
        setContent {
            App()
        }
    }

    companion object {
        lateinit var mainActivity: MainActivity
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    App()
}