package com.bks.meubusu.cadebusu.model;

import java.util.ArrayList;

/**
 * Created by raullermen on 10/6/15.
 */
public class ItinerarioDTO {

    public ArrayList<PontoDTO> Ida;
    public ArrayList<PontoDTO> Volta;

    public ItinerarioDTO(ArrayList<PontoDTO> ida, ArrayList<PontoDTO> volta) {
        Ida = ida;
        Volta = volta;
    }

    public ArrayList<PontoDTO> getIda() {
        return Ida;
    }

    public void setIda(ArrayList<PontoDTO> ida) {
        Ida = ida;
    }

    public ArrayList<PontoDTO> getVolta() {
        return Volta;
    }

    public void setVolta(ArrayList<PontoDTO> volta) {
        Volta = volta;
    }
}
