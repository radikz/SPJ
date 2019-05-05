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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

import java.sql.Connection;
import java.util.Calendar;

import id.co.cng.spj.MenuUtamaActivity;
import id.co.cng.spj.R;
import id.co.cng.spj.utility.PrefManager;


public class Fragment_TBerangkat extends Fragment {

    Switch switchBerangkatStnk, switchBerangkatBukuHt, switchBerangkatBukuTrailer;
    TextView textBerangkatStnk, textBerangkatBukuHt, textBerangkatBukuTrailer;
    TextInputEditText bModaTrans, bHeadTruck, bCodeTrip, bNopol, bLwc, bCustomer, bKeterangan, bKeteranganHt;
    Button buttonKirimBerangkat, resetKirimBerangkat;
    SingleSelectToggleGroup trailerCekLampu, trailerCekRem, trailerCekBan, trailerCekBanSerep, trailerCekPer;
    SingleSelectToggleGroup trailerKondisiTerhubung;
    SingleSelectToggleGroup trailerJenisBanDKiL, trailerKondisiBanDKiL; //depan kiri luar
    SingleSelectToggleGroup trailerJenisBanDKiD, trailerKondisiBanDKiD; //depankiridalam
    SingleSelectToggleGroup trailerJenisBanDKaL, trailerKondisiBanDKaL; //depan kanan luar
    SingleSelectToggleGroup trailerJenisBanDKaD, trailerKondisiBanDKaD; //depan kanan dalam

    SingleSelectToggleGroup trailerJenisBanBKiL, trailerKondisiBanBKiL; //belakang kiri luar
    SingleSelectToggleGroup trailerJenisBanBKiD, trailerKondisiBanBKiD; //belakang kiri dalam
    SingleSelectToggleGroup trailerJenisBanBKaL, trailerKondisiBanBKaL; //belakang kanan luar
    SingleSelectToggleGroup trailerJenisBanBKaD, trailerKondisiBanBKaD; //belajang kanan dalam

    //HT
    SingleSelectToggleGroup trailerHtLampu, trailerHtCekRem, trailerHtCekBan, trailerHtCekBanSerep, trailerHtCekApar;
    SingleSelectToggleGroup trailerHtKondisiTerhubung;
    SingleSelectToggleGroup trailerJenisBanHtDKiL, trailerKondisiBanHtDKiL; //depan kiri luar
    SingleSelectToggleGroup trailerJenisBanHtDKiD, trailerKondisiBanHtDKiD; //depankiridalam
    SingleSelectToggleGroup trailerJenisBanHtDKaL, trailerKondisiBanHtDKaL; //depan kanan luar
    SingleSelectToggleGroup trailerJenisBanHtDKaD, trailerKondisiBanHtDKaD; //depan kanan dalam

    SingleSelectToggleGroup trailerJenisBanHtBKiL, trailerKondisiBanHtBKiL; //belakang kiri luar
    SingleSelectToggleGroup trailerJenisBanHtBKiD, trailerKondisiBanHtBKiD; //belakang kiri dalam
    SingleSelectToggleGroup trailerJenisBanHtBKaL, trailerKondisiBanHtBKaL; //belakang kanan luar
    SingleSelectToggleGroup trailerJenisBanHtBKaD, trailerKondisiBanHtBKaD; //belajang kanan dalam

    String switchOn  = "Yes";
    String switchOff = "No ";

    private Button btnDatePicker2;
    private EditText txtDate2;
    PrefManager prefManager;
    Connection con;

    public Fragment_TBerangkat(){}
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MenuUtamaActivity) getActivity())
                .setActionBarTitle("Trailer Berangkat");

        // For switch button 23
//        bModaTrans, bHeadTruck, bCodeTrip, bNopol, bLwc, bCustomer, bKeterangan, bKeteranganHt;
        bModaTrans = view.findViewById(R.id.text_trailer_berangkat_moda_transport);
        bHeadTruck = view.findViewById(R.id.text_trailer_berangkat_head_truck);
        bCodeTrip = view.findViewById(R.id.text_trailer_berangkat_kode_trip);
        bNopol = view.findViewById(R.id.text_trailer_berangkat_nopol);
        bLwc = view.findViewById(R.id.text_trailer_berangkat_lwc);
        bCustomer = view.findViewById(R.id.text_trailer_berangkat_customer);
        bKeterangan = view.findViewById(R.id.text_trailer_berangkat_keterangan);
        bKeteranganHt = view.findViewById(R.id.text_ht_berangkat_keterangan);

        switchBerangkatStnk = view.findViewById(R.id.switch_trailer_berangkat_stnk);
        textBerangkatStnk = view.findViewById(R.id.textView_trailer_berangkat_stnk);
        switchBerangkatBukuHt = view.findViewById(R.id.switch_trailer_berangkat_buku_ht);
        textBerangkatBukuHt = view.findViewById(R.id.textView_trailer_berangkat_buku_ht);
        switchBerangkatBukuTrailer = view.findViewById(R.id.switch_trailer_berangkat_buku_trailer);
        textBerangkatBukuTrailer = view.findViewById(R.id.textView_trailer_berangkat_buku_trailer);

        trailerCekLampu = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_lampu);
        trailerCekRem = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_rem);
        trailerCekBan = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_ban);
        trailerCekBanSerep = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_ban_serep);
        trailerCekPer = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_per);
        trailerKondisiTerhubung = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_kondisi_trailer);

        trailerJenisBanDKiL = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_jenis_ban_depan_kiri_luar);
        trailerKondisiBanDKiL = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_kondisi_ban_depan_kiri_luar);
//        trailerJenisBanDKiD, trailerKondisiBanDKiD; //depankiridalam
        trailerJenisBanDKiD = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_jenis_ban_depan_kiri_dalam);
        trailerKondisiBanDKiD = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_kondisi_ban_depan_kiri_dalam);
//        trailerJenisBanDKaL, trailerKondisiBanDKaL; //depan kanan luar
        trailerJenisBanDKaL = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_jenis_ban_depan_kanan_luar);
        trailerKondisiBanDKaL = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_kondisi_ban_depan_kanan_luar);
//        trailerJenisBanDKaD, trailerKondisiBanDKaD; //depan kanan dalam
        trailerJenisBanDKaD = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_jenis_ban_depan_kanan_dalam);
        trailerKondisiBanDKaD = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_kondisi_ban_depan_kanan_dalam);
//        trailerJenisBanBKiL, trailerKondisiBanBKiL; //belakang kiri luar
        trailerJenisBanBKiL = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_jenis_ban_belakang_kiri_luar);
        trailerKondisiBanBKiL = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_kondisi_ban_belakang_kiri_luar);
//        trailerJenisBanBKiD, trailerKondisiBanBKiD; //belakang kiri dalam
        trailerJenisBanBKiD = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_jenis_ban_belakang_kiri_dalam);
        trailerKondisiBanBKiD = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_kondisi_ban_belakang_kiri_dalam);
//        trailerJenisBanBKaL, trailerKondisiBanBKaL; //belakang kanan luar
        trailerJenisBanBKaL = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_jenis_ban_belakang_kanan_luar);
        trailerKondisiBanBKaL = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_kondisi_ban_belakang_kanan_luar);
//        trailerJenisBanBKaD, trailerKondisiBanBKaD; //belajang kanan dalam
        trailerJenisBanBKaD = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_cek_jenis_ban_belakang_kanan_dalam);
        trailerKondisiBanBKaD = view.findViewById(R.id.group_single_radiobutton_trailer_berangkat_kondisi_ban_belakang_kanan_dalam);
//        trailerHtLampu, trailerHtCekRem, trailerHtCekBan, trailerHtCekBanSerep, trailerHtCekApar;
        trailerHtLampu = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_lampu);
        trailerHtCekRem = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_rem);
        trailerHtCekBan = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_ban);
        trailerHtCekBanSerep = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_ban_serep);
        trailerHtCekApar = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_apar);
        trailerHtKondisiTerhubung = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_piringan);
//        trailerJenisBanHtDKiL, trailerKondisiBanHtDKiL; //depan kiri luar
        trailerJenisBanHtDKiL = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_jenis_ban_depan_kiri_luar);
        trailerKondisiBanHtDKiL = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_kondisi_ban_depan_kiri_luar);
//        trailerJenisBanHtDKiD, trailerKondisiBanHtDKiD; //depankiridalam
        trailerJenisBanHtDKiD = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_jenis_ban_depan_kiri_dalam);
        trailerKondisiBanHtDKiD = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_kondisi_ban_depan_kiri_dalam);
//        trailerJenisBanHtDKaL, trailerKondisiBanHtDKaL; //depan kanan luar
        trailerJenisBanHtDKaL = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_jenis_ban_depan_kanan_luar);
        trailerKondisiBanHtDKaL = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_kondisi_ban_depan_kanan_luar);
//        trailerJenisBanHtDKaD, trailerKondisiBanHtDKaD; //depan kanan dalam
        trailerJenisBanHtDKaD = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_jenis_ban_depan_kanan_dalam);
        trailerKondisiBanHtDKaD = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_kondisi_ban_depan_kanan_dalam);
//        trailerJenisBanHtBKiL, trailerKondisiBanHtBKiL; //belakang kiri luar
        trailerJenisBanHtBKiL = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_jenis_ban_belakang_kiri_luar);
        trailerKondisiBanHtBKiL = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_kondisi_ban_belakang_kiri_luar);
//        trailerJenisBanHtBKiD, trailerKondisiBanHtBKiD; //belakang kiri dalam
        trailerJenisBanHtBKiD = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_jenis_ban_belakang_kiri_dalam);
        trailerKondisiBanHtBKiD = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_kondisi_ban_belakang_kiri_dalam);
//        trailerJenisBanHtBKaL, trailerKondisiBanHtBKaL; //belakang kanan luar
        trailerJenisBanHtBKaL = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_jenis_ban_belakang_kanan_luar);
        trailerKondisiBanHtBKaL = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_kondisi_ban_belakang_kanan_luar);
//        trailerJenisBanHtBKaD, trailerKondisiBanHtBKaD; //belajang kanan dalam
        trailerJenisBanHtBKaD = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_jenis_ban_belakang_kanan_dalam);
        trailerKondisiBanHtBKaD = view.findViewById(R.id.group_single_radiobutton_ht_berangkat_cek_kondisi_ban_belakang_kanan_dalam);
        

        buttonKirimBerangkat = view.findViewById(R.id.button_trailer_berangkat_kirim);
        resetKirimBerangkat = view.findViewById(R.id.button_trailer_berangkat_reset);

        prefManager = new PrefManager(getActivity());

        LoadPage();

        buttonKirimBerangkat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            KirimBerangkat kirimBerangkat = new KirimBerangkat();
            kirimBerangkat.execute("");


            }
        });

        resetKirimBerangkat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ResetBerangkat resetBerangkat = new ResetBerangkat();
                resetBerangkat.execute("");
            }
        });

        trailerCekLampu.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala:
                        prefManager.saveString("tb_cek_lampu", "off");
                        //("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_trailer_berangkat_cek_lampu_nyala:
                        prefManager.saveString("tb_cek_lampu", "on");
                        //("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        trailerCekRem.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_rem_fungsi:
                        prefManager.saveString("tb_cek_rem", "fungsi");
                        //("tb_cek_rem_pos", R.id.choice_trailer_berangkat_cek_rem_fungsi);
                        break;
                    case R.id.choice_trailer_berangkat_cek_rem_tidak_fungsi:
                        prefManager.saveString("tb_cek_rem", "tidak berfungsi");
                        //("tb_cek_rem_pos", R.id.choice_trailer_berangkat_cek_rem_tidak_fungsi);
                        break;

                }
            }
        });

        trailerCekBan.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_ban_bocor:
                        prefManager.saveString("tb_cek_ban", "bocor");
                        //("tb_cek_ban_pos", R.id.choice_trailer_berangkat_cek_ban_bocor);
                        break;
                    case R.id.choice_trailer_berangkat_cek_ban_tidak_bocor:
                        prefManager.saveString("tb_cek_ban", "tidak bocor");
                        //("tb_cek_ban_pos", R.id.choice_trailer_berangkat_cek_ban_tidak_bocor);
                        break;

                }
            }
        });

        trailerCekBanSerep.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_ban_serep_ada:
                        prefManager.saveString("tb_cek_ban_serep", "ada");
                        //("tb_cek_ban_serep_pos", R.id.choice_trailer_berangkat_cek_ban_serep_ada);
                        break;
                    case R.id.choice_trailer_berangkat_cek_ban_serep_tidak_ada:
                        prefManager.saveString("tb_cek_ban_serep", "tidak ada");
                        //("tb_cek_ban_serep_pos", R.id.choice_trailer_berangkat_cek_ban_serep_tidak_ada);
                        break;

                }
            }
        });

        trailerCekPer.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_per_patah:
                        prefManager.saveString("tb_cek_per", "patah");
                        //("tb_cek_per_pos", R.id.choice_trailer_berangkat_cek_per_patah);
                        break;
                    case R.id.choice_trailer_berangkat_cek_per_tidak_patah:
                        prefManager.saveString("tb_cek_per", "tidak patah");
                        //("tb_cek_per_pos", R.id.choice_trailer_berangkat_cek_per_tidak_patah);
                        break;

                }
            }
        });

        trailerKondisiTerhubung.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_kondisi_trailer_siap_pakai:
                        prefManager.saveString("tb_cek_trailer_siap", "siap");
                        //("tb_cek_trailer_siap_pos", R.id.choice_trailer_berangkat_cek_kondisi_trailer_siap_pakai);
                        break;
                    case R.id.choice_trailer_berangkat_cek_kondisi_trailer_tidak_siap:
                        prefManager.saveString("tb_cek_trailer_siap", "tidak siap");
                        //("tb_cek_trailer_siap_pos", R.id.choice_trailer_berangkat_cek_kondisi_trailer_tidak_siap);
                        break;


                }
            }
        });

        trailerJenisBanDKiL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kiri_luar_original:
                        prefManager.saveString("tb_jenis_bandkil", "original");
                        //("tb_jenis_bandkil_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kiri_luar_original);
                        break;
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kiri_luar_vulkan:
                        prefManager.saveString("tb_jenis_bandkil", "vulkan");
                        //("tb_jenis_bandkil_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kiri_luar_vulkan);
                        break;
                }
            }
        });

        trailerKondisiBanDKiL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_luar_1:
                        prefManager.saveString("tb_kondisi_bandkil", "25%");
                        //("tb_kondisi_bandkil_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_luar_1);
                        //Toast.makeText(getActivity(), String.valueOf(//("tb_kondisi_bandkil_pos")), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_luar_2:
                        prefManager.saveString("tb_kondisi_bandkil", "50%");
                        //("tb_kondisi_bandkil_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_luar_2);
                        Toast.makeText(getActivity(), String.valueOf(R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_luar_2), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_luar_3:
                        prefManager.saveString("tb_kondisi_bandkil", "75%");
                        //("tb_kondisi_bandkil_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_luar_3);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_luar_4:
                        prefManager.saveString("tb_kondisi_bandkil", "100%");
                        //("tb_kondisi_bandkil_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_luar_4);
                        break;

                }
            }
        });

        trailerJenisBanDKiD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kiri_dalam_original:
                        prefManager.saveString("tb_jenis_bandkid", "original");
                        //("tb_jenis_bandkid_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kiri_dalam_original);
                        break;
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kiri_dalam_vulkan:
                        prefManager.saveString("tb_jenis_bandkid", "vulkan");
                        //("tb_jenis_bandkid_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kiri_dalam_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanDKiD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_dalam_1:
                        prefManager.saveString("tb_kondisi_bandkid", "25%");
                        //("tb_kondisi_bandkid_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_dalam_1);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_dalam_2:
                        prefManager.saveString("tb_kondisi_bandkid", "50%");
                        //("tb_kondisi_bandkid_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_dalam_2);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_dalam_3:
                        prefManager.saveString("tb_kondisi_bandkid", "75%");
                        //("tb_kondisi_bandkid_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_dalam_3);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_dalam_4:
                        prefManager.saveString("tb_kondisi_bandkid", "100%");
                        //("tb_kondisi_bandkid_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_dalam_4);
                        break;

                }
            }
        });

        trailerJenisBanDKaL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kanan_luar_original:
                        prefManager.saveString("tb_jenis_bandkal", "original");
                        //("tb_jenis_bandkal_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kanan_luar_original);
                        break;
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kanan_luar_vulkan:
                        prefManager.saveString("tb_jenis_bandkal", "vulkan");
                        //("tb_jenis_bandkal_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kanan_luar_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanDKaL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_luar_1:
                        prefManager.saveString("tb_kondisi_bandkal", "25%");
                        //("tb_kondisi_bandkal_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_luar_1);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_luar_2:
                        prefManager.saveString("tb_kondisi_bandkal", "50%");
                        //("tb_kondisi_bandkal_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_luar_2);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_luar_3:
                        prefManager.saveString("tb_kondisi_bandkal", "75%");
                        //("tb_kondisi_bandkal_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_luar_3);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_luar_4:
                        prefManager.saveString("tb_kondisi_bandkal", "100%");
                        //("tb_kondisi_bandkal_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_luar_4);
                        break;

                }
            }
        });

        trailerJenisBanDKaD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kanan_dalam_original:
                        prefManager.saveString("tb_jenis_bandkad", "original");
                        //("tb_jenis_bandkad_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kanan_dalam_original);
                        break;
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kanan_luar_vulkan:
                        prefManager.saveString("tb_jenis_bandkad", "vulkan");
                        //("tb_jenis_bandkad_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kanan_dalam_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanDKaD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_dalam_1:
                        prefManager.saveString("tb_kondisi_bandkad", "25%");
                        //("tb_kondisi_bandkad_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_dalam_1);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_dalam_2:
                        prefManager.saveString("tb_kondisi_bandkad", "50%");
                        //("tb_kondisi_bandkad_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_dalam_2);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_dalam_3:
                        prefManager.saveString("tb_kondisi_bandkad", "75%");
                        //("tb_kondisi_bandkad_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_dalam_3);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_dalam_4:
                        prefManager.saveString("tb_kondisi_bandkad", "100%");
                        //("tb_kondisi_bandkad_pos", R.id.choice_trailer_berangkat_kondisi_ban_depan_kanan_dalam_4);
                        break;

                }
            }
        });

        trailerJenisBanBKiL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kiri_luar_original:
                        prefManager.saveString("tb_jenis_banbkil", "original");
                        //("tb_jenis_banbkil_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kiri_luar_original);
                        break;
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kiri_luar_vulkan:
                        prefManager.saveString("tb_jenis_banbkil", "vulkan");
                        //("tb_jenis_banbkil_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kiri_luar_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanBKiL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_luar_1:
                        prefManager.saveString("tb_kondisi_banbkil", "25%");
                        //("tb_kondisi_banbkil_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_luar_1);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_luar_2:
                        prefManager.saveString("tb_kondisi_banbkil", "50%");
                        //("tb_kondisi_banbkil_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_luar_2);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_luar_3:
                        prefManager.saveString("tb_kondisi_banbkil", "75%");
                        //("tb_kondisi_banbkil_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_luar_3);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_luar_4:
                        prefManager.saveString("tb_kondisi_banbkil", "100%");
                        //("tb_kondisi_banbkil_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_luar_4);
                        break;


                }
            }
        });

        trailerJenisBanBKiD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kiri_dalam_original:
                        prefManager.saveString("tb_jenis_banbkid", "original");
                        //("tb_jenis_banbkid_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kiri_dalam_original);
                        break;
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kiri_dalam_vulkan:
                        prefManager.saveString("tb_jenis_banbkid", "vulkan");
                        //("tb_jenis_banbkid_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kiri_dalam_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanBKiD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_dalam_1:
                        prefManager.saveString("tb_kondisi_banbkid", "25%");
                        //("tb_kondisi_banbkid_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_dalam_1);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_dalam_2:
                        prefManager.saveString("tb_kondisi_banbkid", "50%");
                        //("tb_kondisi_banbkid_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_dalam_2);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_dalam_3:
                        prefManager.saveString("tb_kondisi_banbkid", "75%");
                        //("tb_kondisi_banbkid_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_dalam_3);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_dalam_4:
                        prefManager.saveString("tb_kondisi_banbkid", "100%");
                        //("tb_kondisi_banbkid_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kiri_dalam_4);
                        break;

                }
            }
        });

        trailerJenisBanBKaL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kanan_luar_original:
                        prefManager.saveString("tb_jenis_banbkal", "original");
                        //("tb_jenis_banbkal_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kanan_luar_original);
                        break;
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kanan_luar_vulkan:
                        prefManager.saveString("tb_jenis_banbkal", "vulkan");
                        //("tb_jenis_banbkal_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kanan_luar_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanBKaL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_luar_1:
                        prefManager.saveString("tb_kondisi_banbkal", "25%");
                        //("tb_kondisi_banbkal_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_luar_1);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_luar_2:
                        prefManager.saveString("tb_kondisi_banbkal", "50%");
                        //("tb_kondisi_banbkal_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_luar_2);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_luar_3:
                        prefManager.saveString("tb_kondisi_banbkal", "75%");
                        //("tb_kondisi_banbkal_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_luar_3);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_luar_4:
                        prefManager.saveString("tb_kondisi_banbkal", "100%");
                        //("tb_kondisi_banbkal_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_luar_4);
                        break;

                }
            }
        });

        trailerJenisBanBKaD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kanan_dalam_original:
                        prefManager.saveString("tb_jenis_banbkad", "original");
                        //("tb_jenis_banbkad_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kanan_dalam_original);
                        break;
                    case R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kanan_dalam_vulkan:
                        prefManager.saveString("tb_jenis_banbkad", "vulkan");
                        //("tb_jenis_banbkad_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_belakang_kanan_dalam_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanBKaD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_dalam_1:
                        prefManager.saveString("tb_kondisi_banbkad", "25%");
                        //("tb_kondisi_banbkad_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_dalam_1);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_dalam_2:
                        prefManager.saveString("tb_kondisi_banbkad", "50%");
                        //("tb_kondisi_banbkad_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_dalam_2);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_dalam_3:
                        prefManager.saveString("tb_kondisi_banbkad", "75%");
                        //("tb_kondisi_banbkad_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_dalam_3);
                        break;
                    case R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_dalam_4:
                        prefManager.saveString("tb_kondisi_banbkad", "100%");
                        //("tb_kondisi_banbkad_pos", R.id.choice_trailer_berangkat_kondisi_ban_belakang_kanan_dalam_4);
                        break;

                }
            }
        });

        //HT

        trailerHtLampu.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_lampu_tidak_nyala:
                        prefManager.saveString("tb_cek_lampu_ht", "tidak menyala");
                        break;
                    case R.id.choice_ht_berangkat_cek_lampu_nyala:
                        prefManager.saveString("tb_cek_lampu_ht", "menyala");
                        break;

                }
            }
        });

        trailerHtCekRem.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_rem_fungsi:
                        prefManager.saveString("tb_cek_rem_ht", "fungsi");
                        break;
                    case R.id.choice_ht_berangkat_cek_rem_tidak_fungsi:
                        prefManager.saveString("tb_cek_rem_ht", "tidak berfungsi");
                        break;

                }
            }
        });

        trailerHtCekBan.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_ban_bocor:
                        prefManager.saveString("tb_cek_ban_ht", "bocor");
                        break;
                    case R.id.choice_ht_berangkat_cek_ban_tidak_bocor:
                        prefManager.saveString("tb_cek_ban_ht", "tidak");
                        break;

                }
            }
        });

        trailerHtCekBanSerep.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_ban_serep_ada:
                        prefManager.saveString("tb_cek_ban_serep_ht", "ada");
                        break;
                    case R.id.choice_ht_berangkat_cek_ban_serep_tidak_ada:
                        prefManager.saveString("tb_cek_ban_serep_ht", "tidak ada");
                        break;

                }
            }
        });

        trailerHtCekApar.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_apar_ada:
                        prefManager.saveString("tb_cek_apar_ht", "ada");
                        break;
                    case R.id.choice_ht_berangkat_cek_apar_tidak_ada:
                        prefManager.saveString("tb_cek_apar_ht", "tidak ada");
                        break;

                }
            }
        });

        trailerHtKondisiTerhubung.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_piringan_siap:
                        prefManager.saveString("tb_cek_piringan_ht", "ada");
                        break;
                    case R.id.choice_ht_berangkat_cek_piringan_tidak_siap:
                        prefManager.saveString("tb_cek_piringan_ht", "tidak ada");
                        break;

                }
            }
        });

        trailerJenisBanHtDKiL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_jenis_ban_depan_kiri_luar_original:
                        prefManager.saveString("tb_jenis_bandkil_ht", "original");
                        //("tb_jenis_bandkil_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kiri_luar_original);
                        break;
                    case R.id.choice_ht_berangkat_cek_jenis_ban_depan_kiri_luar_vulkan:
                        prefManager.saveString("tb_jenis_bandkil_ht", "vulkan");
                        //("tb_jenis_bandkil_pos", R.id.choice_trailer_berangkat_cek_jenis_ban_depan_kiri_luar_vulkan);
                        break;
                }
            }
        });

        trailerKondisiBanHtDKiL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_luar_1:
                        prefManager.saveString("tb_kondisi_bandkil_ht", "25%");
                        //("tb_kondisi_bandkil_pos_ht", R.id.choice_trailer_berangkat_kondisi_ban_depan_kiri_luar_1);
                        //Toast.makeText(getActivity(), String.valueOf(//("tb_kondisi_bandkil_pos")), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_luar_2:
                        prefManager.saveString("tb_kondisi_bandkil_ht_ht", "50%");
                        //("tb_kondisi_bandkil_pos_ht", R.id.choice_ht_berangkat_kondisi_ban_depan_kiri_luar_2);
                        Toast.makeText(getActivity(), String.valueOf(R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_luar_2), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_luar_3:
                        prefManager.saveString("tb_kondisi_bandkil_ht", "75%");
                        //("tb_kondisi_bandkil_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_luar_3);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_luar_4:
                        prefManager.saveString("tb_kondisi_bandkil_ht", "100%");
                        //("tb_kondisi_bandkil_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_luar_4);
                        break;

                }
            }
        });

        trailerJenisBanDKiD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_jenis_ban_depan_kiri_dalam_original:
                        prefManager.saveString("tb_jenis_bandkid_ht", "original");
                        //("tb_jenis_bandkid_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_depan_kiri_dalam_original);
                        break;
                    case R.id.choice_ht_berangkat_cek_jenis_ban_depan_kiri_dalam_vulkan:
                        prefManager.saveString("tb_jenis_bandkid_ht", "vulkan");
                        //("tb_jenis_bandkid_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_depan_kiri_dalam_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanDKiD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_dalam_1:
                        prefManager.saveString("tb_kondisi_bandkid_ht", "25%");
                        //("tb_kondisi_bandkid_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_dalam_1);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_dalam_2:
                        prefManager.saveString("tb_kondisi_bandkid_ht", "50%");
                        //("tb_kondisi_bandkid_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_dalam_2);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_dalam_3:
                        prefManager.saveString("tb_kondisi_bandkid_ht", "75%");
                        //("tb_kondisi_bandkid_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_dalam_3);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_dalam_4:
                        prefManager.saveString("tb_kondisi_bandkid_ht", "100%");
                        //("tb_kondisi_bandkid_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kiri_dalam_4);
                        break;

                }
            }
        });

        trailerJenisBanDKaL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_jenis_ban_depan_kanan_luar_original:
                        prefManager.saveString("tb_jenis_bandkal_ht", "original");
                        //("tb_jenis_bandkal_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_depan_kanan_luar_original);
                        break;
                    case R.id.choice_ht_berangkat_cek_jenis_ban_depan_kanan_luar_vulkan:
                        prefManager.saveString("tb_jenis_bandkal_ht", "vulkan");
                        //("tb_jenis_bandkal_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_depan_kanan_luar_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanDKaL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_luar_1:
                        prefManager.saveString("tb_kondisi_bandkal_ht", "25%");
                        //("tb_kondisi_bandkal_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_luar_1);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_luar_2:
                        prefManager.saveString("tb_kondisi_bandkal_ht", "50%");
                        //("tb_kondisi_bandkal_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_luar_2);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_luar_3:
                        prefManager.saveString("tb_kondisi_bandkal_ht", "75%");
                        //("tb_kondisi_bandkal_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_luar_3);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_luar_4:
                        prefManager.saveString("tb_kondisi_bandkal_ht", "100%");
                        //("tb_kondisi_bandkal_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_luar_4);
                        break;

                }
            }
        });

        trailerJenisBanDKaD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_jenis_ban_depan_kanan_dalam_original:
                        prefManager.saveString("tb_jenis_bandkad_ht", "original");
                        //("tb_jenis_bandkad_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_depan_kanan_dalam_original);
                        break;
                    case R.id.choice_ht_berangkat_cek_jenis_ban_depan_kanan_luar_vulkan:
                        prefManager.saveString("tb_jenis_bandkad_ht", "vulkan");
                        //("tb_jenis_bandkad_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_depan_kanan_dalam_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanDKaD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_dalam_1:
                        prefManager.saveString("tb_kondisi_bandkad_ht", "25%");
                        //("tb_kondisi_bandkad_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_dalam_1);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_dalam_2:
                        prefManager.saveString("tb_kondisi_bandkad_ht", "50%");
                        //("tb_kondisi_bandkad_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_dalam_2);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_dalam_3:
                        prefManager.saveString("tb_kondisi_bandkad_ht", "75%");
                        //("tb_kondisi_bandkad_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_dalam_3);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_dalam_4:
                        prefManager.saveString("tb_kondisi_bandkad_ht", "100%");
                        //("tb_kondisi_bandkad_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_depan_kanan_dalam_4);
                        break;

                }
            }
        });

        trailerJenisBanBKiL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kiri_luar_original:
                        prefManager.saveString("tb_jenis_banbkil_ht", "original");
                        //("tb_jenis_banbkil_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kiri_luar_original);
                        break;
                    case R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kiri_luar_vulkan:
                        prefManager.saveString("tb_jenis_banbkil_ht", "vulkan");
                        //("tb_jenis_banbkil_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kiri_luar_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanBKiL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_luar_1:
                        prefManager.saveString("tb_kondisi_banbkil_ht", "25%");
                        //("tb_kondisi_banbkil_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_luar_1);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_luar_2:
                        prefManager.saveString("tb_kondisi_banbkil_ht", "50%");
                        //("tb_kondisi_banbkil_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_luar_2);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_luar_3:
                        prefManager.saveString("tb_kondisi_banbkil_ht", "75%");
                        //("tb_kondisi_banbkil_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_luar_3);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_luar_4:
                        prefManager.saveString("tb_kondisi_banbkil_ht", "100%");
                        //("tb_kondisi_banbkil_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_luar_4);
                        break;


                }
            }
        });

        trailerJenisBanBKiD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kiri_dalam_original:
                        prefManager.saveString("tb_jenis_banbkid_ht", "original");
                        //("tb_jenis_banbkid_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kiri_dalam_original);
                        break;
                    case R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kiri_dalam_vulkan:
                        prefManager.saveString("tb_jenis_banbkid_ht", "vulkan");
                        //("tb_jenis_banbkid_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kiri_dalam_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanBKiD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_dalam_1:
                        prefManager.saveString("tb_kondisi_banbkid_ht", "25%");
                        //("tb_kondisi_banbkid_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_dalam_1);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_dalam_2:
                        prefManager.saveString("tb_kondisi_banbkid_ht", "50%");
                        //("tb_kondisi_banbkid_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_dalam_2);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_dalam_3:
                        prefManager.saveString("tb_kondisi_banbkid_ht", "75%");
                        //("tb_kondisi_banbkid_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_dalam_3);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_dalam_4:
                        prefManager.saveString("tb_kondisi_banbkid_ht", "100%");
                        //("tb_kondisi_banbkid_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kiri_dalam_4);
                        break;

                }
            }
        });

        trailerJenisBanBKaL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kanan_luar_original:
                        prefManager.saveString("tb_jenis_banbkal_ht", "original");
                        //("tb_jenis_banbkal_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kanan_luar_original);
                        break;
                    case R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kanan_luar_vulkan:
                        prefManager.saveString("tb_jenis_banbkal_ht", "vulkan");
                        //("tb_jenis_banbkal_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kanan_luar_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanBKaL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_luar_1:
                        prefManager.saveString("tb_kondisi_banbkal_ht", "25%");
                        //("tb_kondisi_banbkal_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_luar_1);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_luar_2:
                        prefManager.saveString("tb_kondisi_banbkal_ht", "50%");
                        //("tb_kondisi_banbkal_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_luar_2);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_luar_3:
                        prefManager.saveString("tb_kondisi_banbkal_ht", "75%");
                        //("tb_kondisi_banbkal_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_luar_3);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_luar_4:
                        prefManager.saveString("tb_kondisi_banbkal_ht", "100%");
                        //("tb_kondisi_banbkal_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_luar_4);
                        break;

                }
            }
        });

        trailerJenisBanBKaD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kanan_dalam_original:
                        prefManager.saveString("tb_jenis_banbkad_ht", "original");
                        //("tb_jenis_banbkad_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kanan_dalam_original);
                        break;
                    case R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kanan_dalam_vulkan:
                        prefManager.saveString("tb_jenis_banbkad_ht", "vulkan");
                        //("tb_jenis_banbkad_pos_ht", R.id.choice_ht_berangkat_cek_jenis_ban_belakang_kanan_dalam_vulkan);
                        break;

                }
            }
        });

        trailerKondisiBanBKaD.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_dalam_1:
                        prefManager.saveString("tb_kondisi_banbkad_ht", "25%");
                        //("tb_kondisi_banbkad_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_dalam_1);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_dalam_2:
                        prefManager.saveString("tb_kondisi_banbkad_ht", "50%");
                        //("tb_kondisi_banbkad_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_dalam_2);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_dalam_3:
                        prefManager.saveString("tb_kondisi_banbkad_ht", "75%");
                        //("tb_kondisi_banbkad_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_dalam_3);
                        break;
                    case R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_dalam_4:
                        prefManager.saveString("tb_kondisi_banbkad_ht", "100%");
                        //("tb_kondisi_banbkad_pos_ht", R.id.choice_ht_berangkat_cek_kondisi_ban_belakang_kanan_dalam_4);
                        break;

                }
            }
        });

        switchBerangkatStnk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textBerangkatStnk.setText(switchOn);
                } else {
                    textBerangkatStnk.setText(switchOff);
                }
            }
        });

        if (switchBerangkatStnk.isChecked()) {
            textBerangkatStnk.setText(switchOn);
        } else {
            textBerangkatStnk.setText(switchOff);
        }

        // For switch button 24



        switchBerangkatBukuHt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textBerangkatBukuHt.setText(switchOn);
                } else {
                    textBerangkatBukuHt.setText(switchOff);
                }
            }
        });

        if (switchBerangkatBukuHt.isChecked()) {
            textBerangkatBukuHt.setText(switchOn);
        } else {
            textBerangkatBukuHt.setText(switchOff);
        }

        // For switch button 25



        switchBerangkatBukuTrailer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textBerangkatBukuTrailer.setText(switchOn);
                } else {
                    textBerangkatBukuTrailer.setText(switchOff);
                }
            }
        });

        if (switchBerangkatBukuTrailer.isChecked()) {
            textBerangkatBukuTrailer.setText(switchOn);
        } else {
            textBerangkatBukuTrailer.setText(switchOff);
        }

        // Date Picker
        txtDate2 = view.findViewById(R.id.text_trailer_berangkat_date);
        txtDate2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment pickerFragment = new SelectDateFragment();
                pickerFragment.show(getFragmentManager(),"DatePicker");
            }
        });
        btnDatePicker2 = view.findViewById(R.id.button_trailer_berangkat_date);
        btnDatePicker2.setOnClickListener(new View.OnClickListener() {
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
                                txtDate2.setText( dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePicker.show();
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trailer_berangkat, container, false);


        return view;
    }
    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, mDay, mMonth, mYear);
        }

        public void onDateSet(DatePicker view, int mDay, int mMonth, int mYear) {
            populateSetDate(mDay, mMonth+1, mYear);
        }
        public void populateSetDate(int day, int month, int year) {
            //set the date here
            populateSetDate(year, month+1,day);
        }
    }

    private void LoadPage() {
        switchBerangkatStnk.setChecked(prefManager.catchBoolean("tb_stnk"));
        switchBerangkatBukuHt.setChecked(prefManager.catchBoolean("berangkat_ht_buku"));
        switchBerangkatBukuTrailer.setChecked(prefManager.catchBoolean("tb_buku"));

//        bModaTrans, bHeadTruck, bCodeTrip, bNopol, bLwc, bCustomer, bKeterangan, bKeteranganHt
        //prefManager.deleteKey("tb_date");
        bModaTrans.setText(prefManager.catchString("tb_moda"));
        bHeadTruck.setText(prefManager.catchString("tb_head_truck"));
        bCodeTrip.setText(prefManager.catchString("tb_code_trip"));
        bNopol.setText(prefManager.catchString("tb_nopol"));
        bLwc.setText(prefManager.catchString("tb_lwc"));
        bCustomer.setText(prefManager.catchString("tb_customer"));
        //txtDate2.setText(prefManager.catchString("tb_date"));
        bKeterangan.setText(prefManager.catchString("tb_keterangan"));
        bKeteranganHt.setText(prefManager.catchString("tb_keterangan_ht"));

//        trailerCekLampu.check(//("tb_cek_lampu_pos"));
//        trailerCekRem.check(//("tb_cek_rem_pos"));
//        trailerCekBan.check(//("tb_cek_ban_pos"));
//        trailerCekBanSerep.check(//("tb_cek_ban_serep_pos"));
//        trailerCekPer.check(//("tb_cek_per_pos"));
//        trailerKondisiTerhubung.check(//("tb_cek_trailer_siap_pos"));
//
//        trailerJenisBanDKiL.check(//("tb_jenis_bandkil_pos"));
//        trailerKondisiBanDKiL.check(//("tb_kondisi_bandkil_pos"));
//
//        trailerJenisBanDKiD.check(//("tb_jenis_bandkid_pos"));
//        trailerKondisiBanDKiD.check(//("tb_kondisi_bandkid_pos"));
//
//        trailerJenisBanDKaL.check(//("tb_jenis_bandkal_pos"));
//        trailerKondisiBanDKaL.check(//("tb_kondisi_bandkal_pos"));
//
//        trailerJenisBanDKaD.check(//("tb_jenis_bandkad_pos"));
//        trailerKondisiBanDKaD.check(//("tb_kondisi_bandkad_pos"));
//
//        trailerJenisBanBKiL.check(//("tb_jenis_banbkil_pos"));
//        trailerKondisiBanBKiL.check(//("tb_kondisi_banbkil_pos"));
//
//        trailerJenisBanBKiD.check(//("tb_jenis_banbkid_pos"));
//        trailerKondisiBanBKiD.check(//("tb_kondisi_banbkid_pos"));
//
//        trailerJenisBanBKaL.check(//("tb_jenis_banbkal_pos"));
//        trailerKondisiBanBKaL.check(//("tb_kondisi_banbkal_pos"));
//
//        trailerJenisBanBKaD.check(//("tb_jenis_banbkad_pos"));
//        trailerKondisiBanBKaD.check(//("tb_kondisi_banbkad_pos"));
    }

    private class ResetBerangkat extends AsyncTask<String,String,String>
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

            prefManager.deleteKey("tb_moda");
            prefManager.deleteKey("tb_head_truck");
            prefManager.deleteKey("tb_code_trip");
            prefManager.deleteKey("tb_nopol");
            prefManager.deleteKey("tb_lwc");
            prefManager.deleteKey("tb_customer");
            //prefManager.deleteKey("tb_date");
            prefManager.deleteKey("tb_keterangan");
            prefManager.deleteKey("tb_keterangan_ht");
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

    private class KirimBerangkat extends AsyncTask<String,String,String>
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

            prefManager.saveString("tb_moda", String.valueOf(bModaTrans.getText()));
            prefManager.saveString("tb_head_truck", String.valueOf(bHeadTruck.getText()));
            prefManager.saveString("tb_code_trip", String.valueOf(bCodeTrip.getText()));
            prefManager.saveString("tb_nopol", String.valueOf(bNopol.getText()));
            prefManager.saveString("tb_lwc", String.valueOf(bLwc.getText()));
            prefManager.saveString("tb_customer", String.valueOf(bCustomer.getText()));
            //prefManager.saveString("tb_date", String.valueOf(txtDate2.getText()));
            prefManager.saveString("tb_keterangan", String.valueOf(bKeterangan.getText()));
            prefManager.saveString("tb_keterangan_ht", String.valueOf(bKeteranganHt.getText()));
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
            z = "berhasil dikirim";
            return z;
        }
    }

}