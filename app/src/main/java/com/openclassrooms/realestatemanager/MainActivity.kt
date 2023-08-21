package com.openclassrooms.realestatemanager

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessageCard(name = "hola")
        }
    }
}

@Composable
fun MessageCard(name: String) {
    Text(text = name)
}

@Preview
@Composable
fun PreviewMessageCard() {
    MessageCard(name = "hi")
}
