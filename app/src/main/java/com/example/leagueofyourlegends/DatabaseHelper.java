package com.example.leagueofyourlegends;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dbPesquisa";

    //TABELA HISTORICO DO INVOCADOR
    public static final String HISTINVOCADOR_TABLE_NAME = "tbHistInvocador";
    public static final String COLUMN_IDSUM = "summonerId";
    public static final String COLUMN_NICKNAME = "nickname";
    public static final String COLUMN_REGIAOID = "regiaoId";

    //TABELA REGIAO
    public static final String REGIAO_TABLE_NAME = "tbRegiao";
    public static final String COLUMN_IDREG = "regiaoId";
    public static final String COLUMN_NOME_REGIAO = "regiao";

    //TABELA CAMPEAO
    public static final String CAMPEAO_TABLE_NAME = "tbCampeao";
    public static final String COLUMN_CHAMPIONKEY = "championKey";
    public static final String COLUMN_NOME_CAMPEAO = "nome";


    //CONSTRUTOR
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //CRIANDO TABELA HISTORICO
        String CREATE_HISTINVOCADOR_TABLE = "CREATE TABLE " + HISTINVOCADOR_TABLE_NAME + "("
                + COLUMN_IDSUM + " TEXT(63) PRIMARY KEY,"
                + COLUMN_NICKNAME + " TEXT(16),"
                + COLUMN_REGIAOID + " INTEGER,"
                + "FOREIGN KEY (" + COLUMN_REGIAOID + ") REFERENCES " + REGIAO_TABLE_NAME + "(" + COLUMN_IDREG + ")"
                + ")";

        //CRIANDO TABELA REGIAO
        String CREATE_REGIAO_TABLE = "CREATE TABLE " + REGIAO_TABLE_NAME + "("
                + COLUMN_REGIAOID + " INTEGER PRIMARY KEY,"
                + COLUMN_NOME_REGIAO + " TEXT(4) UNIQUE"
                + ")";

        //CRIANDO TABELA CAMPEAO
        String CREATE_CAMPEAO_TABLE = "CREATE TABLE " + CAMPEAO_TABLE_NAME + "("
                + COLUMN_CHAMPIONKEY + " INTEGER PRIMARY KEY,"
                + COLUMN_NOME_CAMPEAO + " TEXT(16) UNIQUE"
                + ")";

        //EXECUTANDO AS QUERYS
        db.execSQL(CREATE_HISTINVOCADOR_TABLE);
        db.execSQL(CREATE_REGIAO_TABLE);
        db.execSQL(CREATE_CAMPEAO_TABLE);

        //FAZENDO AS INSERÇÕES NA TABELA REGIAO
        db.execSQL("INSERT INTO " + REGIAO_TABLE_NAME
                + "("
                + COLUMN_NOME_REGIAO
                + ") VALUES('BR1'" +
                ")"
        );

        db.execSQL("INSERT INTO " + REGIAO_TABLE_NAME
                + "("
                + COLUMN_NOME_REGIAO
                + ") VALUES('EUN1'" +
                ")"
        );

        db.execSQL("INSERT INTO " + REGIAO_TABLE_NAME
                + "("
                + COLUMN_NOME_REGIAO
                + ") VALUES('EUW1'" +
                ")"
        );

        db.execSQL("INSERT INTO " + REGIAO_TABLE_NAME
                + "("
                + COLUMN_NOME_REGIAO
                + ") VALUES('LA1'" +
                ")"
        );

        db.execSQL("INSERT INTO " + REGIAO_TABLE_NAME
                + "("
                + COLUMN_NOME_REGIAO
                + ") VALUES('LA2'" +
                ")"
        );

        db.execSQL("INSERT INTO " + REGIAO_TABLE_NAME
                + "("
                + COLUMN_NOME_REGIAO
                + ") VALUES('NA1'" +
                ")"
        );

        db.execSQL("INSERT INTO " + REGIAO_TABLE_NAME
                + "("
                + COLUMN_NOME_REGIAO
                + ") VALUES('OC1'" +
                ")"
        );

        db.execSQL("INSERT INTO " + REGIAO_TABLE_NAME
                + "("
                + COLUMN_NOME_REGIAO
                + ") VALUES('RU'" +
                ")"
        );

        db.execSQL("INSERT INTO " + REGIAO_TABLE_NAME
                + "("
                + COLUMN_NOME_REGIAO
                + ") VALUES('TR1'" +
                ")"
        );

        db.execSQL("INSERT INTO " + REGIAO_TABLE_NAME
                + "("
                + COLUMN_NOME_REGIAO
                + ") VALUES('JP1'" +
                ")"
        );

        db.execSQL("INSERT INTO " + REGIAO_TABLE_NAME
                + "("
                + COLUMN_NOME_REGIAO
                + ") VALUES('KR'" +
                ")"
        );


        //FAZENDO AS INSERÇÕES NA TABELA CAMPEÃO
        //fazer depois.


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HISTINVOCADOR_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + REGIAO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CAMPEAO_TABLE_NAME);
        onCreate(db);
    }


    public ArrayList<String> getRegioes() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT " + COLUMN_NOME_REGIAO + " FROM " + REGIAO_TABLE_NAME, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            int indice = cursor.getColumnIndex(COLUMN_NOME_REGIAO);
            array_list.add(cursor.getString(indice));
            cursor.moveToNext();
        }
        return array_list;
    }
}
