package com.example.kidslearntv

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

data class LearningItem(val key: String, val displayText: String, val word: String, val soundText: String, val color: Int)

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var pressCount = 0
    private lateinit var tvInstruction: TextView
    private lateinit var tvFeedback: TextView
    private lateinit var tvPressCount: TextView
    private lateinit var tvLetterDisplay: TextView
    private lateinit var tvWordDisplay: TextView
    private lateinit var ivMainImage: ImageView
    private lateinit var rootLayout: ConstraintLayout

    private val items = mapOf(
        "A" to LearningItem("A", "A", "Apple", "A for Apple", R.color.kid_red),
        "B" to LearningItem("B", "B", "Ball", "B for Ball", R.color.kid_blue),
        "C" to LearningItem("C", "C", "Cat", "C for Cat", R.color.kid_green),
        "D" to LearningItem("D", "D", "Dog", "D for Dog", R.color.kid_orange),
        "E" to LearningItem("E", "E", "Elephant", "E for Elephant", R.color.kid_purple),
        "F" to LearningItem("F", "F", "Fish", "F for Fish", R.color.kid_pink),
        "G" to LearningItem("G", "G", "Giraffe", "G for Giraffe", R.color.kid_purple),
        "H" to LearningItem("H", "H", "House", "H for House", R.color.kid_blue),
        "I" to LearningItem("I", "I", "Ice Cream", "I for Ice Cream", R.color.kid_blue),
        "J" to LearningItem("J", "J", "Jug", "J for Jug", R.color.kid_yellow),
        "K" to LearningItem("K", "K", "Kite", "K for Kite", R.color.kid_green),
        "L" to LearningItem("L", "L", "Lion", "L for Lion", R.color.kid_orange),
        "M" to LearningItem("M", "M", "Monkey", "M for Monkey", R.color.kid_purple),
        "N" to LearningItem("N", "N", "Nest", "N for Nest", R.color.kid_purple),
        "O" to LearningItem("O", "O", "Orange", "O for Orange", R.color.kid_red),
        "P" to LearningItem("P", "P", "Penguin", "P for Penguin", R.color.kid_blue),
        "Q" to LearningItem("Q", "Q", "Queen", "Q for Queen", R.color.kid_pink),
        "R" to LearningItem("R", "R", "Rabbit", "R for Rabbit", R.color.kid_green),
        "S" to LearningItem("S", "S", "Sun", "S for Sun", R.color.kid_yellow),
        "T" to LearningItem("T", "T", "Tree", "T for Tree", R.color.kid_green),
        "U" to LearningItem("U", "U", "Umbrella", "U for Umbrella", R.color.kid_blue),
        "V" to LearningItem("V", "V", "Violin", "V for Violin", R.color.kid_purple),
        "W" to LearningItem("W", "W", "Whale", "W for Whale", R.color.kid_blue),
        "X" to LearningItem("X", "X", "Xylophone", "X for Xylophone", R.color.kid_orange),
        "Y" to LearningItem("Y", "Y", "Yacht", "Y for Yacht", R.color.kid_blue),
        "Z" to LearningItem("Z", "Z", "Zebra", "Z for Zebra", R.color.kid_red),
        "1" to LearningItem("1", "1", "One", "One", R.color.kid_red),
        "2" to LearningItem("2", "2", "Two", "Two", R.color.kid_blue),
        "3" to LearningItem("3", "3", "Three", "Three", R.color.kid_green),
        "4" to LearningItem("4", "4", "Four", "Four", R.color.kid_orange),
        "5" to LearningItem("5", "5", "Five", "Five", R.color.kid_purple),
        "6" to LearningItem("6", "6", "Six", "Six", R.color.kid_pink),
        "7" to LearningItem("7", "7", "Seven", "Seven", R.color.kid_purple),
        "8" to LearningItem("8", "8", "Eight", "Eight", R.color.kid_blue),
        "9" to LearningItem("9", "9", "Nine", "Nine", R.color.kid_yellow),
        "0" to LearningItem("0", "0", "Zero", "Zero", R.color.kid_red),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initTTS()
        setupFocus()
    }

    private fun initViews() {
        tvInstruction = findViewById(R.id.tvInstruction)
        tvFeedback = findViewById(R.id.tvFeedback)
        tvPressCount = findViewById(R.id.tvPressCount)
        tvLetterDisplay = findViewById(R.id.tvLetterDisplay)
        tvWordDisplay = findViewById(R.id.tvWordDisplay)
        ivMainImage = findViewById(R.id.ivMainImage)
        rootLayout = findViewById(R.id.rootLayout)
    }

    private fun initTTS() {
        tts = TextToSpeech(this, this)
    }

    private fun setupFocus() {
        rootLayout.isFocusable = true
        rootLayout.isFocusableInTouchMode = true
        rootLayout.requestFocus()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val keyChar = getKeyChar(keyCode)
        if (keyChar != null) { handleKeyPress(keyChar.uppercase()); return true }
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) { handleKeyPress("A"); return true }
        return super.onKeyDown(keyCode, event)
    }

    private fun getKeyChar(keyCode: Int): String? = when (keyCode) {
        KeyEvent.KEYCODE_A -> "A"; KeyEvent.KEYCODE_B -> "B"; KeyEvent.KEYCODE_C -> "C"; KeyEvent.KEYCODE_D -> "D"; KeyEvent.KEYCODE_E -> "E"
        KeyEvent.KEYCODE_F -> "F"; KeyEvent.KEYCODE_G -> "G"; KeyEvent.KEYCODE_H -> "H"; KeyEvent.KEYCODE_I -> "I"; KeyEvent.KEYCODE_J -> "J"
        KeyEvent.KEYCODE_K -> "K"; KeyEvent.KEYCODE_L -> "L"; KeyEvent.KEYCODE_M -> "M"; KeyEvent.KEYCODE_N -> "N"; KeyEvent.KEYCODE_O -> "O"
        KeyEvent.KEYCODE_P -> "P"; KeyEvent.KEYCODE_Q -> "Q"; KeyEvent.KEYCODE_R -> "R"; KeyEvent.KEYCODE_S -> "S"; KeyEvent.KEYCODE_T -> "T"
        KeyEvent.KEYCODE_U -> "U"; KeyEvent.KEYCODE_V -> "V"; KeyEvent.KEYCODE_W -> "W"; KeyEvent.KEYCODE_X -> "X"; KeyEvent.KEYCODE_Y -> "Y"
        KeyEvent.KEYCODE_Z -> "Z"; KeyEvent.KEYCODE_0 -> "0"; KeyEvent.KEYCODE_1 -> "1"; KeyEvent.KEYCODE_2 -> "2"; KeyEvent.KEYCODE_3 -> "3"
        KeyEvent.KEYCODE_4 -> "4"; KeyEvent.KEYCODE_5 -> "5"; KeyEvent.KEYCODE_6 -> "6"; KeyEvent.KEYCODE_7 -> "7"; KeyEvent.KEYCODE_8 -> "8"
        KeyEvent.KEYCODE_9 -> "9"; else -> null
    }

    private fun handleKeyPress(key: String) {
        val item = items[key]
        if (item != null) {
            pressCount++
            showItem(item)
            speak(item.soundText)
            tvPressCount.text = "Learned: $pressCount"
        } else {
            tvFeedback.text = "Press A-Z or 0-9 keys!"
            tvFeedback.setTextColor(getColor(R.color.kid_red))
            tvFeedback.visibility = View.VISIBLE
            tvFeedback.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left))
        }
    }

    private fun showItem(item: LearningItem) {
        val color = getColor(item.color)
        tvLetterDisplay.text = item.displayText; tvLetterDisplay.setTextColor(color)
        tvWordDisplay.text = item.word; tvWordDisplay.setTextColor(color)
        tvFeedback.text = item.soundText; tvFeedback.setTextColor(color); tvFeedback.visibility = View.VISIBLE
        ivMainImage.setColorFilter(color)
        val anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        tvLetterDisplay.startAnimation(anim)
        tvWordDisplay.startAnimation(anim)
        tvFeedback.startAnimation(anim)
        ivMainImage.startAnimation(anim)
    }

    private fun speak(text: String) {
        tts?.let { if (it.isSpeaking) it.stop(); it.speak(text, TextToSpeech.QUEUE_FLUSH, null, "kids_learning") }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            tts?.setSpeechRate(0.9f)
            tts?.setPitch(1.1f)
            speak("Welcome to Kids Learn TV! Press any letter or number key to learn!")
        }
    }

    override fun onDestroy() { tts?.shutdown(); super.onDestroy() }
}
