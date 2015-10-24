package com.bks.meubusu.cadebusu.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bks.meubusu.cadebusu.R;
import com.bks.meubusu.cadebusu.model.ItinerarioDTO;
import com.bks.meubusu.cadebusu.model.OnibusDTO;
import com.bks.meubusu.cadebusu.model.TerminalDTO;
import com.bks.meubusu.cadebusu.util.TransactionAction;
import com.bks.meubusu.cadebusu.util.Util;
import com.bks.meubusu.cadebusu.util.Webservice;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by raullermen on 9/17/15.
 */
public class MapaActivity extends AppCompatActivity {

    final Webservice ws = new Webservice();
    final Util util = new Util();

    public static int ContadorAtualizacao;
    public static FragmentManager fragmentManager;
    private static GoogleMap mMap;
    private static TextView statusBar;

    private List<OnibusDTO> listaPosicoesOnibus;
    private List<ItinerarioDTO> listaItinerarios;
    private List<Marker> listaMarkers;

    private boolean primeiroZoom;
    Timer timer;
    Bundle extras = new Bundle();

    private void AjustaLaout(){
        TextView textViewCodigoLinha = (TextView) findViewById(R.id.codigoLinha);
        TextView textViewTituloLinha = (TextView) findViewById(R.id.tituloLinha);

        //AJUSTA OS TEXTOS DA LINHA SELECIONADA
        extras = getIntent().getExtras();
        textViewCodigoLinha.setText(extras.getString("CODIGO_LINHA_SELECIONADA"));
        textViewTituloLinha.setText(extras.getString("TITULO_LINHA_SELECIONADA"));

        //SETA ACTION BAR
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar_custom_localizacao, null);

        actionBar.setCustomView(v);

        //AJUSTA BARRA INFERIOR
        statusBar = (TextView) findViewById(R.id.statusLinha);
        statusBar.setText("ATUALIZANDO...");
    }

    private void InicializaTimer(){
        if (timer == null){
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AtualizaBarraStatus();
                        }
                    });
                }
            }, 1000, 1000);
        }

    }

    private void AtualizaBarraStatus(){
        if (ContadorAtualizacao < 1){
            try {
                statusBar.setText("ATUALIZANDO...");
                ws.getLocalizacaoOnibus(this, extras.getString("CODIGO_LINHA_SELECIONADA"),extras.getString("TITULO_LINHA_SELECIONADA"),new TransactionAction() {
                    public void perform() {
                        if (ws.listaPosicoesOnibus.size() > 0){
                            listaPosicoesOnibus = ws.listaPosicoesOnibus;
                            ContadorAtualizacao = 15;
                            AtualizaPosicoesOnibus();
                        } else {
                            statusBar.setText("NENHUM ÔNIBUS ENCONTRADO");
                        }
                    }
                });
            } catch (Exception e){
                statusBar.setText("ERRO AO ATUALIZAR");
            }
        } else {
            ContadorAtualizacao--;
            statusBar.setText("PRÓXIMA ATUALIZAÇÃO EM " + Integer.toString(ContadorAtualizacao) + " SEGUNDOS...");
        }

    }

    private void InicializaMapa(){
        listaMarkers = new ArrayList<Marker>();

        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);

        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, fragment)
                    .commit();
        }

        mMap = fragment.getMap();
        mMap.setMyLocationEnabled(true);
    }

    private void AtualizaPosicoesOnibus(){
        //Remove markers do mapa
        for (Marker mark: listaMarkers){
            mark.remove();
        }
        listaMarkers.clear();

        //Cria novos markers e adiciona no mapa
        for (OnibusDTO onibus: listaPosicoesOnibus){
            MarkerOptions mk =  new MarkerOptions().position(
                    new LatLng(onibus.getoLatitude(),
                            onibus.getoLongitude())).title(
                            onibus.getTituloLinha()).snippet(
                            onibus.getoSentido());

            mk.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icone_onibus));

            Marker marker;
            marker = mMap.addMarker(mk);
            //Adiciona Marker na lista de referencias
            listaMarkers.add(marker);
        }

        if (!primeiroZoom){
            primeiroZoom = true;
            AplicaZoom(listaMarkers);
        }
    }

    private void AplicaZoom(List<Marker> mkList){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mkList) {
            builder.include(marker.getPosition());
        }

        //Adiciona a posicao do usuario ao zoom
        if (mMap.getMyLocation() != null){
            builder.include(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()));
        }

        LatLngBounds bounds = builder.build();

        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    private void MarcaTerminaisNoMapa(){
        for (TerminalDTO terminal: util.RetornaTerminais()){
            MarkerOptions mk =  new MarkerOptions().position(
                    new LatLng(terminal.gettLatitude(),
                            terminal.gettLongitude())).title(
                    terminal.gettNome());

            mk.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_terminal));
            mMap.addMarker(mk);
        }
    }

    private void MarcaItinerarioNoMapa(){
        Polyline line;

        for (int i = 0; i < listaItinerarios.size(); i++) {
            PolylineOptions optionsIda = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            for (int z = 0; z < listaItinerarios.get(i).getIda().size(); z++) {
                LatLng point = new LatLng(listaItinerarios.get(i).getIda().get(z).getLatitude(),
                                    listaItinerarios.get(i).getIda().get(z).getLongitude());
                optionsIda.add(point);
            }
            mMap.addPolyline(optionsIda);


            PolylineOptions optionsVolta = new PolylineOptions().width(5).color(Color.YELLOW).geodesic(true);
            for (int z = 0; z < listaItinerarios.get(i).getVolta().size(); z++) {
                LatLng point = new LatLng(listaItinerarios.get(i).getVolta().get(z).getLatitude(),
                        listaItinerarios.get(i).getVolta().get(z).getLongitude());
                optionsVolta.add(point);
            }
            mMap.addPolyline(optionsVolta);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mapa);
        fragmentManager = getSupportFragmentManager();
        AjustaLaout();

        try {
            InicializaMapa();
        } catch (Exception e){

        }

        if (mMap != null){
            MarcaTerminaisNoMapa();
            //ATUALIZA POSICOES DOS ONIBUS
            try {
                ws.getLocalizacaoOnibus(this, extras.getString("CODIGO_LINHA_SELECIONADA"),extras.getString("TITULO_LINHA_SELECIONADA"),new TransactionAction() {
                    public void perform() {
                        if (!ws.listaPosicoesOnibus.isEmpty()){
                            listaPosicoesOnibus = ws.listaPosicoesOnibus;
                            ContadorAtualizacao = 15;
                            AtualizaPosicoesOnibus();
                            InicializaTimer();
                        } else {
                            if (timer != null){
                                timer.cancel();
                                timer = null;
                            }
                            statusBar.setText("NENHUM ÔNIBUS ENCONTRADO");
                        }
                    }
                });
            } catch (JSONException e) {
                statusBar.setText("ERRO AO ATUALIZAR");
                e.printStackTrace();
            }

            //MARCA ITINERARIO NO MAPA
            try {
                ws.getIninerario(this, extras.getString("CODIGO_LINHA_SELECIONADA"), new TransactionAction() {
                    public void perform() {
                        listaItinerarios = ws.listaItinerarios;
                        MarcaItinerarioNoMapa();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home_favorito) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (!ws.listaPosicoesOnibus.isEmpty()){
            ws.listaPosicoesOnibus.clear();
        }
        if (!listaMarkers.isEmpty()){
            listaMarkers.clear();
        }
        if (timer != null){
            timer.cancel();
            timer = null;
        }
        if (mMap != null){
            mMap = null;
        }
    }

}
