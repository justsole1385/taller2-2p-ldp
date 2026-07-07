package com.example.taller2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private ListView lvHistorial;
    private ArrayList<String> datos = new ArrayList<String>();
    private SurveyDbHelper dbHelper = new SurveyDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lvHistorial = findViewById(R.id.lv_historial);
        TraerDatos();
    }

    public void TraerDatos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursorFechas = db.rawQuery("SELECT DISTINCT " +
                SurveyContract.RespuestasEntry.COLUMN_FECHA +
                " FROM " + SurveyContract.RespuestasEntry.TABLE_NAME +
                " ORDER BY " + SurveyContract.RespuestasEntry.COLUMN_FECHA + " DESC", null);

        while (cursorFechas.moveToNext()) {
            String fecha = cursorFechas.getString(cursorFechas.getColumnIndexOrThrow(SurveyContract.RespuestasEntry.COLUMN_FECHA));
            String resumen = "Fecha: " + fecha + "\n";

            Cursor cursorRespuestas = db.rawQuery("SELECT p." + SurveyContract.PreguntasEntry.COLUMN_TEXTO +
                    ", r." + SurveyContract.RespuestasEntry.COLUMN_RESPUESTA +
                    " FROM " + SurveyContract.RespuestasEntry.TABLE_NAME + " r INNER JOIN " +
                    SurveyContract.PreguntasEntry.TABLE_NAME + " p ON r." +
                    SurveyContract.RespuestasEntry.COLUMN_ID_PREG_FK + " = p." +
                    SurveyContract.PreguntasEntry.COLUMN_ID_PREG +
                    " WHERE r." + SurveyContract.RespuestasEntry.COLUMN_FECHA + " = ?", new String[]{fecha});

            while (cursorRespuestas.moveToNext()) {
                resumen = resumen + cursorRespuestas.getString(0) + ": " + cursorRespuestas.getString(1) + "\n";
            }
            cursorRespuestas.close();
            datos.add(resumen);
        }
        cursorFechas.close();
        db.close();

        if (datos.size() == 0) {
            datos.add("No hay encuestas guardadas");
        }

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos);
        lvHistorial.setAdapter(adaptador);
    }

    public void regresar(View vista) {
        Intent regresar = new Intent(this, MainActivity.class);
        startActivity(regresar);
    }
}
