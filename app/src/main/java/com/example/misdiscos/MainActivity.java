package com.example.misdiscos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText txtGrupo,txtDisco;
    ListView listaDiscos;
    //Objeto de tipo SQLite
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtGrupo=(EditText)findViewById(R.id.txtGrupo);
        txtDisco=(EditText)findViewById(R.id.txtDisco);
        listaDiscos=(ListView)findViewById(R.id.listaDiscos);
        //Crea la base de datos y asigna una instancia
        db=openOrCreateDatabase("MisDisco", Context.MODE_PRIVATE, null);
        //Crea una tabla llamada mis discos
        db.execSQL("CREATE TABLE IF NOT EXISTS misDiscos(Grupo VARCHAR,Disco VARCHAR);");
        Listar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Método que añade un disco
     * @param v
     */
    public void Añadir(View v){
        //Se inserta un disco
        db.execSQL("INSERT INTO MisDiscos VALUES ('"+txtGrupo.getText().toString()+"','"+
                txtDisco.getText().toString()+"')");
        Toast.makeText(this,"Se añadió el disco "+txtDisco.getText().toString(),Toast.LENGTH_LONG).show();
        Listar();
    }

    /**
     * Método que borar un disco
     * @param v
     */
    public void Borrar(View v){
        try {
            db.execSQL("DELETE FROM MisDiscos WHERE Grupo = '" + txtGrupo.getText().toString() + "' AND Disco='" +
                    txtDisco.getText().toString() + "'");
            Toast.makeText(this, "Se borró el disco " + txtDisco.getText().toString(), Toast.LENGTH_LONG).show();
        }
        catch(SQLException s){
            Toast.makeText(this, "Error al borrar!", Toast.LENGTH_LONG).show();
        }
        Listar();
    }
    public void Listar(){
        ArrayAdapter<String> adaptador;
        List<String> lista = new ArrayList<String>();
        //se declara un cursor para recorrer los discos
        Cursor c=db.rawQuery("SELECT * FROM MisDiscos", null);
        //Si no hay ningún disco
        if(c.getCount()==0)
            lista.add("No hay registros");
        else{
            //Recorre el cursor
            while(c.moveToNext())
                lista.add(c.getString(0)+"-"+c.getString(1));
        }
        //Se instancia el adaptador con la lista de discos
        adaptador=new ArrayAdapter<String>(getApplicationContext(),R.layout.lista_fila,lista);
        //Se fija el adaptador en la lista
        listaDiscos.setAdapter(adaptador);
    }
}
