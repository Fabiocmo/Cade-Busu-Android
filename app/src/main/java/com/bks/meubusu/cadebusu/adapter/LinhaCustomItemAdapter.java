package com.bks.meubusu.cadebusu.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bks.meubusu.cadebusu.R;
import com.bks.meubusu.cadebusu.model.LinhaDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raullermen on 9/15/15.
 */
public class LinhaCustomItemAdapter extends BaseAdapter implements Filterable {

    private Activity activity;
    private LayoutInflater inflater;
    private List<LinhaDTO> originalLinhaObjects;
    private List<LinhaDTO> dislplayLinhaObjects;

    public LinhaCustomItemAdapter(Activity activity, List<LinhaDTO> linhas) {
        this.activity = activity;
        this.originalLinhaObjects = linhas;
        this.dislplayLinhaObjects = linhas;
    }

    @Override
    public int getCount() {
        return dislplayLinhaObjects.size();
    }

    @Override
    public Object getItem(int location) {
        return dislplayLinhaObjects.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.linha_custom_cell, null);

        TextView tCodigoLinha = (TextView) convertView.findViewById(R.id.codigoLinha);
        TextView tTituloLinha = (TextView) convertView.findViewById(R.id.tituloLinha);
        TextView tEmpresaLinha = (TextView) convertView.findViewById(R.id.empresaLinha);

        LinhaDTO m = dislplayLinhaObjects.get(position);

        tCodigoLinha.setText(m.getlCodigo());
        tTituloLinha.setText(m.getlNomeLinha());
        tEmpresaLinha.setText(m.getlEmpresa());

        return convertView;
    }

    //FUNCOES DE FILTRAGEM
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<LinhaDTO> resultados = new ArrayList<>();

                if (constraint.length() < 1){
                    for (LinhaDTO linha : originalLinhaObjects){
                        resultados.add(linha);
                    }
                }else{
                    for (LinhaDTO linha : originalLinhaObjects){
                        if (linha.getlNomeLinha().toLowerCase().contains(constraint.toString().toLowerCase())){
                            resultados.add(linha);
                        }
                    }
                }

                dislplayLinhaObjects = resultados;
                filterResults.values = resultados;
                filterResults.count = resultados.size();
                return filterResults;
            }
        };
    }

}
