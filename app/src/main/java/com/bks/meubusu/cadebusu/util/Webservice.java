package com.bks.meubusu.cadebusu.util;

import android.content.Context;
import android.util.Log;

import com.bks.meubusu.cadebusu.model.ItinerarioDTO;
import com.bks.meubusu.cadebusu.model.LinhaDTO;
import com.bks.meubusu.cadebusu.model.OnibusDTO;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by raullermen on 9/20/15.
 */
public class Webservice {

    JsonParse parse = new JsonParse();
    Retrofit retrofit;

    public static final String BASE_URL = "http://site.tcgrandelondrina.com.br:8082/";

    public List<LinhaDTO> listaLinhas;
    public List<OnibusDTO> listaPosicoesOnibus;
    public List<ItinerarioDTO> listaItinerarios;

    public Webservice(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public void getListaLinhas(final TransactionAction completion) throws JSONException {

        GrandeLondrinaInterface apiService = retrofit.create(GrandeLondrinaInterface.class);
        Call<ResponseBody> result = apiService.getListaLinhas("Linhas Convencionais");

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    listaLinhas = parse.ConverteListaLinhas(response.body().string().toString());
                    completion.perform();
                } catch (IOException e) {
                    Log.v("ERRO", "Exception caught : ", e);
                    completion.perform();
                }catch (JSONException e)
                {
                    Log.v("ERRO", "Exception caught : ", e);
                    completion.perform();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                completion.perform();
            }
        });

    }

    public void getLocalizacaoOnibus(final String CodigoLinha, final String NomeLinha, final TransactionAction completion ) throws JSONException {

        GrandeLondrinaInterface apiService = retrofit.create(GrandeLondrinaInterface.class);
        Call<ResponseBody> result = apiService.getLocalizacaoOnibus(CodigoLinha);

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    listaPosicoesOnibus = parse.ConverteLocalizacaoOnibus(response.body().string().toString(), CodigoLinha, NomeLinha);
                    completion.perform();
                } catch (IOException e) {
                    Log.v("ERRO", "Exception caught : ", e);
                    completion.perform();
                }catch (JSONException e)
                {
                    Log.v("ERRO", "Exception caught : ", e);
                    completion.perform();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                completion.perform();
            }
        });
    }

    public void getIninerario(Context c, final String codigoLinha, final TransactionAction completion) throws JSONException {
        GrandeLondrinaInterface apiService = retrofit.create(GrandeLondrinaInterface.class);
        Call<ResponseBody> result = apiService.getIninerario(codigoLinha);

        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    listaItinerarios = parse.ConverteItinerario(response.body().string().toString());
                    completion.perform();
                } catch (IOException e) {
                    Log.v("ERRO", "Exception caught : ", e);
                    completion.perform();
                }catch (JSONException e)
                {
                    Log.v("ERRO", "Exception caught : ", e);
                    completion.perform();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                completion.perform();
            }
        });
    }

}
