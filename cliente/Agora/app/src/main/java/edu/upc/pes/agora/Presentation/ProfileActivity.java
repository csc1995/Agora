package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import edu.upc.pes.agora.Logic.DrawerToggleAdvanced;
import edu.upc.pes.agora.Logic.GetTokenAsyncTask;
import edu.upc.pes.agora.Logic.NavMenuListener;
import edu.upc.pes.agora.Logic.Profile;
import edu.upc.pes.agora.R;

public class ProfileActivity extends AppCompatActivity {

    private Configuration config = new Configuration();
    private Locale locale;
    private JSONObject Jason = new JSONObject();
    private ImageButton editar;

    private TextView username, name, CP, Born, neigh;
    private Profile p;

    private String usernameJ, neighJ, nameJ, BornJ;
    private Integer CPJ;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navMenu);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);

        navigationView.getMenu().getItem(NavMenuListener.profile).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavMenuListener(this, drawer));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_profile);
        toolbar.setLogo(R.mipmap.ic_personw);
        setSupportActionBar(toolbar);

        DrawerToggleAdvanced toggle = new DrawerToggleAdvanced(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        editar = (ImageButton) findViewById(R.id.editarperfil);

        username = (TextView) findViewById(R.id.user);
        name = (TextView) findViewById(R.id.nameprofile);
        neigh = (TextView) findViewById(R.id.barrio);
        CP = (TextView) findViewById(R.id.codipostal);
        Born = (TextView) findViewById(R.id.born);

        @SuppressLint("SimpleDateFormat") final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        final String dateInString = "07/06/2013";

        new GetTokenAsyncTask("https://agora-pes.herokuapp.com/api/profile", this) {

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

                        Log.i("asdProfile", (jsonObject.toString()));

                        usernameJ = jsonObject.getString("username");
                        username.setText(usernameJ);
//                        p.setUsername(usernameJ);

                        if(jsonObject.has("realname")) {
                            nameJ = jsonObject.getString("realname");
                            name.setText(nameJ);
                            p.setName(nameJ);
                        }
                        else {
                            name.setText("");
                        }

                        if(jsonObject.has("neighbourhood")) {
                            neighJ = jsonObject.getString("neighbourhood");
                            neigh.setText(neighJ);
                            p.setNeighborhood(neighJ);
                        }
                        else {
                            neigh.setText("");
                        }

                        if(jsonObject.has("coCode")) {
                            CPJ = jsonObject.getInt("coCode");
                            CP.setText(CPJ);
                            p.setCP(CPJ);
                        }
                        else {
                            CP.setText("");
                        }

                        if(jsonObject.has("bdate")) {
                            BornJ = jsonObject.getString("bdate");
                            Born.setText(BornJ);
                            p.setBorn(BornJ);
                        }
                        else {
                            Born.setText("");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute(Jason);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                myIntent.putExtra("coCode", CPJ);
                myIntent.putExtra("barrio", neighJ);
                myIntent.putExtra("nombre", nameJ);
                myIntent.putExtra("fecha", BornJ);
                //myIntent.putExtra("sex", p.getSex());



                startActivity(myIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //TODO: posar-ho al MenuListener

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent refresh = new Intent(this, ProfileActivity.class);
        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //noinspection SimplifiableIfStatement
        if (id == R.id.men_castella) {
            locale = new Locale("es");
            config.locale = locale;
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();
        }

        else if (id == R.id.men_catala){
            locale = new Locale("ca");
            config.locale = locale;
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();

        }

        else if (id == R.id.men_angles){
            locale = new Locale("en");
            config.locale = locale;
            getResources().updateConfiguration(config, null);
            startActivity(refresh);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // TODO: el boto Back ha d'obrir el navigation drawer.
            drawer.openDrawer(GravityCompat.START);
        }
    }

}
