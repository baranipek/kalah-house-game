package com.bi.kalah.repository.impl;

import com.bi.kalah.model.EndZone;
import com.bi.kalah.model.GameBoard;
import com.bi.kalah.model.Player;
import com.bi.kalah.model.enumeration.PlayerEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class GameBoardRepositoryImplTest {

    @InjectMocks
    private GameBoardRepositoryImpl gameBoardRepository;

    private Player playerNorth;

    private Player playerSouth;

    private EndZone endZone;

    @Before
    public void setUp() throws Exception {
        endZone = EndZone.builder().seeds(0).seeds(0).build();
        playerNorth = Player.builder().id(PlayerEnum.NORTH).endZone(endZone).build();
        playerSouth = Player.builder().id(PlayerEnum.SOUTH).endZone(endZone).build();
    }

    @Test
    public void saveBoardProperly() throws Exception {
        GameBoard gameBoard = GameBoard.builder().activePlayer(PlayerEnum.NORTH).id(UUID.randomUUID().toString())
                .playerNorth(playerNorth).playerSouth(playerSouth).build();
        GameBoard gameBoardUpdated= gameBoardRepository.save(gameBoard);

        assertNotNull(gameBoardUpdated);
        assertThat(gameBoardUpdated.getActivePlayer(), is(PlayerEnum.NORTH));
        assertThat(gameBoard.getId(), is(gameBoardUpdated.getId()));
    }

    @Test
    public void findBoardByIdAsExpected() throws Exception {
        GameBoard gameBoard = GameBoard.builder().activePlayer(PlayerEnum.NORTH).id(UUID.randomUUID().toString())
                .playerNorth(playerNorth).playerSouth(playerSouth).build();
        gameBoardRepository.save(gameBoard);

        GameBoard gameBoardFetched = gameBoardRepository.find(gameBoard.getId());
        assertNotNull(gameBoardFetched);
        assertThat(gameBoardFetched.getActivePlayer(), is(PlayerEnum.NORTH));
        assertEquals(gameBoardFetched.getPlayerNorth().getEndZone().getSeeds(), 0);

    }

    @Test
    public void findAllBoardsAsExpected() throws Exception {
        GameBoard gameBoard = GameBoard.builder().activePlayer(PlayerEnum.NORTH).id(UUID.randomUUID().toString())
                .playerNorth(playerNorth).playerSouth(playerSouth).build();

        GameBoard gameBoardSecondRecord = GameBoard.builder().activePlayer(PlayerEnum.NORTH).id(UUID.randomUUID().toString())
                .playerNorth(playerNorth).playerSouth(playerSouth).build();

        gameBoardRepository.save(gameBoard);
        gameBoardRepository.save(gameBoardSecondRecord);

        List<GameBoard> gameBoards = gameBoardRepository.findAll();
        assertNotNull(gameBoards);
        assertTrue(gameBoards.contains(gameBoard));
        assertTrue(gameBoards.contains(gameBoardSecondRecord));

    }

}