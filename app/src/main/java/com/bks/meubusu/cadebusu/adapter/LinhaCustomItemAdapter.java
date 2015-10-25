package com.bks.meubusu.cadebusu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.bks.meubusu.cadebusu.R;
import com.bks.meubusu.cadebusu.model.LinhaDTO;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raullermen on 9/15/15.
 */
public class LinhaCustomItemAdapter extends BaseSwipeAdapter implements Filterable {

    private boolean blFavoritos;
    private Context context;
    private List<LinhaDTO> originalLinhaObjects;
    private List<LinhaDTO> displayLinhaObjects;

    public LinhaCustomItemAdapter(Context context, List<LinhaDTO> linhas) {
        this.context = context;
        this.originalLinhaObjects = linhas;
        this.displayLinhaObjects = linhas;
        blFavoritos = false;
    }

    public boolean isFavorito() {
        return blFavoritos;
    }

    public void setFavorito(boolean blFavoritos) {
        this.blFavoritos = blFavoritos;
        getFavoritos(blFavoritos);
    }

    @Override
    public int getCount() {
        return displayLinhaObjects.size();
    }

    @Override
    public Object getItem(int location) {
        return displayLinhaObjects.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void getFavoritos(boolean bl){
        ArrayList<LinhaDTO> resultados = new ArrayList<>();
        if (bl){
            for (LinhaDTO linha : originalLinhaObjects){
                if (linha.getlFavorito()) {
                    resultados.add(linha);
                }
            }
            displayLinhaObjects = resultados;
        }else{
            displayLinhaObjects = originalLinhaObjects;
        }
        notifyDataSetChanged();
    }

    //FUNCOES DE FILTRAGEM
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                displayLinhaObjects = (ArrayList<LinhaDTO>) results.values;
                notifyDataSetChanged();
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
                        if ((linha.getlCodigo() + linha.getlNomeLinha()).toUpperCase().contains(constraint.toString().toUpperCase())){
                            resultados.add(linha);
                        }
                    }
                }

                filterResults.values = resultados;
                filterResults.count = resultados.size();
                return filterResults;
            }
        };
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, final ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.linha_custom_cell, null);
        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.favorito));
            }
        });
        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    @Override
    public void fillValues(final int position, final View convertView) {

        TextView tCodigoLinha = (TextView) convertView.findViewById(R.id.codigoLinha);
        TextView tTituloLinha = (TextView) convertView.findViewById(R.id.tituloLinha);
        TextView tEmpresaLinha = (TextView) convertView.findViewById(R.id.empresaLinha);

        LinhaDTO m = displayLinhaObjects.get(position);

        tCodigoLinha.setText(m.getlCodigo());
        tTituloLinha.setText(m.getlNomeLinha());
        tEmpresaLinha.setText(m.getlEmpresa());

        if (m.getlFavorito()){
            convertView.findViewById(R.id.favorito).setBackgroundResource(R.mipmap.ic_estrela_amarela_cheia);
        }else{
            convertView.findViewById(R.id.favorito).setBackgroundResource(R.mipmap.ic_estrela_amarela_vazia);
        }

        convertView.findViewById(R.id.favorito).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinhaDTO linhadto = (LinhaDTO) getItem(position);

                if (!linhadto.getlFavorito()){
                    linhadto.setlFavorito(true);
                    view.findViewById(R.id.favorito).setBackgroundResource(R.mipmap.ic_estrela_amarela_cheia);
                }else{
                    linhadto.setlFavorito(false);
                    view.findViewById(R.id.favorito).setBackgroundResource(R.mipmap.ic_estrela_amarela_vazia);
                }

                //FECHA O SWIPE
                SwipeLayout swipeLayout = (SwipeLayout)convertView.findViewById(getSwipeLayoutResourceId(position));
                swipeLayout.close(true);

                linhadto.save();

                if (blFavoritos){
                    displayLinhaObjects.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
    }

}
