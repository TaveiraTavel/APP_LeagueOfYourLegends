package com.example.leagueofyourlegends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    // Banco de Dados
    DatabaseHelper mydb;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Banco de Dados
        mydb = new DatabaseHelper(this);

        spinner = (Spinner) findViewById(R.id.spinnerRegiao);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, mydb.getRegioes());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // Botão Entrar
    public void abrirPerfilActivity(View view) {

        // FAZER INTENT EXPLICITA ENVIANDO O NOME DO INVOCADOR E A REGIÃO PARA SEREM BUSCADOS NA PERFIL ACTIVITY

        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
        this.overridePendingTransition(0, 0);
        finish();
    }
}