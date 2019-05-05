package id.co.cng.spj.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TextInputEditText;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.lang.Math;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.co.cng.spj.R;

import id.co.cng.spj.utility.ConnectionHelper;
import id.co.cng.spj.utility.PrefManager;
import id.co.cng.spj.utility.BackgroundService;

import static android.view.View.GONE;

public class Filling1ActiveFragment extends Fragment {
    PrefManager prefManager;
    Connection con;
    Button kirim, kirimDataAwal, reset, mulai_hitung, selesai_hitung;
    TextView stopwatch, pressAwal;
    AppCompatAutoCompleteTextView noRak;
    EditText tekAwal, sm3Awal;
    EditText tempAkhir, tempAwal, tekAkhir;
    EditText pressAkhir, sm3Akhir;
    TextInputEditText keterangan;
    short signRakApv, signRakCrane, signRakTrailer;
    String statusCompressor = "";
    String date_time;
    Calendar calendar;
    ProgressBar progressBar;
    SimpleDateFormat simpleDateFormat;

    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;

    private com.nex3z.togglebuttongroup.button.CircularToggle cobaRak;
    private CheckBox compressor;
    short pos = 1;
    short filling = 1;
    private int[] listRak = {
            0,
            R.id.choice_1,
            R.id.choice_2,
            R.id.choice_3,
            R.id.choice_4,
            R.id.choice_5,
            R.id.choice_6,
            R.id.choice_7,
            R.id.choice_8,
            R.id.choice_9,
            R.id.choice_10,
            R.id.choice_11,
            R.id.choice_12

    };

    private int[] listCompressor = {
            0,
            R.id.checkbox_compressor_1,
            R.id.checkbox_compressor_2,
            R.id.checkbox_compressor_3,
            R.id.checkbox_compressor_4,
            R.id.checkbox_compressor_5,
            R.id.checkbox_compressor_6,
            R.id.checkbox_compressor_7,
            R.id.checkbox_compressor_8,
            R.id.checkbox_compressor_9,
            R.id.checkbox_compressor_10

    };

    private int[] zeroInt= {
            R.id.text_pengisian_tek_akhir,
            R.id.text_pengisian_temp_akhir,
            R.id.text_pengisian_sm3_akhir

    };

    private View view;

    public Filling1ActiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pengisian_gas_filling, container, false);

        //
        signRakApv= 1;



        return view;

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        stopwatch =  view.findViewById(R.id.textView_stopwatch);
        mulai_hitung =  view.findViewById(R.id.button_mulai_hitung);
        selesai_hitung = view.findViewById(R.id.button_selesai_hitung);
        kirim = view.findViewById(R.id.button_isi_gas_kirim);
        reset = view.findViewById(R.id.button_isi_gas_reset);
        noRak = view.findViewById(R.id.text_id_rak);
        tekAwal = view.findViewById(R.id.text_pengisian_tek_awal);
        tempAwal = view.findViewById(R.id.text_pengisian_temp_awal);
        pressAwal = view.findViewById(R.id.text_pengisian_press_awal);
        sm3Awal = view.findViewById(R.id.text_pengisian_sm3_awal);

        tekAkhir = view.findViewById(R.id.text_pengisian_tek_akhir);
        tempAkhir = view.findViewById(R.id.text_pengisian_temp_akhir);
        pressAkhir = view.findViewById(R.id.text_pengisian_press_akhir);
        sm3Akhir = view.findViewById(R.id.text_pengisian_sm3_akhir);

        keterangan = view.findViewById(R.id.text_isi_gas_keterangan);
        kirimDataAwal = view.findViewById(R.id.button_kirim_data_awal);


        prefManager = new PrefManager(getActivity());

        final FrameLayout frameApv = view.findViewById(R.id.frame_apv);
        final FrameLayout frameCrane = view.findViewById(R.id.frame_crane);
        final FrameLayout frameTrailer = view.findViewById(R.id.frame_trailer);

        final SingleSelectToggleGroup selectRakCrane = view.findViewById(R.id.group_single_radiobutton_frame_hya);
        final SingleSelectToggleGroup selectRakApv = view.findViewById(R.id.group_single_radiobutton_frame_apv);

        final SingleSelectToggleGroup selectModa = view.findViewById(R.id.group_single_radiobutton);

        final com.nex3z.togglebuttongroup.button.CircularToggle rak1 = view.findViewById(R.id.choice_1);
        final com.nex3z.togglebuttongroup.button.CircularToggle rak9 = view.findViewById(R.id.choice_9);

        progressBar = view.findViewById(R.id.progressBar_isi_gas);

        mpref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mpref.edit();

        /*
         * Ini untuk stopwatch
         * */
        try {
            String str_value = mpref.getString("data", "");
            if (str_value.matches("")) {
                Toast.makeText(getActivity(), "start tru", Toast.LENGTH_SHORT).show();
                stopwatch.setText("00:00:00");


            } else {

                if (mpref.getBoolean("finish", false)) {
                    //et_hours.setEnabled(true);
                    mulai_hitung.setEnabled(true);
                    Toast.makeText(getActivity(), "start true2", Toast.LENGTH_SHORT).show();
                    stopwatch.setText("00:00:00");
                } else {

                    //Toast.makeText(getActivity(), str_value, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "start false", Toast.LENGTH_SHORT).show();
                    mulai_hitung.setEnabled(false);
                    mulai_hitung.setBackgroundColor(Color.GRAY);
                    stopwatch.setEnabled(false);
                    //tv_timer.setText(str_value);
                }
            }
        } catch (Exception e) {

        }

        //mengambil state awal halaman isi gas

        //status compressor




        fungsiAwal();

        FillList fillList = new FillList();
        fillList.execute("");

        noRak.setOnEditorActionListener(new AppCompatAutoCompleteTextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == KeyEvent.KEYCODE_FORWARD_DEL) {
                    //submit_btn.performClick();
                    prefManager.deleteKey("pengisian_filling_act" + filling + "_" + signRakApv + "_noRak");

                    return true;
                }
                return false;
            }
        });



        tempAwal.setOnEditorActionListener(new TextInputEditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    //submit_btn.performClick();
                    if (tempAwal.getText().toString().equals("")) {
                        tempAwal.requestFocus();
                        tempAwal.setError("Untuk menghitung LWC, wajib diisi");
                    }
                    else if (tekAwal.getText().toString().equals("")) {
                        tekAwal.requestFocus();
                        tekAwal.setError("Untuk menghitung LWC, wajib diisi");
                    }
                    else{
                        GetLwc getLwc = new GetLwc();
                        getLwc.execute("");
                        sm3Awal.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });

        tempAkhir.setOnEditorActionListener(new TextInputEditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    //submit_btn.performClick();
                    if (tempAkhir.getText().toString().equals("")) {
                        tempAkhir.requestFocus();
                        tempAkhir.setError("Untuk menghitung LWC, wajib diisi");
                    }
                    else if (tekAkhir.getText().toString().equals("")) {
                        tekAkhir.requestFocus();
                        tekAkhir.setError("Untuk menghitung LWC, wajib diisi");
                    }
                    else{
                        GetLwcAkhir getLwc = new GetLwcAkhir();
                        getLwc.execute("");
                        sm3Akhir.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });

        mulai_hitung.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

//
                selesai_hitung.setEnabled(true);
                mulai_hitung.setEnabled(false);
                mulai_hitung.setBackgroundColor(Color.GRAY);
                selesai_hitung.setBackgroundColor(Color.RED);


                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                date_time = simpleDateFormat.format(calendar.getTime());

                mEditor.putString("timeAwal", date_time).commit();
                prefManager.saveString("pengisian_filling_act1_timeAwal", date_time);


                Intent intent_service = new Intent(getActivity(), BackgroundService.class);
                getActivity().startService(intent_service);
//


            }
        });

        selesai_hitung.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setCompressor();
                selesai_hitung.setBackgroundColor(Color.GRAY);
                mulai_hitung.setBackgroundColor(Color.RED);
                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                date_time = simpleDateFormat.format(calendar.getTime());
                prefManager.saveString("pengisian_filling_act1_timeAkhir", date_time);
                stopwatch.setText(mpref.getString("timeAkhir", ""));

                Intent intent = new Intent(getActivity(),BackgroundService.class);
                getActivity().stopService(intent);

                mEditor.clear().commit();

                mulai_hitung.setEnabled(true);
                selesai_hitung.setEnabled(false);

            }
        });

        kirimDataAwal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (signRakApv > 0 && signRakApv < 9) {
                    fungsiCatchValue(signRakApv);

                }
                else if (signRakCrane >= 9 && signRakCrane <= 11) {
                    fungsiCatchValue(signRakCrane);

                }
                else if (signRakTrailer == 12) {
                    fungsiCatchValue(signRakTrailer);

                }
                Toast.makeText(getActivity(), "Berhasil Disimpan", Toast.LENGTH_SHORT).show();
            }
        });


        kirim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (tekAkhir.getText().toString().trim().equals("")) {
                    tekAkhir.setError("Tolong diisi!");
                    tekAkhir.requestFocus();
                }

                else if (tempAkhir.getText().toString().trim().equals("")) {
                    tempAkhir.setError("Tolong diisi!");
                    tempAkhir.requestFocus();
                }

                else if (sm3Akhir.getText().toString().trim().equals("")) {
                    sm3Akhir.setError("Tolong diisi!");
                    sm3Akhir.requestFocus();
                }
                else if (selesai_hitung.isEnabled()) {
                    Toast.makeText(getActivity(), "Matikan dulu stopwatch!", Toast.LENGTH_SHORT).show();
                }
                else {


                    AddIsiGas addIsiGas = new AddIsiGas();// this is the Asynctask, which is used to process in background to reduce load on app process
                    addIsiGas.execute("");

                }

            }
        });

        reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fungsiReset();
//

            }
        });


        selectRakCrane.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_9 :
                        signRakCrane = 9;
                        fungsiCatchValue(signRakCrane);
                        noRak.requestFocus();

                        break;
                    case R.id.choice_10 :

                        signRakCrane = 10;
                        fungsiCatchValue(signRakCrane);
                        noRak.requestFocus();

                        break;
                    case R.id.choice_11 :
                        signRakCrane = 11;
                        fungsiCatchValue(signRakCrane);
                        noRak.requestFocus();

                        break;


                }
            }
        });

        noRak.setText(prefManager.catchString("pengisian_filling_act" +filling+ "_" +1+"_noRak"));
        tekAwal.setText(prefManager.catchString("pengisian_filling_act" +filling+ "_" +1+"_tekAwal"));
        tempAwal.setText(prefManager.catchString("pengisian_filling_act" +filling+ "_" +1+"_tempAwal"));
        pressAwal.setText(prefManager.catchString("pengisian_filling_act" +filling+ "_" +1+"_pressAwal"));
        sm3Awal.setText(prefManager.catchString("pengisian_filling_act" +filling+ "_" +1+"_sm3Awal"));

        //fungsiAwal();

        selectRakApv.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_1 :
                        signRakApv = 1;
                        fungsiCatchValue(signRakApv);
                        noRak.requestFocus();


                        break;

                    case R.id.choice_2 :
                        signRakApv = 2;
                        fungsiCatchValue(signRakApv);
                        noRak.requestFocus();

                        break;

                    case R.id.choice_3 :
                        signRakApv = 3;
                        fungsiCatchValue(signRakApv);
                        noRak.requestFocus();
                        break;

                    case R.id.choice_4 :
                        signRakApv = 4;
                        fungsiCatchValue(signRakApv);
                        noRak.requestFocus();
                        break;

                    case R.id.choice_5 :
                        signRakApv = 5;
                        fungsiCatchValue(signRakApv);
                        noRak.requestFocus();
                        break;

                    case R.id.choice_6 :
                        signRakApv = 6;
                        fungsiCatchValue(signRakApv);
                        noRak.requestFocus();
                        break;

                    case R.id.choice_7 :
                        signRakApv = 7;
                        fungsiCatchValue(signRakApv);
                        noRak.requestFocus();
                        break;

                    case R.id.choice_8 :
                        signRakApv = 8;
                        fungsiCatchValue(signRakApv);
                        noRak.requestFocus();
                        break;
                }
            }
        });



        selectModa.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_apv :


//                        signRakCrane = 1;
                        signRakApv = 1;
                        signRakTrailer = 0;
                        signRakCrane = 0;

                        rak1.setChecked(true);

//                        fungsiCatchApv();
//                        magicPengubahZero();
                        //fungsiAwal();
                        fungsiCatchValue(signRakApv);
                        noRak.requestFocus();
                        prefManager.saveString("pengisian_mode", "apv");



                        frameApv.setVisibility(View.VISIBLE);
                        frameCrane.setVisibility(GONE);
                        frameTrailer.setVisibility(GONE);

                        break;

                    case R.id.choice_crane :
//
//                        signRakApv = 9;
                        signRakCrane = 9;
                        signRakApv = 0;
                        signRakTrailer = 0;
                        rak9.setChecked(true);

                        //hide and show frame layout
                        frameApv.setVisibility(GONE);
                        frameCrane.setVisibility(View.VISIBLE);
                        frameTrailer.setVisibility(GONE);

                        //frameCrane.requestFocus();

                        prefManager.saveString("pengisian_mode", "crane");

//                        fungsiCatchCrane();
//                        magicPengubahZero();
                        fungsiCatchValue(signRakCrane);
                        noRak.requestFocus();

                        break;

                    case R.id.choice_trailer :

                        signRakTrailer = 12;
                        signRakCrane = 0;
                        signRakApv = 0;
//
                        frameApv.setVisibility(GONE);
                        frameCrane.setVisibility(GONE);
                        frameTrailer.setVisibility(View.VISIBLE);

                        //frameTrailer.requestFocus();
                        fungsiCatchValue(signRakTrailer);

                        noRak.requestFocus();
                        prefManager.saveString("pengisian_mode", "trailer");

                        break;
                }
            }
        });


    }

    private void fungsiCatchValue(final short rak) {


        prefManager.saveString("pengisian_filling_act" +filling+ "_" +pos+"_noRak", String.valueOf(noRak.getText()));
        prefManager.saveString("pengisian_filling_act" +filling+ "_" +pos+"_tekAwal", String.valueOf(tekAwal.getText()));
        prefManager.saveString("pengisian_filling_act" +filling+ "_" +pos+"_tempAwal", String.valueOf(tempAwal.getText()));
        prefManager.saveString("pengisian_filling_act" +filling+ "_" +pos+"_pressAwal", String.valueOf(pressAwal.getText()));
        prefManager.saveString("pengisian_filling_act" +filling+ "_" +pos+"_sm3Awal", String.valueOf(sm3Awal.getText()));
        Toast.makeText(getActivity(), "apv: "+ String.valueOf(signRakApv) + " pos: " + String.valueOf(pos), Toast.LENGTH_SHORT).show();

        cobaRak = view.findViewById(listRak[pos]);

        if (String.valueOf(tekAwal.getText()).equals("")) {

            cobaRak.setBackgroundColor(Color.RED);
        }

        else if (Float.valueOf(prefManager.catchString("pengisian_filling_act" + filling + "_" + pos + "_tekAwal")) <= 200 &&
                Float.valueOf(prefManager.catchString("pengisian_filling_act" + filling + "_" + pos + "_tekAwal")) >= 40) {

            cobaRak.setBackgroundColor(Color.GREEN);
            //((com.nex3z.togglebuttongroup.button.CircularToggle) view.findViewById(pos)).setBackgroundColor(Color.RED);
        } else {

            cobaRak.setBackgroundColor(Color.RED);

        }



        noRak.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //isSelectedText = true;

                //tekAwal.requestFocus();
                short i = 1;
                while (i != 13) {
                    if (String.valueOf(noRak.getText()).contentEquals(prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_noRak"))) {
                        noRak.setError("Rak " + String.valueOf(noRak.getText()) + " sudah digunakan");
                        break;
                    }
                    i++;
                }

            }

        });





        pos = rak;


        noRak.setText(prefManager.catchString("pengisian_filling_act" +filling+ "_" +rak+"_noRak"));
        tekAwal.setText(prefManager.catchString("pengisian_filling_act" +filling+ "_" +rak+"_tekAwal"));
        tempAwal.setText(prefManager.catchString("pengisian_filling_act" +filling+ "_" +rak+"_tempAwal"));
        pressAwal.setText(prefManager.catchString("pengisian_filling_act" +filling+ "_" +rak+"_pressAwal"));
        sm3Awal.setText(prefManager.catchString("pengisian_filling_act" +filling+ "_" +rak+"_sm3Awal"));
//        Float.valueOf(prefManager.catchString("pengisian_filling_act1_tekAkhir"))
        tekAkhir.setText(prefManager.catchString("pengisian_filling_act"+filling+"_tekAkhir"));
        tempAkhir.setText(prefManager.catchString("pengisian_filling_act"+filling+"_tempAkhir"));
        pressAkhir.setText(prefManager.catchString("pengisian_filling_act"+filling+"_pressAkhir"));
        sm3Akhir.setText(prefManager.catchString("pengisian_filling_act"+filling+"_sm3Akhir"));


    }

    private void fungsiReset() {
        short i = 1;
        noRak.setText(prefManager.catchString(""));
        tekAwal.setText(prefManager.catchString(""));
        tempAwal.setText(prefManager.catchString(""));
        pressAwal.setText(prefManager.catchString(""));
        sm3Awal.setText(prefManager.catchString(""));
        tekAkhir.setText(prefManager.catchString(""));
        tempAkhir.setText(prefManager.catchString(""));
        pressAkhir.setText(prefManager.catchString(""));
        sm3Akhir.setText(prefManager.catchString(""));
        try {
            while (i != 13) {
                prefManager.deleteKey("pengisian_filling_act" +filling+ "_" +i+"_noRak");
                prefManager.deleteKey("pengisian_filling_act" +filling+ "_" +i+"_tekAwal");
                prefManager.deleteKey("pengisian_filling_act" +filling+ "_" +i+"_tempAwal");
                prefManager.deleteKey("pengisian_filling_act" +filling+ "_" +i+"_pressAwal");
                prefManager.deleteKey("pengisian_filling_act" +filling+ "_" +i+"_sm3Awal");

                cobaRak = view.findViewById(listRak[i]);
                cobaRak.setBackgroundColor(Color.RED);
                i++;
            }
            i = 1;
            while (i != 11) {
                prefManager.deleteKey("pengisian_filling_act1_compressor" + i);
                i++;
            }
            prefManager.deleteKey("pengisian_filling_act"+filling+"_tekAkhir");
            prefManager.deleteKey("pengisian_filling_act"+filling+"_tempAkhir");
            prefManager.deleteKey("pengisian_filling_act"+filling+"_pressAkhir");
            prefManager.deleteKey("pengisian_filling_act"+filling+"_sm3Akhir");

        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void fungsiAwal () {
        short i = 1;

        while (i != 13) {
            if (String.valueOf(tekAwal.getText()).equals("")) {
                cobaRak = view.findViewById(listRak[i]);
                cobaRak.setBackgroundColor(Color.RED);
            }

            else if (Float.valueOf(prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_tekAwal")) <= 200 &&
                    Float.valueOf(prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_tekAwal")) >= 40) {

                cobaRak = view.findViewById(listRak[i]);
                cobaRak.setBackgroundColor(Color.GREEN);
                //((com.nex3z.togglebuttongroup.button.CircularToggle) view.findViewById(pos)).setBackgroundColor(Color.RED);
            } else {
                cobaRak = view.findViewById(listRak[i]);
                cobaRak.setBackgroundColor(Color.RED);

            }
            i++;
        }
        i = 1;
        while (i != 11) {
            compressor = view.findViewById(listCompressor[i]);
            compressor.setChecked(prefManager.catchBoolean("pengisian_filling_act"+filling+"_compressor" + i));
            i++;
        }

    }

    private void setCompressor() {
        short i = 1;
        statusCompressor = "";
        try {
            while (i != 11) {
                compressor = view.findViewById(listCompressor[i]);
                if (compressor.isChecked()) {
                    statusCompressor += compressor.getText().toString() + "|";
                    prefManager.saveBoolean("pengisian_filling_act1_compressor" + i, true);
                }
                else
                    prefManager.deleteKey("pengisian_filling_act1_compressor" + i);


                i++;
            }
            prefManager.saveString("pengisian_filling_act1_compressor", statusCompressor);
            Toast.makeText(getActivity(), statusCompressor, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str_time = intent.getStringExtra("time");
            stopwatch.setText(str_time);

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter(BackgroundService.str_receiver));

    }

    @Override
    public void onPause() {

        //String str_time = inte.getStringExtra("time");
        //tv_timer.setText(str_time);
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private class FillList extends AsyncTask<String, String, String> {

        String z = "";

        //List<Map<String, String>> prolist  = new ArrayList<Map<String, String>>();
        List<String> prolist = new ArrayList<String>();
        List<String> compressorList = new ArrayList<String>();
        List<String> namaCompressorList = new ArrayList<String>();
        //List<String> your_array_list = new ArrayList<String>();
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>();

        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, prolist);
            noRak.setThreshold(1); //will start working from first character
            noRak.setAdapter(adapter);

            short i = 1;
            String temp = "";
            while (i != 11) {
                CheckBox comp = view.findViewById(listCompressor[i]);
                temp = compressorList.get(i-1) + "" + namaCompressorList.get(i-1);
                comp.setText(temp);
                i++;
            }



        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ConnectionHelper cons = new ConnectionHelper();
                con = cons.connectionclass(prefManager.catchString("server"));        // Connect to database
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "select No_moda from tp.master_moda_transport";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    //ArrayList data1 = new ArrayList();

                    while (rs.next()) {
                        //datanum.pu
                        //datanum.put("A", rs.getString("nama"));

                        prolist.add(rs.getString("No_moda"));
//                        your_array_list.add
                        //prolist.add(datanum);
                    }

                    query = "select kode_compressor, nama_compressor from tp.master_compressor";
                    ps = con.prepareStatement(query);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        compressorList.add(rs.getString("kode_compressor"));
                        namaCompressorList.add(rs.getString("nama_compressor"));
                    }
                }


                //z = "Success";                }


            }
            catch (Exception ex)
            {
                z = "Error retrieving data from table";

            }
            return z;
        }
    }

    private class AddIsiGas extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
            //Toast.makeText(getActivity(), prefManager.catchString("pengisian_filling_act1_timeAwal"), Toast.LENGTH_SHORT).show();
//            prefManager.saveString("pengisian_filling_act1_tekAkhir", String.valueOf(tekAkhir.getText()));
//            prefManager.saveString("pengisian_filling_act1_tempAkhir", String.valueOf(tempAkhir.getText()));
//            prefManager.saveString("pengisian_filling_act1_pressAkhir", String.valueOf(pressAkhir.getText()));
//            prefManager.saveString("pengisian_filling_act1_sm3Akhir", String.valueOf(sm3Akhir.getText()));
//            prefManager.saveString("pengisian_filling_act1_keterangan", String.valueOf(keterangan.getText()));
            //prefManager.saveString("pengisian_filling_act1_sm3Akhir", keterangan.getText().toString());
        }

        @Override
        protected void onPostExecute(String r)
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params)
        {
            String usernam = noRak.getText().toString();;
            String passwordd = tekAwal.getText().toString();
            if (usernam.trim().equals("") || passwordd.trim().equals("")) {

                //aaaaz = "Please enter Username and Password";
            } else {
                try {

                    ConnectionHelper cons = new ConnectionHelper();
                    con = cons.connectionclass(prefManager.catchString("server"));        // Connect to database
                    if (con == null) {
                        z = "Check Your Internet Access!";
                    } else {
                        //Toast.makeText(MainActivity.this, "berhasil", Toast.LENGTH_SHORT).show();
                        //ini konek ke muslih
                        //String query = "select * from master_karyawan where username_karyawan = '" + usernam.toString() + "' and password = '"+ passwordd.toString() +"'  ";
                        //ini localhost
                        String query;
                        PreparedStatement preparedStatement;
                        short i;
                        if (signRakApv >= 1 && signRakApv <= 8) {

                            for (i = 1; i <= 8; i++) {
//                                zero = view.findViewById(zeroInt[i]);
//                                if (zero.getText().toString().contentEquals(""))
//                                    zero.setText("0");
                                if (prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_tekAwal").contentEquals(""))
                                    prefManager.saveString("pengisian_filling_act" +filling+ "_" +i+"_tekAwal", "0");
                                if (prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_tempAwal").contentEquals(""))
                                    prefManager.saveString("pengisian_filling_act" +filling+ "_" +i+"_tempAwal", "0");
                                if (prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_pressAwal").contentEquals(""))
                                    prefManager.saveString("pengisian_filling_act" +filling+ "_" +i+"_pressAwal", "0");
                                if (prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_sm3Awal").contentEquals(""))
                                    prefManager.saveString("pengisian_filling_act" +filling+ "_" +i+"_sm3Awal", "0");
                            }

                            i = 1;
//                            id_pressure tekanan_awal temp_awal pressure_awal SM3_awal tekanan_akhir
//                            temp_akhir pressure_akhir SM3_akhir keterangan
                            //
                            while (i != 9) {
                                //convert(varchar,convert(datetime,'2013-09-17 21:01:00.000'),103)
                                //CONVERT(datetime, '2014-11-30 23:59:59.997', 121);
                                //convert(datetime, '" + prefManager.catchString("pengisian_filling_act1_timeAkhir") + "', 103)
                                query = "insert into tp.pengisian_gas " +
                                        "values (" + prefManager.catchString("id_karyawan_spj") + ", '" +
                                        prefManager.catchString("nama_karyawan_spj") + "', '" +
                                        prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_noRak")  + "', " +
                                        "?, " + //jenis_moda
                                        "?, " + //tekAwal
                                        "?, " + //tekAkhir
                                        Float.valueOf(prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_tempAwal")) + ", " +
                                        String.valueOf(tempAkhir.getText()) + ", " +
                                        Float.valueOf(prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_pressAwal")) + ", " +
                                        String.valueOf(pressAkhir.getText()) + ", " +
                                        Float.valueOf(prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_sm3Awal")) + ", " +
                                        String.valueOf(sm3Akhir.getText()) + ", " +
                                        "convert(date, '" + prefManager.catchString("pengisian_filling_act1_timeAwal") + "', 103), " +
                                        "convert(datetime, convert(datetime, '" + prefManager.catchString("pengisian_filling_act1_timeAwal") + "', 103), 108), "+
                                        "convert(date, '" + prefManager.catchString("pengisian_filling_act1_timeAkhir") + "', 103), " +
                                        "convert(datetime, convert(datetime, '" + prefManager.catchString("pengisian_filling_act1_timeAkhir") + "', 103), 108), " +
                                        "convert(time, convert(datetime, '10/02/1998 " + stopwatch.getText().toString() + "', 103), 108), '" +
                                        prefManager.catchString("pengisian_filling_act1_compressor") + "', '" +
                                        keterangan.getText().toString() + "' )";

                                preparedStatement = con.prepareStatement(query);
                                preparedStatement.setString(1, "APV");
                                preparedStatement.setFloat(2, Float.valueOf(prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_tekAwal")));
                                preparedStatement.setFloat(3, Float.valueOf(String.valueOf(tekAkhir.getText())));
                                preparedStatement.executeUpdate();

//

                                i++;
                            }

                        } else if (signRakCrane >= 9 && signRakCrane <= 11) {

                            for (i = 9; i <= 11; i++) {
//                                zero = view.findViewById(zeroInt[i]);
//                                if (zero.getText().toString().contentEquals(""))
//                                    zero.setText("0");
                                if (prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_tekAwal").contentEquals(""))
                                    prefManager.saveString("pengisian_filling_act" +filling+ "_" +i+"_tekAwal", "0");
                                if (prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_tempAwal").contentEquals(""))
                                    prefManager.saveString("pengisian_filling_act" +filling+ "_" +i+"_tempAwal", "0");
                                if (prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_pressAwal").contentEquals(""))
                                    prefManager.saveString("pengisian_filling_act" +filling+ "_" +i+"_pressAwal", "0");
                                if (prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_sm3Awal").contentEquals(""))
                                    prefManager.saveString("pengisian_filling_act" +filling+ "_" +i+"_sm3Awal", "0");
                            }
                            i = 9;
                            while (i != 12) {
                                //convert(varchar,convert(datetime,'2013-09-17 21:01:00.000'),103)
                                //CONVERT(datetime, '2014-11-30 23:59:59.997', 121);
                                query = "insert into tp.pengisian_gas " +
                                        "values (" + prefManager.catchString("id_karyawan_spj") + ", '" +
                                        prefManager.catchString("nama_karyawan_spj") + "', '" +
                                        prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_noRak")  + "', " +
                                        "?, " + //jenis_moda
                                        "?, " + //tekAwal
                                        "?, " + //tekAkhir
                                        Float.valueOf(prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_tempAwal")) + ", " +
                                        String.valueOf(tempAkhir.getText()) + ", " +
                                        Float.valueOf(prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_pressAwal")) + ", " +
                                        String.valueOf(pressAkhir.getText()) + ", " +
                                        Float.valueOf(prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_sm3Awal")) + ", " +
                                        String.valueOf(sm3Akhir.getText()) + ", " +
                                        "convert(date, '" + prefManager.catchString("pengisian_filling_act1_timeAwal") + "', 103), " +
                                        "convert(datetime, convert(datetime, '" + prefManager.catchString("pengisian_filling_act1_timeAwal") + "', 103), 108), "+
                                        "convert(date, '" + prefManager.catchString("pengisian_filling_act1_timeAkhir") + "', 103), " +
                                        "convert(datetime, convert(datetime, '" + prefManager.catchString("pengisian_filling_act1_timeAkhir") + "', 103), 108), " +
                                        "convert(time, convert(datetime, '10/02/1998 " + stopwatch.getText().toString() + "', 103), 108), '" +
                                        prefManager.catchString("pengisian_filling_act1_compressor") + "', '" +
                                        keterangan.getText().toString() + "' )";

                                preparedStatement = con.prepareStatement(query);
                                preparedStatement.setString(1, "HYAPCRANE");
                                preparedStatement.setFloat(2, Float.valueOf(prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_tekAwal")));
                                preparedStatement.setFloat(3, Float.valueOf(String.valueOf(tekAkhir.getText())));
                                preparedStatement.executeUpdate();

                                //query =
                                i++;
                            }
                        }

                        else {
                            i = 12;
                            if (prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_tekAwal").contentEquals(""))
                                prefManager.saveString("pengisian_filling_act" +filling+ "_" +i+"_tekAwal", "0");
                            if (prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_tempAwal").contentEquals(""))
                                prefManager.saveString("pengisian_filling_act" +filling+ "_" +i+"_tempAwal", "0");
                            if (prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_pressAwal").contentEquals(""))
                                prefManager.saveString("pengisian_filling_act" +filling+ "_" +i+"_pressAwal", "0");
                            if (prefManager.catchString("pengisian_filling_act" + filling + "_" + i + "_sm3Awal").contentEquals(""))
                                prefManager.saveString("pengisian_filling_act" +filling+ "_" +i+"_sm3Awal", "0");
                            query = "insert into tp.pengisian_gas " +
                                    "values (" + prefManager.catchString("id_karyawan_spj") + ", '" +
                                    prefManager.catchString("nama_karyawan_spj") + "', '" +
                                    prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_noRak")  + "', " +
                                    "?, " + //jenis_moda
                                    "?, " + //tekAwal
                                    "?, " + //tekAkhir
                                    Float.valueOf(prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_tempAwal")) + ", " +
                                    String.valueOf(tempAkhir.getText()) + ", " +
                                    Float.valueOf(prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_pressAwal")) + ", " +
                                    String.valueOf(pressAkhir.getText()) + ", " +
                                    Float.valueOf(prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_sm3Awal")) + ", " +
                                    String.valueOf(sm3Akhir.getText()) + ", " +
                                    "convert(date, '" + prefManager.catchString("pengisian_filling_act1_timeAwal") + "', 103), " +
                                    "convert(datetime, convert(datetime, '" + prefManager.catchString("pengisian_filling_act1_timeAwal") + "', 103), 108), "+
                                    "convert(date, '" + prefManager.catchString("pengisian_filling_act1_timeAkhir") + "', 103), " +
                                    "convert(datetime, convert(datetime, '" + prefManager.catchString("pengisian_filling_act1_timeAkhir") + "', 103), 108), " +
                                    "convert(time, convert(datetime, '10/02/1998 " + stopwatch.getText().toString() + "', 103), 108), '" +
                                    prefManager.catchString("pengisian_filling_act1_compressor") + "', '" +
                                    keterangan.getText().toString() + "' )";

                            preparedStatement = con.prepareStatement(query);
                            preparedStatement.setString(1, "TRAILER");
                            preparedStatement.setFloat(2, Float.valueOf(prefManager.catchString("pengisian_filling_act" +filling+ "_" +i+"_tekAwal")));
                            preparedStatement.setFloat(3, Float.valueOf(String.valueOf(tekAkhir.getText())));
                            preparedStatement.executeUpdate();
                        }




                        z = "Added Successfully";
                    }
                } catch (Exception ex) {

                    z = ex.getMessage();
                    //keterangan.setText(ex.getMessage());
                }
            }
            return z;
        }
    }


    private class GetLwc extends AsyncTask<String,String,String>
    {
        String z = "";
        ProgressDialog progress = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute()
        {
            progress.show();
            progress.setCanceledOnTouchOutside(false);
            progress.setMessage("sedang menghitung sm3");
        }

        @Override
        protected void onPostExecute(String r)
        {
            progress.dismiss();
            //Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();

                double p0 = Double.valueOf(tekAwal.getText().toString());
                double t0 = Double.valueOf(tempAwal.getText().toString());
                double gravity = Double.valueOf(prefManager.catchString("kalorigas_sg"));
                double co2 = Double.valueOf(prefManager.catchString("kalorigas_co2"));
                double n2 = Double.valueOf(prefManager.catchString("kalorigas_n2"));

                double p = p0 *14.504;

//        T1 =temperature*9/5+32
                double t1 = (t0*9)/5+32;

//        P2 =((P*(156,47/(160,8-7,22*gravity+CO2-0,392*N2)))+14,7)/1000
                double p2 = ((p*(156.47/(160.8 - 7.22*gravity + co2 - 0.392 * n2))) + 14.7) / 1000;

//#        T2 =((T1+460)*226,29/(99,15+211,9*gravity-(CO2+1,681*N2)))/500
                double t2 = ((t1 + 460) * 226.29 / (99.15 + 211.9 * gravity - (co2 + 1.681*n2)))/500;

//#        H =T2^2
//#0.0330378*($AF21^-2)-0.0221323*($AF21^-3)+0.0161353*($AF21^-5)
//#h = Math.pow(t2, 2);
                double h = 0.0330378 * Math.pow(t2, -2) - 0.0221323 * Math.pow(t2, -3) + 0.0161353 * Math.pow(t2, -5);

//#        I =(0,265827*(T2^-2)+0,0457697*(T2^-4)-0,133185*(T2^-1))/H
                double i =(0.265827*(Math.pow(t2, -2))+0.0457697*(Math.pow(t2, -4))-0.133185*(Math.pow(t2, -1)))/h;

//#        B =(3-H*(I^2))/(9*H*(P2^2))
                double b =(3-h*(Math.pow(i, 2)))/(9*h*(Math.pow(p2, 2)));


//#        E1 =IF(T2<1,09;
//# 1-0,00075*(P2^2,3)*(2-EXP(-20*(1,09-T2)));
//# 1-0,00075*P2^2,3*EXP(-20*(T2-1,09)))
                double e1;
                if (t2 < 1.09)
                    e1 = 1-0.00075*(Math.pow(p2, 2.3))*(2 - Math.exp(-20*(1.09-t2)));
                else
                    e1 = 1-0.00075*Math.pow(p2, 2.3) * Math.exp(-20*(t2-1.09));

//#        E2 =IF(T2<1,09;
//# E1-1,317*(1,09-T2)^4*P2*(1,69-P2^2);
//# E1-0,0011*(T2-1,09)^0,5*P2^2*(2,17+1,4*(T2-1,09)^0,5-P2)^2)
                double e2;
                if (t2<1.09)
                    e2 = e1-1.317*Math.pow((1.09-t2), 4) * p2 *(1.69- Math.pow(p2, 2));
                else
//	#$AJ21-0.0011*($AF21-1.09)^0.5*$AD21^2*(2.17+1.4*($AF21-1.09)^0.5-$AD21)^2)
                    e2 = e1-0.0011*Math.pow((t2-1.09), 0.5) * Math.pow(p2, 2) * Math.pow((2.17 + 1.4 * Math.pow((t2-1.09), 0.5)-p2) , 2);

//#        F =(9*I-2*H*I^3)/(54*H*P2^3)-(E2/(2*H*P2^2))
                double f =(9*i-2*h*Math.pow(i, 3))/(54*h*Math.pow(p2, 3))-(e2/(2*h*Math.pow(p2, 2)));

//#        D =(F+SQRT(F^2+B^3))^(1/3)
                double d =Math.pow((f+Math.sqrt(Math.pow(f, 2)+Math.pow(b, 3))), 0.3333333);

//        #FPV =SQRT(B/D-D+I/(3*P2))/(1+0,00132/(T2^3,25))
                double fpv =Math.sqrt(b/d-d+i/(3*p2))/(1+0.00132/Math.pow(t2, 3.25));



                //z = "Tidak ada nama rak " + noRak.getText().toString();
                //Toast.makeText(getActivity(), "tidak ada rak", Toast.LENGTH_SHORT).show();
                double lwc;
                if (signRakApv >= 1 && signRakApv <= 8)
                    lwc =  Double.parseDouble(prefManager.catchString("pengisian_filling_act1_apv_"+signRakApv+"_lwc"));
                else if (signRakCrane >= 9 && signRakCrane <= 11)
                    lwc =  Double.parseDouble(prefManager.catchString("pengisian_filling_act1_crane_"+signRakCrane+"_lwc"));
                else
                    lwc =  Double.parseDouble(prefManager.catchString("pengisian_filling_act1_trail_12_lwc"));
                double p1 = lwc / 1000;

                double sm3 = p1*(p0+1.01325)/1.01325*(273+27)/(273+t0)*Math.pow(fpv, 2);
                pressAwal.setText(String.format("%.2f", sm3));
                if (signRakApv >= 1 && signRakApv <= 8)
                    prefManager.saveString("pengisian_filling_act1_apv_"+signRakApv+"_pressAwal", String.valueOf(pressAwal.getText()));
                else if (signRakCrane >= 9 && signRakCrane <= 11)
                    prefManager.saveString("pengisian_filling_act1_crane_"+signRakCrane+"_pressAwal", String.valueOf(pressAwal.getText()));
                else
                    prefManager.saveString("pengisian_filling_act1_trail_"+signRakTrailer+"_pressAwal", String.valueOf(pressAwal.getText()));



//
        }
        @Override
        protected String doInBackground(String... params)
        {

            try {

                ConnectionHelper cons = new ConnectionHelper();
                con = cons.connectionclass(prefManager.catchString("server"));        // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access!";
                } else {
                    //Toast.makeText(MainActivity.this, "berhasil", Toast.LENGTH_SHORT).show();
                    //ini konek ke muslih
                    //String query = "select * from master_karyawan where username_karyawan = '" + usernam.toString() + "' and password = '"+ passwordd.toString() +"'  ";
                    //ini localhost
                    //String query = "select LWC from tp.master_moda_transport where Nama = '" + noRak.getText().toString() + "'";
                    String query = "select LWC from tp.master_moda_transport where No_moda = '" + noRak.getText().toString() + "'";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();


                    while (rs.next()) {
                        //pressAwal.setText(rs.getString("value").toString());


                        if (signRakApv >= 1 && signRakApv <= 8)
                            //Toast.makeText(getActivity(), rs.getString("LWC").toString(), Toast.LENGTH_SHORT).show();
                            prefManager.saveString("pengisian_filling_act1_apv_" + signRakApv + "_lwc", rs.getString("LWC").toString());
                        else if (signRakCrane >= 9 && signRakCrane <= 11)
                            prefManager.saveString("pengisian_filling_act1_crane_" + signRakCrane + "_lwc", rs.getString("LWC").toString());
                        else
                            prefManager.saveString("pengisian_filling_act1_trail_12_lwc", rs.getString("LWC").toString());

                    }

                    query = "select top 1 * from tp.kalorigas order by id_kalorigas desc";
                    preparedStatement = con.prepareStatement(query);
                    rs = preparedStatement.executeQuery();
                    rs.next();

                    prefManager.saveString("kalorigas_co2",rs.getString("co2"));
                    prefManager.saveString("kalorigas_n2",rs.getString("n2"));
                    prefManager.saveString("kalorigas_sg",rs.getString("s_gravity"));
                    prefManager.saveString("kalorigas_hv",rs.getString("hv"));
                    prefManager.saveString("kalorigas_keterangan",rs.getString("keterangan"));



                }
            } catch (Exception ex) {

                Toast.makeText(getActivity(), "kosong", Toast.LENGTH_SHORT).show();

            }

            return z;
        }
    }

    private class GetLwcAkhir extends AsyncTask<String,String,String>
    {
        String z = "";
        ProgressDialog progress = new ProgressDialog(getActivity());
        String tekanan, temp;

        @Override
        protected void onPreExecute()
        {
            progress.show();
            progress.setCanceledOnTouchOutside(false);
            progress.setMessage("sedang menghitung sm3");
        }

        @Override
        protected void onPostExecute(String r)
        {
            progress.dismiss();
            //Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
            try {
                double p0 = Double.valueOf(tekAkhir.getText().toString());
                double t0 = Double.valueOf(tempAkhir.getText().toString());
                double gravity = Double.valueOf(prefManager.catchString("kalorigas_sg"));
                double co2 = Double.valueOf(prefManager.catchString("kalorigas_co2"));
                double n2 = Double.valueOf(prefManager.catchString("kalorigas_n2"));

                double p = p0 *14.504;

//        T1 =temperature*9/5+32
                double t1 = (t0*9)/5+32;

//        P2 =((P*(156,47/(160,8-7,22*gravity+CO2-0,392*N2)))+14,7)/1000
                double p2 = ((p*(156.47/(160.8 - 7.22*gravity + co2 - 0.392 * n2))) + 14.7) / 1000;

//#        T2 =((T1+460)*226,29/(99,15+211,9*gravity-(CO2+1,681*N2)))/500
                double t2 = ((t1 + 460) * 226.29 / (99.15 + 211.9 * gravity - (co2 + 1.681*n2)))/500;

//#        H =T2^2
//#0.0330378*($AF21^-2)-0.0221323*($AF21^-3)+0.0161353*($AF21^-5)
//#h = Math.pow(t2, 2);
                double h = 0.0330378 * Math.pow(t2, -2) - 0.0221323 * Math.pow(t2, -3) + 0.0161353 * Math.pow(t2, -5);

//#        I =(0,265827*(T2^-2)+0,0457697*(T2^-4)-0,133185*(T2^-1))/H
                double i =(0.265827*(Math.pow(t2, -2))+0.0457697*(Math.pow(t2, -4))-0.133185*(Math.pow(t2, -1)))/h;

//#        B =(3-H*(I^2))/(9*H*(P2^2))
                double b =(3-h*(Math.pow(i, 2)))/(9*h*(Math.pow(p2, 2)));


//#        E1 =IF(T2<1,09;
//# 1-0,00075*(P2^2,3)*(2-EXP(-20*(1,09-T2)));
//# 1-0,00075*P2^2,3*EXP(-20*(T2-1,09)))
                double e1;
                if (t2 < 1.09)
                    e1 = 1-0.00075*(Math.pow(p2, 2.3))*(2 - Math.exp(-20*(1.09-t2)));
                else
                    e1 = 1-0.00075*Math.pow(p2, 2.3) * Math.exp(-20*(t2-1.09));

//#        E2 =IF(T2<1,09;
//# E1-1,317*(1,09-T2)^4*P2*(1,69-P2^2);
//# E1-0,0011*(T2-1,09)^0,5*P2^2*(2,17+1,4*(T2-1,09)^0,5-P2)^2)
                double e2;
                if (t2<1.09)
                    e2 = e1-1.317*Math.pow((1.09-t2), 4) * p2 *(1.69- Math.pow(p2, 2));
                else
//	#$AJ21-0.0011*($AF21-1.09)^0.5*$AD21^2*(2.17+1.4*($AF21-1.09)^0.5-$AD21)^2)
                    e2 = e1-0.0011*Math.pow((t2-1.09), 0.5) * Math.pow(p2, 2) * Math.pow((2.17 + 1.4 * Math.pow((t2-1.09), 0.5)-p2) , 2);

//#        F =(9*I-2*H*I^3)/(54*H*P2^3)-(E2/(2*H*P2^2))
                double f =(9*i-2*h*Math.pow(i, 3))/(54*h*Math.pow(p2, 3))-(e2/(2*h*Math.pow(p2, 2)));

//#        D =(F+SQRT(F^2+B^3))^(1/3)
                double d =Math.pow((f+Math.sqrt(Math.pow(f, 2)+Math.pow(b, 3))), 0.3333333);

//        #FPV =SQRT(B/D-D+I/(3*P2))/(1+0,00132/(T2^3,25))
                double fpv =Math.sqrt(b/d-d+i/(3*p2))/(1+0.00132/Math.pow(t2, 3.25));



                //z = "Tidak ada nama rak " + noRak.getText().toString();
                //Toast.makeText(getActivity(), "tidak ada rak", Toast.LENGTH_SHORT).show();
                double lwc;
                if (signRakApv >= 1 && signRakApv <= 8)
                    lwc =  Double.parseDouble(prefManager.catchString("pengisian_filling_act1_apv_"+signRakApv+"_lwc"));
                else if (signRakCrane >= 9 && signRakCrane <= 11)
                    lwc =  Double.parseDouble(prefManager.catchString("pengisian_filling_act1_crane_"+signRakCrane+"_lwc"));
                else
                    lwc =  Double.parseDouble(prefManager.catchString("pengisian_filling_act1_trail_12_lwc"));
                double p1 = lwc / 1000;

                double sm3 = p1*(p0+1.01325)/1.01325*(273+27)/(273+t0)*Math.pow(fpv, 2);
                pressAkhir.setText(String.format("%.2f", sm3));
                if (signRakApv >= 1 && signRakApv <= 8)
                    prefManager.saveString("pengisian_filling_act1_apv_"+signRakApv+"_pressAkhir", String.valueOf(pressAwal.getText()));
                else if (signRakCrane >= 9 && signRakCrane <= 11)
                    prefManager.saveString("pengisian_filling_act1_crane_"+signRakCrane+"_pressAkhir", String.valueOf(pressAwal.getText()));
                else
                    prefManager.saveString("pengisian_filling_act1_trail_"+signRakTrailer+"_pressAkhir", String.valueOf(pressAwal.getText()));
            } catch (Exception ex) {
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }


//
        }
        @Override
        protected String doInBackground(String... params)
        {

            try {

                ConnectionHelper cons = new ConnectionHelper();
                con = cons.connectionclass(prefManager.catchString("server"));        // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access!";
                } else {
                    //Toast.makeText(MainActivity.this, "berhasil", Toast.LENGTH_SHORT).show();
                    //ini konek ke muslih
                    //String query = "select * from master_karyawan where username_karyawan = '" + usernam.toString() + "' and password = '"+ passwordd.toString() +"'  ";
                    //ini localhost
                    //String query = "select LWC from tp.master_moda_transport where Nama = '" + noRak.getText().toString() + "'";
                    String query = "select LWC from tp.master_moda_transport where No_moda = '" + noRak.getText().toString() + "'";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();


                    while (rs.next()) {
                        //pressAwal.setText(rs.getString("value").toString());


                        if (signRakApv >= 1 && signRakApv <= 8)
                            //Toast.makeText(getActivity(), rs.getString("LWC").toString(), Toast.LENGTH_SHORT).show();
                            prefManager.saveString("pengisian_filling_act1_apv_" + signRakApv + "_lwc", rs.getString("LWC").toString());
                        else if (signRakCrane >= 9 && signRakCrane <= 11)
                            prefManager.saveString("pengisian_filling_act1_crane_" + signRakCrane + "_lwc", rs.getString("LWC").toString());
                        else
                            prefManager.saveString("pengisian_filling_act1_trail_12_lwc", rs.getString("LWC").toString());

                    }

                    query = "select top 1 * from tp.kalorigas order by id_kalorigas desc";
                    preparedStatement = con.prepareStatement(query);
                    rs = preparedStatement.executeQuery();
                    rs.next();

                    prefManager.saveString("kalorigas_co2",rs.getString("co2"));
                    prefManager.saveString("kalorigas_n2",rs.getString("n2"));
                    prefManager.saveString("kalorigas_sg",rs.getString("s_gravity"));
                    prefManager.saveString("kalorigas_hv",rs.getString("hv"));
                    prefManager.saveString("kalorigas_keterangan",rs.getString("keterangan"));



                }
            } catch (Exception ex) {

                Toast.makeText(getActivity(), "kosong", Toast.LENGTH_SHORT).show();

            }

            return z;
        }
    }
}
