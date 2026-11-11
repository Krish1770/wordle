package com.example.wordle_backend.controller;

import com.example.wordle_backend.dto.CheckWordDto;
import com.example.wordle_backend.dto.ResponseDto;
import com.example.wordle_backend.service.WordleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/wordle")
public class WordleController {

    @Autowired
    private WordleService wordleService;

    @GetMapping("secretWord")
    public ResponseEntity<ResponseDto> getSecretWord()
    {
        return wordleService.getSecretWord();
    }

    @PostMapping("validate")
    public ResponseEntity<ResponseDto> validateWord(@RequestBody CheckWordDto checkWordDto )
    {
        return wordleService.validateWord(checkWordDto);
    }
}
