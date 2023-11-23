package com.example.guessinggame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    private val words = listOf("Android", "Activity", "Fragment")
    private val secretWord = words.random().uppercase()

    val secretWordDisplay: LiveData<String> get() = _secretWordDisplay
    private val _secretWordDisplay = MutableLiveData<String>()

    private var correctGuesses = ""

    val incorrectGuesses: LiveData<String> get() = _incorrectGuesses
    private val _incorrectGuesses = MutableLiveData("")

    val livesLeft: LiveData<Int> get() = _livesLeft
    private val _livesLeft = MutableLiveData(8)

    val gameOver: LiveData<Boolean> get() = _gameOver
    private val _gameOver = MutableLiveData(false)

    init {
        _secretWordDisplay.value = produceSecretWordDisplay()
    }

    private fun checkLetter(str: String) = when (correctGuesses.contains(str)) {
        true -> str
        false -> "_"
    }

    private fun produceSecretWordDisplay(): String {
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())
        }
        return display
    }

    fun makeGuess(guess: String) {
        if (guess.length == 1) {
            if (secretWord.contains(guess)) {
                correctGuesses += guess
                _secretWordDisplay.value = produceSecretWordDisplay()
            } else {
                _incorrectGuesses.value += "$guess "
                _livesLeft.value = livesLeft.value?.minus(1)
            }
            if (isWon() || isLost()) {
                _gameOver.value = true
            }
        }
    }

    private fun isWon() = secretWord.equals(_secretWordDisplay.value, true)

    private fun isLost() = (livesLeft.value ?: 0) <= 0

    fun wonLostMessage(): String {
        var message = ""
        if (isWon()) message = "You won!"
        else if (isLost()) message = "You lost!"
        message += " The word was $secretWord."
        return message
    }
}