package com.mayberry.gestureunlock

import android.graphics.Rect
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.mayberry.gestureunlock.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val barHeight: Int by lazy {
        getBarHeight()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        val dotRect = Rect(
//            binding.dot1.x.toInt(),
//            binding.dot1.y.toInt(),
//            (binding.dot1.x + binding.dot1.width).toInt(),
//            (binding.dot1.y + binding.dot1.height).toInt(),
//        )
//        event?.let {
//            //判断圆点区域是否包含触摸点坐标
//            val res = dotRect.contains(event.x.toInt(), event.y.toInt() - barHeight)
//            if (res) {
//                val selectedDot = binding.container.findViewWithTag<ImageView>("1")
//                selectedDot.visibility = View.VISIBLE
//            }
//        }
//
//        return true
//    }

    @JvmName("getBarHeight1")
    private fun getBarHeight(): Int {
        //获取屏幕高度
        val screenHeight = windowManager.currentWindowMetrics.bounds.height()
        //获取绘制区域高度
        val rect = Rect()
        val content = window.findViewById<View>(Window.ID_ANDROID_CONTENT)
        content.getDrawingRect(rect)
        //获取状态栏高度
        return screenHeight - rect.height()
    }

}