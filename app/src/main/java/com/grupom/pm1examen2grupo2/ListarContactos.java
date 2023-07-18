package com.grupom.pm1examen2grupo2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.grupom.pm1examen2grupo2.Config.CustomAdapter;
import com.grupom.pm1examen2grupo2.Config.Usuario;
import com.grupom.pm1examen2grupo2.Config.RestApiMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

public class ListarContactos extends AppCompatActivity {

    private List<Usuario> mLista = new ArrayList<>();
    ListView listUsuario;
    List<Usuario> usuarioList;
    ArrayList<String> arrayUsuario;
    Button btnActualizar, btnAtras, btnEliminar, btnUbicacion;
    Usuario usuario;
    EditText buscar;
    ArrayAdapter adp;
    private static CustomAdapter adaptercustom;
    int contador = 0;
    public String imagenString = "";
    public Bitmap imagen;

    Intent intent;


    int previousPosition = 1;
    int count=1;
    long previousMil=0;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_contactos);

        listUsuario = (ListView) findViewById(R.id.listaContacto);
        //intent = new Intent(getApplicationContext(),ListarContactos.class);//para obtener el contacto seleccionado mas adelante


        usuarioList = new ArrayList<>();
        arrayUsuario = new ArrayList<String>();

        btnActualizar = (Button) findViewById(R.id.alcbtnActualizar);
        btnAtras = (Button) findViewById(R.id.alcbtnAtras);
        btnEliminar = (Button) findViewById(R.id.alcbtnEliminar);
        btnUbicacion = (Button) findViewById(R.id.btnUbicacion);

        buscar = (EditText) findViewById(R.id.alctxtbuscar);

        listarUsuarios();

        try{
            listUsuario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(ListarContactos.this, usuarioList.get(i).getNombre(), Toast.LENGTH_SHORT).show();
                    Usuario User = usuarioList.get(i);

                    if (contador<1 || User.isSeleccionado()) {
                        if (User.isSeleccionado()) {
                            User.setSeleccionado(false);
                            contador--;
                            Toast.makeText(ListarContactos.this, "Seleccionado: " + contador, Toast.LENGTH_SHORT).show();
                        } else {
                            User.setSeleccionado(true);
                            contador++;
                            Toast.makeText(ListarContactos.this, "Seleccionado: " + contador, Toast.LENGTH_SHORT).show();

                        }
                        usuario = usuarioList.get(i);
                        usuarioList.set(i, User);
                        adaptercustom.update(usuarioList);
                    }

                }
            });

        }catch (Exception e){
            Log.i("LOG",e.getMessage());
        }



        //------------------------------EVENTOS BOTONES Y BUSCAR------------------------------------
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    BuscarUsuario(buscar.getText().toString());
                    if (buscar.getText().toString().equals("")){
                        listarUsuarios();
                    }
                } catch (Exception ex){
                    Toast.makeText(getApplicationContext(),"Valor invalido",Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActualizarContactos.class);
                intent.putExtra("id", usuario.getId()+"");
                intent.putExtra("nombre", usuario.getNombre()+"");
                intent.putExtra("telefono", usuario.getTelefono()+"");
                intent.putExtra("latitud", usuario.getLatitud()+"");
                intent.putExtra("longitud", usuario.getLongitud()+"");
                intent.putExtra("imagen", Base64.decode(usuario.getImagen(), Base64.DEFAULT));

                startActivity(intent);

            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Usuario.class);
                startActivity(intent);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set title

                alertDialogBuilder.setTitle("Eliminar Usuario");


                // set dialog message
                alertDialogBuilder
                        .setMessage("¿Está seguro de eliminar el usuario "+usuario.getNombre()+"?")
                        .setCancelable(false)
                        .setPositiveButton("SI",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // si el usuario da click en si procede a llamar el metodo de eliminar
                                eliminarUsuario(String.valueOf(usuario.getId()));
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        btnUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("latitud", usuario.getLatitud());
                intent.putExtra("longitud", usuario.getLongitud());
                startActivity(intent);
            }
        });

        //-- lista evento click
       /* listUsuario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(check.isChecked()){
                    isSelected = true;
                    Log.i("LOG", "TRUE");
                }else{
                    isSelected = false;
                    Log.i("LOG", "FALSE");
                }

                String nombre = usuarioList.get(i).getNombre();
                Log.i("LOG", nombre);
                Toast.makeText(getApplicationContext(), nombre,Toast.LENGTH_SHORT).show();

                if(previousPosition==i)
                {
                    count++;
                    if(count==2 && System.currentTimeMillis()-previousMil<=1000)
                    {
                        //Toast.makeText(getApplicationContext(), "Doble Click ",Toast.LENGTH_LONG).show();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle("Acción");
                        alertDialogBuilder
                                .setMessage("¿Desea ir a la Ubicacion de "+usuario.getNombre()+"?")
                                .setCancelable(false)
                                .setPositiveButton("SI",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // se procede a ir a la ubicacion seteando los parametros
                                        try{
                                            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                                            intent.putExtra("latitud", usuario.getLatitud());
                                            intent.putExtra("longitud", usuario.getLongitud());
                                            startActivity(intent);

                                        }catch (Exception ex){
                                            ex.toString();
                                        }
                                    }
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        count=1;
                    }
                }
                else
                {
                    previousPosition=i;
                    count=1;
                    previousMil=System.currentTimeMillis();
                    //un clic
                    usuario = usuarioList.get(i);//lleno la lista de contacto
                    setUsuarioSeleccionado();//obtengo el usuario seleccionado de la lista
                    //Toast.makeText(getApplicationContext(),"usuario id: "+usuario.getId(), Toast.LENGTH_SHORT).show();
                }
            }


        });*/



    }
    //-----------------------------------METODOS---------------------------------------



    private void listarUsuarios() {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, RestApiMethods.apiGetList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray contactoArray = jsonObject.getJSONArray( "contactos");

                            arrayUsuario.clear();//limpiar la lista de usuario antes de comenzar a listar
                            usuarioList.clear();
                            contador = 0;
                            for (int i=0; i<contactoArray.length(); i++)
                            {
                                JSONObject RowUsuario = contactoArray.getJSONObject(i);
                                usuario = new Usuario(  RowUsuario.getInt("id"),
                                        RowUsuario.getString("nombre"),
                                        RowUsuario.getInt("telefono"),
                                        RowUsuario.getString("latitud"),
                                        RowUsuario.getString("longitud"),
                                        RowUsuario.getString("imagen")
                                );

                                usuarioList.add(usuario);
                                arrayUsuario.add(usuario.getNombre()+' '+usuario.getTelefono());
                            }

                            adaptercustom = new CustomAdapter(usuarioList, getApplicationContext());

                            // ArrayAdapter adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, autoLista);
                            listUsuario.setAdapter(adaptercustom);

                          //  adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_checked, arrayUsuario);
                          //  listUsuario.setAdapter(adp);

                        }catch (JSONException ex){
                            Toast.makeText(getApplicationContext(), "mensaje "+ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(), "mensaje "+error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void BuscarUsuario(String dato) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, RestApiMethods.apiFindContact+dato,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray contactoArray = jsonObject.getJSONArray( "contacto");

                            arrayUsuario.clear();//limpiar la lista de usuario antes de comenzar a buscar
                            usuarioList.clear();
//                            if ()){
//                                Toast.makeText(getApplicationContext(), "No se encontro el valor", Toast.LENGTH_SHORT).show();
//                            }

                            for (int i=0; i<contactoArray.length(); i++)
                            {
                                JSONObject RowUsuario = contactoArray.getJSONObject(i);
                                Usuario usuario = new Usuario(  RowUsuario.getInt("id"),
                                        RowUsuario.getString("nombre"),
                                        RowUsuario.getInt("telefono"),
                                        RowUsuario.getString("latitud"),
                                        RowUsuario.getString("longitud"),
                                        RowUsuario.getString("imagen")
                                );
                                usuarioList.add(usuario);
                                arrayUsuario.add(usuario.getNombre()+' '+usuario.getTelefono());
                            }

                            adaptercustom = new CustomAdapter(usuarioList, getApplicationContext());
                            // ArrayAdapter adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, autoLista);
                            listUsuario.setAdapter(adaptercustom);

                        }catch (JSONException ex){
                            Toast.makeText(getApplicationContext(), "mensaje "+ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(), "No hay datos a mostrar", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void eliminarUsuario(String dato) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = RestApiMethods.apiDeleteContact;
        Log.i("LOG", url+dato);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+dato,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Usuario eliminado exitosamente", Toast.LENGTH_SHORT).show();
                        listarUsuarios();
                        adaptercustom.update(usuarioList);
                        buscar.setText("");
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(), "mensaje "+error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }



    private void setUsuarioSeleccionado() {

        //contacto = listaContactos.get(id);
        //intent = new Intent(getApplicationContext(),ActivityActualizarContacto.class);
        intent.putExtra("id", usuario.getId()+"");
        intent.putExtra("nombre", usuario.getNombre());
        intent.putExtra("telefono", usuario.getTelefono()+"");
        intent.putExtra("latitud", usuario.getLatitud());
        intent.putExtra("longitud", usuario.getLongitud());
        intent.putExtra("firma2", usuario.getImagen()+"").toString();

        //startActivity(intent);
    }

    private void mostrarAlertaOK(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ListarContactos.this);
        builder.setMessage(mensaje)
                .setTitle("Alerta")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private Bitmap ConvertBase64toImage(String Base64String)
    {
        //String base64Image  = Base64String.split(",")[1];
        String base64Image  = Base64String;
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}