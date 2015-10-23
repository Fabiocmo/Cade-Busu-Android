package com.bks.meubusu.cadebusu.model;

/**
 * Created by raullermen on 10/6/15.
 */
public class PontoDTO {

    public String Nome;
    public Integer Sentido;
    public Float Latitude;
    public Float Longitude;

    public PontoDTO(Float latitude, Float longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }

    public PontoDTO() {

    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public Integer getSentido() {
        return Sentido;
    }

    public void setSentido(Integer sentido) {
        Sentido = sentido;
    }

    public Float getLatitude() {
        return Latitude;
    }

    public void setLatitude(Float latitude) {
        Latitude = latitude;
    }

    public Float getLongitude() {
        return Longitude;
    }

    public void setLongitude(Float longitude) {
        Longitude = longitude;
    }
}
