package com.bi.kalah.model.domain;

import com.bi.kalah.model.enumeration.HoleEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Hole {
    private HoleEnum id;
    private int seeds;

}
