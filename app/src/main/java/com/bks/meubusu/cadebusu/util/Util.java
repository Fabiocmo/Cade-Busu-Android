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

    public String RetornaListaOnibus(){
        return "[{\"cod\":[\"001\",\"100\",\"101\",\"102\",\"103\",\"104\",\"105\",\"106\",\"107\",\"108\",\"109\",\"110\",\"111\",\"112\",\"113\",\"114\",\"121\",\"200\",\"201\",\"206\",\"208\",\"209\",\"211\",\"213\",\"217\",\"220\",\"221\",\"223\",\"224\",\"225\",\"227\",\"250\",\"300\",\"301\",\"302\",\"303\",\"305\",\"306\",\"307\",\"308\",\"309\",\"310\",\"311\",\"312\",\"313\",\"314\",\"315\",\"317\",\"350\",\"351\",\"400\",\"401\",\"402\",\"404\",\"405\",\"406\",\"407\",\"408\",\"409\",\"410\",\"411\",\"412\",\"413\",\"414\",\"415\",\"416\",\"417\",\"418\",\"419\",\"420\",\"422\",\"423\",\"424\",\"425\",\"426\",\"427\",\"428\",\"429\",\"444\",\"445\",\"501\",\"502\",\"503\",\"504\",\"505\",\"506\",\"512\",\"601\",\"605\",\"800\",\"801\",\"802\",\"803\",\"806\",\"807\",\"808\",\"810\",\"830\",\"834\",\"835\",\"900\",\"901\",\"902\",\"903\",\"904\",\"905\",\"906\",\"908\",\"909\",\"931\",\"932\",\"933\",\"934\",\"951\",\"952\",\"957\",\"959\"],\"valor\":[\"001 - CENTRO LIVRE\",\"100 - JD. ALEMANHA - TC\",\"101 - JARDIM NOVO AMPARO\",\"102 - JARDIM IDEAL\",\"103 - JARDIM SANTA FE\",\"104 - JD. INTERLAGOS\",\"105 - JARDIM SAO PEDRO\",\"106 - CJ. GUILHERME PIRES\",\"107 - JARDIM ARAGARÇA\",\"108 - JARDIM ALBATROZ\",\"109 - RODOVIÁRIA\",\"110 - MISTER THOMAS\",\"111 - CJ. EUCALIPTOS\",\"112 - ALEXANDRE URBANAS\",\"113 - PIONEIROS\",\"114 - BOULEVARD SHOPPING\",\"121 - TRÊS FIGUEIRAS\",\"200 - VILA BRASIL\",\"201 - JARDIM CALIFÓRNIA\",\"206 - JARDIM EUROPA\",\"208 - VILA HIGIENÓPOLIS\",\"209 - JARDIM CLAUDIA\",\"211 - PATRIMÔNIO REGINA\",\"213 - SHOPING CATUAÍ\",\"217 - CJ. VIVENDAS DO ARVOREDO\",\"220 - EMAUS\",\"221 - LIMOEIRO\",\"223 - CANAÃ\",\"224 - SONORA\",\"225 - PITÁGORAS-SHOP.CATUAÍ\",\"227 - JD.BOTÂNICO\",\"250 - SÃO LUIZ\",\"300 - 300- T.R.OESTE - CENTRO\",\"301 - JARDIM PRESIDENTE\",\"302 - JARDIM HEDY\",\"303 - JARDIM TOKIO\",\"305 - CAMPUS\",\"306 - CIDADE UNIVERSITÁRIA\",\"307 - CJ. AVELINO VIEIRA\",\"308 - JARDIM BANDEIRANTES\",\"309 - PQ. GOV. N.BRAGA\",\"310 - JARDIM DO SOL\",\"311 - JARDIM SANTA RITA\",\"312 - JARDIM STA  MADALENA\",\"313 - JARDIM MARIA LUCIA\",\"314 - JARDIM OLIMPICO\",\"315 - COLUMBIA\",\"317 - 314/317 - JD.MARACANA-TC\",\"350 - 350 - EXPRESSO T.R.OESTE - T. CENTRAL\",\"351 - PARADOR TRO- TERMINAO OESTE - TC TERMINA\",\"400 - CJ. PARIGOT DE SOUZA\",\"401 - CJ CHEFE NEWTON\",\"402 - CONJ. VIVI XAVIER\",\"404 - CONJ. HEIMTAL\",\"405 - CJ. MARIA CECÍLIA\",\"406 - CJ. AQUILES STENGUEL\",\"407 - CONJ. JOÃO PAZ\",\"408 - VILA RECREIO\",\"409 - VILA YARA\",\"410 - VILA NOVA\",\"411 - JARDIM PARAISO\",\"412 - CJ.HILDA MANDARINO\",\"413 - RESIDENCIAL DO CAFÉ\",\"414 - JD. BARCELONA\",\"415 - CONJ. ITAPOA\",\"416 - CJ. FARID LIBOS\",\"417 - JARDIM CATUAI\",\"418 - JD.PRIMAVERA\",\"419 - JD. PORTO SEGURO\",\"420 - T.VIVI XAVIER / SHOP. LONDRINA NORTE\",\"422 - CJ MARIA CELINA\",\"423 - CJ.SÃO JORGE\",\"424 - MARISTELA\",\"425 - COMERCIAL NORTE\",\"426 - JARDIM PARIS\",\"427 - WARTA - VIVI XAVIER\",\"428 - VISTA BELA\",\"429 - JD. PAULISTA\",\"444 - WARTA\",\"445 - JD. FELICIDADE\",\"501 - PARADOR VIVI XAVIER - TC\",\"502 - PARADOR OURO VERDE - TC\",\"503 - RÁPIDO AQUILES - CENTRO\",\"504 - PARADOR M.  GAVETTI - TC\",\"505 - EXPRESSO VIVI  XAVIER - TC\",\"506 - RAPIDO VIOLIM - MARIA CECÍLIA\",\"512 - EXPRESSO OURO VERDE\",\"601 - PARADOR ACAPULCO - TC\",\"605 - EXP TERM. ACAPULCO/ TC\",\"800 - VIVI XAVIER - ACAPULCO\",\"801 - VIVI XAVIER - T.GAVETTI - CENTRO CÍVICO\",\"802 - VIVI XAVIER - AV. BANDEIRANTES\",\"803 - VIVI XAVIER - SHOPPING CATUAI\",\"806 - SAUL ELKIND - SHOPPING CATUAI\",\"807 - TRO/SAN.F - DIAMETRAL TRO - SAN FERNANDO\",\"808 - BAND/ HU J- DIAMETRAL BANDEIRANTES-HU\",\"810 - SÃO JOÃO - TIRADENTES\",\"830 - FAC.PITÁGORAS - VIVI XAVIER\",\"834 - PITÁGORAS - MILTON GAVETTI\",\"835 - U.E.L- MILTON GAVETTI\",\"900 - VIVI XAVIER - H.U. (HOSPITAL UNIVERSITAR\",\"901 - PERIMETRAL 5 CJ. / CACIQUE (V.T VIV-COB.\",\"902 - MARITACAS / SAUL ELKIND\",\"903 - CIRCULAR - TC - CENTRO\",\"904 - PER/S.LOURENÇO/SABARÁ\",\"905 - TERMINAL ACAPULCO - H. U\",\"906 - COLUMBIA-SHOPPING CATUAÍ\",\"908 - PARÁ - ANEL CENTRAL\",\"909 - PERNAMBUCO - ANEL CENTRAL\",\"931 - TRO/LONORTE - T.R.OESTE - LONDRINA NORTE\",\"932 - TVX / TRO - T. VIVI XAVIER - T. R. OESTE\",\"933 - T.V.XAVIER / UEL / SHOPPING CATUAI\",\"934 - INTERCLINICAS\",\"951 - PARIGOT\",\"952 - PARQUE WALDEMAR HAUER\",\"957 - CORUJÃO AVELINO\",\"959 - JARDIM SÃO FRANCISCO\"]}]";
    }

}
