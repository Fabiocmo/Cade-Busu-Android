package com.bks.meubusu.cadebusu.util;

import android.app.Application;

import com.bks.meubusu.cadebusu.model.LinhaDTO;
import com.bks.meubusu.cadebusu.model.PontoDTO;
import com.bks.meubusu.cadebusu.model.TerminalDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raullermen on 9/20/15.
 */
public class Util extends Application{

    public List<TerminalDTO> RetornaTerminais(){

        TerminalDTO TCentral = new TerminalDTO("Terminal Central", -23.308276, -51.160905);
        TerminalDTO TOeste = new TerminalDTO("Terminal Oeste", -23.296867, -51.187323);
        TerminalDTO TOuroVerde = new TerminalDTO("Terminal Ouro Verde", -23.281739, -51.171591);
        TerminalDTO TCatuai = new TerminalDTO("Terminal Catuaí", -23.343897, -51.186245);
        TerminalDTO TAcapulco = new TerminalDTO("Terminal Acapulco", -23.360421, -51.155372);
        TerminalDTO TGavetti = new TerminalDTO("Terminal Gavetti", -23.281480, -51.152499);
        TerminalDTO TViviXavier = new TerminalDTO("Terminal Vivi Xavier", -23.260735, -51.172544);

        List<TerminalDTO> listaTerminais = new ArrayList<>();

        listaTerminais.add(TCentral);
        listaTerminais.add(TOeste);
        listaTerminais.add(TOuroVerde);
        listaTerminais.add(TCatuai);
        listaTerminais.add(TAcapulco);
        listaTerminais.add(TGavetti);
        listaTerminais.add(TViviXavier);

        return listaTerminais;

    }

}
