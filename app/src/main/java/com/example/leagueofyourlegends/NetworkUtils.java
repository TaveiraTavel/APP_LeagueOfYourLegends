package com.example.leagueofyourlegends;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String API_KEY = "RGAPI-3f7fcf3d-4114-485c-b637-129c0ac945a8";

    static String buscaInvocador(String REGIAO, String NICKNAME){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String InvocadorJSONString = null;

        String DOMINIO = REGIAO.toLowerCase() + ".api.riotgames.com";

        try {
            // Contrução da URL de busca
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("https")
                    .authority( DOMINIO )
                    .appendPath("lol")
                    .appendPath("summoner")
                    .appendPath("v4")
                    .appendPath("summoners")
                    .appendPath("by-name")
                    .appendPath( NICKNAME )
                    .appendQueryParameter("api_key", API_KEY)
                    .build();

            // Converte a URI para a URL.
            URL UrlBusca = new URL(uriBuilder.toString());

            // Abre a conexão de rede
            try {
                urlConnection = (HttpURLConnection) UrlBusca.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();

                // Busca o InputStream
                InputStream inputStream = urlConnection.getInputStream();

                // Cria o buffer para o input stream
                reader = new BufferedReader(new InputStreamReader(inputStream));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Usa o StringBuilder para receber a resposta.
            StringBuilder stringBuilder = new StringBuilder();
            String linha;

            if (reader != null) {
                while ((linha = reader.readLine()) != null) {
                    // Adiciona a linha a string.
                    stringBuilder.append(linha);
                    stringBuilder.append("\n");
                }
            } else {
                // Se o stream estiver vazio não faz nada
                return null;
            }

            // String com o JSON
            InvocadorJSONString = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // fecha a conexão e o buffer.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // Retornar e escrever o Json no log
        Log.d(LOG_TAG, InvocadorJSONString);
        return InvocadorJSONString;
        }
}
