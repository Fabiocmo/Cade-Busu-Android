package com.bks.meubusu.cadebusu.model;

/**
 * Created by raullermen on 10/3/15.
 */
public class OnibusDTO {

    private String oCodigoLinha;
    private String oNomeLinha;
    private String oSentido;
    private double oLatitude;
    private double oLongitude;

    public OnibusDTO(String oCodigoLinha, String oNomeLinha,double oLatitude, double oLongitude, String oSentido) {
        this.oCodigoLinha = oCodigoLinha;
        this.oNomeLinha = oNomeLinha;
        this.oLatitude = oLatitude;
        this.oLongitude = oLongitude;
        this.oSentido = oSentido;
    }

    public String getoCodigoLinha() {
        return oCodigoLinha;
    }

    public void setoCodigoLinha(String oCodigoLinha) {
        this.oCodigoLinha = oCodigoLinha;
    }

    public double getoLatitude() {
        return oLatitude;
    }

    public void setoLatitude(double oLatitude) {
        this.oLatitude = oLatitude;
    }

    public double getoLongitude() {
        return oLongitude;
    }

    public void setoLongitude(double oLongitude) {
        this.oLongitude = oLongitude;
    }

    public String getoSentido() {
        return oSentido;
    }

    public String getoNomeLinha() {
        return oNomeLinha;
    }

    public void setoNomeLinha(String oNomeLinha) {
        this.oNomeLinha = oNomeLinha;
    }

    public void setoSentido(String oSentido) {
        this.oSentido = oSentido;
    }

    public String getTituloLinha(){
        return getoCodigoLinha() + " - " + getoNomeLinha();
    }
}
