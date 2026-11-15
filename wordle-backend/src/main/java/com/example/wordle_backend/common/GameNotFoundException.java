package com.example.wordle_backend.common;

public class GameNotFoundException extends RuntimeException{

    public GameNotFoundException(String Message) {
        super(Message);
    }
}
