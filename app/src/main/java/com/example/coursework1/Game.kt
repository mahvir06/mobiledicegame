package com.example.coursework1

import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import java.util.Random



class Game : AppCompatActivity() {

    lateinit var scoreButton: Button
    lateinit var throwButton : Button

    lateinit var dice1 : ImageView
    lateinit var dice2 : ImageView
    lateinit var dice3 : ImageView
    lateinit var dice4 : ImageView
    lateinit var dice5 : ImageView
    lateinit var dice6 : ImageView
    lateinit var dice7 : ImageView
    lateinit var dice8 : ImageView
    lateinit var dice9 : ImageView
    lateinit var dice10 : ImageView

    lateinit var humanScore : TextView
    lateinit var computerScore : TextView

    lateinit var score : EditText

    lateinit var listForRemoving : Array<ImageView>

    var humanTotal = 0
    var compTotal = 0
    var OneTimeFlag = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)
        humanScore = findViewById<TextView>(R.id.humanscore)
        computerScore = findViewById<TextView>(R.id.computerscore)

        if (savedInstanceState != null) {
            // Restore the count value from the saved instance state
            humanTotal = savedInstanceState.getInt("HS", 30)
            compTotal = savedInstanceState.getInt("CS", 31)
            humanScore.text = humanTotal.toString()
            computerScore.text = compTotal.toString()
            OneTimeFlag = savedInstanceState.getInt("flag", 1)


        }

    ///////CREATE HUMAN-SIDE IMAGES ON INITIALIZATION///////
        val diceIDs = arrayOf(
            "die_face_1",
            "die_face_2",
            "die_face_3",
            "die_face_4",
            "die_face_5",
            "die_face_6")

         dice1 = findViewById<ImageView>(R.id.d1)
         dice2 = findViewById<ImageView>(R.id.d2)
         dice3 = findViewById<ImageView>(R.id.d3)
         dice4 = findViewById<ImageView>(R.id.d4)
         dice5 = findViewById<ImageView>(R.id.d5)

        //DICE# = PLACEMENT
        //ID# = NUMBER OF DOTS
        val res1: String = diceIDs[0]
        val id1 = resources.getIdentifier(res1, "drawable", "com.example.coursework1")
        dice1.setImageResource(id1)

        val res2: String = diceIDs[1]
        val id2 = resources.getIdentifier(res2, "drawable", "com.example.coursework1")
        dice2.setImageResource(id2)

        val res3: String = diceIDs[2]
        val id3 = resources.getIdentifier(res3, "drawable", "com.example.coursework1")
        dice3.setImageResource(id3)

        val res4: String = diceIDs[3]
        val id4 = resources.getIdentifier(res4, "drawable", "com.example.coursework1")
        dice4.setImageResource(id4)

        val res5: String = diceIDs[4]
        val id5 = resources.getIdentifier(res5, "drawable", "com.example.coursework1")
        dice5.setImageResource(id5)
        //////////////////////////////

        ///////CREATE COMPUTER-SIDE IMAGES ON INITIALIZATION///////
        dice6 = findViewById<ImageView>(R.id.c1)
        dice7 = findViewById<ImageView>(R.id.c2)
        dice8 = findViewById<ImageView>(R.id.c3)
        dice9 = findViewById<ImageView>(R.id.c4)
        dice10 = findViewById<ImageView>(R.id.c5)

        listForRemoving = arrayOf(dice1, dice2, dice3, dice4, dice5, dice6, dice7, dice8, dice9, dice10)
        var placements = arrayOf(dice6, dice7, dice8, dice9, dice10)
        var iter = 0
        for (n in placements){
            val comp: String = diceIDs[iter]
            val ID = resources.getIdentifier(comp, "drawable", "com.example.coursework1")
            n.setImageResource(ID)
            iter++
        }


        //Variable initializations
        throwButton = findViewById<Button>(R.id.throwbutton)
        scoreButton = findViewById<Button>(R.id.score)
        humanScore = findViewById<TextView>(R.id.humanscore)
        computerScore = findViewById<TextView>(R.id.computerscore)

        score = findViewById<EditText>(R.id.scoreInput)


        var humanAddends = 0 //Current sums of dice lineups, only added when score button is clicked
        var compAddends = 0  //Otherwise, reset to 0 at the beginning of every throw

        humanTotal = 0 //Values to be added to front-end score variables
        compTotal = 0
        var rolls = 0 //Amount of times throw is clicked

        //Throw button clicked, rolling dice up to 3 times
        throwButton.setOnClickListener {
            rolls++
            humanAddends = 0
            compAddends = 0

            OneTimeFlag = 1

            humanAddends += throwDie(diceIDs, dice1, dice2, dice3, dice4, dice5)

            //Only randomly roll everything at the beginning of a round
            if (rolls == 1) {
                computerThrowDie(diceIDs, dice6, dice7, dice8, dice9, dice10)
            }
            scoreButton.contentDescription = "not clicked" //After throwing, I reset the score button
            //again to be able to be clicked again. Otherwise you could continuously press the score button

            if (rolls > 2) {
                scoreButton.performClick()
            }
        }

        // Activates when score button is clicked, adding up totals of the scores
        scoreButton.setOnClickListener{

            //Do not allow score to be clicked when game has not started
            if(OneTimeFlag == 0){
                return@setOnClickListener
            }

            //if button has not been clicked right before this, continue
            if(scoreButton.contentDescription != "clicked"){
                //compAddends += computerThrowDie(diceIDs, dice6, dice7, dice8, dice9, dice10)
                humanTotal += humanAddends
                compTotal += randomComp(diceIDs, dice6, dice7, dice8, dice9, dice10)
                humanScore.text = humanTotal.toString()
                computerScore.text = compTotal.toString()
                rolls = 0


                var temp = score.text.toString()
                var finalScore = temp.toInt()
                if (humanTotal >= finalScore || compTotal >= finalScore){

                    //remove all buttons as game is over

                     if (humanTotal == compTotal){
                        println("Tie! Rerolling until tie broken.")
                        endtie(humanTotal, compTotal, diceIDs)

                    } else if (humanTotal > compTotal){
                        displayPopUp()
                    } else if (compTotal > humanTotal){
                        displayLossPopUp()
                    }
                    scoreButton.isVisible = false
                    throwButton.isVisible = false
                    for (d in listForRemoving){
                        d.isClickable = false
                    }
                }
            }
            scoreButton.contentDescription = "clicked"

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save the count value to the outState bundle
        outState.putInt("HS", humanTotal)
        outState.putInt("CS", compTotal)
        outState.putInt("flag", OneTimeFlag)

        //outState.putInt("1", dice1)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Restore the count value from the saved instance state
//        count = savedInstanceState.getInt("count", 0)
//        updateCountText()
        humanTotal = savedInstanceState.getInt("HS", 34)
        compTotal = savedInstanceState.getInt("CS", 35)
        OneTimeFlag = savedInstanceState.getInt("flag", 1)
    }

    fun throwDie(dots : Array<String>, vararg dice : ImageView): Int {

        val random = Random()
        var value = 0
        for (placement in dice) {


            // Skip rolling and calculations for this die, as it was clicked last time
            // However, only abide by this if the user did not just score.
            if (scoreButton.contentDescription != "clicked"){
                if (placement.contentDescription == "clicked"){
                    var num = placement.tag.toString()
                    value += (num.toInt() + 1)
                    continue
                }
            }


            val r = random.nextInt(6)
            value += (r + 1)
            val res: String = dots[r]
            val id = resources.getIdentifier(res, "drawable", "com.example.coursework1")
            placement.setImageResource(id)
            placement.contentDescription = "not clicked"

            placement.setOnClickListener {

                placement.contentDescription = "clicked"
                placement.tag = r //Set value to the die, so that the value is saved for next throw
               // placement.setColorFilter(Color.BLACK)
                placement.setBackgroundColor(Color.BLACK)
                // Your code to handle the click event
            }
        }

        for (placement in dice) {
            placement.contentDescription = "not clicked"
            placement.colorFilter = null
            placement.tag = null
            placement.background = null
        }
            return value

    }


    fun computerThrowDie(dots : Array<String>, vararg dice : ImageView): Int {

        val random = Random()
        var value = 0

        //Initial roll on first round throw
            for (placement in dice) {
                val r = random.nextInt(6)
                value += (r + 1)
                val res: String = dots[r]
                val id = resources.getIdentifier(res, "drawable", "com.example.coursework1")
                placement.setImageResource(id)
                placement.contentDescription = "not clicked"
                placement.tag = (r + 1)
            }

        //After score button is clicked
        return value
    }

    fun randomComp(dots : Array<String>, vararg dice : ImageView): Int{
        val random = Random()
        var value = 0
        var choose = 0
        var reroll = 0
        var rolls = 1

        reroll = random.nextInt(2)
        println("Reroll randomness generated")
        while(reroll == 1 && rolls < 3){
            println("Dice have changed")

            rolls++
            for (d in dice){
                choose = random.nextInt(2)

                //ALGORITHM HERE: IF CURRENT DIE IS GREATER THAN 3, THEN
                // REROLLING FOR IT IS SKIPPED. This is because the chances of
                //the reroll benefitting the computer player are low, as the dice
                // value is already high. Otherwise, the dice will reroll and have a
                // higher likelihood of becoming a higher value.
                var tempNum = d.tag.toString()
                if (tempNum.toInt() > 3) {
                    continue
                }
                val r = random.nextInt(6)
                value += (r + 1)
                val res: String = dots[r]
                val id = resources.getIdentifier(res, "drawable", "com.example.coursework1")
                d.setImageResource(id)
                d.contentDescription = "not clicked"
                d.tag = (r + 1)
            }
            reroll = random.nextInt(2)
        }


        value = 0
        for (d in dice){
            var num = d.tag.toString()
            value += num.toInt()
        }
        return value
    }

    fun endtie(human:Int, computer:Int, dots : Array<String>) {
        var yourScore = human
        var compScore = computer
        while (yourScore == compScore){

            compScore += computerThrowDie(dots, dice6, dice7, dice8, dice9, dice10)
            yourScore += throwDie(dots, dice1, dice2, dice3, dice4, dice5)

        }

        humanScore.text = yourScore.toString()
        computerScore.text = compScore.toString()
        if (compScore > yourScore){
            displayLossPopUp()
        } else if (yourScore > compScore){
            displayPopUp()
        }

        scoreButton.isVisible = false
        throwButton.isVisible = false
        for (d in listForRemoving){
            d.isClickable = false
        }
    }

    fun displayPopUp(){
        val popupView = layoutInflater.inflate(R.layout.win, null)
        val popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        // Get the display size
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val x = size.x / 2
        val y = size.y / 2
        popupWindow.showAtLocation(popupView, Gravity.CENTER, x, y)

    }

    fun displayLossPopUp(){
        val popupView = layoutInflater.inflate(R.layout.lose, null)
        val popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        // Get the display size
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val x = size.x / 2
        val y = size.y / 2
        popupWindow.showAtLocation(popupView, Gravity.CENTER, x, y)

    }

}