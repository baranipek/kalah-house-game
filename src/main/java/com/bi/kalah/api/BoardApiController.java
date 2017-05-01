package com.bi.kalah.api;

import com.bi.kalah.model.GameBoard;
import com.bi.kalah.model.enumeration.HoleEnum;
import com.bi.kalah.service.GameBoardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/api/gameboard",consumes = MediaType.APPLICATION_JSON_VALUE)
public class BoardApiController {
    private GameBoardService boardService;

    @Autowired
    public BoardApiController(GameBoardService boardService) {this.boardService = boardService;}

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new BoardResponse", notes = "Create new BoardResponse")
    public ResponseEntity<GameBoard> create() {
        return new ResponseEntity<>(this.boardService.create(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{gameBoardId}",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get Board By Id", notes = "Get Board")
    public ResponseEntity<GameBoard> find(@PathVariable("gameBoardId") String boardId) {
        return new ResponseEntity<>(boardService.find(boardId), HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get All Boards", notes = "Get All Board")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 201, message = "") })
    public ResponseEntity<List<GameBoard>> findAll() {
        return new ResponseEntity<>(boardService.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{gameBoardId}/hole/{holeId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Move Seeds", notes = "Move Seeds ")
    public ResponseEntity<GameBoard> update(@PathVariable("gameBoardId") String id, @PathVariable("holeId") HoleEnum holeId) {
        return new ResponseEntity<>(boardService.move(id, holeId), HttpStatus.OK);
    }
}
