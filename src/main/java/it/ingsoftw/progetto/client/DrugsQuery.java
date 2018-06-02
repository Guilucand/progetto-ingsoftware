package it.ingsoftw.progetto.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.javafx.fxml.builder.URLBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import it.ingsoftw.progetto.common.Drug;
import it.ingsoftw.progetto.common.utils.StringUtils;

public class DrugsQuery {


    private static String baseAddress = "https://www.agenziafarmaco.gov.it/services/search/";


    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static String getStringFromJsonElement(JsonElement el) {
        return el == null ? null : StringUtils.unescapeHtml3(el.getAsString());
    }

    public Drug[] queryDatabase(String query) {


        List<Drug> drugList = new ArrayList<>();

        try {
            String select = "select?fl=*";
            String where = "q=bundle:confezione_farmaco+sm_field_descrizione_farmaco:"+ URLEncoder.encode(query.trim(), "UTF-8") + "*";
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
                        getStringFromJsonElement(drug.get("sm_field_codice_farmaco")),
                        getStringFromJsonElement(drug.get("sm_field_codice_confezione")),
                        getStringFromJsonElement(drug.get("sm_field_codice_atc")),
                        getStringFromJsonElement(drug.get("sm_field_descrizione_atc")),
                        getStringFromJsonElement(drug.get("sm_field_aic")),
                        getStringFromJsonElement(drug.get("sm_field_chiave_confezione"))));
            }
        } catch (IOException e) {
            return null;
        }


        return drugList.toArray(new Drug[0]);
    }


}
