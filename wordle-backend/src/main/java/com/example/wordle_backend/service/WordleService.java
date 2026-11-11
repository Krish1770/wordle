package com.example.wordle_backend.service;


import com.example.wordle_backend.common.WordDictionary;
import com.example.wordle_backend.dto.CheckWordDto;
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
    
    public ResponseEntity<ResponseDto> getSecretWord() {
        String secretWord = wordDictionary.getSecretWord();
        Game game= Game.builder().secretWord(secretWord).build();
      game= wordleRepoService.Save(game);
       return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK,"value retrieved",game));

    }

    public ResponseEntity<ResponseDto> validateWord(CheckWordDto checkWordDto) {
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
                  char ch=guessWord.charAt(i);

                  if(countMap.containsKey(ch))
                  {
                      int val= Math.toIntExact(countMap.getOrDefault(ch, 0L));
                       if(val==0)
                       {
                            arr[i]="absent";
                       }
                       else
                       {
                           val--;
                           if(answer.charAt(i)==guessWord.charAt(i))
                                 arr[i]="correct";
                           else
                               arr[i]="present";

                           countMap.put(ch, (long) val);
                       }
                  }

                  else
                      arr[i]="absent";
               }

           }
        GuessResultDto guessResultDto=new GuessResultDto(checkWordDto.getGameId(),arr);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(HttpStatus.OK,"guess result validated",guessResultDto));

    }

}
