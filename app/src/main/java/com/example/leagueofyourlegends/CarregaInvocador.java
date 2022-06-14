package com.example.leagueofyourlegends;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class CarregaInvocador extends AsyncTaskLoader<String> {
    private String queryRegiao;
    private String queryInvocador;
    public CarregaInvocador(Context context, String REGIAO, String NICKNAME) {
        super(context);
        queryRegiao = REGIAO;
        queryInvocador = NICKNAME;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.buscaInvocador(queryRegiao, queryInvocador);
    }
}

