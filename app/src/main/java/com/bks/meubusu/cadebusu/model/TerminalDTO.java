package com.bks.meubusu.cadebusu.model;

/**
 * Created by raullermen on 10/17/15.
 */
public class TerminalDTO {
    private String tNome;
    private double tLatitude;
    private double tLongitude;

    public TerminalDTO(String tNome, double tLatitude, double tLongitude) {
        this.tNome = tNome;
        this.tLatitude = tLatitude;
        this.tLongitude = tLongitude;
    }

    public String gettNome() {
        return tNome;
    }

    public void settNome(String tNome) {
        this.tNome = tNome;
    }

    public double gettLatitude() {
        return tLatitude;
    }

    public void settLatitude(double tLatitude) {
        this.tLatitude = tLatitude;
    }

    public double gettLongitude() {
        return tLongitude;
    }

    public void settLongitude(double tLongitude) {
        this.tLongitude = tLongitude;
    }
}
