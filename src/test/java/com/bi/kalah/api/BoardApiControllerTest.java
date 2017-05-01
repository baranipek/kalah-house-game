package com.bi.kalah.api;

import com.bi.kalah.model.GameBoard;
import com.bi.kalah.model.enumeration.HoleEnum;
import com.bi.kalah.model.enumeration.PlayerEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.LOG_DEBUG)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BoardApiControllerTest {

    @Autowired
    private MockMvc mockModelView;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void createNewBoardProperly() throws Exception {
        mockModelView.perform(post("/api/gameboard")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.activePlayer").value(PlayerEnum.NORTH.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[0].id").value(HoleEnum.ONE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[0].seeds").value(6))
                .andExpect(jsonPath("$.playerNorth.holeList[1].id").value(HoleEnum.TWO.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[1].seeds").value(6))
                .andExpect(jsonPath("$.playerNorth.holeList[2].id").value(HoleEnum.THREE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[2].seeds").value(6))
                .andExpect(jsonPath("$.playerNorth.holeList[3].id").value(HoleEnum.FOUR.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[3].seeds").value(6))
                .andExpect(jsonPath("$.playerNorth.holeList[4].id").value(HoleEnum.FIVE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[4].seeds").value(6))
                .andExpect(jsonPath("$.playerNorth.holeList[5].id").value(HoleEnum.SIX.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[5].seeds").value(6))
                //south
                .andExpect(jsonPath("$.playerSouth.holeList[0].id").value(HoleEnum.ONE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[0].seeds").value(6))
                .andExpect(jsonPath("$.playerSouth.holeList[1].id").value(HoleEnum.TWO.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[1].seeds").value(6))
                .andExpect(jsonPath("$.playerSouth.holeList[2].id").value(HoleEnum.THREE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[2].seeds").value(6))
                .andExpect(jsonPath("$.playerSouth.holeList[3].id").value(HoleEnum.FOUR.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[3].seeds").value(6))
                .andExpect(jsonPath("$.playerSouth.holeList[4].id").value(HoleEnum.FIVE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[4].seeds").value(6))
                .andExpect(jsonPath("$.playerSouth.holeList[5].id").value(HoleEnum.SIX.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[5].seeds").value(6))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void playerStartsWithNorthAndMoveOneSeedRight() throws Exception {
        String board = mockModelView.perform(post("/api/gameboard").contentType(APPLICATION_JSON_UTF8)).
                andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getContentAsString();
        GameBoard boardJson = mapper.readValue(board, GameBoard.class);

        mockModelView.perform(put("/api/gameboard/" + boardJson.getId() + "/hole/ONE")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.activePlayer").value(PlayerEnum.NORTH.toString()))
                .andExpect(jsonPath("$.playerNorth.endZone.seeds").value(1))
                .andExpect(jsonPath("$.playerNorth.holeList[0].id").value(HoleEnum.ONE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[0].seeds").value(0))
                .andExpect(jsonPath("$.playerNorth.holeList[1].id").value(HoleEnum.TWO.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[1].seeds").value(7))
                .andExpect(jsonPath("$.playerNorth.holeList[2].id").value(HoleEnum.THREE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[2].seeds").value(7))
                .andExpect(jsonPath("$.playerNorth.holeList[3].id").value(HoleEnum.FOUR.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[3].seeds").value(7))
                .andExpect(jsonPath("$.playerNorth.holeList[4].id").value(HoleEnum.FIVE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[4].seeds").value(7))

                //south player

                .andExpect(jsonPath("$.playerSouth.endZone.seeds").value(0))
                .andExpect(jsonPath("$.playerSouth.holeList[0].id").value(HoleEnum.ONE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[0].seeds").value(6))
                .andExpect(jsonPath("$.playerSouth.holeList[1].id").value(HoleEnum.TWO.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[1].seeds").value(6))
                .andExpect(jsonPath("$.playerSouth.holeList[2].id").value(HoleEnum.THREE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[2].seeds").value(6))
                .andExpect(jsonPath("$.playerSouth.holeList[3].id").value(HoleEnum.FOUR.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[3].seeds").value(6))
                .andExpect(jsonPath("$.playerSouth.holeList[4].id").value(HoleEnum.FIVE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[4].seeds").value(6))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void playerStartsWithNorthAndMoveFirstSeedRightAndThenMoveSecondRight() throws Exception {
        String board = mockModelView.perform(post("/api/gameboard").contentType(APPLICATION_JSON_UTF8)).
                andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getContentAsString();
        GameBoard boardJson = mapper.readValue(board, GameBoard.class);

        mockModelView.perform(put("/api/gameboard/" + boardJson.getId() + "/hole/ONE").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk());
        mockModelView.perform(put("/api/gameboard/" + boardJson.getId() + "/hole/TWO").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.activePlayer").value(PlayerEnum.SOUTH.toString()))
                .andExpect(jsonPath("$.playerNorth.endZone.seeds").value(2))
                .andExpect(jsonPath("$.playerNorth.holeList[0].id").value(HoleEnum.ONE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[0].seeds").value(0))
                .andExpect(jsonPath("$.playerNorth.holeList[1].id").value(HoleEnum.TWO.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[1].seeds").value(0))
                .andExpect(jsonPath("$.playerNorth.holeList[2].id").value(HoleEnum.THREE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[2].seeds").value(8))
                .andExpect(jsonPath("$.playerNorth.holeList[3].id").value(HoleEnum.FOUR.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[3].seeds").value(8))
                .andExpect(jsonPath("$.playerNorth.holeList[4].id").value(HoleEnum.FIVE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[4].seeds").value(8))

                //south player

                .andExpect(jsonPath("$.playerSouth.endZone.seeds").value(0))
                .andExpect(jsonPath("$.playerSouth.holeList[0].id").value(HoleEnum.ONE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[0].seeds").value(7))
                .andExpect(jsonPath("$.playerSouth.holeList[1].id").value(HoleEnum.TWO.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[1].seeds").value(7))
                .andExpect(jsonPath("$.playerSouth.holeList[2].id").value(HoleEnum.THREE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[2].seeds").value(6))
                .andExpect(jsonPath("$.playerSouth.holeList[3].id").value(HoleEnum.FOUR.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[3].seeds").value(6))
                .andExpect(jsonPath("$.playerSouth.holeList[4].id").value(HoleEnum.FIVE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[4].seeds").value(6))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void playerStartsWithNorthMoveFirstAndThenSecondAndSouthMovesOne() throws Exception {
        String board = mockModelView.perform(post("/api/gameboard").contentType(APPLICATION_JSON_UTF8)).
                andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getContentAsString();
        GameBoard boardJson = mapper.readValue(board, GameBoard.class);

        mockModelView.perform(put("/api/gameboard/" + boardJson.getId() + "/hole/ONE").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk());
        mockModelView.perform(put("/api/gameboard/" + boardJson.getId() + "/hole/TWO").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk());
        mockModelView.perform(put("/api/gameboard/" + boardJson.getId() + "/hole/ONE").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.activePlayer").value(PlayerEnum.NORTH.toString()))
                .andExpect(jsonPath("$.playerNorth.endZone.seeds").value(2))
                .andExpect(jsonPath("$.playerNorth.holeList[0].id").value(HoleEnum.ONE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[0].seeds").value(1))
                .andExpect(jsonPath("$.playerNorth.holeList[1].id").value(HoleEnum.TWO.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[1].seeds").value(0))
                .andExpect(jsonPath("$.playerNorth.holeList[2].id").value(HoleEnum.THREE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[2].seeds").value(8))
                .andExpect(jsonPath("$.playerNorth.holeList[3].id").value(HoleEnum.FOUR.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[3].seeds").value(8))
                .andExpect(jsonPath("$.playerNorth.holeList[4].id").value(HoleEnum.FIVE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[4].seeds").value(8))
                .andExpect(jsonPath("$.playerNorth.holeList[5].id").value(HoleEnum.SIX.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[5].seeds").value(8))

                //south player

                .andExpect(jsonPath("$.playerSouth.endZone.seeds").value(1))
                .andExpect(jsonPath("$.playerSouth.holeList[0].id").value(HoleEnum.ONE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[0].seeds").value(0))
                .andExpect(jsonPath("$.playerSouth.holeList[1].id").value(HoleEnum.TWO.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[1].seeds").value(8))
                .andExpect(jsonPath("$.playerSouth.holeList[2].id").value(HoleEnum.THREE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[2].seeds").value(7))
                .andExpect(jsonPath("$.playerSouth.holeList[3].id").value(HoleEnum.FOUR.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[3].seeds").value(7))
                .andExpect(jsonPath("$.playerSouth.holeList[4].id").value(HoleEnum.FIVE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[4].seeds").value(7))
                .andExpect(jsonPath("$.playerSouth.holeList[5].id").value(HoleEnum.SIX.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[5].seeds").value(7))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void playerNorthFirstAndSecondAndSouthFirstAndNorthFirstAndEndZoneRuleIsOccured() throws Exception {
        String board = mockModelView.perform(post("/api/gameboard").contentType(APPLICATION_JSON_UTF8)).
                andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getContentAsString();
        GameBoard boardJson = mapper.readValue(board, GameBoard.class);

        mockModelView.perform(put("/api/gameboard/" + boardJson.getId() + "/hole/ONE").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk());
        mockModelView.perform(put("/api/gameboard/" + boardJson.getId() + "/hole/TWO").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk());
        mockModelView.perform(put("/api/gameboard/" + boardJson.getId() + "/hole/ONE").contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk());
        mockModelView.perform(put("/api/gameboard/" + boardJson.getId() + "/hole/ONE").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.activePlayer").value(PlayerEnum.SOUTH.toString()))
                .andExpect(jsonPath("$.playerNorth.endZone.seeds").value(10))
                .andExpect(jsonPath("$.playerNorth.holeList[0].id").value(HoleEnum.ONE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[0].seeds").value(0))
                .andExpect(jsonPath("$.playerNorth.holeList[1].id").value(HoleEnum.TWO.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[1].seeds").value(0))
                .andExpect(jsonPath("$.playerNorth.holeList[2].id").value(HoleEnum.THREE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[2].seeds").value(8))
                .andExpect(jsonPath("$.playerNorth.holeList[3].id").value(HoleEnum.FOUR.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[3].seeds").value(8))
                .andExpect(jsonPath("$.playerNorth.holeList[4].id").value(HoleEnum.FIVE.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[4].seeds").value(8))
                .andExpect(jsonPath("$.playerNorth.holeList[5].id").value(HoleEnum.SIX.toString()))
                .andExpect(jsonPath("$.playerNorth.holeList[5].seeds").value(8))

                //south player

                .andExpect(jsonPath("$.playerSouth.endZone.seeds").value(1))
                .andExpect(jsonPath("$.playerSouth.holeList[0].id").value(HoleEnum.ONE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[0].seeds").value(0))
                .andExpect(jsonPath("$.playerSouth.holeList[1].id").value(HoleEnum.TWO.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[1].seeds").value(8))
                .andExpect(jsonPath("$.playerSouth.holeList[2].id").value(HoleEnum.THREE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[2].seeds").value(7))
                .andExpect(jsonPath("$.playerSouth.holeList[3].id").value(HoleEnum.FOUR.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[3].seeds").value(7))
                .andExpect(jsonPath("$.playerSouth.holeList[4].id").value(HoleEnum.FIVE.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[4].seeds").value(0))
                .andExpect(jsonPath("$.playerSouth.holeList[5].id").value(HoleEnum.SIX.toString()))
                .andExpect(jsonPath("$.playerSouth.holeList[5].seeds").value(7))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findGameBoardAsExpected() throws Exception {
        String board = mockModelView.perform(post("/api/gameboard").contentType(APPLICATION_JSON_UTF8)).
                andDo(MockMvcResultHandlers.print()).andReturn().getResponse().getContentAsString();
        GameBoard boardJson = mapper.readValue(board, GameBoard.class);

        mockModelView.perform(get("/api/gameboard/"+boardJson.getId()).contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

}

