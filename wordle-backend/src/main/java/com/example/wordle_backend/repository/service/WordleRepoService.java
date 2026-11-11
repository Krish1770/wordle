package com.example.wordle_backend.repository.service;

import com.example.wordle_backend.entity.Game;
import com.example.wordle_backend.repository.WordleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WordleRepoService {

    @Autowired
    WordleRepository wordleRepository;

   public Game Save(Game game)
   {
       return wordleRepository.save(game);
   }

    public Optional<Game> findById(String gameId) {
       return  wordleRepository.findById(gameId);
    }
}
