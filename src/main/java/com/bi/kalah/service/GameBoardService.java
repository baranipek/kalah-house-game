package com.bi.kalah.service;


import com.bi.kalah.model.domain.GameBoard;
import com.bi.kalah.model.enumeration.HoleEnum;

import java.util.List;

public interface GameBoardService {
    GameBoard create();

    GameBoard move(String id, HoleEnum holeId);

    GameBoard find(String boardId);

    List<GameBoard> findAll();
}
