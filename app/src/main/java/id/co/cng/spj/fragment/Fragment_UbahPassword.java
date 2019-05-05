package id.co.cng.spj.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import id.co.cng.spj.MenuUtamaActivity;
import id.co.cng.spj.R;
import id.co.cng.spj.utility.ConnectionHelper;
import id.co.cng.spj.utility.PrefManager;

public class Fragment_UbahPassword extends Fragment {
    private TextInputEditText passLama, passBaru, ulangiPassBaru;
    private Connection con;
    private PrefManager prefManager;
    private Button kirim;
    public Fragment_UbahPassword(){}
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MenuUtamaActivity) getActivity())
                .setActionBarTitle("Ubah Password");

        passLama = view.findViewById(R.id.text_ubah_pass_lama);
        passBaru = view.findViewById(R.id.text_ubah_pass_baru);
        ulangiPassBaru = view.findViewById(R.id.text_ubah_pass_baru_ulang);
        kirim = view.findViewById(R.id.button_ubah_password_kirim);

        prefManager = new PrefManager(getActivity());

        LoadUbahPassword loadUbahPassword = new LoadUbahPassword();
        loadUbahPassword.execute("");

        kirim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (!String.valueOf(passLama.getText()).contentEquals( prefManager.catchString("pass_karyawan_spj"))) {
                    Toast.makeText(getActivity(), "Password lama salah", Toast.LENGTH_SHORT).show();
                } else if (!String.valueOf(passBaru.getText()).contentEquals(String.valueOf(ulangiPassBaru.getText()))) {
                    Toast.makeText(getActivity(), "Password tidak sama", Toast.LENGTH_SHORT).show();
                } else {
                    KirimUbahPassword kirimUbahPassword = new KirimUbahPassword();
                    kirimUbahPassword.execute("");
                }


            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_ubah_password, container, false);
    }

    private class LoadUbahPassword extends AsyncTask<String, String, String> {

        String z = "";
        ProgressDialog progress = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {

            progress.setTitle("Loading");
            progress.setMessage("Harap tunggu");
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progress.dismiss();
            Toast.makeText(getActivity(), prefManager.catchString("pass_karyawan_spj"), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ConnectionHelper cons = new ConnectionHelper();
                con = cons.connectionclass(prefManager.catchString("server"));        // Connect to database
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "select * from tp.master_karyawan_spj where nama_karyawan= '" + prefManager.catchString("nama_karyawan_spj") + "'";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        prefManager.saveString("pass_karyawan_spj", rs.getString("password"));

                    }

                }

            }
            catch (Exception ex)
            {
                z = "Error retrieving data from table";

            }
            return z;
        }
    }

    private class KirimUbahPassword extends AsyncTask<String, String, String> {

        String z = "";
        ProgressDialog progress = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {

            progress.setTitle("Loading");
            progress.setMessage("Harap tunggu");
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }

        @Override
        protected void onPostExecute(String r) {
            progress.dismiss();
            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ConnectionHelper cons = new ConnectionHelper();
                con = cons.connectionclass(prefManager.catchString("server"));        // Connect to database
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {


                        String query = "update tp.master_karyawan_spj set password = ? where nama_karyawan = ?";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.setString(1, String.valueOf(passBaru.getText()));
                        preparedStatement.setString(2, prefManager.catchString("nama_karyawan_spj"));
                        preparedStatement.executeUpdate();

                        z = "Password berhasil diubah";

                }


            }
            catch (Exception ex)
            {
                z = "Error retrieving data from table";

            }
            return z;
        }
    }
}