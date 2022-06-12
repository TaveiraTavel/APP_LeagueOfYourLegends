package com.example.leagueofyourlegends;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Pegando dados da tela anterior
        Bundle extras = getIntent().getExtras();
        String summonerNickname = extras.getString("summonerNickname");
        String summonerRegion = extras.getString("summonerRegion");
        int summonerLevel = extras.getInt("summonerLevel");
        int profileIconId = extras.getInt("profileIconId");

        // Colocando Nickname na View
        TextView textNickname = (TextView) findViewById(R.id.textNickname);
        textNickname.setText(summonerNickname);
        textNickname.setContentDescription(summonerNickname);

        // Colocando Icone na View
        ImageView imgInvIcon = (ImageView) findViewById(R.id.imgInvIcon);
        String imageIconURL = "https://ddragon.leagueoflegends.com/cdn/" + "12.11.1" +
                          "/img/profileicon/" + profileIconId + ".png";
        Picasso.get().load(imageIconURL).into(imgInvIcon);

        // Colocando Região na View
        TextView textRegiao = (TextView) findViewById(R.id.textRegiao);
        textRegiao.setText(summonerRegion);
        textRegiao.setContentDescription("Região " + summonerRegion);

        // Colocando Level na View
        TextView textLevel = (TextView) findViewById(R.id.textLevel);
        textLevel.setText("NV." + summonerLevel);
        textLevel.setContentDescription("Nível " + textLevel);
    }

    public void voltarBuscaActivity(View view){
        onBackPressed();
    }
}