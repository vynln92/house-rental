package com.example.timebox1

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import kotlinx.android.synthetic.main.activity_main.*
import java.text.ParsePosition
import java.util.*
import android.view.LayoutInflater
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.dialog.*
import kotlinx.android.synthetic.main.getlayout.*


class MainActivity : AppCompatActivity() {
    var isTouch = false
    var startTouch: MotionEvent? = null

    var saveEditText = ""

    var chieuDoc = 0
    var chieuNgang = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 0 until 24) {
            createTextTimeRange(i)
            createTimeLine(i)

        }

        relativeTimeRange.post {
            var timeHeightRangeLine = relativeTimeRange.height
            //chiều cao của màn hình = pixel
            var totalHourRange = 1440f
            // 24h * 60 = 1440
            var currentTimMinute = getCurrentTimeMinute(100)
            // giờ hiện tại =

            var positionTimLine = currentTimMinute / totalHourRange
            // vị trí của time line = giờ hiện tại * tổng giờ

            var pos = positionTimLine * timeHeightRangeLine
            line.translationY = pos

        }
        getCurrentTimeMinute(100)
        setTimeHeightForGroup()
        setLineHeightForGroup()

        relativeLine1.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    Log.d("TOUCH", "MotionEvent.ACTION_MOVE ${event.y}")
                    if (event.y - startTouch!!.y > 10 || event.y - startTouch!!.y < -10) {
                        isTouch = false
                    }
                }
                MotionEvent.ACTION_DOWN -> {
                    Log.d("TOUCH", "MotionEvent.ACTION_DOWN ${event.y}")
                    startTouch = event
                    isTouch = true
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("TOUCH", "MotionEvent.ACTION_UP -> Show dialog")
                    if (isTouch) {
                        val gioDauRa = getTimeIMinuteOfPositionY(pxToDp(event.y.toInt()))
                        val widthDauRa = getTimeOfPositionY(pxToDp(event.x.toInt()))
                        chieuDoc = gioDauRa.first
                        chieuNgang = widthDauRa.first
                        val giobatdau = getTextFromMinute(gioDauRa.first)
                        val gioketthuc = getTextFromMinute(gioDauRa.second)
                        var abc = Pair(giobatdau, gioketthuc)
                        showDialogCustom(abc)

                    }
                }
            }

            return@setOnTouchListener true
        }


    }

    private fun getTimeIMinuteOfPositionY(positionInDp: Float): Pair<Int, Int> {
        val heightIn50 = (positionInDp / 50).toInt()
        var pairOutput = Pair(0, 0)
        if (heightIn50 == 23 * 2) {
            pairOutput = Pair(heightIn50 - 1, heightIn50)
        } else {
            pairOutput = Pair(heightIn50, heightIn50 + 1)
        }
        return pairOutput
    }

    private fun getTextFromMinute(first: Int): String {
        val gio = first / 2
        val phut = (first - (gio * 2)) * 30

        var gioText = gio.toString()
        if (gio < 10) {
            gioText = "0${gio}"
        } else {
            gioText = "${gio}"
        }
        var phutText = phut.toString()
        if (phut < 10) {
            phutText = "0${phut}"
        }

        return "${gioText}:${phutText}"
    }

    @SuppressLint("ResourceType")
    private fun addLayout() {
        val inflater = this.layoutInflater

        val layout = inflater.inflate(R.layout.getlayout, null, false)

        var textNew = layout.findViewById(R.id.textAdd) as TextView
        textNew.setText(saveEditText)
        relativeLine1.addView(layout)
        layout.translationY = dpToPx((chieuDoc * 50) + 25)
        layout.translationX = dpToPx((chieuNgang * 100) + 50)
        layout.setOnClickListener {
            // TODO: Sai không xóa được view
            Toast.makeText(this, "sdfhsda", Toast.LENGTH_LONG).show()
            relativeLine1.removeView(layout)

// TODO: Đã sửa
            asdfa
            asd
            fa
            sdf
        }
    }

    private fun showDialogCustom(abc: Pair<String, String>) {
        val dialogBuilder = AlertDialog.Builder(this)
// ...Irrelevant code for customizing the buttons and title
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog, null)
        dialogBuilder.setView(dialogView)

        val textView = dialogView.findViewById(R.id.textDialogTime) as TextView
        textView.setText("Bắt đầu: " + abc.first + " - Kết thúc: " + abc.second)
        val alertDialog = dialogBuilder.create()

        val editText = dialogView.findViewById(R.id.textDialogNoiDung) as EditText

        val buttonLuu1 = dialogView.findViewById(R.id.buttonLuu) as Button
        val buttonHuy1 = dialogView.findViewById(R.id.buttonHuy) as Button


        buttonLuu1.setOnClickListener {
            alertDialog.dismiss()
            saveEditText = editText.text.toString()
            addLayout()
        }
        buttonHuy1.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun doiThanhGio(gioDauRa: Pair<Int, Int>): Pair<String, String> {
        var bienTruoc = ""
        var bienSau = ""
        if (gioDauRa.first < 10) {
            bienTruoc = ("0" + gioDauRa.first)
        } else {
            bienTruoc = "" + gioDauRa.first
        }
        if (gioDauRa.second < 10) {
            bienSau = ("0" + gioDauRa.second)
        } else {
            bienSau = "" + gioDauRa.second
        }
        return Pair(bienTruoc, bienSau)
    }

    private fun getTimeOfPositionY(positionInDp: Float): Pair<Int, Int> {
        if (positionInDp < 100) {
            return Pair(0, 1)
        } else {
            var heightIn100 = (positionInDp / 100).toInt()
            if (heightIn100 == 23) {
                return Pair(heightIn100 - 1, heightIn100)
            } else {
                return Pair(heightIn100, heightIn100 + 1)
            }
        }
    }

    private fun getCurrentTimeMinute(i: Int): Int {
        var currentTime = Calendar.getInstance()

        val gio = currentTime.get(Calendar.HOUR_OF_DAY)

        val phut = currentTime.get(Calendar.MINUTE)

        val traVe = (gio * 60) + phut
        return traVe
    }

    private fun setLineHeightForGroup() {
        var cao = relativeTimeLine.layoutParams as RelativeLayout.LayoutParams

        cao.width = (dpToPx(700).toInt())

        cao.height = (dpToPx(100) * 24).toInt()
    }

    private fun createTimeLine(i: Int) {

        var line = View(this)

        line.translationY = (dpToPx(100) * i) + dpToPx(25)
        line.setBackgroundColor(Color.BLACK)

        relativeTimeLine.addView(line)

        var layoutParam = line.layoutParams as RelativeLayout.LayoutParams

        layoutParam.width = MATCH_PARENT

        layoutParam.height = 1
    }

    private fun setTimeHeightForGroup() {
        var timeRangeHeight = relativeTimeRange.layoutParams as RelativeLayout.LayoutParams

        timeRangeHeight.width = WRAP_CONTENT

        timeRangeHeight.height = (dpToPx(100) * 24).toInt()
    }

    private fun createTextTimeRange(hour: Int) {
        var text = TextView(this)

        text.translationY = (dpToPx(100) * hour)


        text.setText("${getTimeHaiCot(hour)}:00")
        relativeTimeRange.addView(text)
        var layoutParam = text.layoutParams as RelativeLayout.LayoutParams

        layoutParam.width = WRAP_CONTENT
        layoutParam.height = 50
    }

    private fun getTimeHaiCot(hour: Int): String {
        if (hour < 10) {
            return "0${hour}"
        }
        return hour.toString()
    }

    fun pxToDp(px: Int): Float {
        return (px / Resources.getSystem().getDisplayMetrics().density)
    }

    fun dpToPx(dp: Int): Float {
        return (dp * Resources.getSystem().getDisplayMetrics().density)
    }
}
