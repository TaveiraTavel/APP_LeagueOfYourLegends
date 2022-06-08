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

public class MainActivity extends AppCompatActivity {

    // Banco de Dados
    DatabaseHelper mydb;

    private Spinner spinner;

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
    }

    // Botão Entrar
    public void abrirPerfilActivity(View view) {


        // Verifica o status da conexão de rede
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // Acessando a tela
        EditText edtNickname = (EditText) findViewById(R.id.edtNickname);
        Spinner spinRegiao = (Spinner) findViewById(R.id.spinRegiao);

        String NICKNAME = edtNickname.getText().toString();
        String REGIAO = spinRegiao.getSelectedItem().toString();

        // Se a rede estiver disponivel e o campo de busca não estiver vazio, vai para a tela de Perfil
        if (networkInfo != null && networkInfo.isConnected()
            && NICKNAME.length() != 0) {

            LEMBRAR DE VERIFICAR SE NÃO ESTÁ VAZIO A CAIXA DE TEXTO!!!!!!!!!!!!!!!!!!!!!!!

            // Intent explícita
            Intent intent = new Intent(this, PerfilActivity.class);
            intent.putExtra("invNickname", NICKNAME);
            intent.putExtra("invRegiao", REGIAO);
            startActivity(intent);
            this.overridePendingTransition(0, 0);
        }
        // atualiza a textview para informar que não há conexão ou termo de busca
        else {
            if (NICKNAME.length() == 0) {
                Toast.makeText(getApplicationContext(), "Informe seu Nickname.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Verifique sua conexão...", Toast.LENGTH_SHORT).show();
           \ }
        }
    }

}