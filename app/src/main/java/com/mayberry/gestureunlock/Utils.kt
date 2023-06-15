package com.mayberry.gestureunlock

import android.app.Activity
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.core.graphics.contains
import com.mayberry.gestureunlock.databinding.ActivityMainBinding

lateinit var binding: ActivityMainBinding
val dotsArray: Array<ImageView> by lazy {
    arrayOf(
        binding.dot1, binding.dot2, binding.dot3, binding.dot4, binding.dot5,
        binding.dot6, binding.dot7, binding.dot8, binding.dot9
    )
}
val lineTagArray = arrayOf(
    12, 23, 45, 56, 78, 89,
    14, 47, 25, 58, 36, 69,
    24, 35, 57, 68,
    15, 26, 48, 59
)
lateinit var activity: Activity

fun getBarHeight(activity: Activity): Int {
    var screenHeight = 0
    screenHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        activity.windowManager.currentWindowMetrics.bounds.height()
    } else {
        val mMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(mMetrics)
        mMetrics.heightPixels
    }
    //获取绘制区域尺寸
    val rect = Rect()
    val content = activity.window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
    content.getDrawingRect(rect)
    //获取底部navigationBar的高度
    val resourceId = activity.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    val navBarHeight = activity.resources.getDimensionPixelSize(resourceId)
    //bar的高度
    return screenHeight - rect.height() - navBarHeight
}

//将某个视图坐标转化为Rect
private fun convert(dot: ImageView): Rect {
    return Rect(
        dot.x.toInt(),
        dot.y.toInt(),
        (dot.x + dot.width).toInt(),
        (dot.y + dot.height).toInt()
    )
}

//判断触摸点是否在某个dot内
fun checkTouchView(event: MotionEvent): View? {
    //获取触摸点在容器中的坐标
    val point = getTouchPoint(event)
    //遍历保存9个点的数组
    dotsArray.forEach {
        //获取当前视图的Rect
        val dotRect = convert(it)
        //判断触点是否在矩形区域内
        val result = dotRect.contains(point)
        if (result) {
            return it
        }
    }
    return null
}

//计算触摸点相对于父容器的坐标
fun getTouchPoint(event: MotionEvent): Point {
    //相对于屏幕的高度 - 顶部Bar的高度 - 容器和内容区域顶部的高度
    val y = event.y - getBarHeight(activity) - binding.dotsContainer.y
    val touchPoint = Point()
    touchPoint.x = event.x.toInt()
    touchPoint.y = y.toInt()
    return touchPoint
}
