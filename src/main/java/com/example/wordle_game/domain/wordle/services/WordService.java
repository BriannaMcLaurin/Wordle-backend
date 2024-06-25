package com.example.wordle_game.domain.wordle.services;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;

@Service
public class WordService {

    private List<String> words;
    @PostConstruct
    private void loadWords() throws IOException {
        words = FileUtils.readLines(new File("src/main/resources/sgb-words.txt"), "utf-8");
    }

    public String getRandomWord() {
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }
}
