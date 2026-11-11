package com.example.wordle_backend.repository;

import com.example.wordle_backend.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordleRepository extends JpaRepository<Game,String> {
}
