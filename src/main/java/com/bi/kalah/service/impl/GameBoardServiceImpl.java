package com.bi.kalah.service.impl;

import com.bi.kalah.exception.ResourceNotFoundException;
import com.bi.kalah.helper.GameBoardHelper;
import com.bi.kalah.model.domain.EndZone;
import com.bi.kalah.model.domain.GameBoard;
import com.bi.kalah.model.domain.Hole;
import com.bi.kalah.model.domain.Player;
import com.bi.kalah.model.enumeration.HoleEnum;
import com.bi.kalah.model.enumeration.PlayerEnum;
import com.bi.kalah.repository.GameBoardRepository;
import com.bi.kalah.service.GameBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Service
@Slf4j
public class GameBoardServiceImpl implements GameBoardService {

    private static final int SEED_INCREMENT_INDEX = 1;
    private static final int EMPTY_HOLE_SEED_COUNT = 0;
    private static final int MAX_HOLE_COUNT = 6;

    private GameBoardRepository boardRepository;

    private GameBoardHelper helper;

    @Autowired
    public GameBoardServiceImpl(final GameBoardRepository boardRepository, final GameBoardHelper helper) {
        this.boardRepository = boardRepository;
        this.helper = helper;
    }

    @Override
    public GameBoard create() {
        EndZone endZone = EndZone.builder().seeds(0).build();

        final Player playerNorth = Player.builder().id(PlayerEnum.NORTH).endZone(endZone).holeList(this.helper.generateDefaultPitList()).build();
        final Player playerSouth = Player.builder().id(PlayerEnum.SOUTH).endZone(endZone).holeList(this.helper.generateDefaultPitList()).build();

        GameBoard board = GameBoard.builder().activePlayer(PlayerEnum.NORTH).id(UUID.randomUUID().toString())
                .playerNorth(playerNorth).playerSouth(playerSouth).build();

        return this.boardRepository.save(board);
    }

    @Override
    public GameBoard move(String id, HoleEnum holeId) {
        final GameBoard gameBoard = getBoardById(id);

        final Player activePlayer = this.helper.getActivePlayer(gameBoard);
        this.helper.gameBoardHasWinner(gameBoard);

        final Hole activeHole = activePlayer.getHoleList().get(holeId.getIndex());
        this.helper.holeHasSeeds(activeHole);

        this.moveSeedRight(gameBoard, activeHole, activePlayer);
        this.anyWinnerCheck(gameBoard);

        return gameBoard;
    }

    @Override
    public GameBoard find(String boardId) {
        if (boardRepository.find(boardId) == null) {
            throw new ResourceNotFoundException("Game Board does not found with" + boardId);
        }
        return this.boardRepository.find(boardId);
    }

    @Override
    public List<GameBoard> findAll() {
        return boardRepository.findAll();
    }

    private void moveSeedRight(final GameBoard gameBoard, final Hole activeHole, final Player activePlayer) {
        int[] seeds = {activeHole.getSeeds()};
        activeHole.setSeeds(EMPTY_HOLE_SEED_COUNT);

        this.helper.switchActiveUser(gameBoard);
        deliverSeedsToHoles(activeHole, activePlayer, seeds, gameBoard);

        this.addSeedEndZone(activePlayer, seeds, gameBoard);
        if (--seeds[0] > EMPTY_HOLE_SEED_COUNT) {
            this.addSeedOpponentSide(gameBoard, seeds);
        }


    }

    private void anyWinnerCheck(GameBoard gameBoard) {
        Player playerNorth = gameBoard.getPlayerNorth();
        Player playerSouth = gameBoard.getPlayerSouth();

        int totalSeedPlayerNorth = playerNorth.getHoleList().stream().mapToInt(Hole::getSeeds).sum();
        int totalSeedPlayerSouth = playerSouth.getHoleList().stream().mapToInt(Hole::getSeeds).sum();

        if (totalSeedPlayerSouth == EMPTY_HOLE_SEED_COUNT || totalSeedPlayerNorth == EMPTY_HOLE_SEED_COUNT) {
            this.addTotalSeedsToEndZone(gameBoard, totalSeedPlayerNorth, totalSeedPlayerSouth);

            if (playerNorth.getEndZone().getSeeds() > playerSouth.getEndZone().getSeeds()) {
                gameBoard.setActivePlayer(playerNorth.getId());
            } else {
                gameBoard.setActivePlayer(playerSouth.getId());
            }
            gameBoard.setHasWinner(Boolean.TRUE);

            log.info("Winner - " + gameBoard.getActivePlayer().toString());
        }


    }

    private void addTotalSeedsToEndZone(GameBoard gameBoard, int totalSeedPlayerNorth, int totalSeedPlayerSouth) {
        int[] totalEndZoneCount = new int[1];
        totalEndZoneCount[0] = totalSeedPlayerNorth;
        this.addSeedEndZoneByRule(gameBoard.getPlayerNorth(), totalEndZoneCount);

        totalEndZoneCount[0] = totalSeedPlayerSouth;
        this.addSeedEndZoneByRule(gameBoard.getPlayerSouth(), totalEndZoneCount);
    }

    private void addSeedOpponentSide(GameBoard gameBoard, int[] seeds) {
        Player opponentPlayer = helper.getActivePlayer(gameBoard);

        for (int index = 0; index < seeds[0]; index++) {
            Hole hole = opponentPlayer.getHoleList().get(index);
            hole.setSeeds(hole.getSeeds() + SEED_INCREMENT_INDEX);

        }
    }
    
    private void deliverSeedsToHoles(Hole activeHold, Player activePlayer, int[] seeds, GameBoard gameBoard) {
        IntStream.range(activeHold.getId().getIndex() + SEED_INCREMENT_INDEX, MAX_HOLE_COUNT).
                filter(index -> seeds[0] > EMPTY_HOLE_SEED_COUNT).forEach(index -> {
                    Hole hole = activePlayer.getHoleList().get(index);
                    hole.setSeeds(hole.getSeeds() + SEED_INCREMENT_INDEX);
                    seeds[0]--;

                    if (seeds[0] == EMPTY_HOLE_SEED_COUNT && hole.getSeeds() == SEED_INCREMENT_INDEX) {
                        this.isLastSeedOnEmptyHole(activePlayer, hole, gameBoard);
                    }
                }

        );
    }

    private void isLastSeedOnEmptyHole(Player activePlayer, Hole hole, GameBoard gameBoard) {
        int opponentIndex = (MAX_HOLE_COUNT - hole.getId().getIndex()) - SEED_INCREMENT_INDEX;
        final int[] opponentSeedCount = new int[1];
        int holeOpponentCount;

        holeOpponentCount = this.getHoleOpponentCount(activePlayer, gameBoard, opponentIndex);
        activePlayer.getHoleList().get(hole.getId().getIndex()).setSeeds(EMPTY_HOLE_SEED_COUNT);

        opponentSeedCount[0] = holeOpponentCount;
        this.addSeedEndZoneByRule(activePlayer, opponentSeedCount);

    }

    private int getHoleOpponentCount(Player activePlayer, GameBoard gameBoard, int opponentIndex) {
        int holeOpponentCount;
        if (activePlayer.getId() == PlayerEnum.SOUTH) {
            holeOpponentCount = gameBoard.getPlayerNorth().getHoleList().get(opponentIndex).getSeeds();
            gameBoard.getPlayerNorth().getHoleList().get(opponentIndex).setSeeds(EMPTY_HOLE_SEED_COUNT);

        } else {
            holeOpponentCount = gameBoard.getPlayerSouth().getHoleList().get(opponentIndex).getSeeds();
            gameBoard.getPlayerSouth().getHoleList().get(opponentIndex).setSeeds(EMPTY_HOLE_SEED_COUNT);
        }
        return holeOpponentCount;
    }

    private void addSeedEndZoneByRule(Player activePlayer, int[] opponentSeedCount) {
        EndZone endZone = EndZone.builder().seeds(activePlayer.getEndZone().getSeeds() + opponentSeedCount[0]).build();
        activePlayer.setEndZone(endZone);
    }

    private void addSeedEndZone(Player activePlayer, int[] seeds, GameBoard gameBoard) {
        EndZone endZone = EndZone.builder().seeds(activePlayer.getEndZone().getSeeds() + SEED_INCREMENT_INDEX).build();
        activePlayer.setEndZone(endZone);
        //if you drop last seed to endzone active player plays one more
        if (seeds[0] == SEED_INCREMENT_INDEX)
            helper.switchActiveUser(gameBoard);
    }


    private GameBoard getBoardById(String id) {
        if (id == null || boardRepository.find(id) == null) {
            throw new ResourceNotFoundException(" Resource not Found with " + id);
        }
        return boardRepository.find(id);
    }
}
