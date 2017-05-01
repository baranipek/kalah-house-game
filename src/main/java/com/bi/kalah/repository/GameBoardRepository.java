package com.bi.kalah.repository;

import com.bi.kalah.model.GameBoard;

import java.util.List;


public interface GameBoardRepository {

    GameBoard save(GameBoard board);

    GameBoard find(String id);

    List<GameBoard> findAll();
}