package com.example.wordle_backend.service;


import com.example.wordle_backend.common.GameNotFoundException;
import com.example.wordle_backend.common.WordDictionary;
import com.example.wordle_backend.dto.CheckWordDto;
import com.example.wordle_backend.dto.GameStartResponseDto;
import com.example.wordle_backend.dto.GuessResultDto;
import com.example.wordle_backend.dto.ResponseDto;
import com.example.wordle_backend.entity.Game;
import com.example.wordle_backend.repository.service.WordleRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WordleService {


    @Autowired
    private WordDictionary wordDictionary;

    @Autowired
    private WordleRepoService wordleRepoService;
    
    public GameStartResponseDto getSecretWord() {
        String secretWord = wordDictionary.getSecretWord();
        Game game= Game.builder().secretWord(secretWord).build();
      game= wordleRepoService.Save(game);
        return new GameStartResponseDto(game.getGameId());
    }

    public GuessResultDto validateWord(CheckWordDto checkWordDto) {
           Optional<Game> game = wordleRepoService.findById(checkWordDto.getGameId());
           String guessWord= checkWordDto.getGuessWord();
        String[] arr =new String[5];

        if(game.isPresent())
           {
               String answer=game.get().getSecretWord();
               Map<Character, Long> countMap=answer.chars().mapToObj(c->(char)c)
                       .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));

                for(int i=0;i<5;i++)
                {
                    char ch = guessWord.charAt(i);
                    if(guessWord.charAt(i)==answer.charAt(i))
                    {
                        arr[i]="Correct";
                        countMap.put(ch, countMap.get(ch)-1);
                    }
                }
               for(int i=0;i<5;i++)
               {
                  char ch=guessWord.charAt(i);
                  if(!arr[i].isEmpty())
                  {
                      continue;
                  }
                  else
                  {
                     if(countMap.containsKey(ch))
                      {
                          countMap.put(ch, countMap.get(ch)-1);
                        arr[i]="Present";
                      }
                     else
                         arr[i]="Absent";
                  }
               }

           }
        else
        {
           throw  new GameNotFoundException("Game Id not found for "+checkWordDto.getGameId());
        }
        return new GuessResultDto(checkWordDto.getGameId(),arr);

    }

}
