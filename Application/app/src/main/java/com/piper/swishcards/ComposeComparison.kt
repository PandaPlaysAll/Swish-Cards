package com.piper.swishcards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ComposeComparison : AppCompatActivity() {
    private lateinit var viewModel: FlashCardViewModelz
    private lateinit var cardButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose_comparison)

        //uses MVVM approach.
        viewModel = ViewModelProvider(this).get(FlashCardViewModelz::class.java)
        cardButton = findViewById(R.id.flashCardBTN)
        nextButton = findViewById(R.id.nextCardBTN)

        cardButton.text = viewModel.flashCards.currentFlashCards

        cardButton.setOnClickListener {
            viewModel.flashCards.changeState(State.Answer)
            cardButton.text = viewModel.flashCards.currentFlashCards
        }

        nextButton.setOnClickListener {
            viewModel.flashCards.incrementQuestion()
            cardButton.text = viewModel.flashCards.currentFlashCards
        }
    }
}

data class Question(val question: String, val answer: String)

enum class State {
    Question,
    Answer
}

class FlashCards(cards: List<Question>) {

    val flashCards = cards

    var currentQuestion = 0

    val currentFlashCards
        get() = if (currentState == State.Question) flashCards[currentQuestion].question else flashCards[currentQuestion].answer

    var currentState: State = State.Question

    val changeState = {state: State -> currentState = state }

    fun incrementQuestion() {
        if (currentQuestion + 1 >= flashCards.size) {
            currentQuestion = 0
        } else {
            currentQuestion++
        }
        currentState = State.Question
    }
}


class FlashCardViewModelz: ViewModel() {

    var flashCards = (FlashCards( listOf(
        Question("How many Bananas should go in a Smoothie?", "3 Bananas"),
        Question("How many Eggs does it take to make an Omellete?", "8 Eggs"),
        Question("How do you say Hello in Japenese?", "Konichiwa"),
        Question("What is Korea's currency?", "Won")
    )))
}
