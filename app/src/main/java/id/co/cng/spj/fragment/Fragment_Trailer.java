package id.co.cng.spj.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;

import id.co.cng.spj.MenuUtamaActivity;
import id.co.cng.spj.R;
import id.co.cng.spj.utility.PrefManager;

public class Fragment_Trailer extends Fragment {
    PrefManager prefManager;
    Connection con;
    TextInputEditText
            trailer_trans,
            trailer_nama_cust,
            trailer_ket;
    Button
            button_kirim_tberangkat;
    ProgressBar
            progressBar;
    Switch
            switchPasangSelangHose,
            switchBaruBukaValve,
            switchLamaValveSambung,
            switchLamaBukaValve,
            switchLamaSelang ;

    TextView
            textPasangSelangHose,
            textBaruBukaValve,
            textLamaValveSambung,
            textLamaBukaValve,
            textLamaSelang;
    String switchOn  = "Yes";
    String switchOff = "No ";

    public Fragment_Trailer(){}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        trailer_trans           = view.findViewById(R.id.text_trailer_baru_moda_transport);
        trailer_nama_cust       = view.findViewById(R.id.text_trailer_lama_moda_transport);
        trailer_ket             = view.findViewById(R.id.text_trailer_keterangan);

        progressBar             = view.findViewById(R.id.progressBar_trailer);
        button_kirim_tberangkat = view.findViewById(R.id.button_moda_transport_kirim);
        prefManager             = new PrefManager(getActivity());
////////////////////////////////////////////////////////////////////////////////////////////////////
        trailer_trans           .setText(prefManager.catchString("tra_trans"));
        trailer_nama_cust       .setText(prefManager.catchString("tra_nama_cust"));
        trailer_ket             .setText(prefManager.catchString("tra_ket"));
////////////////////////////////////////////////////////////////////////////////////////////////////
        switchPasangSelangHose      = view.findViewById(R.id.switch_baru_pasang_selang);
        textPasangSelangHose        = view.findViewById(R.id.textView_baru_pasang_selang);
        switchBaruBukaValve         = view.findViewById(R.id.switch_baru_buka_valve);
        textBaruBukaValve           = view.findViewById(R.id.textView_baru_buka_valve);
        switchLamaValveSambung      = view.findViewById(R.id.switch_lama_valve_ditransporter);
        textLamaValveSambung        = view.findViewById(R.id.textView_lama_valve_ditransporter);
        switchLamaBukaValve         = view.findViewById(R.id.switch_lama_buka_valve);
        textLamaBukaValve           = view.findViewById(R.id.textView_lama_buka_valve);
        switchLamaSelang            = view.findViewById(R.id.switch_lama_selang);
        textLamaSelang              = view.findViewById(R.id.textView_lama_selang);
////////////////////////////////////////////////////////////////////////////////////////////////////
        //onclicklistener
        button_kirim_tberangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trailer_trans.getText().toString().trim().equals("")) {
                    trailer_trans.setError("Tolong diisi!");
                    trailer_trans.requestFocus();
                } else if (trailer_nama_cust.getText().toString().trim().equals("")) {
                    trailer_nama_cust.setError("Tolong diisi!");
                    trailer_nama_cust.requestFocus();
                } else if (trailer_ket.getText().toString().trim().equals("")) {
                    trailer_ket.setError("Tolong diisi!");
                    trailer_ket.requestFocus();
                } else {
                    AddTrailer addTrailer = new AddTrailer();// this is the Asynctask, which is used to process in background to reduce load on app process
                    addTrailer.execute("");
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
        ((MenuUtamaActivity) getActivity())
                .setActionBarTitle("Penggantian Trailer");

        // For first switch button
        switchPasangSelangHose = (Switch) view.findViewById(R.id.switch_baru_pasang_selang);
        textPasangSelangHose = (TextView) view.findViewById(R.id.textView_baru_pasang_selang);

        switchPasangSelangHose.setChecked(false);
        switchPasangSelangHose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textPasangSelangHose.setText(switchOn);
                } else {
                    textPasangSelangHose.setText(switchOff);
                }
            }
        });

        if (switchPasangSelangHose.isChecked()) {
            textPasangSelangHose.setText(switchOn);
        } else {
            textPasangSelangHose.setText(switchOff);
        }

        // for second switch button
        switchBaruBukaValve = (Switch) view.findViewById(R.id.switch_baru_buka_valve);
        textBaruBukaValve = (TextView) view.findViewById(R.id.textView_baru_buka_valve);

        switchBaruBukaValve.setChecked(false);
        switchBaruBukaValve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textBaruBukaValve.setText(switchOn);
                } else {
                    textBaruBukaValve.setText(switchOff);
                }
            }
        });

        if (switchBaruBukaValve.isChecked()) {
            textBaruBukaValve.setText(switchOn);
        } else {
            textBaruBukaValve.setText(switchOff);
        }

        // for second switch button3
        switchLamaValveSambung = (Switch) view.findViewById(R.id.switch_lama_valve_ditransporter);
        textLamaValveSambung = (TextView) view.findViewById(R.id.textView_lama_valve_ditransporter);

        switchLamaValveSambung.setChecked(false);
        switchLamaValveSambung.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textLamaValveSambung.setText(switchOn);
                } else {
                    textLamaValveSambung.setText(switchOff);
                }
            }
        });

        if (switchLamaValveSambung.isChecked()) {
            textLamaValveSambung.setText(switchOn);
        } else {
            textLamaValveSambung.setText(switchOff);
        }

        // for second switch button3
        switchLamaBukaValve = (Switch) view.findViewById(R.id.switch_lama_buka_valve);
        textLamaBukaValve = (TextView) view.findViewById(R.id.textView_lama_buka_valve);

        switchLamaBukaValve.setChecked(false);
        switchLamaBukaValve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textLamaBukaValve.setText(switchOn);
                } else {
                    textLamaBukaValve.setText(switchOff);
                }
            }
        });

        if (switchLamaBukaValve.isChecked()) {
            textLamaBukaValve.setText(switchOn);
        } else {
            textLamaBukaValve.setText(switchOff);
        }

        // for second switch button3
        switchLamaSelang = (Switch) view.findViewById(R.id.switch_lama_selang);
        textLamaSelang = (TextView) view.findViewById(R.id.textView_lama_selang);

        switchLamaSelang.setChecked(false);
        switchLamaSelang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textLamaSelang.setText(switchOn);
                } else {
                    textLamaSelang.setText(switchOff);
                }
            }
        });

        if (switchLamaSelang.isChecked()) {
            textLamaSelang.setText(switchOn);
        } else {
            textLamaSelang.setText(switchOff);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_trailer, container, false);

    }
    public class AddTrailer extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
            prefManager.saveString("trailer_trans"  ,trailer_trans      .getText().toString());
            prefManager.saveString("trailer_nama"   ,trailer_nama_cust  .getText().toString());
            prefManager.saveString("trailer_ket"    ,trailer_ket        .getText().toString());

            progressBar.setVisibility(View.VISIBLE);
            //Toast.makeText(getActivity(), prefManager.catchString("pengisian_filling1_timeAwal"), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String r)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            return "";
        }
    }
}
