package com.bks.meubusu.cadebusu.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bks.meubusu.cadebusu.model.ItinerarioDTO;
import com.bks.meubusu.cadebusu.model.LinhaDTO;
import com.bks.meubusu.cadebusu.model.OnibusDTO;
import com.bks.meubusu.cadebusu.model.PontoDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

/**
 * Created by raullermen on 9/20/15.
 */
public class Webservice {

    public List<LinhaDTO> listaLinhas;
    public List<OnibusDTO> listaPosicoesOnibus;
    public List<ItinerarioDTO> listaItinerarios;

    public void getListaLinhas(Context c, final TransactionAction completion) throws JSONException {
        String url = "http://site.tcgrandelondrina.com.br:8082/Soap/BuscarLinhas";
        final List<LinhaDTO> listaLinhasTemp = new  ArrayList<>();
        listaLinhas = new ArrayList<>();

        RequestQueue rq = Volley.newRequestQueue(c);

        StringRequest sr = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);
                    JSONObject jObj = arr.getJSONObject(0);
                    JSONArray codigos = jObj.getJSONArray("cod");

                    // Parsing json - CODIGO DAS LINHAS
                    for (int i = 0; i < codigos.length(); i++) {
                        try {
                            LinhaDTO linha = new LinhaDTO();
                            linha.setlCodigo(codigos.get(i).toString());
                            listaLinhasTemp.add(linha);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // Parsing json - NOME DAS LINHAS
                    JSONArray linhas = jObj.getJSONArray("valor");

                    for (int i = 0; i < listaLinhasTemp.size(); i++) {
                        String nomeLinha = linhas.get(i).toString().substring(6,linhas.get(i).toString().length());

                        LinhaDTO linha = new LinhaDTO();
                        linha.setlCodigo(listaLinhasTemp.get(i).getlCodigo());
                        linha.setlNomeLinha(nomeLinha);
                        linha.setlEmpresa("GRANDE LONDRINA");
                        linha.setlFavorito(false);
                        listaLinhas.add(linha);
                    }
                    completion.perform();

                }catch (JSONException e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Log.d(TAG, ""+error.getMessage()+","+error.toString());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("buscarlinha", "Linhas Convencionais");

                return params;
            }

            //@Override
            //public Map<String, String> getHeaders() throws AuthFailureError {
            //    Map<String,String> headers = new HashMap<String, String>();
            //    headers.put("Content-Type","application/form-data");
            //    return headers;
            //}
        };

        rq.add(sr);
    }

    public void getLocalizacaoOnibus(Context c, final String CodigoLinha, final String NomeLinha, final TransactionAction completion ) throws JSONException {
        String url = "http://site.tcgrandelondrina.com.br:8082/soap/buscamapa";
        RequestQueue rq = Volley.newRequestQueue(c);

        StringRequest sr = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String coordenadas = obj.getString("cordenadas");

                    // Parsing json - COORDENADAS DOS ONIBUS
                    listaPosicoesOnibus = new ArrayList<>();
                    ArrayList<Float> arrayCoordenadas = new ArrayList<Float>();
                    String[] arraySplit = coordenadas.split("&");
                    ArrayList<String> listaSentidos = new ArrayList<>();

                    //Parse sentido da linha
                    arraySplit = coordenadas.split("\n");
                    for (int i = 0; i < arraySplit.length; i++) {
                        if (arraySplit[i].contains("-2")) {
                            listaSentidos.add(arraySplit[i].split("&")[0]);
                        }
                    }

                    //Extrai floats com as posicoes
                    arraySplit = coordenadas.split("&");
                    for (int i = 0; i < arraySplit.length; i++) {
                        try{
                            Float valor = Float.parseFloat(arraySplit[i]);
                            if (valor < -10) {
                                arrayCoordenadas.add(valor);
                            }
                        } catch(Exception e) {

                        }
                    }

                    //Cria Objetos e adiciona no retorno
                    listaPosicoesOnibus.clear();
                    if (arrayCoordenadas.size() > 1) {
                        for (int i = 0; i < arrayCoordenadas.size() / 2; i++) {
                            OnibusDTO onibus =
                                    new OnibusDTO(CodigoLinha,
                                            NomeLinha,
                                            arrayCoordenadas.get(i * 2),
                                            arrayCoordenadas.get((i * 2) + 1),
                                            listaSentidos.get(i).toString()
                                    );
                            listaPosicoesOnibus.add(onibus);
                        }
                    }

                    completion.perform();

                }catch (JSONException e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("idLinha", CodigoLinha);

                return params;
            }

        };

        rq.add(sr);
    }

    public void getIninerario(Context c, final String codigoLinha, final TransactionAction completion) throws JSONException {
        String url = "http://site.tcgrandelondrina.com.br:8082/Soap/BuscaItinerarios";

        RequestQueue rq = Volley.newRequestQueue(c);

        StringRequest sr = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    // Parsing json - ITINERARIO IDA
                    JSONArray pontosIda = obj.getJSONArray("ida");
                    ArrayList<PontoDTO> listaPontosIda = new ArrayList<>();
                    for (int i = 0; i < pontosIda.length(); i++) {
                        try {
                            PontoDTO ponto = new PontoDTO();
                            ponto.setNome(pontosIda.getJSONObject(i).getString("Nome"));
                            ponto.setSentido(Integer.parseInt(pontosIda.getJSONObject(i).getString("Sentido")));
                            ponto.setLatitude(Float.parseFloat(pontosIda.getJSONObject(i).getString("Lat")));
                            ponto.setLongitude(Float.parseFloat(pontosIda.getJSONObject(i).getString("Lng")));
                            listaPontosIda.add(ponto);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // Parsing json - ITINERARIO VOLTA
                    JSONArray pontosVolta = obj.getJSONArray("volta");
                    ArrayList<PontoDTO> listaPontosVolta = new ArrayList<>();
                    for (int i = 0; i < pontosVolta.length(); i++) {
                        try {
                            PontoDTO ponto = new PontoDTO();
                            ponto.setNome(pontosVolta.getJSONObject(i).getString("Nome"));
                            ponto.setSentido(Integer.parseInt(pontosVolta.getJSONObject(i).getString("Sentido")));
                            ponto.setLatitude(Float.parseFloat(pontosVolta.getJSONObject(i).getString("Lat")));
                            ponto.setLongitude(Float.parseFloat(pontosVolta.getJSONObject(i).getString("Lng")));
                            listaPontosVolta.add(ponto);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ItinerarioDTO itinerario = new ItinerarioDTO(listaPontosIda, listaPontosVolta);
                    listaItinerarios = new ArrayList<>();
                    listaItinerarios.add(itinerario);

                    completion.perform();

                }catch (JSONException e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Log.d(TAG, ""+error.getMessage()+","+error.toString());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("linha", codigoLinha);

                return params;
            }

        };

        rq.add(sr);
    }


}
