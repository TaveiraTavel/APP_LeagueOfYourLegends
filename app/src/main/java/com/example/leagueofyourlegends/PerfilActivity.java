package com.example.leagueofyourlegends;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PerfilActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }

        Bundle extras = getIntent().getExtras();
        String NICKNAME = extras.getString("invNickname");
        String REGIAO = extras.getString("invRegiao");

        TextView textNickname = (TextView) findViewById(R.id.textNickname);
        textNickname.setText(NICKNAME);
        textNickname.setContentDescription(NICKNAME);

        // API Trabajo.

            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", NICKNAME);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);


    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}