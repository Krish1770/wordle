package com.example.wordle_backend.common;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WordDictionary {

    private final Set<String> words;

    public WordDictionary(Set<String> words) {
        try (InputStream input = getClass().getResourceAsStream("/words.csv")) {

            if(input== null)
                throw new IllegalStateException("no words found");

            try (BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(input, StandardCharsets.UTF_8)))
            {
                this.words= bufferedReader.lines()
                        .skip(1)
                        .map(String::trim)
                        .map(String::toUpperCase)
                        .filter(word -> word.matches("[A-Z]{5}"))
                        .collect(Collectors.toUnmodifiableSet());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean  isWordValid(String word)
    {
        return word!=null && words.contains(word.trim().toUpperCase());
    }

    public String getSecretWord()
    {
        int len= words.size();

          Random random=new Random();

          int randomIndex= random.nextInt(len);
               String secretWrd = new ArrayList<String>(words).get(randomIndex
               );
        System.out.println(secretWrd);
          return secretWrd;
    }
}
