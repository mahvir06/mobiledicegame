package com.example.coursework1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var startbutton = findViewById<Button>(R.id.start)

        startbutton.setOnClickListener {
            val intent = Intent(this, Game::class.java)
            startActivity(intent)

        }
//
        var aboutButton = findViewById<Button>(R.id.about)

        aboutButton.setOnClickListener {

            val popupView = layoutInflater.inflate(R.layout.aboutpopup, null)
            val popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

//
            val location = IntArray(2)
            aboutButton.getLocationOnScreen(location)
            popupWindow.showAtLocation(aboutButton, Gravity.CENTER_VERTICAL, location[0], location[1] + 500)
       }





    }

//    fun displayGameOver(winner: Char) {
//        var popupView: View = layoutInflater.inflate(R.layout.popup_game_over, null)
//
//        var gameover_textView  = popupView.findViewById<TextView>(R.id.gameover_tv)
//        gameover_textView.setText("GAME OVER! - ")
//        if (winner == '-')
//            gameover_textView.append("It is a draw")
//        else
//            gameover_textView.append("$winner wins")
//
//        var popupWindow = PopupWindow(this)
//        popupWindow.contentView = popupView
//        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)
//
//        popupView.setOnClickListener { popupWindow.dismiss() }
//
//        /*popupView.setOnTouchListener { v, event ->
//            popupWindow.dismiss()
//            true } */
//
//        // Disable all buttons
//        for (r in buttons)
//            for (b in r)
//                b.isClickable = false
//    }
}