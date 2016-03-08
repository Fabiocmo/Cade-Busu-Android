package com.bks.meubusu.cadebusu.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bks.meubusu.cadebusu.R;
import com.bks.meubusu.cadebusu.adapter.LinhaCustomItemAdapter;
import com.bks.meubusu.cadebusu.model.LinhaDTO;
import com.bks.meubusu.cadebusu.util.GlobalClass;
import com.bks.meubusu.cadebusu.util.TransactionAction;
import com.bks.meubusu.cadebusu.util.Webservice;

import org.json.JSONException;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ListView listaLinhas ;
    SearchView inputSearch;
    LinhaCustomItemAdapter listaLinhasAdapter;

    ProgressDialog mProgressDialog;
    AlertDialog.Builder alertDialogBuilder;

    public void AjustaLayout(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void InicializaInputSearch(){
        inputSearch = (SearchView) findViewById(R.id.input_search);

        //COR BRANCA NO HINT DA SEARCHVIEW
        int searchTextId = inputSearch.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = (TextView) inputSearch.findViewById(searchTextId);
        if (searchText!=null) {
            searchText.setTextColor(Color.WHITE);
            searchText.setHintTextColor(Color.WHITE);
        }

        inputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
                callSearch(newText);
//              }
                return true;
            }

            public void callSearch(final String query) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listaLinhasAdapter.getFilter().filter(query);
                    }
                });
            }
        });
    }

    private void RefreshTable(List<LinhaDTO> novaLista){
        listaLinhasAdapter = new LinhaCustomItemAdapter(this, novaLista);
        listaLinhas.setAdapter(listaLinhasAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listaLinhas = (ListView) findViewById(R.id.listaLinhas);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        AjustaLayout();
        InicializaInputSearch();

        List<LinhaDTO> linhas = LinhaDTO.listAll(LinhaDTO.class);

        listaLinhas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinhaDTO itemValue = (LinhaDTO) listaLinhas.getItemAtPosition(position);

                Intent mapaIntent = new Intent(HomeActivity.this, MapsActivity.class);
                mapaIntent.putExtra("CODIGO_LINHA_SELECIONADA", itemValue.getlCodigo());
                mapaIntent.putExtra("TITULO_LINHA_SELECIONADA", ((LinhaDTO) listaLinhas.getItemAtPosition(position)).getlNomeLinha());
                HomeActivity.this.startActivity(mapaIntent);
            }
        });

        if (linhas.size() < 120) {
            try {
                for (LinhaDTO item : linhas) {
                    item.delete();
                }
                getLinhasWebService();
            } catch (JSONException e) {
                mProgressDialog.dismiss();
                mostrarAlertaTentarNovamente();
                e.printStackTrace();
            }
        } else {
            RefreshTable(linhas);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.home_favorito) {
            if (!listaLinhasAdapter.isFavorito()){
                item.setIcon(R.drawable.ic_star_filled);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listaLinhasAdapter.setFavorito(true);
                    }});
            }else{
                item.setIcon(R.drawable.ic_star);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listaLinhasAdapter.setFavorito(false);
                    }});
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        hideSoftKeyboard();
    }

    @Override
    public void onStart(){
        super.onStart();
        hideSoftKeyboard();
    }

    public void mostrarCarregando(){
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Carregando linhas... ");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
        }
    }

    public void mostrarAlertaTentarNovamente(){
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Erro!");

        alertDialogBuilder
                .setMessage("Erro ao carregar dados! Tente novamente.")
                .setCancelable(false)
                .setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            getLinhasWebService();
                        } catch (JSONException e) {
                            mProgressDialog.dismiss();
                            mostrarAlertaTentarNovamente();
                            e.printStackTrace();
                        }
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getLinhasWebService() throws JSONException {
        if (!isNetworkAvailable()){
            mostrarAlertaTentarNovamente();
        }else{
            final Webservice ws = ((GlobalClass) getApplicationContext()).webservice;
            mostrarCarregando();
            mProgressDialog.show();
            ws.getListaLinhas(new TransactionAction() {
                public void perform() {
                    for (LinhaDTO item: ws.listaLinhas){
                        item.save();
                    }
                    mProgressDialog.dismiss();
                    RefreshTable(ws.listaLinhas);
                }
            });
        }
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}
