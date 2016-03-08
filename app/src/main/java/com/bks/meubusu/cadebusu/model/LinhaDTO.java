package com.bks.meubusu.cadebusu.model;

import com.orm.SugarRecord;

/**
 * Created by raullermen on 9/13/15.
 */
public class LinhaDTO extends SugarRecord {

    private String lCodigo;
    private String lNomeLinha;
    private String lEmpresa;
    private Boolean lFavorito;

    public LinhaDTO(){
    }

    public LinhaDTO(String lCodigo, String lNomeLinha, String lEmpresa, Boolean lFavorito) {
        this.lCodigo = lCodigo;
        this.lNomeLinha = lNomeLinha;
        this.lFavorito = lFavorito;
        this.lEmpresa = lEmpresa;

    }

    //GETS AND SETS
    public Boolean getlFavorito() {
        return lFavorito;
    }

    public void setlFavorito(Boolean lFavorito) {
        this.lFavorito = lFavorito;
    }

    public String getCodigoEnome() {
        return this.getlCodigo() + this.getlNomeLinha();
    }


    public String getlNomeLinha() {
        return lNomeLinha;
    }

    public void setlNomeLinha(String lNomeLinha) {
        this.lNomeLinha = lNomeLinha;
    }

    public String getlCodigo() {
        return lCodigo;
    }

    public void setlCodigo(String lCodigo) {
        this.lCodigo = lCodigo;
    }

    public String getlEmpresa() {
        return lEmpresa;
    }

    public void setlEmpresa(String lEmpresa) {
        this.lEmpresa = lEmpresa;
    }

}
