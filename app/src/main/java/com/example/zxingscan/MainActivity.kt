package com.example.zxingscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.zxingscan.ui.theme.ZxingScanTheme

class MainActivity : ComponentActivity() {

    private var scannedText = mutableStateOf("test text") // Will change with each scan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZxingScanTheme {
                Ui()
            }
        }
    }

    @Composable
    private fun Ui() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        )
        {
            LinkText()
            ScanButton()
        }
    }

    @Composable
    fun LinkText() {
        val text by remember { scannedText }
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = createAnnotatedLinkText(text),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        // processing a link click
                    }
            )
        }
    }

    @Composable
    fun ScanButton() {
            Button(onClick = {
                // processing a button click
            }) {
                Text(text = "Scan")
            }
    }

    private fun createAnnotatedLinkText(text: String): AnnotatedString {
        return buildAnnotatedString {
            pushStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline))
            append(text)
            addStringAnnotation(
                tag = "URL",
                annotation = text,
                start = 0,
                end = text.length
            )
            pop()
        }
    }
}
