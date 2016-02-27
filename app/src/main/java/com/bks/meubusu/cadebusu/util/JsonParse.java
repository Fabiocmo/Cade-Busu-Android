package com.bks.meubusu.cadebusu.util;

import com.bks.meubusu.cadebusu.model.ItinerarioDTO;
import com.bks.meubusu.cadebusu.model.LinhaDTO;
import com.bks.meubusu.cadebusu.model.OnibusDTO;
import com.bks.meubusu.cadebusu.model.PontoDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raullermen on 2/27/16.
 */
public class JsonParse {

    public List<LinhaDTO> ConverteListaLinhas(String response) throws JSONException {

        List<LinhaDTO> listaLinhasTemp = new ArrayList<>();
        List<LinhaDTO> listaLinhasFinal = new ArrayList<>();

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
            String nomeLinha = linhas.get(i).toString().substring(6, linhas.get(i).toString().length());

            LinhaDTO linha = new LinhaDTO();
            linha.setlCodigo(listaLinhasTemp.get(i).getlCodigo());
            linha.setlNomeLinha(nomeLinha);
            linha.setlEmpresa("GRANDE LONDRINA");
            linha.setlFavorito(false);
            listaLinhasFinal.add(linha);
        }

        return listaLinhasFinal;
    }

    public List<OnibusDTO> ConverteLocalizacaoOnibus(String response, String CodigoLinha, String NomeLinha) throws JSONException {
        JSONObject obj = new JSONObject(response);
        String coordenadas = obj.getString("cordenadas");

        List<OnibusDTO> listaPosicoesOnibus;

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
        return listaPosicoesOnibus;
    }

    public List<ItinerarioDTO> ConverteItinerario(String response) throws JSONException{
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
        List<ItinerarioDTO> listaItinerarios = new ArrayList<>();
        listaItinerarios.add(itinerario);

        return listaItinerarios;
    }
}
