package id.co.cng.spj;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.annotation.MainThread;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.co.cng.spj.utility.ConnectionHelper;
import id.co.cng.spj.utility.PrefManager;


public class MainActivity extends AppCompatActivity
{
    // Declaring layout button, edit texts
    Button login;
    EditText username,password;
    ProgressBar progressBar;
    // End Declaring layout button, edit texts

    // Declaring connection variables
    Connection con;
    PrefManager prefManager;


    //End Declaring connection variables

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting values from button, texts and progress bar
        login = (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        // End Getting values from button, texts and progress bar

        // Declaring Server ip, username, database name and password
        prefManager = new PrefManager(this);



        if (prefManager.catchBoolean("login")){
            startActivity(new Intent(MainActivity.this, MenuUtamaActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }


        // Setting up the function when button login is clicked
        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                String date_time = simpleDateFormat.format(calendar.getTime());

                prefManager.saveString("tanggal_login_karyawan", date_time);

                CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
                checkLogin.execute("");
//                Intent myIntent = new Intent(MainActivity.this, MenuUtamaActivity.class);
//                MainActivity.this.startActivity(myIntent);
            }
        });
        //End Setting up the function when button login is clicked



        setActionBarTitle("Login");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent myIntent = new Intent(MainActivity.this, SettingActivity.class);
                MainActivity.this.startActivity(myIntent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        ProgressDialog progress = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute()
        {
            progress.setTitle("Loading");
            progress.setMessage("Harap tunggu");
            progress.setCanceledOnTouchOutside(false);
            progress.show();
            Toast.makeText(MainActivity.this, prefManager.catchString("server"), Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(String r)
        {
            progress.dismiss();

            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
            if (prefManager.catchBoolean("login")) {
                Intent myIntent = new Intent(MainActivity.this, MenuUtamaActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                MainActivity.this.startActivity(myIntent);
            }
        }
        @Override
        protected String doInBackground(String... params)
        {
            String usernam = username.getText().toString();
            String passwordd = password.getText().toString();
            if(usernam.trim().equals("")|| passwordd.trim().equals(""))
                z = "Please enter Username and Password";

            else
            {
                try
                {

                    ConnectionHelper cons = new ConnectionHelper();
                    con = cons.connectionclass(prefManager.catchString("server"));        // Connect to database
                    if (con == null)
                    {
                        z = "Check Your Internet Access!";
                    }
                    else
                    {
                        //Toast.makeText(MainActivity.this, "berhasil", Toast.LENGTH_SHORT).show();
                        //ini konek ke muslih
                        //String query = "select * from master_karyawan where username_karyawan = '" + usernam.toString() + "' and password = '"+ passwordd.toString() +"'  ";
                        //ini localhost
                        String query = "select * from tp.master_karyawan_spj where username = '" + usernam.toString() + "' and password = '"+ passwordd.toString() +"'  ";
                        //String query = "select * from keu.master_login where username = '" + usernam.toString() + "' and password = '"+ passwordd.toString() +"'  ";
//                        Statement stmt = con.createStatement();
//                        ResultSet rs = stmt.executeQuery(query);
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        ResultSet rs = preparedStatement.executeQuery();
                        if(rs.next())
                        {
                            z = "login berhasil";

                            prefManager.saveString("id_karyawan_spj", rs.getString("id_karyawan"));
                            prefManager.saveString("nama_karyawan_spj", rs.getString("nama_karyawan"));
                            prefManager.saveBoolean("login", true);
                        }
                        else
                        {
                            z = "user/pass is not correct!";
                        }

                        query = "insert into tp.absensi_karyawan_spj (id_karyawan, nama_karyawan, login_date) values (" + prefManager.catchString("id_karyawan_spj") +
                                ", '" + prefManager.catchString("nama_karyawan_spj") + "', convert( datetime, '" + prefManager.catchString("tanggal_login_karyawan") +
                                "', 103))";
                        preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();

                        z = "berhasil ditambahkan";
                    }
                }
                catch (Exception ex)
                {
                    z = ex.getMessage();
                    progress.dismiss();
                }
            }
            return z;
        }
    }



}