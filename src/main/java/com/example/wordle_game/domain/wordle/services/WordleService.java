package com.example.wordle_game.domain.wordle.services;

import com.example.wordle_game.domain.wordle.models.CharacterValue;
import com.example.wordle_game.domain.wordle.models.GameStatus;
import com.example.wordle_game.domain.wordle.models.GuessResponse;
import com.example.wordle_game.domain.wordle.models.Wordle;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.wordle_game.domain.wordle.models.CharacterValue.*;
import static com.example.wordle_game.domain.wordle.models.GameStatus.IN_PROGRESS;
import static com.example.wordle_game.domain.wordle.models.GameStatus.WIN;
import static java.util.Optional.of;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;


@Scope(value = SCOPE_SINGLETON)
@Service
public class WordleService {
    ConcurrentHashMap<String, Wordle> wordles = new ConcurrentHashMap<>();

    public String addGameToDataStore(String word){
        String userKey = UUID.randomUUID().toString();
        wordles.put(userKey,new Wordle(word, 0));
        return userKey;
    }

    public GuessResponse submitGuess(String requestWord,String userToken) {
        Wordle wordle = of(wordles.get(userToken)).orElseThrow(() -> new RuntimeException("Session doesn't exist"));
        if(wordle.getCurrentTries()>5){
            throw new RuntimeException("User game is finished already");
        }

        int currentTry=wordle.getCurrentTries();
        wordle.setCurrentTries(++currentTry);
       String userWord =  wordle.getWord();

        Map<Character, CharacterValue> characterValueMap = new HashMap<>();

        if(userWord.equalsIgnoreCase(requestWord)){
            wordles.remove(userToken);
            return new GuessResponse(currentTry,requestWord, WIN, null);
        } else{
            for (int i = 0; i < userWord.length(); i++) {
                if(userWord.charAt(i)==requestWord.charAt(i)){
                    characterValueMap.put(requestWord.charAt(i), CORRECT);
                }else if (userWord.contains(Character.toString(requestWord.charAt(i)))){
                    characterValueMap.put(requestWord.charAt(i), PRESENT_BUT_MISPLACED);
                }else{
                    characterValueMap.put(userWord.charAt(i), NOT_PRESENT);
                }
            }
        }
        wordles.replace(userToken, wordle);
        return new GuessResponse(currentTry, " ", IN_PROGRESS, characterValueMap);
    }
}
