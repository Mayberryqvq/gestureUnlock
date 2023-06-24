package com.mayberry.gestureunlock

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mayberry.gestureunlock.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private var lastSelectedView: View? = null
    private val highlightedViewArray = mutableListOf<View>()
    private val setPassword = StringBuilder()
    private val checkPassword = StringBuilder()
    private val checkPasswordViewArray = mutableListOf<View>()
    private var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        activity = this
        setContentView(binding.root)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val dotView = checkTouchView(event)
                lastSelectedView = dotView
                if (dotView != null) {
                    dotView.visibility = View.VISIBLE
                    if (!flag) {
                        setPassword.append(dotView.tag)
                        highlightedViewArray.add(dotView)
                    } else {
                        checkPassword.append(dotView.tag)
                        checkPasswordViewArray.add(dotView)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                Timer().schedule(object: TimerTask() {
                    override fun run() {
                        highlightedViewArray.forEach {
                            it.visibility = View.INVISIBLE
                        }
                        lastSelectedView = null
                        if (!flag) {
                            Log.v("Mayberry's flag is $flag", "The password you set is $setPassword")
                            flag = !flag
                        } else {
                            Log.v("Mayberry's flag is $flag", "The password you input is $checkPassword")
                            var checkingResult = true
                            Looper.prepare()
                            highlightedViewArray.forEach {
                                if (!checkPasswordViewArray.contains(it)) {
                                    checkingResult = false
                                }
                            }
                            if (checkingResult && highlightedViewArray.size == checkPasswordViewArray.size) {
                                Toast.makeText(applicationContext, "Your password is right", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(applicationContext, "Your password is wrong", Toast.LENGTH_LONG).show()
                            }
                            setPassword.clear()
                            checkPassword.clear()
                            highlightedViewArray.clear()
                            checkPasswordViewArray.clear()
                            flag = !flag
                            Looper.loop()
                        }
                    }
                }, 1000)
            }
            MotionEvent.ACTION_MOVE -> {
                val dotView = checkTouchView(event)
                if (dotView != null) {
                    if (dotView.visibility == View.INVISIBLE) {
                        if (lastSelectedView == null) {
                            dotView.visibility = View.VISIBLE
                            lastSelectedView = dotView
                            if (!flag) {
                                setPassword.append(dotView.tag)
                                highlightedViewArray.add(dotView)
                            } else {
                                checkPassword.append(dotView.tag)
                                checkPasswordViewArray.add(dotView)
                            }
                        } else {
                            val lastTag = (lastSelectedView?.tag as String).toInt()
                            val currentTag = (dotView.tag as String).toInt()
                            val lineTag = min(lastTag, currentTag) * 10 + max(lastTag, currentTag)
                            if (lineTagArray.contains(lineTag)) {
                                val lineView = binding.dotsContainer.findViewWithTag<ImageView>(lineTag.toString())
                                lineView.visibility = View.VISIBLE
                                dotView.visibility = View.VISIBLE
                                lastSelectedView = dotView
                                if (!flag) {
                                    setPassword.append(dotView.tag)
                                    highlightedViewArray.add(dotView)
                                    highlightedViewArray.add(lineView)
                                } else {
                                    checkPassword.append(dotView.tag)
                                    checkPasswordViewArray.add(dotView)
                                    checkPasswordViewArray.add(lineView)
                                }
                            }
                        }
                    }
                }
            }
        }
        return true
    }

}