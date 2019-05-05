package id.co.cng.spj.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.co.cng.spj.MenuUtamaActivity;
import id.co.cng.spj.R;
import id.co.cng.spj.utility.ConnectionHelper;
import id.co.cng.spj.utility.PrefManager;

public class Fragment_PLN extends Fragment {

    private Spinner spin3;
    private String[] shift_pln = {
            "P2(07:00AM-03:00PM)",
            "S2(03:00AM-11:00PM)",
            "M2(11:00PM-07:00AM)"
    };

    PrefManager prefManager;
    Connection con;

    Spinner plnshift;

    TextInputEditText kvar,klwbp,kwbp,ktotal,kgedung,kket;

    Button button_kirim_pln;

    ProgressBar progressBar;

    public Fragment_PLN() {
        // Required empty public constructor
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        plnshift    = view.findViewById(R.id.spinner_pln_shift);;
        kvar        = view.findViewById(R.id.text_generator_pln_kvar);
        klwbp       = view.findViewById(R.id.text_generator_pln_kwh_lwbp);
        kwbp        = view.findViewById(R.id.text_generator_pln_kwh_wbp);
        ktotal      = view.findViewById(R.id.text_generator_pln_kwh_total);
        kgedung     = view.findViewById(R.id.text_generator_pln_kwh_gedung);
        kket        = view.findViewById(R.id.text_generator_pln_keterangan);

        progressBar = view.findViewById(R.id.progressBar_pln);
        button_kirim_pln=view.findViewById(R.id.button_generator_pln_kirim);
        prefManager = new PrefManager(getActivity());

////////////////////////////////////////////////////////////////////////////////////////////////////
        //
        // plnshift.(prefManager.catchString("pln_shift"));
        kvar    .setText(prefManager.catchString("pln_kvar"));
        klwbp   .setText(prefManager.catchString("pln_klwbp"));
        kwbp    .setText(prefManager.catchString("pln_kwbp"));
        ktotal  .setText(prefManager.catchString("pln_ktotal"));
        kgedung .setText(prefManager.catchString("pln_kgedung"));
        kket    .setText(prefManager.catchString("pln_ket"));

        ktotal.setEnabled(false);

        klwbp.setOnEditorActionListener(new TextInputEditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    double total;
                    //submit_btn.performClick();
                    if (kwbp.getText().toString().equals(""))
                        total = Double.valueOf(klwbp.getText().toString());
                    else
                        total = Double.valueOf(klwbp.getText().toString()) + Double.valueOf(kwbp.getText().toString());
                    ktotal.setText(String.valueOf(total));

                    //kwbp.setNextFocusDownId(R.id.text_generator_pln_kwh_wbp);
                    kwbp.requestFocus();
                    return true;
                }
                return false;
            }
        });

        kwbp.setOnEditorActionListener(new TextInputEditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    //submit_btn.performClick();
                    double total;
                    if (klwbp.getText().toString().equals(""))
                        total = Double.valueOf(kwbp.getText().toString());
                    else
                        total = Double.valueOf(klwbp.getText().toString()) + Double.valueOf(kwbp.getText().toString());
                    ktotal.setText(String.valueOf(total));
                    //kwbp.setNext
                    // FocusDownId(R.id.text_generator_pln_kwh_wbp);
                    kgedung.requestFocus();
                    return true;
                }
                return false;
            }
        });

//onclicklistener
        button_kirim_pln.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (kvar.getText().toString().trim().equals("")) {
                    kvar.setError("Tolong diisi!");
                    kvar.requestFocus();
                } else if (klwbp.getText().toString().trim().equals("")) {
                    klwbp.setError("Tolong diisi!");
                    klwbp.requestFocus();
                } else if (kwbp.getText().toString().trim().equals("")) {
                    kwbp.setError("Tolong diisi!");
                    kwbp.requestFocus();
                } else if (ktotal.getText().toString().trim().equals("")) {
                    ktotal.setError("Tolong diisi!");
                    ktotal.requestFocus();
                } else if (kgedung.getText().toString().trim().equals("")) {
                    kgedung.setError("Tolong diisi!");
                    kgedung.requestFocus();
                } else {
                    AddPln addPln = new AddPln();// this is the Asynctask, which is used to process in background to reduce load on app process
                    addPln.execute("");
                }
            }
        });
//        super.onViewCreated(view, savedInstanceState);
//        ((MenuUtamaActivity) getActivity())
//                .setActionBarTitle("Cek Compressor");

        spin3 = (Spinner) view.findViewById(R.id.spinner_pln_shift);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, shift_pln);

        // mengeset Array Adapter tersebut ke Spinner
        spin3.setAdapter(adapter3);
//        spin3.setSelection(2);
//        prefManager.deleteKey("pln_shiftpln");

        spin3.setSelection(prefManager.catchInt("pln_shiftpln_pos"));
//        Toast.makeText(getActivity(), prefManager.catchInt("pln_shiftpln_posi").toString(), Toast.LENGTH_SHORT).show();

        // mengeset listener untuk mengetahui saat item dipilih
        spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
//                Toast.makeText(getActivity(), "Selected " + adapter3.getItem(i), Toast.LENGTH_SHORT).show();
                prefManager.saveString("pln_shiftpln", adapter3.getItem(i));
                prefManager.saveInt("pln_shiftpln_pos", i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pln, container, false);

//spin shift pln

        return view;
    }

    private class AddPln extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        ProgressDialog progress = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute()
        {
            progress.setTitle("Loading");
            progress.setMessage("Harap tunggu");
            progress.show();

            prefManager.saveString("pln_kvar",kvar.getText().toString());
            prefManager.saveString("pln_klwbp",klwbp.getText().toString());
            prefManager.saveString("pln_kwbp",kwbp.getText().toString());
            prefManager.saveString("pln_ktotal",ktotal.getText().toString());
            prefManager.saveString("pln_kgedung",kgedung.getText().toString());
            prefManager.saveString("pln_ket",kket.getText().toString());

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            String date_time = simpleDateFormat.format(calendar.getTime());

            prefManager.saveString("tanggal_kirim_generator", date_time);


            //Toast.makeText(getActivity(), prefManager.catchString("pengisian_filling1_timeAwal"), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String r)
        {
            progress.dismiss();
            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {

                ConnectionHelper cons = new ConnectionHelper();
                con = cons.connectionclass(prefManager.catchString("server"));        // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access!";
                } else {
                    String query = "insert into tp.cek_KWH_PLN values ( " + prefManager.catchString("id_karyawan_spj") + ", '" +
                            prefManager.catchString("nama_karyawan_spj") + "', convert (datetime, '" + prefManager.catchString("tanggal_kirim_generator") + "', 103), " +
                            "convert(datetime, convert(datetime, '" + prefManager.catchString("tanggal_kirim_generator") + "', 103), 108), '" +
                            prefManager.catchString("pln_shiftpln") + "', " +
                            String.valueOf(kvar.getText()) + ", " +
                            String.valueOf(klwbp.getText()) + ", " +
                            String.valueOf(kwbp.getText()) + ", " +
                            String.valueOf(ktotal.getText()) + ", " +
                            String.valueOf(kgedung.getText()) + ", '" +
                            String.valueOf(kket.getText()) + "')";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    z = "added success";
                }
            }
            catch (Exception ex)
            {
                z = ex.getMessage();
                progress.dismiss();
            }
            return z;
        }
    }

}
