package com.example.leagueofyourlegends;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Bundle extras = getIntent().getExtras();
        String NICKNAME = extras.getString("invNickname");
        String REGIAO = extras.getString("invRegiao");

        TextView textNickname = (TextView) findViewById(R.id.textNickname);
        textNickname.setText(NICKNAME);
        textNickname.setContentDescription(NICKNAME);


    }
}