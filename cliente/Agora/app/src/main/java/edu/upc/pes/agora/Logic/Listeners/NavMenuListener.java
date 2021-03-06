package edu.upc.pes.agora.Logic.Listeners;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upc.pes.agora.Logic.Utils.Constants;
import edu.upc.pes.agora.Logic.Utils.Helpers;
import edu.upc.pes.agora.Logic.ServerConection.DeleteAsyncTask;
import edu.upc.pes.agora.Presentation.AboutActivity;
import edu.upc.pes.agora.Presentation.HelpActivity;
import edu.upc.pes.agora.Presentation.LoginActivity;
import edu.upc.pes.agora.Presentation.LogrosActivity;
import edu.upc.pes.agora.Presentation.MainActivity;
import edu.upc.pes.agora.Presentation.MyFavoritesActivity;
import edu.upc.pes.agora.Presentation.MyProposalsActivity;
import edu.upc.pes.agora.Presentation.MyProfileActivity;
import edu.upc.pes.agora.R;

public class NavMenuListener implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private DrawerLayout navDrawer;
    private static boolean activityChanged;

    public static final int homneButton = 0;
    public static final int myproposals = 1;
    public static final int favorite = 2;
    public static final int profile = 3;
    public static final int logros = 4;
    public static final int help = 5;
    public static final int about = 6;

    public NavMenuListener(Context context, DrawerLayout navDrawer) {
        this.context = context;
        this.navDrawer = navDrawer;
    }

    public static boolean getActivityChanged() {
        if (activityChanged) {
            activityChanged = false;
            return true;
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Va a la pagina principal on surten totes les propostes
            if (!context.getClass().equals(MainActivity.class)) {
                Intent myIntent = new Intent(context, MainActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();

        } else if (id == R.id.nav_myporposals) {
            // Va a la pantalla de MyProposals
            if (!context.getClass().equals(MyProposalsActivity.class)) {
                Intent myIntent = new Intent(context, MyProposalsActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();

        } else if (id == R.id.nav_favorites) {
            // Va a la pantalla de Favorites
            if (!context.getClass().equals(MyFavoritesActivity.class)) {
                Intent myIntent = new Intent(context, MyFavoritesActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();

        } else if (id == R.id.nav_perfilprinc) {
            // Va a la Pagina principal del perfil
            if (!context.getClass().equals(MyProfileActivity.class)) {
                Intent myIntent = new Intent(context, MyProfileActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();

        } else if (id == R.id.nav_logros) {
            // Va a la Pagina principal del perfil
            if (!context.getClass().equals(LogrosActivity.class)) {
                Intent myIntent = new Intent(context, LogrosActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();

        } else if (id == R.id.nav_help) {

            if (!context.getClass().equals(HelpActivity.class)) {
                Intent myIntent = new Intent(context, HelpActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();

        } else if (id == R.id.nav_about) {

            if (!context.getClass().equals(AboutActivity.class)) {
                Intent myIntent = new Intent(context, AboutActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();

        } else if (id == R.id.nav_logout) {
            // Es desasigna el token per sortir de l'aplicació
            JSONObject jObject = new JSONObject();

            new DeleteAsyncTask("https://agora-pes.herokuapp.com/api/logout", context) {
                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    try {
                        if(jsonObject.has("error")) {
                            String error = jsonObject.get("error").toString();
                            Log.i("asd123", error);
                        }
                        else {
                            Constants.SH_PREF_NAME = "";
                            Constants.fotoperfil = null;
                            Intent myIntent = new Intent(context, LoginActivity.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(myIntent);
                        }
                    } catch (JSONException ignored) {
                        Log.i("DEBUG","error al get user");
                    }
                }
            }.execute(jObject);
        }

        activityChanged = true;
        return true;
    }
}