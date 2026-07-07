package com.example.taller2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LinearLayout containerPreguntas;
    private ArrayList<Integer> listaIds = new ArrayList<Integer>();
    private ArrayList<EditText> listaRespuestas = new ArrayList<EditText>();
    private SurveyDbHelper dbHelper = new SurveyDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        containerPreguntas = findViewById(R.id.container_preguntas);
        cargarPreguntas();
    }

    public void listar(View vista) {
        Intent listar = new Intent(this, HistoryActivity.class);
        startActivity(listar);
    }

    public void cargarPreguntas() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                SurveyContract.PreguntasEntry.COLUMN_ID_PREG,
                SurveyContract.PreguntasEntry.COLUMN_TEXTO
        };
        String sortOrder = SurveyContract.PreguntasEntry.COLUMN_ID_PREG + " ASC";
        Cursor cursor = db.query(
                SurveyContract.PreguntasEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        while (cursor.moveToNext()) {
            int idPregunta = cursor.getInt(cursor.getColumnIndexOrThrow(SurveyContract.PreguntasEntry.COLUMN_ID_PREG));
            String textoPregunta = cursor.getString(cursor.getColumnIndexOrThrow(SurveyContract.PreguntasEntry.COLUMN_TEXTO));
            agregarPregunta(idPregunta, textoPregunta);
        }
        cursor.close();
        db.close();
    }

    public void agregarPregunta(int id, String texto) {
        TextView txtPregunta = new TextView(this);
        txtPregunta.setText(texto);
        txtPregunta.setTextSize(18);
        txtPregunta.setTextColor(Color.rgb(38, 50, 56));
        txtPregunta.setPadding(0, 16, 0, 8);
        containerPreguntas.addView(txtPregunta);

        EditText txtRespuesta = new EditText(this);
        txtRespuesta.setHint("Escriba su respuesta aqui");
        txtRespuesta.setTextColor(Color.rgb(38, 50, 56));
        txtRespuesta.setHintTextColor(Color.rgb(96, 125, 139));
        txtRespuesta.setBackgroundResource(R.drawable.fondo_campo);

        LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        parametros.setMargins(0, 0, 0, 6);
        containerPreguntas.addView(txtRespuesta, parametros);

        listaIds.add(id);
        listaRespuestas.add(txtRespuesta);
    }

    public void guardar(View vista) {
        for (int i = 0; i < listaRespuestas.size(); i++) {
            if (listaRespuestas.get(i).getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Por favor responda todas las preguntas", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean guardado = true;

        for (int i = 0; i < listaIds.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(SurveyContract.RespuestasEntry.COLUMN_ID_PREG_FK, listaIds.get(i));
            values.put(SurveyContract.RespuestasEntry.COLUMN_RESPUESTA, listaRespuestas.get(i).getText().toString());
            values.put(SurveyContract.RespuestasEntry.COLUMN_FECHA, fecha);
            long newRowId = db.insert(SurveyContract.RespuestasEntry.TABLE_NAME, null, values);
            if (newRowId == -1) {
                guardado = false;
            }
        }
        db.close();

        if (guardado) {
            Toast.makeText(this, "Encuesta guardada", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Error al guardar encuesta", Toast.LENGTH_SHORT).show();
        }
    }

    public void limpiarCampos() {
        for (int i = 0; i < listaRespuestas.size(); i++) {
            listaRespuestas.get(i).setText("");
        }
    }
}
