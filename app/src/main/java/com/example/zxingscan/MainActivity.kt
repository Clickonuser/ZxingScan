package com.example.zxingscan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class MainActivity : ComponentActivity() {

    private val scanLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this, "No result", Toast.LENGTH_SHORT).show()
        } else {
            scannedText.value = result.contents
        }
    }

    private var scannedText = mutableStateOf("") // Will change with each scan

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
                        openLink(text)
                    }
            )
        }
    }

    @Composable
    fun ScanButton() {
        Button(onClick = {
            scan()
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

    private fun scan() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE) // type code
        options.setPrompt("Scan a QR code")
        options.setCameraId(0) // Use a specific camera of the device
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        scanLauncher.launch(options)
    }

    private fun openLink(text: String) {
        val uri = if (text.startsWith("http://") || text.startsWith("https://")) {
            Uri.parse(text)
        } else {
            Uri.parse("https://www.google.com/search?q=$text")
        }
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}
