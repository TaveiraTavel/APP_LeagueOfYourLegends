package com.example.leagueofyourlegends;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String API_KEY = "RGAPI-1e0b5c8b-138b-44c8-81bf-09fbc096d742";

    static String buscaInvocador(String regiao, String nickname){
        String SummonerJSONString = null;
        String LeagueJSONString = null;
        String ChampionMasteryJSONString = null;
        String ChampionsJSONString = null;
        String PerfilJSONString = null;

        String apiDomain = regiao.toLowerCase() + ".api.riotgames.com";
        String versao = "12.11.1";

        try {
            // Busca no EndPoint Summoner
            SummonerJSONString = getSummonerJSONString(apiDomain, nickname);
            if (SummonerJSONString == null){
                return null;
            }
            JSONObject SummonerJSONObject = new JSONObject(SummonerJSONString);

            // Inicializar contador e itens a serem buscados
            String summonerNickname = null;
            String encryptedSummonerId = null;
            Integer profileIconId = null;
            Integer summonerLevel = null;

            // Procurando pelos itens
            try {
                summonerNickname = SummonerJSONObject.getString("name");
                encryptedSummonerId = SummonerJSONObject.getString("id");
                profileIconId = SummonerJSONObject.getInt("profileIconId");
                summonerLevel = SummonerJSONObject.getInt("summonerLevel");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Busca no EndPoint League
            LeagueJSONString = getLeagueJSONString(apiDomain, encryptedSummonerId);
            JSONArray LeagueJSONArray = new JSONArray(LeagueJSONString);

            // Inicializar contador e itens a serem buscados
            String tier = null;
            String rank = null;
            Integer leaguePoints = null;
            Integer wins = null;
            Integer losses = null;
            Boolean hotStreak = null;
            Boolean inactive = null;

            // Procurando pelos itens
            try {
                if (LeagueJSONArray.length() < 1){
                    return null;
                }
                JSONObject LeagueJSONObject = LeagueJSONArray.getJSONObject(0);

                tier = LeagueJSONObject.getString("tier");
                rank = LeagueJSONObject.getString("rank");
                leaguePoints = LeagueJSONObject.getInt("leaguePoints");
                wins = LeagueJSONObject.getInt("wins");
                losses = LeagueJSONObject.getInt("losses");
                hotStreak = LeagueJSONObject.getBoolean("hotStreak");
                inactive = LeagueJSONObject.getBoolean("inactive");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Busca no EndPoint Champion Mastery
            ChampionMasteryJSONString = getChampionMasteryJSONString(apiDomain, encryptedSummonerId);
            JSONArray ChampionMasteryJSONArray = new JSONArray(ChampionMasteryJSONString);
            JSONArray topChampionMasteryJSONArray = new JSONArray();

            // Contador e itens a serem buscados
            for (int i = 0; i <= 2; i++){
                JSONObject ChampionMasteryJSONObject = ChampionMasteryJSONArray.getJSONObject(i);

                JSONObject topChampionMasteryJSONObject = new JSONObject();

                // Busca no EndPoint Champions
                ChampionsJSONString = getChampionsJSONString(versao);
                JSONObject ChampionsJSONObject = new JSONObject(ChampionsJSONString);

                String championName = null;
                String championImage = null;

                try {
                    JSONObject ChampionsDataJSONObject = ChampionsJSONObject.getJSONObject("data");
                    Iterator<String> keys = ChampionsDataJSONObject.keys();
                    String key;
                    while (keys.hasNext()) {
                        key = keys.next();
                        JSONObject ChampionDataObject = ChampionsDataJSONObject.getJSONObject(key);

                        if (ChampionDataObject.getInt("key") == ChampionMasteryJSONObject.getInt("championId")){
                            championName = ChampionDataObject.getString("name");
                            championImage = key;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                topChampionMasteryJSONObject.put("championName", championName);
                topChampionMasteryJSONObject.put("championImage", championImage);
                topChampionMasteryJSONObject.put("championLevel", ChampionMasteryJSONObject.getInt("championLevel"));
                topChampionMasteryJSONObject.put("championPoints", ChampionMasteryJSONObject.getInt("championPoints"));

                topChampionMasteryJSONArray.put(topChampionMasteryJSONObject);

                Log.d(LOG_TAG, topChampionMasteryJSONArray.toString());
            }

            // Criando JSON com todos os dados
            JSONObject PerfilJSONObject = new JSONObject();
            try {
                PerfilJSONObject.put("nickname", summonerNickname);
                PerfilJSONObject.put("summonerId", encryptedSummonerId);
                PerfilJSONObject.put("iconId", profileIconId);
                PerfilJSONObject.put("level", summonerLevel);

                PerfilJSONObject.put("tier", tier);
                PerfilJSONObject.put("rank", rank);
                PerfilJSONObject.put("points", leaguePoints);
                PerfilJSONObject.put("wins", wins);
                PerfilJSONObject.put("losses", losses);
                PerfilJSONObject.put("hotStreak", hotStreak);
                PerfilJSONObject.put("inactive", inactive);

                PerfilJSONObject.put("topChampionsMastery", topChampionMasteryJSONArray.toString());

                PerfilJSONString = PerfilJSONObject.toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, PerfilJSONString);
        return PerfilJSONString;
    }

    static String getSummonerJSONString(String apiDomain, String nickname){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JSONString = null;

        try {
            // Contrução da URL de busca
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("https")
                    .authority( apiDomain )
                    .appendPath("lol")
                    .appendPath("summoner")
                    .appendPath("v4")
                    .appendPath("summoners")
                    .appendPath("by-name")
                    .appendPath( nickname )
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
            JSONString = stringBuilder.toString();

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

        // Retornar o Json
        Log.d(LOG_TAG, JSONString);
        return JSONString;
    }

    static String getLeagueJSONString(String apiDomain, String leagueID){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JSONString = null;

        try {
            // Contrução da URL de busca
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("https")
                    .authority( apiDomain )
                    .appendPath("lol")
                    .appendPath("league")
                    .appendPath("v4")
                    .appendPath("entries")
                    .appendPath("by-summoner")
                    .appendPath( leagueID )
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
            JSONString = stringBuilder.toString();

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

        // Retornar o Json
        Log.d(LOG_TAG, JSONString);
        return JSONString;
    }

    static String getChampionMasteryJSONString(String apiDomain, String leagueID){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JSONString = null;

        try {
            // Contrução da URL de busca
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("https")
                    .authority( apiDomain )
                    .appendPath("lol")
                    .appendPath("champion-mastery")
                    .appendPath("v4")
                    .appendPath("champion-masteries")
                    .appendPath("by-summoner")
                    .appendPath( leagueID )
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
            JSONString = stringBuilder.toString();

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

        // Retornar o Json
        Log.d(LOG_TAG, JSONString);
        return JSONString;
    }

    static String getChampionsJSONString(String versao){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JSONString = null;

        try {
            // Contrução da URL de busca
            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme("https")
                    .authority("ddragon.leagueoflegends.com")
                    .appendPath("cdn")
                    .appendPath( versao )
                    .appendPath("data")
                    .appendPath("pt_BR")
                    .appendPath("champion.json")
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
            JSONString = stringBuilder.toString();

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

        // Retornar o Json
        Log.d(LOG_TAG, JSONString);
        return JSONString;
    }
}
