package com.bks.meubusu.cadebusu.util;

import android.content.Context;
import com.bks.meubusu.cadebusu.model.ItinerarioDTO;
import com.bks.meubusu.cadebusu.model.LinhaDTO;
import com.bks.meubusu.cadebusu.model.OnibusDTO;
import com.bks.meubusu.cadebusu.model.PontoDTO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by raullermen on 9/20/15.
 */
public class Webservice {

    public List<LinhaDTO> listaLinhas;
    public List<OnibusDTO> listaPosicoesOnibus;
    public List<ItinerarioDTO> listaItinerarios;
    public boolean erro = false;

    public void getListaLinhas(Context c, final TransactionAction completion) {
        InputStream is = null;
        String result = "";

        // Download JSON data from URL
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://site.tcgrandelondrina.com.br:8082/Soap/BuscarLinhas");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("buscarlinha", "Linhas Convencionais"));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            completion.perform();
        }

        // Convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            completion.perform();
        }

        //PARSE OBJECT
        try {
            final List<LinhaDTO> listaLinhasTemp = new  ArrayList<>();
            listaLinhas = new ArrayList<>();

            JSONArray arr = new JSONArray(result);
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
        } catch (JSONException e) {
            completion.perform();
        }
    }

    public void getLocalizacaoOnibus(Context c, final String CodigoLinha, final String NomeLinha, final TransactionAction completion) {
        listaPosicoesOnibus = new ArrayList<>();
        InputStream is = null;
        String result = "";

        // Download JSON data from URL
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://site.tcgrandelondrina.com.br:8082/soap/buscamapa");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("idLinha", CodigoLinha));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            completion.perform();
        }

        // Convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            completion.perform();
        }

        //PARSE OBJECT
        try {
            JSONObject obj = new JSONObject(result);
            String coordenadas = obj.getString("cordenadas");

            // Parsing json - COORDENADAS DOS ONIBUS
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
        } catch (JSONException e) {
            completion.perform();
        }
    }

    public void getIninerario(Context c, final String CodigoLinha, final TransactionAction completion) {
        listaItinerarios = new ArrayList<>();
        InputStream is = null;
        String result = "";

        // Download JSON data from URL
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://site.tcgrandelondrina.com.br:8082/Soap/BuscaItinerarios");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("linha", CodigoLinha));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

        } catch (Exception e) {
            completion.perform();
        }

        // Convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            completion.perform();
        }

        //PARSE OBJECT
        try {
            JSONObject obj = new JSONObject(result);

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
            listaItinerarios.add(itinerario);

            completion.perform();
        } catch (JSONException e) {
            completion.perform();
        }
    }
}
