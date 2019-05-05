package id.co.cng.spj.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.co.cng.spj.utility.BackgroundService;
import id.co.cng.spj.utility.PrefManager;
import id.co.cng.spj.utility.ConnectionHelper;

import id.co.cng.spj.R;

public class KaloriGasFragment extends Fragment {

    PrefManager prefManager;
    Connection con;
    EditText kaloriCo2, kaloriN2, kaloriSg, kaloriHv, keterangan;
    Button edit, lock, kirim;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_kalori, container, false);

        return view;

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        kaloriCo2 = view.findViewById(R.id.text_kalori_co2);
        kaloriN2 = view.findViewById(R.id.text_kalori_n2);
        kaloriSg = view.findViewById(R.id.text_kalori_sg);
        kaloriHv = view.findViewById(R.id.text_kalori_hv);
        keterangan = view.findViewById(R.id.text_kalori_keterangan);
        edit = view.findViewById(R.id.button_kalori_unlock);
        lock = view.findViewById(R.id.button_kalori_lock);
        kirim = view.findViewById(R.id.button_keterangan_kirim);
        progressBar = view.findViewById(R.id.progressBar_kalori);


        prefManager = new PrefManager(getActivity());
        lock.setEnabled(false);

//
        GetKaloriAfterInstall kaloriAfterInstall = new GetKaloriAfterInstall();
        kaloriAfterInstall.execute("");

        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                edit.setBackgroundColor(Color.GRAY);
                edit.setEnabled(false);

                lock.setBackgroundColor(Color.RED);
                lock.setEnabled(true);

                kaloriSg.setEnabled(true);
                kaloriN2.setEnabled(true);
                kaloriHv.setEnabled(true);
                kaloriCo2.setEnabled(true);
                keterangan.setEnabled(true);
                kaloriSg.requestFocus();

            }
        });

        lock.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                lock.setBackgroundColor(Color.GRAY);
                lock.setEnabled(false);

                edit.setBackgroundColor(Color.RED);
                edit.setEnabled(true);

                kaloriSg.setEnabled(false);
                kaloriN2.setEnabled(false);
                kaloriHv.setEnabled(false);
                kaloriCo2.setEnabled(false);
                keterangan.setEnabled(false);

            }
        });

        kirim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                KirimDataKalori dataKalori = new KirimDataKalori();
                dataKalori.execute("");

            }
        });
//
    }

    public class GetKaloriAfterInstall extends AsyncTask<String,String,String>
    {
        String z = "";
        ProgressDialog progress = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute()
        {
            progress.setTitle("Loading");
            progress.setMessage("Harap tunggu");
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            //progress.dismiss();
        }

        @Override
        protected void onPostExecute(String r)
        {
            progress.dismiss();
            kaloriCo2.setText(prefManager.catchString("kalorigas_co2"));
            kaloriN2.setText(prefManager.catchString("kalorigas_n2"));
            kaloriSg.setText(prefManager.catchString("kalorigas_sg"));
            kaloriHv.setText(prefManager.catchString("kalorigas_hv"));
            keterangan.setText(prefManager.catchString("kalorigas_keterangan"));

        }
        @Override
        protected String doInBackground(String... params)
        {

            try {

                ConnectionHelper cons = new ConnectionHelper();
                con = cons.connectionclass(prefManager.catchString("server"));        // Connect to database
                if (con == null) {
                    //z = "Check Your Internet Access!";
                    z = "Check Your Internet Access!";
                } else {

                    String query = "select top 1 * from tp.kalorigas order by id_kalorigas desc";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();

                        prefManager.saveString("kalorigas_co2",rs.getString("co2"));
                        prefManager.saveString("kalorigas_n2",rs.getString("n2"));
                        prefManager.saveString("kalorigas_sg",rs.getString("s_gravity"));
                        prefManager.saveString("kalorigas_hv",rs.getString("hv"));
                        prefManager.saveString("kalorigas_keterangan",rs.getString("keterangan"));
;



                }
            } catch (Exception ex) {
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();

            }

            return z;
        }
    }

    public class KirimDataKalori extends AsyncTask<String,String,String>
    {
        String z = "";
        ProgressDialog progress = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute()
        {
            progress.setTitle("Loading");
            progress.setMessage("Harap tunggu");
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            String date_time = simpleDateFormat.format(calendar.getTime());

            prefManager.saveString("tanggal_kirim_kalorigas", date_time);
        }

        @Override
        protected void onPostExecute(String r)
        {
            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
            progress.dismiss();
            prefManager.saveString("kalorigas_co2",kaloriCo2.getText().toString());
            prefManager.saveString("kalorigas_n2",kaloriN2.getText().toString());
            prefManager.saveString("kalorigas_sg",kaloriSg.getText().toString());
            prefManager.saveString("kalorigas_hv",kaloriHv.getText().toString());
            prefManager.saveString("kalorigas_keterangan",keterangan.getText().toString());

        }
        @Override
        protected String doInBackground(String... params)
        {

            try {

                ConnectionHelper cons = new ConnectionHelper();
                con = cons.connectionclass(prefManager.catchString("server"));        // Connect to database
                if (con == null) {
                    progress.cancel();
                    z = "Check Your Internet Access!";
                } else {


                    String query = "insert into tp.kalorigas (date_time ,s_gravity, co2, n2, hv, keterangan) values ( convert (datetime, '" + prefManager.catchString("tanggal_kirim_generator") + "', 103), " +
                            kaloriSg.getText().toString() + ", " +
                            kaloriCo2.getText().toString() + ", " +
                            kaloriN2.getText().toString() + ", " +
                            kaloriHv.getText().toString() + ", '" +
                            keterangan.getText().toString() + "')";

                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    z = "Berhasil Diubah";

                }
            } catch (Exception ex) {
                progress.cancel();
                Toast.makeText(getActivity(), "kosong", Toast.LENGTH_SHORT).show();
            }

            return z;
        }
    }
}
