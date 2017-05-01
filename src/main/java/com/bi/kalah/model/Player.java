package com.bi.kalah.model;

import com.bi.kalah.model.enumeration.PlayerEnum;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Player {
    PlayerEnum id;
    List<Hole> holeList = new ArrayList<>();
    private EndZone endZone;
}
