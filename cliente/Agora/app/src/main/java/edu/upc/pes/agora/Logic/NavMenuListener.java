package edu.upc.pes.agora.Logic;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import edu.upc.pes.agora.Presentation.LoginActivity;
import edu.upc.pes.agora.Presentation.MainActivity;
import edu.upc.pes.agora.Presentation.MyProposalsActivity;
import edu.upc.pes.agora.Presentation.ProfileActivity;
import edu.upc.pes.agora.Presentation.ProposalsActivity;
import edu.upc.pes.agora.R;

public class NavMenuListener implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    private DrawerLayout navDrawer;
    private static boolean activityChanged;

    public static final int homneButton = 0;
    public static final int addproposalbutton = 1;
    public static final int myproposals = 2;
    public static final int profile = 3;

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

        } else if (id == R.id.nav_addproposta) {
            if (!context.getClass().equals(ProposalsActivity.class)) {
                Intent myIntent = new Intent(context, ProposalsActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();

            //  Va a Proposals
        } else if (id == R.id.nav_myporposals) {
            if (!context.getClass().equals(MyProposalsActivity.class)) {
                Intent myIntent = new Intent(context, MyProposalsActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();
        } else if (id == R.id.nav_perfilprinc) {
            // Va a la Pagina principal del perfil
            if (!context.getClass().equals(ProfileActivity.class)) {
                Intent myIntent = new Intent(context, ProfileActivity.class);
                context.startActivity(myIntent);
            }
            navDrawer.closeDrawers();
        }  else if (id == R.id.nav_logout) {
            // Logout --> TODO: Desasignacion de token

            new DeleteAsyncTask("https://agora-pes.herokuapp.com/api/proposal/user", context) {

            }.execute();


            Helpers.logout(context);

            if (!context.getClass().equals(LoginActivity.class)) {
                Intent myIntent = new Intent(context, LoginActivity.class);
                context.startActivity(myIntent);
            }
        }

        activityChanged = true;
        return true;
    }
}