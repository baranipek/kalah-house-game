package com.bi.kalah.service.impl;

import com.bi.kalah.exception.ResourceNotFoundException;
import com.bi.kalah.model.EndZone;
import com.bi.kalah.model.GameBoard;
import com.bi.kalah.model.Hole;
import com.bi.kalah.model.Player;
import com.bi.kalah.model.enumeration.HoleEnum;
import com.bi.kalah.model.enumeration.PlayerEnum;
import com.bi.kalah.repository.GameBoardRepository;
import com.bi.kalah.utility.GameBoardHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameBoardServiceImplTest {

    @InjectMocks
    private GameBoardServiceImpl boardService;

    @Mock
    private GameBoardRepository boardRepository;

    @Spy
    private GameBoardHelper helper;

    private Player playerNorth;

    private Player playerSouth;

    private EndZone endZoneSouth;

    private EndZone endZoneNorth;


    @Before
    public void setUp() throws Exception {
        endZoneSouth = EndZone.builder().seeds(0).seeds(0).build();
        endZoneNorth = EndZone.builder().seeds(0).seeds(0).build();

        List<Hole> holeListSouth = this.getHoles();
        List<Hole> holeListNorth = this.getHoles();

        playerNorth = Player.builder().id(PlayerEnum.NORTH).holeList(holeListNorth).
                endZone(endZoneNorth).build();
        playerSouth = Player.builder().id(PlayerEnum.SOUTH).holeList(holeListSouth).
                endZone(endZoneSouth).build();

    }


    @Test
    public void createGameBoardProperly() throws Exception {
        GameBoard gameBoard = GameBoard.builder().activePlayer(PlayerEnum.NORTH).id(UUID.randomUUID().toString())
                .playerNorth(playerNorth).playerSouth(playerSouth).build();
        when(boardRepository.save(any())).thenReturn(gameBoard);

        GameBoard board = boardService.create();

        assertNotNull(board.getId());
        assertNotNull(board.getPlayerNorth());
        assertNotNull(board.getPlayerSouth());
        assertFalse(board.isHasWinner());
        assertThat(board.getActivePlayer(), is(PlayerEnum.NORTH));
        assertThat(board.getPlayerNorth().getHoleList().size(), is(6));
        assertThat(board.getPlayerSouth().getHoleList().size(), is(6));

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findGameBoardResourceWithWrongId() {
        when(boardRepository.find("Wrong way")).thenReturn(null);

        boardService.move("Wrong", HoleEnum.FIVE);
    }


    @Test
    public void playerNorthMoveFirstSeedRight() {
        GameBoard gameBoard = GameBoard.builder().activePlayer(PlayerEnum.NORTH).id(UUID.randomUUID().toString())
                .playerNorth(playerNorth).playerSouth(playerSouth).activePlayer(PlayerEnum.NORTH).build();
        when(boardRepository.save(any())).thenReturn(gameBoard);

        when(boardRepository.find(gameBoard.getId())).thenReturn(gameBoard);

        GameBoard board = boardService.move(gameBoard.getId(), HoleEnum.ONE);

        assertFalse(board.isHasWinner());
        assertThat(board.getPlayerNorth().getEndZone().getSeeds(), is(1));
        assertThat(board.getPlayerNorth().getHoleList().get(0).getSeeds(), is(0));
        assertThat(board.getPlayerNorth().getHoleList().get(1).getSeeds(), is(7));
        assertThat(board.getPlayerNorth().getHoleList().get(2).getSeeds(), is(7));
        assertThat(board.getPlayerNorth().getHoleList().get(3).getSeeds(), is(7));
        assertThat(board.getPlayerNorth().getHoleList().get(4).getSeeds(), is(7));
        assertThat(board.getPlayerNorth().getHoleList().get(5).getSeeds(), is(7));

        //south
        assertThat(board.getPlayerSouth().getHoleList().get(0).getSeeds(), is(6));
        assertThat(board.getPlayerSouth().getHoleList().get(1).getSeeds(), is(6));
        assertThat(board.getPlayerSouth().getHoleList().get(2).getSeeds(), is(6));
        assertThat(board.getPlayerSouth().getHoleList().get(3).getSeeds(), is(6));
        assertThat(board.getPlayerSouth().getHoleList().get(4).getSeeds(), is(6));
        assertThat(board.getPlayerSouth().getHoleList().get(5).getSeeds(), is(6));
        assertThat(board.getPlayerSouth().getEndZone().getSeeds(), is(0));


    }


    @Test
    public void playerNorthWinsWhenLastSeedOnNorthSixHole() {
        GameBoard gameBoard = GameBoard.builder().activePlayer(PlayerEnum.NORTH).id(UUID.randomUUID().toString())
                .playerNorth(playerNorth).playerSouth(playerSouth).activePlayer(PlayerEnum.NORTH).build();
        when(boardRepository.save(any())).thenReturn(gameBoard);

        gameBoard.getPlayerNorth().getHoleList().forEach(each -> each.setSeeds(0));
        gameBoard.getPlayerSouth().getHoleList().forEach(each -> each.setSeeds(0));

        gameBoard.getPlayerNorth().getHoleList().get(HoleEnum.SIX.getIndex()).setSeeds(1);
        gameBoard.getPlayerSouth().getHoleList().get(HoleEnum.SIX.getIndex()).setSeeds(1);

        gameBoard.getPlayerNorth().getEndZone().setSeeds(15);
        gameBoard.getPlayerSouth().getEndZone().setSeeds(3);

        when(boardRepository.find(gameBoard.getId())).thenReturn(gameBoard);

        GameBoard board = boardService.move(gameBoard.getId(), HoleEnum.SIX);

        assertTrue(board.isHasWinner());
        assertEquals(board.getActivePlayer(), PlayerEnum.NORTH);

    }

    @Test
    public void playerSouthWinsWhenLastSeedOnSouthSixHole() {
        GameBoard gameBoard = GameBoard.builder().activePlayer(PlayerEnum.NORTH).id(UUID.randomUUID().toString())
                .playerNorth(playerNorth).playerSouth(playerSouth).activePlayer(PlayerEnum.NORTH).build();
        when(boardRepository.save(any())).thenReturn(gameBoard);

        gameBoard.getPlayerNorth().getHoleList().forEach(each -> each.setSeeds(0));
        gameBoard.getPlayerSouth().getHoleList().forEach(each -> each.setSeeds(0));

        gameBoard.getPlayerNorth().getHoleList().get(HoleEnum.SIX.getIndex()).setSeeds(2);
        gameBoard.getPlayerSouth().getHoleList().get(HoleEnum.SIX.getIndex()).setSeeds(1);

        gameBoard.getPlayerNorth().getEndZone().setSeeds(3);
        gameBoard.getPlayerSouth().getEndZone().setSeeds(12);
        gameBoard.setActivePlayer(PlayerEnum.SOUTH);

        when(boardRepository.find(gameBoard.getId())).thenReturn(gameBoard);

        GameBoard board = boardService.move(gameBoard.getId(), HoleEnum.SIX);

        assertTrue(board.isHasWinner());
        assertEquals(board.getActivePlayer(), PlayerEnum.SOUTH);

    }

    @Test
    public void findGameBoardByIdAsExpected() {
        GameBoard gameBoard = GameBoard.builder().activePlayer(PlayerEnum.NORTH).id(UUID.randomUUID().toString())
                .playerNorth(playerNorth).playerSouth(playerSouth).activePlayer(PlayerEnum.NORTH).build();

        when(boardRepository.find(any())).thenReturn(gameBoard);
        GameBoard selectedGameBoard = boardService.find(gameBoard.getId());
        assertEquals(gameBoard,selectedGameBoard);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void throwResourceNotFoundExceptionWithWrongBoardId() {
        GameBoard gameBoard = GameBoard.builder().activePlayer(PlayerEnum.NORTH).id(UUID.randomUUID().toString())
                .playerNorth(playerNorth).playerSouth(playerSouth).activePlayer(PlayerEnum.NORTH).build();

        when(boardRepository.find(any())).thenReturn(null);
        GameBoard selectedGameBoard = boardService.find(gameBoard.getId());
        assertEquals(gameBoard,selectedGameBoard);

    }

    @Test
    public void saveGameBoardAndFetchAllProperly() {
        List<GameBoard> boards = new ArrayList<>() ;
        when(boardRepository.findAll()).thenReturn(boards);

        List<GameBoard> selectedGameBoard = boardService.findAll();
        assertNotNull(selectedGameBoard);
        assertTrue(selectedGameBoard.containsAll(boards));

    }

    private List<Hole> getHoles() {
        List<Hole> holes = new ArrayList<>();

        for (HoleEnum holeEnum : HoleEnum.values()) {
            holes.add(Hole.builder().id(holeEnum).seeds(6).build());
        }
        return holes;
    }
}