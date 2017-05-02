package com.bi.kalah.utility;

import com.bi.kalah.exception.BusinessException;
import com.bi.kalah.helper.GameBoardHelper;
import com.bi.kalah.model.domain.GameBoard;
import com.bi.kalah.model.domain.Hole;
import com.bi.kalah.model.domain.Player;
import com.bi.kalah.model.enumeration.PlayerEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GameBoardHelperTest {
    @Spy
    private GameBoardHelper helper;

    @Test
    public void getActivePlayerAsExpected() throws Exception {
        Player playerNorth = Player.builder().id(PlayerEnum.NORTH).build();
        Player playerSouth = Player.builder().id(PlayerEnum.SOUTH).build();

        GameBoard gameBoard = GameBoard.builder().activePlayer(PlayerEnum.NORTH).playerNorth(playerNorth)
                .playerSouth(playerSouth).build();
        Player player = helper.getActivePlayer(gameBoard);
        assertThat(player.getId(), is(PlayerEnum.NORTH));

    }

    @Test(expected = BusinessException.class)
    public void throwExceptionWhenGameBoardHasWinner() throws Exception {
        GameBoard gameBoard = GameBoard.builder().hasWinner(true).build();
        helper.gameBoardHasWinner(gameBoard);

    }

    @Test(expected = BusinessException.class)
    public void throwExceptionWhenHoleHasNoSeeds() throws Exception {
        Hole hole = Hole.builder().seeds(0).build();
        helper.holeHasSeeds(hole);

    }

    @Test
    public void switchGameBoardNorthToSouthAsExpected() throws Exception {
        GameBoard previousBoard = GameBoard.builder().activePlayer(PlayerEnum.NORTH).build();
        helper.switchActiveUser(previousBoard);
        assertThat(previousBoard.getActivePlayer(), is(PlayerEnum.SOUTH));
    }

    @Test
    public void switchGameBoardSouthToNorthAsExpected() throws Exception {
        GameBoard previousBoard = GameBoard.builder().activePlayer(PlayerEnum.SOUTH).build();
        helper.switchActiveUser(previousBoard);
        assertThat(previousBoard.getActivePlayer(), is(PlayerEnum.NORTH));
    }

}