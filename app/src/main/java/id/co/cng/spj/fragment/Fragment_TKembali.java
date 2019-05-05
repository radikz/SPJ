package id.co.cng.spj.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

import java.sql.Connection;
import java.util.Calendar;

import id.co.cng.spj.MenuUtamaActivity;
import id.co.cng.spj.R;
import id.co.cng.spj.utility.PrefManager;

public class Fragment_TKembali extends Fragment {

    PrefManager
            prefManager;
    Connection
            con;
    TextInputEditText
            tiba_moda, tiba_head_truck, tiba_code_trip,
            tiba_nopol, tiba_lwc, tiba_customer,
            tiba_keterangan_trailer;

    SingleSelectToggleGroup trailerCekLampu, trailerCekRem, trailerCekBan, trailerCekBanSerep, trailerCekPer,trailerKondisiTerhubung;
    SingleSelectToggleGroup trailerJenisBanDKiL, trailerKondisiBanDKiL; //depan kiri luar
    SingleSelectToggleGroup trailerJenisBanDKiD, trailerKondisiBanDKiD; //depankiridalam
    SingleSelectToggleGroup trailerJenisBanDKaL, trailerKondisiBanDKaL; //depan kanan luar
    SingleSelectToggleGroup trailerJenisBanDKaD, trailerKondisiBanDKaD; //depan kanan dalam

    SingleSelectToggleGroup trailerJenisBanBKiL, trailerKondisiBanBKiL; //belakang kiri luar
    SingleSelectToggleGroup trailerJenisBanBKiD, trailerKondisiBanBKiD; //belakang kiri dalam
    SingleSelectToggleGroup trailerJenisBanBKaL, trailerKondisiBanBKaL; //belakang kanan luar
    SingleSelectToggleGroup trailerJenisBanBKaD, trailerKondisiBanBKaD; //belajang kanan dalam

    Button buttonKirimTiba,buttonResetTiba;

    ProgressBar progressBar;

    Switch switchTibaStnk, switchTibaBukuHt, switchTibaBukuTrailer;

    TextView textTibaStnk, textTibaBukuHt, textTibaBukuTrailer;

    String switchOn  = "Yes";
    String switchOff = "No ";

    private Button btnDatePicker3;
    private EditText txtDate3;

    public Fragment_TKembali(){

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {

        tiba_moda               = view.findViewById(R.id.text_trailer_tiba_moda_transport);
        tiba_head_truck         = view.findViewById(R.id.text_trailer_tiba_head_truck);
        tiba_code_trip          = view.findViewById(R.id.text_trailer_tiba_kode_trip);
        tiba_nopol              = view.findViewById(R.id.text_trailer_tiba_nopol);
        tiba_lwc                = view.findViewById(R.id.text_trailer_tiba_lwc);
        tiba_customer           = view.findViewById(R.id.text_trailer_tiba_customer);
        tiba_keterangan_trailer = view.findViewById(R.id.text_trailer_tiba_keterangan);

        prefManager             = new PrefManager(getActivity());

////////////////////////////////////////////////////////////////////////////////////////////////////
        tiba_moda               .setText(prefManager.catchString("tiba_moda"));
        tiba_head_truck         .setText(prefManager.catchString("tiba_head_truck"));
        tiba_code_trip          .setText(prefManager.catchString("tiba_code_trip"));
        tiba_nopol              .setText(prefManager.catchString("tiba_nopol"));
        tiba_lwc                .setText(prefManager.catchString("tiba_lwc"));
        tiba_customer           .setText(prefManager.catchString("tiba_customer"));
        tiba_keterangan_trailer .setText(prefManager.catchString("tiba_keterangan_trailer"));

///////////////////////////////////////////////////////////////////////////////////////////////////
        switchTibaStnk          = view.findViewById(R.id.switch_trailer_tiba_stnk);
        textTibaStnk            = view.findViewById(R.id.textView_trailer_tiba_stnk);
        switchTibaBukuHt        = view.findViewById(R.id.switch_trailer_tiba_buku_ht);
        textTibaBukuHt          = view.findViewById(R.id.textView_trailer_tiba_buku_ht);
        switchTibaBukuTrailer   = view.findViewById(R.id.switch_trailer_tiba_buku_trailer);
        textTibaBukuTrailer     = view.findViewById(R.id.textView_trailer_tiba_buku_trailer);

        trailerCekLampu             = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_lampu);
        trailerCekRem               = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_rem);
        trailerCekBan               = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_ban);
        trailerCekBanSerep          = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_ban_serep);
        trailerCekPer               = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_per);
        trailerKondisiTerhubung     = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_kondisi_trailer);
        trailerJenisBanDKiL         = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_jenis_ban_depan_kiri_luar);
        trailerKondisiBanDKiL       = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_kondisi_ban_depan_kiri_luar);

//        trailerJenisBanDKiD, trailerKondisiBanDKiD; //depankiridalam
        trailerJenisBanDKiD         = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_jenis_ban_depan_kiri_dalam);
        trailerKondisiBanDKiD       = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_kondisi_ban_depan_kiri_dalam);

//        trailerJenisBanDKaL, trailerKondisiBanDKaL; //depan kanan luar
        trailerJenisBanDKaL         = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_jenis_ban_depan_kanan_luar);
        trailerKondisiBanDKaL       = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_kondisi_ban_depan_kanan_luar);

//        trailerJenisBanDKaD, trailerKondisiBanDKaD; //depan kanan dalam
        trailerJenisBanDKaD         = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_jenis_ban_depan_kanan_dalam);
        trailerKondisiBanDKaD       = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_kondisi_ban_depan_kanan_dalam);

//        trailerJenisBanBKiL, trailerKondisiBanBKiL; //belakang kiri luar
        trailerJenisBanBKiL         = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_jenis_ban_belakang_kiri_luar);
        trailerKondisiBanBKiL       = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_kondisi_ban_belakang_kiri_luar);

//        trailerJenisBanBKiD, trailerKondisiBanBKiD; //belakang kiri dalam
        trailerJenisBanBKiD         = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_jenis_ban_belakang_kiri_dalam);
        trailerKondisiBanBKiD       = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_kondisi_ban_belakang_kiri_dalam);

//        trailerJenisBanBKaL, trailerKondisiBanBKaL; //belakang kanan luar
        trailerJenisBanBKaL         = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_jenis_ban_belakang_kanan_luar);
        trailerKondisiBanBKaL       = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_kondisi_ban_belakang_kanan_luar);

//        trailerJenisBanBKaD, trailerKondisiBanBKaD; //belajang kanan dalam
        trailerJenisBanBKaD         = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_cek_jenis_ban_belakang_kanan_dalam);
        trailerKondisiBanBKaD       = view.findViewById(R.id.group_single_radiobutton_trailer_tiba_kondisi_ban_belakang_kanan_dalam);

        buttonKirimTiba             = view.findViewById(R.id.button_trailer_tiba_kirim);
        buttonResetTiba             = view.findViewById(R.id.button_trailer_tiba_reset);

        progressBar                 = view.findViewById(R.id.progressBar_tra_tiba);

        LoadPage();

///////////////////////////////////////////////////////////////////////////////////////////////////
        buttonKirimTiba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tiba_moda.getText().toString().trim().equals("")) {
                    tiba_moda.setError("Tolong diisi!");
                    tiba_moda.requestFocus();
                } else if (tiba_head_truck.getText().toString().trim().equals("")) {
                    tiba_head_truck.setError("Tolong diisi!");
                    tiba_head_truck.requestFocus();
                } else if (tiba_code_trip.getText().toString().trim().equals("")) {
                    tiba_code_trip.setError("Tolong diisi!");
                    tiba_code_trip.requestFocus();
                } else if (tiba_nopol.getText().toString().trim().equals("")) {
                    tiba_nopol.setError("Tolong diisi!");
                    tiba_nopol.requestFocus();
                } else if (tiba_lwc.getText().toString().trim().equals("")) {
                    tiba_lwc.setError("Tolong diisi!");
                    tiba_lwc.requestFocus();
                } else if (tiba_customer.getText().toString().trim().equals("")) {
                    tiba_customer.setError("Tolong diisi!");
                    tiba_customer.requestFocus();
                } else if (tiba_keterangan_trailer.getText().toString().trim().equals("")) {
                    tiba_keterangan_trailer.setError("Tolong diisi!");
                    tiba_keterangan_trailer.requestFocus();
                } else {
                    AddTTiba addTTiba = new AddTTiba();// this is the Asynctask, which is used to process in background to reduce load on app process
                    addTTiba.execute("");
                }
            }
        });

        buttonResetTiba.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ResetTiba resetTiba = new ResetTiba();
                resetTiba.execute("");
            }
        });


        super.onViewCreated(view, savedInstanceState);
        ((MenuUtamaActivity) getActivity())
                .setActionBarTitle("Trailer Tiba");

        // For switch button 26
        switchTibaStnk = (Switch) view.findViewById(R.id.switch_trailer_tiba_stnk);
        textTibaStnk = (TextView) view.findViewById(R.id.textView_trailer_tiba_stnk);

        switchTibaStnk.setChecked(false);
        switchTibaStnk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textTibaStnk.setText(switchOn);
                } else {
                    textTibaStnk.setText(switchOff);
                }
            }
        });

        if (switchTibaStnk.isChecked()) {
            textTibaStnk.setText(switchOn);
        } else {
            textTibaStnk.setText(switchOff);
        }

        // For switch button 27
        switchTibaBukuHt = (Switch) view.findViewById(R.id.switch_trailer_tiba_buku_ht);
        textTibaBukuHt = (TextView) view.findViewById(R.id.textView_trailer_tiba_buku_ht);

        switchTibaBukuHt.setChecked(false);
        switchTibaBukuHt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textTibaBukuHt.setText(switchOn);
                } else {
                    textTibaBukuHt.setText(switchOff);
                }
            }
        });

        if (switchTibaBukuHt.isChecked()) {
            textTibaBukuHt.setText(switchOn);
        } else {
            textTibaBukuHt.setText(switchOff);
        }

        // For switch button 28
        switchTibaBukuTrailer= (Switch) view.findViewById(R.id.switch_trailer_tiba_buku_trailer);
        textTibaBukuTrailer = (TextView) view.findViewById(R.id.textView_trailer_tiba_buku_trailer);

        switchTibaBukuTrailer.setChecked(false);
        switchTibaBukuTrailer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textTibaBukuTrailer.setText(switchOn);
                } else {
                    textTibaBukuTrailer.setText(switchOff);
                }
            }
        });

        if (switchTibaBukuTrailer.isChecked()) {
            textTibaBukuTrailer.setText(switchOn);
        } else {
            textTibaBukuTrailer.setText(switchOff);
        }


        trailerCekLampu.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_lampu_tidak_nyala:
                        prefManager.saveString("tiba_cek_lampu", "off");
                        //prefManager.saveInt("tiba_cek_lampu_pos", R.id.choice_trailer_tiba_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_trailer_tiba_cek_lampu_nyala:
                        prefManager.saveString("tiba_cek_lampu", "on");
                        //prefManager.saveInt("tiba_cek_lampu_pos", R.id.choice_trailer_tiba_cek_lampu_nyala);
                        break;
                }
            }
        });

        trailerCekRem.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_rem_fungsi:
                        prefManager.saveString("tiba_cek_rem", "fungsi");
                        //prefManager.saveInt("tiba_cek_rem_pos", R.id.choice_trailer_tiba_cek_rem_fungsi);
                        break;
                    case R.id.choice_trailer_tiba_cek_rem_tidak_fungsi:
                        prefManager.saveString("tiba_cek_rem", "tidak");
                        //prefManager.saveInt("tiba_cek_rem_pos", R.id.choice_trailer_tiba_cek_rem_tidak_fungsi);
                        break;
                }
            }
        });

        trailerCekBan.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_ban_bocor:
                        prefManager.saveString("tiba_cek_ban", "bocor");
                        //prefManager.saveInt("tiba_cek_ban_pos", R.id.choice_trailer_tiba_cek_ban_bocor);
                        break;
                    case R.id.choice_trailer_tiba_cek_ban_tidak_bocor:
                        prefManager.saveString("tiba_cek_ban", "tidak");
                        //prefManager.saveInt("tiba_cek_ban_pos", R.id.choice_trailer_tiba_cek_ban_tidak_bocor);
                        break;

                }
            }
        });

        trailerCekBanSerep.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_ban_serep_ada:
                        prefManager.saveString("tiba_cek_ban_serep", "ada");
                        //prefManager.saveInt("tiba_cek_ban_serep_pos", R.id.choice_trailer_tiba_cek_ban_serep_ada);
                        break;
                    case R.id.choice_trailer_tiba_cek_ban_serep_tidak_ada:
                        prefManager.saveString("tiba_cek_ban_serep", "Tidak");
                        //prefManager.saveInt("tiba_cek_ban_serep_pos", R.id.choice_trailer_tiba_cek_ban_serep_tidak_ada);
                        break;
                }
            }
        });

        trailerCekPer.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_per_patah:
                        prefManager.saveString("tiba_cek_per", "patah");
                        //prefManager.saveInt("tiba_cek_per_pos", R.id.choice_trailer_tiba_cek_per_patah);
                        break;
                    case R.id.choice_trailer_tiba_cek_per_tidak_patah:
                        prefManager.saveString("tiba_cek_per", "Tidak");
                        //prefManager.saveInt("tiba_cek_per_pos", R.id.choice_trailer_tiba_cek_per_tidak_patah);
                        break;
                }
            }
        });

        trailerKondisiTerhubung.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_kondisi_trailer_siap_pakai:
                        prefManager.saveString("tiba_cek_trailer_siap", "siap");
                        //prefManager.saveInt("tiba_cek_trailer_siap_pos", R.id.choice_trailer_tiba_cek_kondisi_trailer_siap_pakai);
                        break;
                    case R.id.choice_trailer_tiba_cek_kondisi_trailer_tidak_siap:
                        prefManager.saveString("tiba_cek_trailer_siap", "sudah");
                        //prefManager.saveInt("tiba_cek_trailer_siap_pos", R.id.choice_trailer_tiba_cek_kondisi_trailer_tidak_siap);
                        break;

                }
            }
        });

        trailerJenisBanDKiL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_jenis_ban_depan_kiri_luar_original:
                        prefManager.saveString("tiba_jenis_bandkil", "original");
                        //prefManager.saveInt("tiba_jenis_bandkil_pos", R.id.choice_trailer_tiba_cek_jenis_ban_depan_kiri_luar_original);
                        break;
                    case R.id.choice_trailer_tiba_cek_jenis_ban_depan_kiri_luar_vulkan:
                        prefManager.saveString("tiba_jenis_bandkil", "vulkan");
                        //prefManager.saveInt("tiba_jenis_bandkil_pos", R.id.choice_trailer_tiba_cek_jenis_ban_depan_kiri_luar_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanDKiL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_luar_1:
                        prefManager.saveString("tiba_kondisi_bandkil", "25%");
                        //prefManager.saveInt("tiba_kondisi_bandkil_pos", R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_luar_1);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_luar_2:
                        prefManager.saveString("tiba_kondisi_bandkil", "50%");
                        //prefManager.saveInt("tiba_kondisi_bandkilpos", R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_luar_2);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_luar_3:
                        prefManager.saveString("tiba_kondisi_bandkil", "75%");
                        //prefManager.saveInt("tiba_kondisi_bandkilpos", R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_luar_3);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_luar_4:
                        prefManager.saveString("tiba_kondisi_bandkil", "100%");
                        //prefManager.saveInt("tiba_kondisi_bandkilpos", R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_luar_4);
                        break;

                }
            }
        });

        trailerJenisBanDKiD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_jenis_ban_depan_kiri_dalam_original:
                        prefManager.saveString("tiba_jenis_bandkid", "original");
                        //prefManager.saveInt("tiba_jenis_bandkid_pos", R.id.choice_trailer_tiba_cek_jenis_ban_depan_kiri_dalam_original);
                        break;
                    case R.id.choice_trailer_tiba_cek_jenis_ban_depan_kiri_dalam_vulkan:
                        prefManager.saveString("tiba_jenis_bandkid", "vulkan");
                        //prefManager.saveInt("tiba_jenis_bandkid_pos", R.id.choice_trailer_tiba_cek_jenis_ban_depan_kiri_dalam_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanDKiD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_dalam_1:
                        prefManager.saveString("tiba_kondisi_bandkid", "25%");
                        //prefManager.saveInt("tiba_kondisi_bandkid_pos", R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_dalam_1);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_dalam_2:
                        prefManager.saveString("tiba_kondisi_bandkid", "50%");
                        //prefManager.saveInt("tiba_kondisi_bandkildpos", R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_dalam_2);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_dalam_3:
                        prefManager.saveString("tiba_kondisi_bandkid", "75%");
                        //prefManager.saveInt("tiba_kondisi_bandkidpos", R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_dalam_3);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_dalam_4:
                        prefManager.saveString("tiba_kondisi_bandkid", "100%");
                        //prefManager.saveInt("tiba_kondisi_bandkidpos", R.id.choice_trailer_tiba_kondisi_ban_depan_kiri_dalam_4);
                        break;

                }
            }
        });

        trailerJenisBanDKaL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_jenis_ban_depan_kanan_luar_original:
                        prefManager.saveString("tiba_jenis_bandkal", "original");
                        //prefManager.saveInt("tiba_jenis_bandkal_pos", R.id.choice_trailer_tiba_cek_jenis_ban_depan_kanan_luar_original);
                        break;
                    case R.id.choice_trailer_tiba_cek_jenis_ban_depan_kanan_luar_vulkan:
                        prefManager.saveString("tiba_jenis_bandkal", "vulkan");
                        //prefManager.saveInt("tiba_jenis_bandkal_pos", R.id.choice_trailer_tiba_cek_jenis_ban_depan_kanan_luar_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanDKaL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_luar_1:
                        prefManager.saveString("tiba_kondisi_bandkal", "25%");
                        //("tiba_kondisi_bandkal_pos", R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_luar_1);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_luar_2:
                        prefManager.saveString("tiba_kondisi_bandkal", "50%");
                        //("tiba_kondisi_bandkal_pos", R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_luar_2);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_luar_3:
                        prefManager.saveString("tiba_kondisi_bandkal", "75%");
                        //("tiba_kondisi_bandkal_pos", R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_luar_3);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_luar_4:
                        prefManager.saveString("tiba_kondisi_bandkal", "100%");
                        //("tiba_kondisi_bandkal_pos", R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_luar_4);
                        break;

                }
            }
        });

        trailerJenisBanDKaD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_jenis_ban_depan_kanan_dalam_original:
                        prefManager.saveString("tiba_jenis_bandkad", "original");
                        //("tiba_jenis_bandkad_pos", R.id.choice_trailer_tiba_cek_jenis_ban_depan_kanan_dalam_original);
                        break;
                    case R.id.choice_trailer_tiba_cek_jenis_ban_depan_kanan_luar_vulkan:
                        prefManager.saveString("tiba_jenis_bandkad", "vulkan");
                        //("tiba_jenis_bandkad_pos", R.id.choice_trailer_tiba_cek_jenis_ban_depan_kanan_dalam_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanDKaD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_dalam_1:
                        prefManager.saveString("tiba_kondisi_bandkad", "25%");
                        //("tiba_kondisi_bandkad_pos", R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_dalam_1);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_dalam_2:
                        prefManager.saveString("tiba_kondisi_bandkad", "50%");
                        //("tiba_kondisi_bandkad_pos", R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_dalam_2);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_dalam_3:
                        prefManager.saveString("tiba_kondisi_bandkad", "75%");
                        //("tiba_kondisi_bandkad_pos", R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_dalam_3);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_dalam_4:
                        prefManager.saveString("tiba_kondisi_bandkad", "100%");
                        //("tiba_kondisi_bandkad_pos", R.id.choice_trailer_tiba_kondisi_ban_depan_kanan_dalam_4);
                        break;

                }
            }
        });

        trailerJenisBanBKiL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kiri_luar_original:
                        prefManager.saveString("tiba_jenis_banbkil", "original");
                        //("tiba_jenis_banbkil_pos", R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kiri_luar_original);
                        break;
                    case R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kiri_luar_vulkan:
                        prefManager.saveString("tiba_jenis_banbkil", "vulkan");
                        //("tiba_jenis_banbkil_pos", R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kiri_luar_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanBKiL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_luar_1:
                        prefManager.saveString("tiba_kondisi_banbkil", "25%");
                        //("tiba_kondisi_banbkil_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_luar_1);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_luar_2:
                        prefManager.saveString("tiba_kondisi_banbkil", "50%");
                        //("tiba_kondisi_banbkil_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_luar_2);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_luar_3:
                        prefManager.saveString("tiba_kondisi_banbkil", "75%");
                        //("tiba_kondisi_banbkil_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_luar_3);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_luar_4:
                        prefManager.saveString("tiba_kondisi_banbkil", "100%");
                        //("tiba_kondisi_banbkil_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_luar_4);
                        break;


                }
            }
        });

        trailerJenisBanBKiD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kiri_dalam_original:
                        prefManager.saveString("tiba_jenis_banbkid", "original");
                        //("tiba_jenis_banbkid_pos", R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kiri_dalam_original);
                        break;
                    case R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kiri_dalam_vulkan:
                        prefManager.saveString("tiba_jenis_banbkid", "vulkan");
                        //("tiba_jenis_banbkid_pos", R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kiri_dalam_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanBKiD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_dalam_1:
                        prefManager.saveString("tiba_kondisi_banbkid", "25%");
                        //("tiba_kondisi_banbkid_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_dalam_1);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_dalam_2:
                        prefManager.saveString("tiba_kondisi_banbkid", "50%");
                        //("tiba_kondisi_banbkid_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_dalam_2);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_dalam_3:
                        prefManager.saveString("tiba_kondisi_banbkid", "75%");
                        //("tiba_kondisi_banbkid_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_dalam_3);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_dalam_4:
                        prefManager.saveString("tiba_kondisi_banbkid", "100%");
                        //("tiba_kondisi_banbkid_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kiri_dalam_4);
                        break;

                }
            }
        });

        trailerJenisBanBKaL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kanan_luar_original:
                        prefManager.saveString("tiba_jenis_banbkal", "original");
                        //("tiba_jenis_banbkal_pos", R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kanan_luar_original);
                        break;
                    case R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kanan_luar_vulkan:
                        prefManager.saveString("tiba_jenis_banbkal", "vulkan");
                        //("tiba_jenis_banbkal_pos", R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kanan_luar_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanBKaL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_luar_1:
                        prefManager.saveString("tiba_kondisi_banbkal", "25%");
                        //("tiba_kondisi_banbkal_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_luar_1);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_luar_2:
                        prefManager.saveString("tiba_kondisi_banbkal", "50%");
                        //("tiba_kondisi_banbkal_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_luar_2);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_luar_3:
                        prefManager.saveString("tiba_kondisi_banbkal", "75%");
                        //("tiba_kondisi_banbkal_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_luar_3);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_luar_4:
                        prefManager.saveString("tiba_kondisi_banbkal", "100%");
                        //("tiba_kondisi_banbkal_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_luar_4);
                        break;

                }
            }
        });

        trailerJenisBanBKaD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kanan_dalam_original:
                        prefManager.saveString("tiba_jenis_banbkad", "original");
                        //("tiba_jenis_banbkad_pos", R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kanan_dalam_original);
                        break;
                    case R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kanan_dalam_vulkan:
                        prefManager.saveString("tiba_jenis_banbkad", "vulkan");
                        //("tiba_jenis_banbkad_pos", R.id.choice_trailer_tiba_cek_jenis_ban_belakang_kanan_dalam_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanBKaD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_dalam_1:
                        prefManager.saveString("tiba_kondisi_banbkad", "25%");
                        //("tiba_kondisi_banbkad_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_dalam_1);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_dalam_2:
                        prefManager.saveString("tiba_kondisi_banbkad", "50%");
                        //("tiba_kondisi_banbkad_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_dalam_2);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_dalam_3:
                        prefManager.saveString("tiba_kondisi_banbkad", "75%");
                        //("tiba_kondisi_banbkad_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_dalam_3);
                        break;
                    case R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_dalam_4:
                        prefManager.saveString("tiba_kondisi_banbkad", "100%");
                        //("tiba_kondisi_banbkad_pos", R.id.choice_trailer_tiba_kondisi_ban_belakang_kanan_dalam_4);
                        break;

                }
            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trailer_tiba, container, false);

// Date Picker
        txtDate3 = (EditText) view.findViewById(R.id.text_trailer_tiba_date);
        txtDate3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment pickerFragment = new SelectDateFragment();
                pickerFragment.show(getFragmentManager(),"DatePicker");
            }
        });
        btnDatePicker3 = (Button) view.findViewById(R.id.button_trailer_tiba_date);
        btnDatePicker3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                txtDate3.setText( dayOfMonth + " - " + (monthOfYear + 1) + " - " + year);
                            }
                        }, mYear, mMonth, mDay);
                datePicker.show();
            }
        });
        return view;
    }
    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, mDay, mMonth, mYear);
        }

        public void onDateSet(DatePicker view, int mDay, int mMonth, int mYear) {
            populateSetDate(mDay, mMonth + 1, mYear);
        }

        public void populateSetDate(int day, int month, int year) {
            //set the date here
            populateSetDate(year, month + 1, day);
        }
    }
    private void LoadPage() {
        switchTibaStnk.setChecked(prefManager.catchBoolean("tiba_stnk"));
        switchTibaBukuHt.setChecked(prefManager.catchBoolean("tiba_ht_buku"));
        switchTibaBukuTrailer.setChecked(prefManager.catchBoolean("tiba_buku"));

//        bModaTrans, bHeadTruck, bCodeTrip, bNopol, bLwc, bCustomer, bKeterangan, bKeteranganHt
        //prefManager.deleteKey("tiba_date");
        tiba_moda.setText(prefManager.catchString("tiba_moda"));
        tiba_head_truck.setText(prefManager.catchString("tiba_head_truck"));
        tiba_code_trip.setText(prefManager.catchString("tiba_code_trip"));
        tiba_nopol.setText(prefManager.catchString("tiba_nopol"));
        tiba_lwc.setText(prefManager.catchString("tiba_lwc"));
        tiba_customer.setText(prefManager.catchString("tiba_customer"));
        //txtDate2.setText(prefManager.catchString("tiba_date"));
        tiba_keterangan_trailer.setText(prefManager.catchString("tiba_keterangan"));
        //bKeteranganHt.setText(prefManager.catchString("tiba_keterangan_ht"));

//        trailerCekLampu.check(prefManager.catchInt("tiba_cek_lampu_pos"));
//        trailerCekRem.check(prefManager.catchInt("tiba_cek_rem_pos"));
//        trailerCekBan.check(prefManager.catchInt("tiba_cek_ban_pos"));
//        trailerCekBanSerep.check(prefManager.catchInt("tiba_cek_ban_serep_pos"));
//        trailerCekPer.check(prefManager.catchInt("tiba_cek_per_pos"));
//        trailerKondisiTerhubung.check(prefManager.catchInt("tiba_cek_trailer_siap_pos"));
//        trailerJenisBanDKiL.check(prefManager.catchInt("tiba_jenis_bandkil_pos"));
    }
    public class AddTTiba extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            prefManager.saveString("tiba_moda"              ,tiba_moda              .getText().toString());
            prefManager.saveString("tiba_head_truck"        ,tiba_head_truck        .getText().toString());
            prefManager.saveString("tiba_code_trip"         ,tiba_code_trip         .getText().toString());
            prefManager.saveString("tiba_nopol"             ,tiba_nopol             .getText().toString());
            prefManager.saveString("tiba_lwc"               ,tiba_lwc               .getText().toString());
            prefManager.saveString("tiba_customer"          ,tiba_customer          .getText().toString());
            prefManager.saveString("tiba_keterangan_trailer",tiba_keterangan_trailer.getText().toString());

            progressBar.setVisibility(View.VISIBLE);
            //Toast.makeText(getActivity(), prefManager.catchString("pengisian_filling1_timeAwal"), Toast.LENGTH_SHORT).show();
        }
    }
    private class ResetTiba extends AsyncTask<String,String,String>
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

            prefManager.deleteKey("tiba_moda");
            prefManager.deleteKey("tiba_head_truck");
            prefManager.deleteKey("tiba_code_trip");
            prefManager.deleteKey("tiba_nopol");
            prefManager.deleteKey("tiba_lwc");
            prefManager.deleteKey("tiba_customer");
            //prefManager.deleteKey("tiba_date");
            prefManager.deleteKey("tiba_keterangan");
            //Toast.makeText(getActivity(), prefManager.catchString("pengisian_filling1_timeAwal"), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String r)
        {
            progress.dismiss();
            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
            LoadPage();
        }
        @Override
        protected String doInBackground(String... params) {
            z = "berhasil reset";
            return z;
        }
    }

}