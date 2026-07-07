package com.example.taller2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SurveyDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Encuestas.db";

    public SurveyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SurveyContract.SQL_CREATE_PREGUNTAS);
        db.execSQL(SurveyContract.SQL_CREATE_RESPUESTAS);

        db.execSQL("INSERT INTO " + SurveyContract.PreguntasEntry.TABLE_NAME +
                " (" + SurveyContract.PreguntasEntry.COLUMN_ID_PREG + ", " +
                SurveyContract.PreguntasEntry.COLUMN_TEXTO + ") VALUES (1, 'Como califica la atencion recibida?')");
        db.execSQL("INSERT INTO " + SurveyContract.PreguntasEntry.TABLE_NAME +
                " (" + SurveyContract.PreguntasEntry.COLUMN_ID_PREG + ", " +
                SurveyContract.PreguntasEntry.COLUMN_TEXTO + ") VALUES (2, 'Recomendaria nuestro servicio?')");
        db.execSQL("INSERT INTO " + SurveyContract.PreguntasEntry.TABLE_NAME +
                " (" + SurveyContract.PreguntasEntry.COLUMN_ID_PREG + ", " +
                SurveyContract.PreguntasEntry.COLUMN_TEXTO + ") VALUES (3, 'El tiempo de respuesta fue adecuado?')");
        db.execSQL("INSERT INTO " + SurveyContract.PreguntasEntry.TABLE_NAME +
                " (" + SurveyContract.PreguntasEntry.COLUMN_ID_PREG + ", " +
                SurveyContract.PreguntasEntry.COLUMN_TEXTO + ") VALUES (4, 'Que aspecto considera que debemos mejorar?')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SurveyContract.SQL_DELETE_PREGUNTAS);
        db.execSQL(SurveyContract.SQL_DELETE_RESPUESTAS);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
