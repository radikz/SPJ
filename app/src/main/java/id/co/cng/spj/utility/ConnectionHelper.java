package id.co.cng.spj.utility;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionHelper {

    //String ip,db,DBUserNameStr,DBPasswordStr;

    String user = "sa";
    String password1 = "mus2828";
//    String password1 = "mus282828";


    @SuppressLint("NewApi")
    public Connection connectionclass(String server)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL;


        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://"+server+"/etracking";
//            ConnectionURL = "jdbc:jtds:sqlserver://192.168.43.227/etracking";
            //ConnectionURL = "jdbc:jtds:sqlserver://192.168.43.226/esubmission;encrypt=fasle;user=sa;password=Radikzid29();instance=SQLEXPRESS;";
//            ConnectionURL = "jdbc:jtds:sqlserver://" + server +";databaseName="+ database + ";user=" + user+ ";password=" + password + ";";
//            ConnectionURL = "jdbc:jtds:sqlserver://" + server + ";"
//                     + "databaseName=" + database + ";user=" + user + ";password="
//                          + password1 + ";";
            //ConnectionURL = "jdbc:jtds:sqlserver://" + server + "/" + database + ";instance=MSSQLSERVER" +";user=" + user+ ";password=" + password1 + ";";
            //ConnectionURL = "jdbc:jtds:sqlserver://" + server + database + ";user=" + user+ ";password=" + password1 + ";";
            connection = DriverManager.getConnection(ConnectionURL,user,password1);
        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
//            Toast.makeText(getClass(), "error1", Toast.LENGTH_SHORT).show();
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
            //Toast.makeText(MainActivity.this, "error2", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
            //Toast.makeText(MainActivity.this, "error3", Toast.LENGTH_SHORT).show();
        }
        return connection;
    }
}