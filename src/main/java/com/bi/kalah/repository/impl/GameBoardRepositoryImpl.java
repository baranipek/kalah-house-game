package com.bi.kalah.repository.impl;

import com.bi.kalah.model.domain.GameBoard;
import com.bi.kalah.repository.GameBoardRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GameBoardRepositoryImpl implements GameBoardRepository {

    private static final Map<String, GameBoard> gameBoards = new ConcurrentHashMap<>();

    @Override
    public GameBoard save(GameBoard board) {
        gameBoards.put(board.getId(), board);
        return board;
    }

    @Override
    public GameBoard find(String id) {
        return gameBoards.get(id);
    }

    @Override
    public List<GameBoard> findAll() {
        List<GameBoard> gameBoardList = new ArrayList<>(gameBoards.values());
        return gameBoardList;
    }

}