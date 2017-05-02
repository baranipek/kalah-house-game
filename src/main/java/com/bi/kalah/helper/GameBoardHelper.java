package com.bi.kalah.helper;

import com.bi.kalah.exception.BusinessException;
import com.bi.kalah.model.domain.GameBoard;
import com.bi.kalah.model.domain.Hole;
import com.bi.kalah.model.domain.Player;
import com.bi.kalah.model.enumeration.HoleEnum;
import com.bi.kalah.model.enumeration.PlayerEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameBoardHelper {

    private static final int MIN_SEED_COUNT_IN_HOLE=0;

    private static final int MAX_HOLE_COUNT=6;

    public List<Hole> generateDefaultPitList() {
        List<Hole> holes = new ArrayList<>();

        for (HoleEnum holeEnum : HoleEnum.values()) {
            holes.add(Hole.builder().id(holeEnum).seeds(MAX_HOLE_COUNT).build());
        }

        return holes;
    }

    public Player getActivePlayer(final GameBoard gameBoard) {
        return gameBoard.getActivePlayer() == PlayerEnum.SOUTH ? gameBoard.getPlayerSouth() : gameBoard.getPlayerNorth();
    }


    public void gameBoardHasWinner(GameBoard gameBoard) {
        if (gameBoard.isHasWinner())
            throw new BusinessException("Game won by" + gameBoard.getActivePlayer());
    }

    public void holeHasSeeds(Hole activeHold) {
        if (MIN_SEED_COUNT_IN_HOLE == activeHold.getSeeds())
            throw new BusinessException("No Seed In The Hole, Try Another");
    }

    public void switchActiveUser(GameBoard gameBoard) {
        if (gameBoard.getActivePlayer() == PlayerEnum.SOUTH) {
            gameBoard.setActivePlayer(PlayerEnum.NORTH);
        } else {
            gameBoard.setActivePlayer(PlayerEnum.SOUTH);
        }
    }

}
