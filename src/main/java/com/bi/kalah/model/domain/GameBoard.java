package com.bi.kalah.model.domain;

import com.bi.kalah.model.enumeration.PlayerEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameBoard {
    private String id;
    private PlayerEnum activePlayer;
    private Player playerNorth;
    private Player playerSouth;
    private boolean hasWinner;

}
