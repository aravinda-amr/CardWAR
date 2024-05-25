package com.example.cardwar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var iv_card1: ImageView
    lateinit var iv_card2: ImageView

    lateinit var tv_player1: TextView
    lateinit var tv_player2: TextView

    lateinit var tv_war: TextView

    lateinit var b_deal: Button
    lateinit var b_replay: Button
    lateinit var b_back: Button

    lateinit var random: Random

    var player1 = 0
    var player2 = 0

    var highestScore = 0

    var cardsArray = intArrayOf(
        R.drawable.ace_of_clubs,
        R.drawable.two_of_clubs,
        R.drawable.three_of_clubs,
        R.drawable.four_of_clubs,
        R.drawable.five_of_clubs,
        R.drawable.six_of_clubs,
        R.drawable.seven_of_clubs,
        R.drawable.eight_of_clubs,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        random = Random

        // Restoring the highest score from SharedPreferences
        loadHighestScore()

        //init objects
        iv_card1 = findViewById(R.id.iv_card1)
        iv_card2 = findViewById(R.id.iv_card2)

        iv_card1.setImageResource(R.drawable.black_joker)
        iv_card2.setImageResource(R.drawable.black_joker)

        tv_player1 = findViewById(R.id.tv_player1)
        tv_player2 = findViewById(R.id.tv_player2)

        tv_war = findViewById(R.id.tv_war)
        tv_war.visibility = View.INVISIBLE

        b_replay = findViewById(R.id.b_replay)
        b_replay.visibility = View.INVISIBLE

        b_back = findViewById(R.id.b_home)
        b_back.visibility = View.INVISIBLE

        //back to home intent
        b_back.setOnClickListener{
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
        }


        b_deal = findViewById(R.id.b_deal)
        b_deal.setOnClickListener {

            //hide war label
            tv_war.visibility = View.INVISIBLE

            //generates cards
            val card1 = random.nextInt(cardsArray.size)
            val card2 = random.nextInt(cardsArray.size)

            //set images
            setCardImage(card1, iv_card1)
            setCardImage(card2, iv_card2)

            //compare the cards
            if(card1 > card2) {
                player1++
                tv_player1.text = "Player 1: $player1"
            } else if (card1 < card2){
                player2++
                tv_player2.text = "Player 2: $player2"
            } else {

                // Determine the winner
                val winner: String
                if (player1 > player2) {
                    winner = "Player 1 wins!"
                } else if (player2 > player1) {
                    winner = "Player 2 wins!"
                } else {
                    winner = "It's a tie!"
                }

                tv_war.text = winner

                //show war label
                tv_war.visibility = View.VISIBLE
                loadHighestScore()
                b_deal.visibility = View.INVISIBLE
                b_replay.visibility = View.VISIBLE
                b_back.visibility = View.VISIBLE
                tv_player1.visibility = View.INVISIBLE
                tv_player2.visibility = View.INVISIBLE
            }

            //reset game
            b_replay.setOnClickListener {
                resetGame()
            }



            // Save highest score
            if (player1 > highestScore) {
                highestScore = player1
                saveHighestScore()
            }
            if (player2 > highestScore) {
                highestScore = player2
                saveHighestScore()
            }
        }

    }

    private fun setCardImage(number: Int, imageView: ImageView) {
        imageView.setImageResource(cardsArray[number])
    }

    private fun saveHighestScore() {
        val sharedPref = getPreferences(MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("highest_score", highestScore)
            apply()
        }
    }

    private fun loadHighestScore() {
        val sharedPref = getPreferences(MODE_PRIVATE)
        highestScore = sharedPref.getInt("highest_score", 0)
        val highestScoreTextView: TextView = findViewById(R.id.tv_highest_score)
        highestScoreTextView.text = "Highest Score: $highestScore"
    }

    private fun resetGame() {

        tv_player1.visibility = View.VISIBLE
        tv_player2.visibility = View.VISIBLE

        // Reset player scores
        player1 = 0
        player2 = 0
        tv_player1.text = "Player 1: $player1"
        tv_player2.text = "Player 2: $player2"

        // Reset highest score display
        val highestScoreTextView: TextView = findViewById(R.id.tv_highest_score)
        highestScoreTextView.text = "Highest Score: $highestScore"

        // Show deal button and hide replay button
        b_deal.visibility = View.VISIBLE
        b_replay.visibility = View.INVISIBLE
        b_back.visibility = View.INVISIBLE

        // Hide war label
        tv_war.visibility = View.INVISIBLE
    }


}