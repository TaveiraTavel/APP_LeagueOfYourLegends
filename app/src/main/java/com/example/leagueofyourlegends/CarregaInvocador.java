package com.example.leagueofyourlegends;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class CarregaInvocador extends AsyncTaskLoader<String> {
    private String queryRegiao;
    private String queryInvocador;
    private String queryAPI_KEY;
    public CarregaInvocador(Context context, String REGIAO, String NICKNAME, String API_KEY) {
        super(context);
        queryRegiao = REGIAO;
        queryInvocador = NICKNAME;
        queryAPI_KEY = API_KEY;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.buscaInvocador(queryRegiao, queryInvocador, queryAPI_KEY);
    }
}

