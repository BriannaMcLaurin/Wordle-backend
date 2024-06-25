package com.example.wordle_game.domain.wordle.models;

public class Wordle {
    private String word;
    private int currentTries;

    public Wordle(String word, int currentTries) {
        this.word = word;
        this.currentTries = currentTries;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCurrentTries() {
        return currentTries;
    }

    public void setCurrentTries(int currentTries) {
        this.currentTries = currentTries;
    }
}
