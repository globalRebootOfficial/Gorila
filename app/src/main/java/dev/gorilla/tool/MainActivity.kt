package dev.gorilla.tool

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private external fun nativeVersion(): String
    private external fun nativeReadOwnStatus(): String

    companion object {
        init { System.loadLibrary("gorilla") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val output = TextView(this).apply {
            textSize = 14f
            setPadding(18, 18, 18, 18)
            text = "${nativeVersion()}\n\n${nativeReadOwnStatus()}"
        }

        val refresh = Button(this).apply {
            text = "Refresh diagnostics"
            setOnClickListener { output.text = nativeReadOwnStatus() }
        }
        val permission = Button(this).apply {
            text = "Overlay permission"
            setOnClickListener {
                if (!Settings.canDrawOverlays(this@MainActivity)) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivity(intent)
                } else {
                    output.text = "Overlay permission: granted\n\n${nativeReadOwnStatus()}"
                }
            }
        }
        val startOverlay = Button(this).apply {
            text = "Show diagnostic overlay"
            setOnClickListener {
                if (Settings.canDrawOverlays(this@MainActivity)) {
                    startService(Intent(this@MainActivity, DiagnosticOverlayService::class.java))
                } else {
                    output.text = "Grant overlay permission first."
                }
            }
        }
        val stopOverlay = Button(this).apply {
            text = "Hide overlay"
            setOnClickListener {
                stopService(Intent(this@MainActivity, DiagnosticOverlayService::class.java))
            }
        }

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            addView(refresh)
            addView(permission)
            addView(startOverlay)
            addView(stopOverlay)
            addView(
                ScrollView(this@MainActivity).apply { addView(output) },
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
                )
            )
        }
        setContentView(content)
    }
}
