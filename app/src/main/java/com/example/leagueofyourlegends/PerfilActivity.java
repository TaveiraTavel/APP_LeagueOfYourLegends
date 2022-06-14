package com.example.leagueofyourlegends;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Pegando dados da tela anterior
        Bundle extras = getIntent().getExtras();

        String nickname = extras.getString("nickname");
        String regiao = extras.getString("regiao");
        int iconId = extras.getInt("iconId");
        int level = extras.getInt("level");

        String tier = extras.getString("tier");
        String rank = extras.getString("rank");
        int points = extras.getInt("points");
        int wins = extras.getInt("wins");
        int losses = extras.getInt("losses");
        Boolean hotStreak = extras.getBoolean("hotStreak");
        Boolean inactive = extras.getBoolean("inactive");

        String championName = null;
        String championImage = null;
        int championLevel;
        int championPoints;

        try {
            JSONArray campeoesJSONArray = new JSONArray(extras.getString("topChampionMastery"));
            for (int i = 0; i <= 2; i++){
                JSONObject campeaoJSONObject = new JSONObject(campeoesJSONArray.getString(i));
                championName = campeaoJSONObject.getString("championName");
                championImage = campeaoJSONObject.getString("championImage");
                championLevel = campeaoJSONObject.getInt("championLevel");
                championPoints = campeaoJSONObject.getInt("championPoints");

                // Colocando o nome do campeão na View
                TextView textChampion = (TextView) findViewById(getResources().getIdentifier("textChampion" + i+1, "id", getPackageName()));
                textChampion.setText(championName);
                textChampion.setContentDescription(championName);

                // Colocando q imagem do campeão na View
                ImageView imgChampion = (ImageView) findViewById(getResources().getIdentifier("imgChampion" + i+1, "id", getPackageName()));
                String imageChampURL = "https://ddragon.leagueoflegends.com/cdn/12.11.1/img/champion/" + championImage + ".png";
                Picasso.get().load(imageChampURL).into(imgChampion);

                // Colocando a maestria do campeão na View
                ImageView imgMaestria = (ImageView) findViewById(getResources().getIdentifier("imgMaestria" + i+1, "id", getPackageName()));
                imgMaestria.setBackgroundResource(getResources().getIdentifier("master" + championLevel, "drawable", getPackageName()));

                // Colocando os pontos de maestria na View
                TextView textPoints = (TextView) findViewById(getResources().getIdentifier("textPoints" + i+1, "id", getPackageName()));
                textChampion.setText(championPoints);
                textChampion.setContentDescription("Pontos:" + championPoints);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Colocando Nickname na View
        TextView textNickname = (TextView) findViewById(R.id.textNickname);
        textNickname.setText(nickname);
        textNickname.setContentDescription(nickname);

        // Colocando Icone na View
        ImageView imgInvIcon = (ImageView) findViewById(R.id.imgInvIcon);
        String imageIconURL = "https://ddragon.leagueoflegends.com/cdn/" + "12.11.1" +
                          "/img/profileicon/" + iconId + ".png";
        Picasso.get().load(imageIconURL).into(imgInvIcon);

        // Colocando Região na View
        TextView textRegiao = (TextView) findViewById(R.id.textRegiao);
        textRegiao.setText(regiao);
        textRegiao.setContentDescription("Região " + regiao);

        // Colocando Level na View
        TextView textLevel = (TextView) findViewById(R.id.textLevel);
        textLevel.setText("NV." + level);
        textLevel.setContentDescription("Nível " + textLevel);

        // Colocando Emblema na View
        ImageView imgInvTier = (ImageView) findViewById(R.id.imgInvTier);
        imgInvTier.setBackgroundResource(getResources().getIdentifier("emblem_" + tier.toLowerCase(), "drawable", getPackageName()));

        // Colocando Tier na View
        TextView textInvTier = (TextView) findViewById(R.id.textInvTier);
        textInvTier.setText(tier);
        textInvTier.setContentDescription(tier);

        // Colocando Rank na View
        TextView textInvRank = (TextView) findViewById(R.id.textInvRank);
        textInvRank.setText(rank);
        textInvRank.setContentDescription(rank);

        // Colocando LP na View
        TextView textInvLp = (TextView) findViewById(R.id.textInvLp);
        textInvLp.setText("LP: " + points);
        textInvLp.setContentDescription("Pontos de Liga: " + points);

        // Colocando Partidas na View
        PieChart piePartidas = (PieChart) findViewById(R.id.piePartidas);
        piePartidas.addPieSlice(new PieModel("Partidas", 100, Color.parseColor("#836840")));
        piePartidas.startAnimation();

        TextView textPartidas = (TextView) findViewById(R.id.textPartidas);
        float partidas = wins + losses;
        textPartidas.setText(String.format("%.0f", partidas));
        textPartidas.setContentDescription(String.valueOf(wins + losses) + " partidas");

        // Colocando Winrate na View
        PieChart pieWinrate = (PieChart) findViewById(R.id.pieWinrate);
        pieWinrate.addPieSlice(new PieModel("Derrotas", losses, Color.parseColor("#979797")));
        pieWinrate.addPieSlice(new PieModel("Vitórias", wins, Color.parseColor("#836840")));
        pieWinrate.startAnimation();

        TextView textWinrate = (TextView) findViewById(R.id.textWinrate);
        float winrate = (wins / partidas) * 100;
        textWinrate.setText(String.format("%.2f", winrate) + "%");
        textWinrate.setContentDescription("Winrate de " + String.valueOf(winrate) + "%");

        // Colocando Hotstreak
        LinearLayout linearHotstreak = (LinearLayout) findViewById(R.id.linearHotstreak);
        if (!hotStreak){
            linearHotstreak.setVisibility(View.GONE);
        }

        // Colocando Inativo
        LinearLayout linearInactive = (LinearLayout) findViewById(R.id.linearInactive);
        if (!inactive){
            linearInactive.setVisibility(View.GONE);
        }
    }

    public void voltarBuscaActivity(View view) {
        onBackPressed();
    }
}