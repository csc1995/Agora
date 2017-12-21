package edu.upc.pes.agora.Presentation;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.upc.pes.agora.Logic.Listeners.DrawerToggleAdvanced;
import edu.upc.pes.agora.Logic.Listeners.NavMenuListener;
import edu.upc.pes.agora.Logic.ServerConection.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.R;

public class LogrosActivity extends AppCompatActivity {

    private List<String> logros = new ArrayList<>();
    private JSONObject Jason = new JSONObject();

    private String[] logros2;
    private String  itemLogro = "logro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navMenu);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        TextView headerUserName = (TextView) navigationView.findViewById(R.id.head_username);
        headerUserName.setText(Constants.Username);

        navigationView.getMenu().getItem(NavMenuListener.logros).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavMenuListener(this, drawer));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.logros);
        setSupportActionBar(toolbar);

        DrawerToggleAdvanced toggle = new DrawerToggleAdvanced(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        final ListView listView = (ListView) findViewById(R.id.lista);

        new GetTokenAsyncTask("https://agora-pes.herokuapp.com/api/profile/achievements", this) {


            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    if (jsonObject.has("error")) {
                        String error = jsonObject.get("error").toString();
                        Log.i("asdProfile", "Error");
                        Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    else {
                        if(jsonObject.has("achievements")){

                            JSONArray arrJson = jsonObject.getJSONArray("achievements");
                            logros2 = new String[arrJson.length()];
                            for(int i = 0; i < arrJson.length(); i++) {

                                itemLogro = codificaLogro(arrJson.getString(i));
                     //           logros.add(itemLogro);
                                if(itemLogro!="Something went wrong"){
                                 /*   if (i ==0)logros.set(i,itemLogro);
                                    else*/ logros.add(itemLogro);

                                }

                            }
                        }


                    }


                    ArrayAdapter<String> adaptador = new ArrayAdapter<String>(LogrosActivity.this, android.R.layout.simple_list_item_1, logros);
                    listView.setAdapter(adaptador);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(Jason);

       /* try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/

       /* logros.add("Crear 1 propuestaaaaaaaaaaa");
        logros.add("Crear 5 propuestasssssssssssssssss");
        logros.add("Crear 10 propuestasssssssssssssssssss");
        logros.add("Compartir 1 propuesta en Twitterrrrrrrrrrrrrrrrrrrrrrrrrrr");
        logros.add("Compartir 5 propuestas en Twitterrrrrrrrrrrrrrrrrrrr");
        logros.add("Compartir 10 propuestas en Twitterrrrrrrrrrrrrrrrrrrrrrrr");*/
        //logros.add(itemLogro);
      //  logros.add("Logros conseguidos por el usuario");
      //  logros.set(0,"cambiado");
        listView.setBackgroundColor(Color.WHITE);

        final List<Boolean> list = new ArrayList<Boolean>(Arrays.asList(new Boolean[10]));
        Collections.fill(list, Boolean.FALSE);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setBackgroundColor(Color.CYAN);
                Toast.makeText(getApplicationContext(), "Ha pulsado el item en posicion " + i, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Estado del boolean de la lista " + list.get(i), Toast.LENGTH_LONG).show();
           //     list.set(i,true);
           //     Toast.makeText(getApplicationContext(), "Estado del boolean de la lista version 2" + list.get(i), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LogrosActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_trophy, null);

                TextView textView = (TextView)mView.findViewById(R.id.textView);
                Button mAccept = (Button) mView.findViewById(R.id.etAccept);

                ImageView imageView = (ImageView) mView.findViewById(R.id.image);
                imageView.setImageResource(R.drawable.ic_trofeo_logro2);



                mBuilder.setView(mView);

                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                mAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    private String codificaLogro(String codigoLogro) {

        String Logro ="";
        switch(codigoLogro) {
            case "COM10": Logro = "Comenta 10 veces en una propuesta";
                    break;
            case "PROP100": Logro = "Crea 100 propuestas";
                break;
            default: Logro = "Something went wrong";
                    break;
        }
        return Logro;
    }

}
