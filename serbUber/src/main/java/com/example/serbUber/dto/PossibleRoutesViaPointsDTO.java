package com.example.serbUber.dto;

import java.util.List;

public class PossibleRoutesViaPointsDTO {

    List<PossibleRouteDTO> possibleRouteDTOList;

    public PossibleRoutesViaPointsDTO() {
    }

    public PossibleRoutesViaPointsDTO(List<PossibleRouteDTO> possibleRouteDTOList) {
        this.possibleRouteDTOList = possibleRouteDTOList;
    }

    public List<PossibleRouteDTO> getPossibleRouteDTOList() {
        return possibleRouteDTOList;
    }

    public void setPossibleRouteDTOList(List<PossibleRouteDTO> possibleRouteDTOList) {
        this.possibleRouteDTOList = possibleRouteDTOList;
    }
}
