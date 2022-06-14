package com.example.leagueofyourlegends;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>  {

    // Banco de Dados
    DatabaseHelper mydb;

    private Spinner spinner;

    // Acessando a tela
    EditText edtNickname;
    Spinner spinRegiao;
    String queryNickname;
    String queryRegiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }

        // Banco de Dados
        mydb = new DatabaseHelper(this);

        spinner = (Spinner) findViewById(R.id.spinRegiao);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, mydb.getRegioes());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Acessando a tela
        edtNickname = (EditText) findViewById(R.id.edtNickname);
        spinRegiao = (Spinner) findViewById(R.id.spinRegiao);
    }

    // Botão Entrar
    public void buscarInvocador(View view) {


        // Verifica o status da conexão de rede
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // Acessando a tela
        queryNickname = edtNickname.getText().toString();
        queryRegiao = spinRegiao.getSelectedItem().toString();

        // Se a rede estiver disponivel e o campo de busca não estiver vazio, iniciar o Loader CarregaInvocador
        if (networkInfo != null && networkInfo.isConnected()
            && queryNickname.length() != 0) {

            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryRegiao", queryRegiao);
            queryBundle.putString("queryNickname", queryNickname);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            Toast.makeText(getApplicationContext(), "Procurando pelo invocador...", Toast.LENGTH_SHORT).show();
        }
        // atualiza a textview para informar que não há conexão ou termo de busca
        else {
            if (queryNickname.length() == 0) {
                Toast.makeText(getApplicationContext(), "Informe um nickname", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Verifique sua conexão!", Toast.LENGTH_SHORT).show();
           }
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryRegiao = "";
        String queryNickname = "";
        if (args != null) {
            queryRegiao = args.getString("queryRegiao");
            queryNickname = args.getString("queryNickname");
        }
        return new CarregaInvocador(this, queryRegiao, queryNickname);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try{
            // Converter a resposta em JSON
            JSONObject jsonObject = new JSONObject(data);

            // Inicializar contador e itens a serem buscados
            String summonerNickname = null;
            String encryptedSummonerId = null;
            Integer profileIconId = null;
            Integer summonerLevel = null;

            String tier = null;
            String rank = null;
            Integer leaguePoints = null;
            Integer wins = null;
            Integer losses = null;
            Boolean hotStreak = null;
            Boolean inactive = null;

            // Procurando pelos itens
            try {
                summonerNickname = jsonObject.getString("nickname");
                encryptedSummonerId = jsonObject.getString("summonerId");
                profileIconId = jsonObject.getInt("iconId");
                summonerLevel = jsonObject.getInt("level");

                tier = jsonObject.getString("tier");
                rank = jsonObject.getString("rank");
                leaguePoints = jsonObject.getInt("points");
                wins = jsonObject.getInt("wins");
                losses = jsonObject.getInt("losses");
                hotStreak = jsonObject.getBoolean("hotStreak");
                inactive = jsonObject.getBoolean("inactive");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Verificando dados retornados
            if (summonerNickname != null &&
                encryptedSummonerId != null &&
                profileIconId != null &&
                summonerLevel != null &&

                tier != null &&
                rank != null &&
                leaguePoints != null &&
                wins != null &&
                losses != null &&
                hotStreak != null &&
                inactive != null){
                // Abrir próxima activity
                abrirPerfilActivity(summonerNickname,
                                    encryptedSummonerId,
                                    profileIconId,
                                    summonerLevel,
                                    tier,
                                    rank,
                                    leaguePoints,
                                    wins,
                                    losses,
                                    hotStreak,
                                    inactive);

                // Destruir loader para fazer próxima busca
                getSupportLoaderManager().destroyLoader(0);
            }

        } catch (Exception e) {
            // Se não receber um JSON válido, informa ao usuário
            Toast.makeText(getApplicationContext(), "Não encontramos esse invocador :/", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // Obrigatório implementar, nenhuma ação executada
    }

    // Abrir PerfilActivity
    public void abrirPerfilActivity(String summonerNickname,
                                    String encryptedSummonerId,
                                    int profileIconId,
                                    int summonerLevel,

                                    String tier,
                                    String rank,
                                    int leaguePoints,
                                    int wins,
                                    int losses,
                                    Boolean hotStreak,
                                    Boolean inactive) {

        // Intent explícita
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra("nickname", summonerNickname);
        intent.putExtra("regiao", spinRegiao.getSelectedItem().toString());
        intent.putExtra("summonerId", encryptedSummonerId);
        intent.putExtra("iconId", profileIconId);
        intent.putExtra("level", summonerLevel);

        intent.putExtra("tier", tier);
        intent.putExtra("rank", rank);
        intent.putExtra("points", leaguePoints);
        intent.putExtra("wins", wins);
        intent.putExtra("losses", losses);
        intent.putExtra("hotStreak", hotStreak);
        intent.putExtra("inactive", inactive);
        startActivity(intent);
        this.overridePendingTransition(0, 0);
    }
}