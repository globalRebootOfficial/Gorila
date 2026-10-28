package dev.gorilla.tool

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager

class DiagnosticOverlayService : Service(), SurfaceHolder.Callback {
    private lateinit var windowManager: WindowManager
    private var surface: SurfaceView? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val view = SurfaceView(this)
        view.holder.addCallback(this)
        view.setZOrderOnTop(true)
        view.holder.setFormat(PixelFormat.TRANSLUCENT)
        surface = view

        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START }

        windowManager.addView(view, params)
    }

    override fun surfaceCreated(holder: SurfaceHolder) = drawDiagnostic(holder)
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) =
        drawDiagnostic(holder)
    override fun surfaceDestroyed(holder: SurfaceHolder) = Unit

    private fun drawDiagnostic(holder: SurfaceHolder) {
        val canvas: Canvas = holder.lockCanvas() ?: return
        try {
            canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                textSize = 36f
                setShadowLayer(5f, 1f, 1f, Color.BLACK)
            }
            canvas.drawText("GORILLA / diagnostic overlay", 32f, 64f, paint)
            paint.textSize = 24f
            canvas.drawText("local UI only — no game memory access", 32f, 100f, paint)
        } finally {
            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun onDestroy() {
        surface?.let {
            runCatching { windowManager.removeView(it) }
        }
        surface = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
