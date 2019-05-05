package id.co.cng.spj;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.co.cng.spj.fragment.CekGeneratorFragment;
import id.co.cng.spj.fragment.Fragment_Compressor;
import id.co.cng.spj.fragment.Fragment_Customer;
import id.co.cng.spj.fragment.Fragment_SpjBerangkat;
import id.co.cng.spj.fragment.Fragment_SpjTiba;
import id.co.cng.spj.fragment.Fragment_TBerangkat;
import id.co.cng.spj.fragment.Fragment_TKembali;
import id.co.cng.spj.fragment.Fragment_Trailer;
import id.co.cng.spj.fragment.Fragment_UbahPassword;
import id.co.cng.spj.fragment.HomeFragment;
import id.co.cng.spj.fragment.KaloriGasFragment;
import id.co.cng.spj.fragment.PengisianGasInactiveFragment;

import id.co.cng.spj.fragment.PengisianGasActiveFragment;
import id.co.cng.spj.utility.ConnectionHelper;
import id.co.cng.spj.utility.PrefManager;

public class MenuUtamaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment newFragment = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_frame, newFragment);
            ft.addToBackStack(null);
            ft.commit();
        }

//        MenuItem item = menu.findItem(R.id.addAction);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        prefManager = new PrefManager(this);

        setActionBarTitle("Home");
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            finish();
//        }
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {

            getFragmentManager().popBackStackImmediate();
        } else {

            finish();
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_home) {
            //fragment = new MainActivity();
            fragment = new HomeFragment();
            // Handle the camera action
        } else if (id == R.id.nav_generator) {
            fragment = new CekGeneratorFragment();
        } else if (id == R.id.nav_compressor) {
            fragment = new Fragment_Compressor();
        } else if (id == R.id.nav_pengisian_gas_inactive) {
            fragment = new PengisianGasInactiveFragment();
        } else if (id == R.id.nav_pengisian_gas_active) {
            fragment = new PengisianGasActiveFragment();
        } else if (id == R.id.nav_spj_berangkat) {
            fragment = new Fragment_SpjBerangkat();
        } else if (id == R.id.nav_spj_kembali) {
            fragment = new Fragment_SpjTiba();
        } else if (id == R.id.nav_berangkat) {
            fragment = new Fragment_TBerangkat();
        } else if (id == R.id.nav_kembali) {
            fragment = new Fragment_TKembali();
        } else if (id == R.id.nav_trailer) {
            fragment = new Fragment_Trailer();
        } else if (id == R.id.nav_customer_check) {
            fragment = new Fragment_Customer();
        } else if (id == R.id.nav_kalori_gas) {
            fragment = new KaloriGasFragment();

        } else if (id == R.id.nav_ubah_password) {
            fragment = new Fragment_UbahPassword();
        } else if (id == R.id.nav_Logout) {
            prefManager.saveBoolean("login", false);
            Intent myIntent = new Intent(MenuUtamaActivity.this, MainActivity.class);
            MenuUtamaActivity.this.startActivity(myIntent);
            LogoutKaryawan logoutKaryawan = new LogoutKaryawan();
            logoutKaryawan.execute("");

        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class LogoutKaryawan extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        ProgressDialog progress = new ProgressDialog(MenuUtamaActivity.this);

        @Override
        protected void onPreExecute()
        {
            progress.setTitle("Loading");
            progress.setMessage("Harap tunggu");
            progress.setCancelable(false);
            progress.show();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            String date_time = simpleDateFormat.format(calendar.getTime());

            prefManager.saveString("tanggal_logout_karyawan", date_time);

            //Toast.makeText(getActivity(), prefManager.catchString("pengisian_filling1_timeAwal"), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String r)
        {
            progress.dismiss();
            Toast.makeText(MenuUtamaActivity.this, r, Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                ConnectionHelper cons = new ConnectionHelper();
                Connection con = cons.connectionclass(prefManager.catchString("server"));        // Connect to database
                if (con == null)
                {
                    z = "Check Your Internet Access!";
                }
                else {
                    String query = "update tp.absensi_karyawan_spj set logout_date =  convert( datetime, '" + prefManager.catchString("tanggal_logout_karyawan") +
                            "', 103) where id_karyawan = " + prefManager.catchString("id_karyawan_spj") + " and login_date = convert( datetime, '" + prefManager.catchString("tanggal_login_karyawan") +
                            "', 103)";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "berhasil logout";

                }
            }
            catch (Exception ex)
            {
                z = ex.getMessage();
            }
            return z;
        }
    }
}