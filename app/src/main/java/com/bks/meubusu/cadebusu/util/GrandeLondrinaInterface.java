package com.bks.meubusu.cadebusu.util;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by raullermen on 2/27/16.
 */
public interface GrandeLondrinaInterface {

    //http://site.tcgrandelondrina.com.br:8082/Soap/BuscarLinhas

    @POST("/Soap/BuscarLinhas")
    @FormUrlEncoded
    Call<ResponseBody> getListaLinhas(@Field("buscarlinha")String  tipoLinhas);

    @POST("/Soap/buscamapa")
    @FormUrlEncoded
    Call<ResponseBody> getLocalizacaoOnibus(@Field("idLinha")String  codLinha);

    @POST("/Soap/BuscaItinerarios")
    @FormUrlEncoded
    Call<ResponseBody> getIninerario(@Field("linha")String  codLinha);

}
