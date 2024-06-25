package com.example.wordle_game.domain.wordle.controller;

import com.example.wordle_game.domain.wordle.models.GuessResponse;
import com.example.wordle_game.domain.wordle.services.WordService;
import com.example.wordle_game.domain.wordle.services.WordleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@CrossOrigin(origins = "*")
public class WordleController {

    private WordleService wordleService;
    private WordService wordService;

    @Autowired
    public WordleController(WordleService wordleService, WordService wordService) {
        this.wordleService = wordleService;
        this.wordService = wordService;
    }

    @GetMapping("/startGame")
    public String startGame(){
        String word =wordService.getRandomWord();
        System.out.println(word);
        return wordleService.addGameToDataStore(word);
    }

    @PostMapping("/submitGuess")
    public GuessResponse submitGuess(@RequestParam String guess,@RequestParam String userToken){
       return wordleService.submitGuess(guess, userToken);
    }

    private int guessesRemaining = 6;
    private String wordToGuess = "example";
    private String hint = null;

    @Value("${dictionary.api.key}")
    private String apiKey;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("guessesRemaining", guessesRemaining);
        model.addAttribute("hint",hint);
        return "index";
    }
    @GetMapping("/hint")
    public String getHint(Model model){
        if(guessesRemaining <= 3){
            hint = getDefinition(wordToGuess);
        }
        model.addAttribute("guessesRemaining", guessesRemaining);
        model.addAttribute("hint",hint);
        return "index";
    }
    private  String getDefinition(String word){
        String url = UriComponentsBuilder.fromHttpUrl("https://www.dictionaryapi.com/api/v3/references/collegiate/json/"
        + word).queryParam("key", apiKey).toUriString();

        RestTemplate restTemplate = new RestTemplate();
        String[] response = restTemplate.getForObject(url, String[].class);

        if(response != null && response.length > 0){
            return response[0];
        }else{
            return "No definition found.";
        }
    }
}
