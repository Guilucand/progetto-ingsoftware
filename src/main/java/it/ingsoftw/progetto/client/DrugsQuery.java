package it.ingsoftw.progetto.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import it.ingsoftw.progetto.common.Drug;
import it.ingsoftw.progetto.common.utils.StringUtils;


/**
 * Classe che interroga il database dell'agenzia farmaco
 * per ottenere informazioni aggiornate sui farmaci
 * I dati sono ricevuti in formato JSON e convertiti in una class interna 'Drug'
 * E' possibile ricercare per nome farmaco o principio attivo
 */
public class DrugsQuery {

    public enum QueryType {
        Drug,
        ActivePrinciple
    }


    /**
     * Url base utilizzato per interrogare il database
     */
    private static String baseAddress = "https://www.agenziafarmaco.gov.it/services/search/";

    /**
     * Legge una stringa da un Reader
     * @param rd il Reader
     * @return la string letta
     * @throws IOException
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * Ritorna una stringa da un elemento JSON, convertendola da Html a testo normale
     * @param el l'elemento JSON.
     * @return la stringa o null se 'el' e' null
     */
    private static String getStringFromJsonElement(JsonElement el) {
        return el == null ? null : StringUtils.unescapeHtml3(el.getAsString());
    }

    /**
     * Interroga il database
     * @param query il testo della query
     * @param queryType la tipologia della query (farmaco o principio attivo)
     * @param wildcard se true ricerca entries che iniziano con la query, altrimenti solo nomi esatti
     * @return la lista dei farmaci trovati
     */
    public Drug[] queryDatabase(String query, QueryType queryType, boolean wildcard) {


        List<Drug> drugList = new ArrayList<>();

        try {
            String queryField;
            switch (queryType) {
                case ActivePrinciple:
                    queryField = "sm_field_descrizione_atc";
                    break;
                case Drug:
                default:
                    queryField = "sm_field_descrizione_farmaco";
                    break;
            }


            String select = "select?fl=*";
            String where = "q=bundle:confezione_farmaco+" + queryField + ":"+ URLEncoder.encode(query.trim(), "UTF-8") + (wildcard ? "*" : "");
            String options = "df=sm_field_descrizione_farmaco&wt=json&rows=150000";

            InputStream jsonStream = new URL(baseAddress + select + "&" + where + "&" + options).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(jsonStream, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);

            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(jsonText);

            JsonArray jsonQuery = je.getAsJsonObject()
                    .get("response").getAsJsonObject()
                    .get("docs").getAsJsonArray();

            for (JsonElement drugEl : jsonQuery) {
                JsonObject drug = drugEl.getAsJsonObject();

                drugList.add(new Drug(
                        getStringFromJsonElement(drug.get("sm_field_descrizione_ditta")),
                        getStringFromJsonElement(drug.get("sm_field_descrizione_farmaco")),
                        getStringFromJsonElement(drug.get("sm_field_descrizione_confezione")),
//                        getStringFromJsonElement(drug.get("sm_field_codice_farmaco")),
//                        getStringFromJsonElement(drug.get("sm_field_codice_confezione")),
                        getStringFromJsonElement(drug.get("sm_field_codice_atc")),
                        getStringFromJsonElement(drug.get("sm_field_descrizione_atc")),
                        getStringFromJsonElement(drug.get("sm_field_aic"))
//                        getStringFromJsonElement(drug.get("sm_field_chiave_confezione"))
                ));
            }
        } catch (IOException e) {
            return null;
        }

        return drugList.toArray(new Drug[0]);
    }


}
