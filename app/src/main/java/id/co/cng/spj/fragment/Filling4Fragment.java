package id.co.cng.spj.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TextInputEditText;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
import id.co.cng.spj.utility.BackgroundService4;

import static android.view.View.GONE;

public class Filling4Fragment extends Fragment {
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
    String date_time, date_now;
    Calendar calendar;
    ProgressBar progressBar;
    SimpleDateFormat simpleDateFormat;

    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;

    public Filling4Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengisian_gas_filling, container, false);

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

        final CheckBox compressor1 = view.findViewById(R.id.checkbox_compressor_1);
        final CheckBox compressor2 = view.findViewById(R.id.checkbox_compressor_2);
        final CheckBox compressor3 = view.findViewById(R.id.checkbox_compressor_3);
        final CheckBox compressor4 = view.findViewById(R.id.checkbox_compressor_4);
        final CheckBox compressor5 = view.findViewById(R.id.checkbox_compressor_5);
        final CheckBox compressor6 = view.findViewById(R.id.checkbox_compressor_6);
        final CheckBox compressor7 = view.findViewById(R.id.checkbox_compressor_7);
        final CheckBox compressor8 = view.findViewById(R.id.checkbox_compressor_8);
        final CheckBox compressor9 = view.findViewById(R.id.checkbox_compressor_9);
        final CheckBox compressor10 = view.findViewById(R.id.checkbox_compressor_10);

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
        final com.nex3z.togglebuttongroup.button.CircularToggle rak2 = view.findViewById(R.id.choice_2);
        final com.nex3z.togglebuttongroup.button.CircularToggle rak3 = view.findViewById(R.id.choice_3);
        final com.nex3z.togglebuttongroup.button.CircularToggle rak4 = view.findViewById(R.id.choice_4);
        final com.nex3z.togglebuttongroup.button.CircularToggle rak5 = view.findViewById(R.id.choice_5);
        final com.nex3z.togglebuttongroup.button.CircularToggle rak6 = view.findViewById(R.id.choice_6);
        final com.nex3z.togglebuttongroup.button.CircularToggle rak7 = view.findViewById(R.id.choice_7);
        final com.nex3z.togglebuttongroup.button.CircularToggle rak8 = view.findViewById(R.id.choice_8);
        final com.nex3z.togglebuttongroup.button.CircularToggle rak9 = view.findViewById(R.id.choice_9);
        final com.nex3z.togglebuttongroup.button.CircularToggle rak10 = view.findViewById(R.id.choice_10);
        final com.nex3z.togglebuttongroup.button.CircularToggle rak11 = view.findViewById(R.id.choice_11);
        final com.nex3z.togglebuttongroup.button.CircularToggle rak12 = view.findViewById(R.id.choice_12);

        progressBar = view.findViewById(R.id.progressBar_isi_gas);

        mpref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mpref.edit();

        /*
         * Ini untuk stopwatch
         * */
        try {
            String str_value = mpref.getString("data4", "");
            if (str_value.matches("")) {
                Toast.makeText(getActivity(), "start tru", Toast.LENGTH_SHORT).show();
                stopwatch.setText("00:00:00");

            } else {

                if (mpref.getBoolean("finish4", false)) {
                    //et_hours.setEnabled(true);
                    mulai_hitung.setEnabled(true);
                    Toast.makeText(getActivity(), "start true2", Toast.LENGTH_SHORT).show();
                    stopwatch.setText("00:00:00");
                } else {

                    Toast.makeText(getActivity(), str_value, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(this, "start false", Toast.LENGTH_SHORT).show();
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
        compressor1.setChecked(prefManager.catchBoolean("pengisian_filling4_compressor1"));
        compressor2.setChecked(prefManager.catchBoolean("pengisian_filling4_compressor2"));
        compressor3.setChecked(prefManager.catchBoolean("pengisian_filling4_compressor3"));
        compressor4.setChecked(prefManager.catchBoolean("pengisian_filling4_compressor4"));
        compressor5.setChecked(prefManager.catchBoolean("pengisian_filling4_compressor5"));
        compressor6.setChecked(prefManager.catchBoolean("pengisian_filling4_compressor6"));
        compressor7.setChecked(prefManager.catchBoolean("pengisian_filling4_compressor7"));
        compressor8.setChecked(prefManager.catchBoolean("pengisian_filling4_compressor8"));
        compressor9.setChecked(prefManager.catchBoolean("pengisian_filling4_compressor9"));
        compressor10.setChecked(prefManager.catchBoolean("pengisian_filling4_compressor10"));

        if ((prefManager.catchFloat("pengisian_filling4_apv_1_tekAwal")) <= 200 &&
                (prefManager.catchFloat("pengisian_filling4_apv_1_tekAwal")) >= 40) {
            rak1.setBackgroundColor(Color.GREEN);
        } else {
            rak1.setBackgroundColor(Color.RED);
        }

        if ((prefManager.catchFloat("pengisian_filling4_apv_2_tekAwal")) <= 200 &&
                (prefManager.catchFloat("pengisian_filling4_apv_2_tekAwal")) >= 40) {
            rak2.setBackgroundColor(Color.GREEN);
        } else {
            rak2.setBackgroundColor(Color.RED);
        }

        if ((prefManager.catchFloat("pengisian_filling4_apv_3_tekAwal")) <= 200 &&
                (prefManager.catchFloat("pengisian_filling4_apv_3_tekAwal")) >= 40) {
            rak3.setBackgroundColor(Color.GREEN);
        } else {
            rak3.setBackgroundColor(Color.RED);
        }

        if ((prefManager.catchFloat("pengisian_filling4_apv_4_tekAwal")) <= 200 &&
                (prefManager.catchFloat("pengisian_filling4_apv_4_tekAwal")) >= 40) {
            rak4.setBackgroundColor(Color.GREEN);
        } else {
            rak4.setBackgroundColor(Color.RED);
        }

        if ((prefManager.catchFloat("pengisian_filling4_apv_5_tekAwal")) <= 200 &&
                (prefManager.catchFloat("pengisian_filling4_apv_5_tekAwal")) >= 40) {
            rak5.setBackgroundColor(Color.GREEN);
        } else {
            rak5.setBackgroundColor(Color.RED);
        }

        if ((prefManager.catchFloat("pengisian_filling4_apv_6_tekAwal")) <= 200 &&
                (prefManager.catchFloat("pengisian_filling4_apv_6_tekAwal")) >= 40) {
            rak6.setBackgroundColor(Color.GREEN);
        } else {
            rak6.setBackgroundColor(Color.RED);
        }

        if ((prefManager.catchFloat("pengisian_filling4_apv_7_tekAwal")) <= 200 &&
                (prefManager.catchFloat("pengisian_filling4_apv_7_tekAwal")) >= 40) {
            rak7.setBackgroundColor(Color.GREEN);
        } else {
            rak7.setBackgroundColor(Color.RED);
        }

        if ((prefManager.catchFloat("pengisian_filling4_apv_8_tekAwal")) <= 200 &&
                (prefManager.catchFloat("pengisian_filling4_apv_8_tekAwal")) >= 40) {
            rak8.setBackgroundColor(Color.GREEN);
        } else {
            rak8.setBackgroundColor(Color.RED);
        }

        FillList fillList = new FillList();
        fillList.execute("");



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
                long date_diff;


                calendar = Calendar.getInstance();
//                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
//                Date firstDate = sdf.parse("06/24/2017");
//                Date secondDate = sdf.parse("06/30/2017");
//
//                long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
//                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                date_time = simpleDateFormat.format(calendar.getTime());

                mEditor.putString("timeAwal4", date_time).commit();
                prefManager.saveString("pengisian_filling4_timeAwal", date_time);


                Intent intent_service = new Intent(getActivity(), BackgroundService4.class);
                getActivity().startService(intent_service);
//


            }
        });

        selesai_hitung.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selesai_hitung.setBackgroundColor(Color.GRAY);
                mulai_hitung.setBackgroundColor(Color.RED);
                calendar = Calendar.getInstance();
                simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                date_time = simpleDateFormat.format(calendar.getTime());
                prefManager.saveString("pengisian_filling4_timeAkhir", date_time);
                stopwatch.setText(mpref.getString("timeAkhir4", ""));

                Intent intent = new Intent(getActivity(),BackgroundService4.class);
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
                if (noRak.getText().toString().trim().equals("")) {
                    noRak.setError("Tolong diisi!");
                    noRak.requestFocus();
                }

                else if (tekAwal.getText().toString().trim().equals("")) {
                    tekAwal.setError("Tolong diisi!");
                    tekAwal.requestFocus();
                }

                else if (tempAwal.getText().toString().trim().equals("")) {
                    tempAwal.setError("Tolong diisi!");
                    tempAwal.requestFocus();
                }

                else if (sm3Awal.getText().toString().trim().equals("")) {
                    sm3Awal.setError("Tolong diisi!");
                    sm3Awal.requestFocus();
                }
                else {
//
                    if (signRakApv > 0 && signRakApv < 9) {
                        prefManager.saveString("pengisian_filling4_apv_" + signRakApv + "_noRak", noRak.getText().toString());
                        prefManager.saveFloat("pengisian_filling4_apv_" + signRakApv + "_tempAwal", Float.parseFloat(tempAwal.getText().toString()));
                        prefManager.saveFloat("pengisian_filling4_apv_" + signRakApv + "_tekAwal", Float.parseFloat(tekAwal.getText().toString()));
                        prefManager.saveFloat("pengisian_filling4_apv_" + signRakApv + "_sm3Awal", Float.parseFloat(sm3Awal.getText().toString()));

//                    CalculatePress hitungPress = new CalculatePress();// this is the Asynctask, which is used to process in background to reduce load on app process
//                    hitungPress.execute("");


                        if ((prefManager.catchFloat("pengisian_filling4_apv_1_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_apv_1_tekAwal")) >= 40) {
                            rak1.setBackgroundColor(Color.GREEN);
                        } else {
                            rak1.setBackgroundColor(Color.RED);
                        }

                        if ((prefManager.catchFloat("pengisian_filling4_apv_2_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_apv_2_tekAwal")) >= 40) {
                            rak2.setBackgroundColor(Color.GREEN);
                        } else {
                            rak2.setBackgroundColor(Color.RED);
                        }

                        if ((prefManager.catchFloat("pengisian_filling4_apv_3_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_apv_3_tekAwal")) >= 40) {
                            rak3.setBackgroundColor(Color.GREEN);
                        } else {
                            rak3.setBackgroundColor(Color.RED);
                        }

                        if ((prefManager.catchFloat("pengisian_filling4_apv_4_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_apv_4_tekAwal")) >= 40) {
                            rak4.setBackgroundColor(Color.GREEN);
                        } else {
                            rak4.setBackgroundColor(Color.RED);
                        }

                        if ((prefManager.catchFloat("pengisian_filling4_apv_5_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_apv_5_tekAwal")) >= 40) {
                            rak5.setBackgroundColor(Color.GREEN);
                        } else {
                            rak5.setBackgroundColor(Color.RED);
                        }

                        if ((prefManager.catchFloat("pengisian_filling4_apv_6_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_apv_6_tekAwal")) >= 40) {
                            rak6.setBackgroundColor(Color.GREEN);
                        } else {
                            rak6.setBackgroundColor(Color.RED);
                        }

                        if ((prefManager.catchFloat("pengisian_filling4_apv_7_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_apv_7_tekAwal")) >= 40) {
                            rak7.setBackgroundColor(Color.GREEN);
                        } else {
                            rak7.setBackgroundColor(Color.RED);
                        }

                        if ((prefManager.catchFloat("pengisian_filling4_apv_8_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_apv_8_tekAwal")) >= 40) {
                            rak8.setBackgroundColor(Color.GREEN);
                        } else {
                            rak8.setBackgroundColor(Color.RED);
                        }
                    } else if (signRakCrane >= 9 && signRakCrane <= 11) {
                        prefManager.saveString("pengisian_filling4_crane_" + signRakCrane + "_noRak", noRak.getText().toString());
                        prefManager.saveFloat("pengisian_filling4_crane_" + signRakCrane + "_tempAwal", Float.parseFloat(tempAwal.getText().toString()));
                        prefManager.saveFloat("pengisian_filling4_crane_" + signRakCrane + "_tekAwal", Float.parseFloat(tekAwal.getText().toString()));
                        prefManager.saveFloat("pengisian_filling4_crane_" + signRakCrane + "_sm3Awal", Float.parseFloat(sm3Awal.getText().toString()));

                        if ((prefManager.catchFloat("pengisian_filling4_crane_9_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_crane_9_tekAwal")) >= 40) {
                            rak9.setBackgroundColor(Color.GREEN);
                        } else {
                            rak9.setBackgroundColor(Color.RED);
                        }

                        if ((prefManager.catchFloat("pengisian_filling4_crane_10_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_crane_10_tekAwal")) >= 40) {
                            rak10.setBackgroundColor(Color.GREEN);
                        } else {
                            rak10.setBackgroundColor(Color.RED);
                        }

                        if ((prefManager.catchFloat("pengisian_filling4_crane_11_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_crane_11_tekAwal")) >= 40) {
                            rak11.setBackgroundColor(Color.GREEN);
                        } else {
                            rak11.setBackgroundColor(Color.RED);
                        }
                    } else if (signRakTrailer == 12) {
                        prefManager.saveString("pengisian_filling4_trail_12_noRak", noRak.getText().toString());
                        prefManager.saveFloat("pengisian_filling4_trail_12_tempAwal", Float.parseFloat(tempAwal.getText().toString()));
                        prefManager.saveFloat("pengisian_filling4_trail_12_tekAwal", Float.parseFloat(tekAwal.getText().toString()));
                        prefManager.saveFloat("pengisian_filling4_trail_12_sm3Awal", Float.parseFloat(sm3Awal.getText().toString()));

                        if ((prefManager.catchFloat("pengisian_filling4_trail_12_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_trail_12_tekAwal")) >= 40) {
                            rak12.setBackgroundColor(Color.GREEN);
                        } else {
                            rak12.setBackgroundColor(Color.RED);
                        }
                    }
                    GetLwc getlwc = new GetLwc();
                    getlwc.execute("");
                }
            }
        });


        kirim.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (noRak.getText().toString().trim().equals("")) {
                    noRak.setError("Tolong diisi!");
                    noRak.requestFocus();
                }

                else if (tekAwal.getText().toString().trim().equals("")) {
                    tekAwal.setError("Tolong diisi!");
                    tekAwal.requestFocus();
                }

                else if (tempAwal.getText().toString().trim().equals("")) {
                    tempAwal.setError("Tolong diisi!");
                    tempAwal.requestFocus();
                }

                else if (pressAwal.getText().toString().trim().equals("")) {
                    pressAwal.setError("Tolong diisi!");
                    pressAwal.requestFocus();
                }

                else if (sm3Awal.getText().toString().trim().equals("")) {
                    sm3Awal.setError("Tolong diisi!");
                    sm3Awal.requestFocus();
                }

                else if (tekAkhir.getText().toString().trim().equals("")) {
                    tekAkhir.setError("Tolong diisi!");
                    tekAkhir.requestFocus();
                }

                else if (tempAkhir.getText().toString().trim().equals("")) {
                    tempAkhir.setError("Tolong diisi!");
                    tempAkhir.requestFocus();
                }

                else if (pressAkhir.getText().toString().trim().equals("")) {
                    pressAkhir.setError("Tolong diisi!");
                    pressAkhir.requestFocus();
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

                    //prefManager.saveBoolean("pengisian_filling4_apv_compressor1", compressor1.);
                    statusCompressor = "";

                    if (compressor1.isChecked()) {
                        statusCompressor += "C1 ";
                        prefManager.saveBoolean("pengisian_filling4_compressor1", true);
                    }
                    if (compressor2.isChecked()) {
                        statusCompressor += "C2 ";
                        prefManager.saveBoolean("pengisian_filling4_compressor2", true);
                    }
                    if (compressor3.isChecked()) {
                        statusCompressor += "C3 ";
                        prefManager.saveBoolean("pengisian_filling4_compressor3", true);
                    }
                    if (compressor4.isChecked()) {
                        statusCompressor += "C4 ";
                        prefManager.saveBoolean("pengisian_filling4_compressor4", true);
                    }
                    if (compressor5.isChecked()) {
                        statusCompressor += "C5 ";
                        prefManager.saveBoolean("pengisian_filling4_compressor5", true);
                    }
                    if (compressor6.isChecked()) {
                        statusCompressor += "C6 ";
                        prefManager.saveBoolean("pengisian_filling4_compressor6", true);
                    }
                    if (compressor7.isChecked()) {
                        statusCompressor += "C7 ";
                        prefManager.saveBoolean("pengisian_filling4_compressor7", true);
                    }
                    if (compressor8.isChecked()) {
                        statusCompressor += "C8 ";
                        prefManager.saveBoolean("pengisian_filling4_compressor8", true);
                    }
                    if (compressor9.isChecked()) {
                        statusCompressor += "C9 ";
                        prefManager.saveBoolean("pengisian_filling4_compressor9", true);
                    }
                    if (compressor10.isChecked()) {
                        statusCompressor += "C10 ";
                        prefManager.saveBoolean("pengisian_filling4_compressor10", true);
                    }

//
//
//                    if (signRakApv > 0 && signRakApv < 9) {
//                        prefManager.saveString("pengisian_filling4_apv_"+signRakApv+"_noRak", noRak.getText().toString());
//                        prefManager.saveFloat("pengisian_filling4_apv_"+signRakApv+"_tempAwal", Float.parseFloat(tempAwal.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_apv_"+signRakApv+"_pressAwal", Float.parseFloat(pressAwal.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_apv_"+signRakApv+"_tekAwal", Float.parseFloat(tekAwal.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_apv_"+signRakApv+"_sm3Awal", Float.parseFloat(sm3Awal.getText().toString()));
//
//                        prefManager.saveFloat("pengisian_filling4_apv_"+signRakApv+"_tekAkhir", Float.parseFloat(tekAkhir.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_apv_"+signRakApv+"_tempAkhir", Float.parseFloat(tempAkhir.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_apv_"+signRakApv+"_pressAkhir", Float.parseFloat(pressAkhir.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_apv_"+signRakApv+"_sm3Akhir", Float.parseFloat(sm3Akhir.getText().toString()));
//                        prefManager.saveString("pengisian_filling4_apv_"+signRakApv+"_keterangan", keterangan.getText().toString());
//
//
//
//
//                    }
//                    else if (signRakCrane >= 9 && signRakCrane <= 11) {
//                        prefManager.saveString("pengisian_filling4_crane_"+signRakCrane+"_noRak", noRak.getText().toString());
//                        prefManager.saveFloat("pengisian_filling4_crane_"+signRakCrane+"_tempAwal", Float.parseFloat(tempAwal.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_crane_"+signRakCrane+"_pressAwal", Float.parseFloat(pressAwal.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_crane_"+signRakCrane+"_tekAwal", Float.parseFloat(tekAwal.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_crane_"+signRakCrane+"_sm3Awal", Float.parseFloat(sm3Awal.getText().toString()));
//
//                        prefManager.saveFloat("pengisian_filling4_crane_"+signRakCrane+"_tekAkhir", Float.parseFloat(tekAkhir.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_crane_"+signRakCrane+"_tempAkhir", Float.parseFloat(tempAkhir.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_crane_"+signRakCrane+"_pressAkhir", Float.parseFloat(pressAkhir.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_crane_"+signRakCrane+"_sm3Akhir", Float.parseFloat(sm3Akhir.getText().toString()));
//                        prefManager.saveString("pengisian_filling4_crane_"+signRakCrane+"_keterangan", keterangan.getText().toString());
//
//                        if ((prefManager.catchFloat("pengisian_filling4_crane_9_tekAwal")) <= 200 &&
//                                (prefManager.catchFloat("pengisian_filling4_crane_9_tekAwal")) >= 40) {
//                            rak9.setBackgroundColor(Color.GREEN);
//                        } else {
//                            rak9.setBackgroundColor(Color.RED);
//                        }
//
//                        if ((prefManager.catchFloat("pengisian_filling4_crane_10_tekAwal")) <= 200 &&
//                                (prefManager.catchFloat("pengisian_filling4_crane_10_tekAwal")) >= 40) {
//                            rak10.setBackgroundColor(Color.GREEN);
//                        } else {
//                            rak10.setBackgroundColor(Color.RED);
//                        }
//
//                        if ((prefManager.catchFloat("pengisian_filling4_crane_11_tekAwal")) <= 200 &&
//                                (prefManager.catchFloat("pengisian_filling4_crane_11_tekAwal")) >= 40) {
//                            rak11.setBackgroundColor(Color.GREEN);
//                        } else {
//                            rak11.setBackgroundColor(Color.RED);
//                        }
//                    }
//
//
//                    else if (signRakTrailer == 12) {
//                        prefManager.saveString("pengisian_filling4_trail_12_noRak", noRak.getText().toString());
//                        prefManager.saveFloat("pengisian_filling4_trail_12_tempAwal", Float.parseFloat(tempAwal.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_trail_12_pressAwal", Float.parseFloat(pressAwal.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_trail_12_tekAwal", Float.parseFloat(tekAwal.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_trail_12_sm3Awal", Float.parseFloat(sm3Awal.getText().toString()));
//
//                        prefManager.saveFloat("pengisian_filling4_trail_12_tekAkhir", Float.parseFloat(tekAkhir.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_trail_12_tempAkhir", Float.parseFloat(tempAkhir.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_trail_12_pressAkhir", Float.parseFloat(pressAkhir.getText().toString()));
//                        prefManager.saveFloat("pengisian_filling4_trail_12_sm3Akhir", Float.parseFloat(sm3Akhir.getText().toString()));
//                        prefManager.saveString("pengisian_filling4_trail_12_keterangan", keterangan.getText().toString());
//
//                        if ((prefManager.catchFloat("pengisian_filling4_trail_12_tekAwal")) <= 200 &&
//                                (prefManager.catchFloat("pengisian_filling4_trail_12_tekAwal")) >= 40) {
//                            rak12.setBackgroundColor(Color.GREEN);
//                        } else {
//                            rak12.setBackgroundColor(Color.RED);
//                        }
//                    }
//
                }

            }
        });

        reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                short reset;
                if (signRakApv > 0 && signRakApv < 9) {
                    reset = 1;
                    while (reset != 9) {
                        prefManager.saveString("pengisian_filling4_apv_"+reset+"_noRak", "");
                        prefManager.saveFloat("pengisian_filling4_apv_"+reset+"_tempAwal", 0);
                        prefManager.saveFloat("pengisian_filling4_apv_"+reset+"_pressAwal", 0);
                        prefManager.saveFloat("pengisian_filling4_apv_"+reset+"_tekAwal", 0);
                        prefManager.saveFloat("pengisian_filling4_apv_"+reset+"_sm3Awal", 0);

                        prefManager.saveFloat("pengisian_filling4_apv_"+reset+"_tekAkhir", 0);
                        prefManager.saveFloat("pengisian_filling4_apv_"+reset+"_tempAkhir", 0);
                        prefManager.saveFloat("pengisian_filling4_apv_"+reset+"_pressAkhir", 0);
                        prefManager.saveFloat("pengisian_filling4_apv_"+reset+"_sm3Akhir", 0);
                        prefManager.saveString("pengisian_filling4_apv_"+reset+"_keterangan", "");
                        reset++;
                    }
                    rak1.setBackgroundColor(Color.RED);
                    rak2.setBackgroundColor(Color.RED);
                    rak3.setBackgroundColor(Color.RED);
                    rak4.setBackgroundColor(Color.RED);
                    rak5.setBackgroundColor(Color.RED);
                    rak6.setBackgroundColor(Color.RED);
                    rak7.setBackgroundColor(Color.RED);
                    rak8.setBackgroundColor(Color.RED);


                } else if (signRakCrane >= 9 && signRakCrane <= 11) {
                    reset = 9;
                    while (reset != 12) {
                        prefManager.saveString("pengisian_filling4_crane_"+reset+"_noRak", "");
                        prefManager.saveFloat("pengisian_filling4_crane_"+reset+"_tempAwal", 0);
                        prefManager.saveFloat("pengisian_filling4_crane_"+reset+"_pressAwal", 0);
                        prefManager.saveFloat("pengisian_filling4_crane_"+reset+"_tekAwal", 0);
                        prefManager.saveFloat("pengisian_filling4_crane_"+reset+"_sm3Awal", 0);

                        prefManager.saveFloat("pengisian_filling4_crane_"+reset+"_tekAkhir", 0);
                        prefManager.saveFloat("pengisian_filling4_crane_"+reset+"_tempAkhir", 0);
                        prefManager.saveFloat("pengisian_filling4_crane_"+reset+"_pressAkhir", 0);
                        prefManager.saveFloat("pengisian_filling4_crane_"+reset+"_sm3Akhir", 0);
                        prefManager.saveString("pengisian_filling4_crane_"+reset+"_keterangan", "");
                        reset++;
                    }
                    rak9.setBackgroundColor(Color.RED);
                    rak10.setBackgroundColor(Color.RED);
                    rak11.setBackgroundColor(Color.RED);

                } else if (signRakTrailer == 12) {
                    prefManager.saveString("pengisian_filling4_apv_12_noRak", "");
                    prefManager.saveFloat("pengisian_filling4_apv_12_tempAwal", 0);
                    prefManager.saveFloat("pengisian_filling4_apv_12_pressAwal", 0);
                    prefManager.saveFloat("pengisian_filling4_apv_12_tekAwal", 0);
                    prefManager.saveFloat("pengisian_filling4_apv_12_sm3Awal", 0);

                    prefManager.saveFloat("pengisian_filling4_apv_12_tekAkhir", 0);
                    prefManager.saveFloat("pengisian_filling4_apv_12_tempAkhir", 0);
                    prefManager.saveFloat("pengisian_filling4_apv_12_pressAkhir", 0);
                    prefManager.saveFloat("pengisian_filling4_apv_12_sm3Akhir", 0);
                    prefManager.saveString("pengisian_filling4_apv_12_keterangan", "");

                    rak12.setBackgroundColor(Color.RED);
                }
                noRak.setText("");
                tekAwal.setText("");
                tempAwal.setText("");
                pressAwal.setText("");
                sm3Awal.setText("");

                tekAkhir.setText("");
                tempAkhir.setText("");
                pressAkhir.setText("");
                sm3Akhir.setText("");
                keterangan.setText("");
//
//                CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
//                checkLogin.execute("");

            }
        });


        selectRakCrane.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_9 :
                        signRakCrane = 9;
                        fungsiCatchCrane();
                        magicPengubahZero();
                        noRak.requestFocus();

                        break;
                    case R.id.choice_10 :

                        signRakCrane = 10;
                        fungsiCatchCrane();
                        magicPengubahZero();
                        noRak.requestFocus();

                        break;
                    case R.id.choice_11 :
                        signRakCrane = 11;
                        fungsiCatchCrane();
                        magicPengubahZero();
                        noRak.requestFocus();

                        break;


                }
            }
        });

        selectRakApv.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_1 :
                        signRakApv = 1;
                        fungsiCatchApv();
                        magicPengubahZero();
                        noRak.requestFocus();

                        break;

                    case R.id.choice_2 :
                        signRakApv = 2;
                        fungsiCatchApv();
                        magicPengubahZero();
                        noRak.requestFocus();

                        break;

                    case R.id.choice_3 :
                        signRakApv = 3;
                        fungsiCatchApv();
                        magicPengubahZero();
                        noRak.requestFocus();
                        break;

                    case R.id.choice_4 :
                        signRakApv = 4;
                        fungsiCatchApv();
                        magicPengubahZero();
                        noRak.requestFocus();
                        break;

                    case R.id.choice_5 :
                        signRakApv = 5;
                        fungsiCatchApv();
                        magicPengubahZero();
                        noRak.requestFocus();
                        break;

                    case R.id.choice_6 :
                        signRakApv = 6;
                        fungsiCatchApv();
                        magicPengubahZero();
                        noRak.requestFocus();
                        break;

                    case R.id.choice_7 :
                        signRakApv = 7;
                        fungsiCatchApv();
                        magicPengubahZero();
                        noRak.requestFocus();
                        break;

                    case R.id.choice_8 :
                        signRakApv = 8;
                        fungsiCatchApv();
                        magicPengubahZero();
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

                        fungsiCatchApv();
                        magicPengubahZero();
                        noRak.requestFocus();


                        frameApv.setVisibility(View.VISIBLE);
                        frameCrane.setVisibility(GONE);
                        frameTrailer.setVisibility(GONE);

                        //frameApv.requestFocus();

//                        crane.setVisibility(View.GONE);
//                        trailer.setVisibility(View.GONE);
//                        Toast.makeText(getActivity(), "1!", Toast.LENGTH_SHORT).show();
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

                        fungsiCatchCrane();
                        magicPengubahZero();
                        noRak.requestFocus();

                        if ((prefManager.catchFloat("pengisian_filling4_crane_9_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_crane_9_tekAwal")) >= 40) {
                            rak9.setBackgroundColor(Color.GREEN);
                        } else {
                            rak9.setBackgroundColor(Color.RED);
                        }

                        if ((prefManager.catchFloat("pengisian_filling4_crane_10_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_crane_10_tekAwal")) >= 40) {
                            rak10.setBackgroundColor(Color.GREEN);
                        } else {
                            rak10.setBackgroundColor(Color.RED);
                        }

                        if ((prefManager.catchFloat("pengisian_filling4_crane_11_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_crane_11_tekAwal")) >= 40) {
                            rak11.setBackgroundColor(Color.GREEN);
                        } else {
                            rak11.setBackgroundColor(Color.RED);
                        }

//                        Toast.makeText(getActivity(), "2!", Toast.LENGTH_SHORT).show();
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

                        noRak.setText(prefManager.catchString("pengisian_filling4_trail_12_noRak"));
                        tekAwal.setText(prefManager.catchFloat("pengisian_filling4_trail_12_tekAwal").toString());
                        tempAwal.setText(prefManager.catchFloat("pengisian_filling4_trail_12_tempAwal").toString());
                        pressAwal.setText(prefManager.catchFloat("pengisian_filling4_trail_12_pressAwal").toString());
                        sm3Awal.setText(prefManager.catchFloat("pengisian_filling4_trail_12_sm3Awal").toString());

                        tekAkhir.setText(prefManager.catchFloat("pengisian_filling4_tekAkhir").toString());
                        tempAkhir.setText(prefManager.catchFloat("pengisian_filling4_tempAkhir").toString());
                        pressAkhir.setText(prefManager.catchFloat("pengisian_filling4_pressAkhir").toString());
                        sm3Akhir.setText(prefManager.catchFloat("pengisian_filling4_sm3Akhir").toString());
                        keterangan.setText(prefManager.catchString("pengisian_filling4_keterangan"));

                        magicPengubahZero();
                        noRak.requestFocus();

                        if ((prefManager.catchFloat("pengisian_filling4_trail_12_tekAwal")) <= 200 &&
                                (prefManager.catchFloat("pengisian_filling4_trail_12_tekAwal")) >= 40) {
                            rak12.setBackgroundColor(Color.GREEN);
                        } else {
                            rak12.setBackgroundColor(Color.RED);
                        }

//                        Toast.makeText(getActivity(), "3!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
    public void magicPengubahZero() {
        if (tekAwal.getText().toString().equals("0.0"))
            tekAwal.setText("");
        if (tempAwal.getText().toString().equals("0.0"))
            tempAwal.setText("");
        if (sm3Awal.getText().toString().equals("0.0"))
            sm3Awal.setText("");
        if (tekAkhir.getText().toString().equals("0.0"))
            tekAkhir.setText("");
        if (tempAkhir.getText().toString().equals("0.0"))
            tempAkhir.setText("");
        if (pressAkhir.getText().toString().equals("0.0"))
            pressAkhir.setText("");
        if (sm3Akhir.getText().toString().equals("0.0"))
            sm3Akhir.setText("");
    }

    public void fungsiCatchApv() {
        noRak.setText(prefManager.catchString("pengisian_filling4_apv_"+signRakApv+"_noRak"));
        tekAwal.setText(prefManager.catchFloat("pengisian_filling4_apv_"+signRakApv+"_tekAwal").toString());
        tempAwal.setText(prefManager.catchFloat("pengisian_filling4_apv_"+signRakApv+"_tempAwal").toString());
        pressAwal.setText(prefManager.catchFloat("pengisian_filling4_apv_"+signRakApv+"_pressAwal").toString());
        sm3Awal.setText(prefManager.catchFloat("pengisian_filling4_apv_"+signRakApv+"_sm3Awal").toString());

        tekAkhir.setText(prefManager.catchFloat("pengisian_filling4_tekAkhir").toString());
        tempAkhir.setText(prefManager.catchFloat("pengisian_filling4_tempAkhir").toString());
        pressAkhir.setText(prefManager.catchFloat("pengisian_filling4_pressAkhir").toString());
        sm3Akhir.setText(prefManager.catchFloat("pengisian_filling4_sm3Akhir").toString());
        keterangan.setText(prefManager.catchString("pengisian_filling4_keterangan"));
    }

    public void fungsiCatchCrane() {
        noRak.setText(prefManager.catchString("pengisian_filling4_crane_"+signRakCrane+"_noRak"));
        tekAwal.setText(prefManager.catchFloat("pengisian_filling4_crane_"+signRakCrane+"_tekAwal").toString());
        tempAwal.setText(prefManager.catchFloat("pengisian_filling4_crane_"+signRakCrane+"_tempAwal").toString());
        pressAwal.setText(prefManager.catchFloat("pengisian_filling4_crane_"+signRakCrane+"_pressAwal").toString());
        sm3Awal.setText(prefManager.catchFloat("pengisian_filling4_crane_"+signRakCrane+"_sm3Awal").toString());

        tekAkhir.setText(prefManager.catchFloat("pengisian_filling4_tekAkhir").toString());
        tempAkhir.setText(prefManager.catchFloat("pengisian_filling4_tempAkhir").toString());
        pressAkhir.setText(prefManager.catchFloat("pengisian_filling4_pressAkhir").toString());
        sm3Akhir.setText(prefManager.catchFloat("pengisian_filling4_sm3Akhir").toString());
        keterangan.setText(prefManager.catchString("pengisian_filling4_keterangan"));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str_time = intent.getStringExtra("time4");
            stopwatch.setText(str_time);

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver,new IntentFilter(BackgroundService4.str_receiver));

    }

    @Override
    public void onPause() {

        //String str_time = inte.getStringExtra("time");
        //tv_timer.setText(str_time);
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    public class FillList extends AsyncTask<String, String, String> {

        String z = "";

        //List<Map<String, String>> prolist  = new ArrayList<Map<String, String>>();
        List<String> prolist = new ArrayList<String>();
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
                        Map<String, String> datanum = new HashMap<String, String>();
                        //datanum.pu
                        //datanum.put("A", rs.getString("nama"));

                        prolist.add(rs.getString("No_moda"));
//                        your_array_list.add
                        //prolist.add(datanum);
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

    public class AddIsiGas extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
            //Toast.makeText(getActivity(), prefManager.catchString("pengisian_filling4_timeAwal"), Toast.LENGTH_SHORT).show();
            prefManager.saveFloat("pengisian_filling4_tekAkhir", Float.valueOf(tekAkhir.getText().toString()));
            prefManager.saveFloat("pengisian_filling4_tempAkhir", Float.valueOf(tempAkhir.getText().toString()));
            prefManager.saveFloat("pengisian_filling4_pressAkhir", Float.valueOf(pressAkhir.getText().toString()));
            prefManager.saveFloat("pengisian_filling4_sm3Akhir", Float.valueOf(sm3Akhir.getText().toString()));
            prefManager.saveString("pengisian_filling4_keterangan", keterangan.getText().toString());
            //prefManager.saveString("pengisian_filling4_sm3Akhir", keterangan.getText().toString());
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
                        short aku;
                        if (signRakApv >= 1 && signRakApv <= 8) {
                            aku = 1;


//                            id_pressure tekanan_awal temp_awal pressure_awal SM3_awal tekanan_akhir
//                            temp_akhir pressure_akhir SM3_akhir keterangan
                            //
                            while (aku != 9) {
                                //convert(varchar,convert(datetime,'2013-09-17 21:01:00.000'),103)
                                //CONVERT(datetime, '2014-11-30 23:59:59.997', 121);
                                //convert(datetime, '" + prefManager.catchString("pengisian_filling4_timeAkhir") + "', 103)
                                query = "insert into tp.master_pressure values (" + prefManager.catchFloat("pengisian_filling4_apv_"+aku+"_tekAwal") + ", " + prefManager.catchFloat("pengisian_filling4_apv_"+aku+"_tempAwal") +
                                        ", " + prefManager.catchFloat("pengisian_filling4_apv_"+aku+"_pressAwal") + ", " + prefManager.catchFloat("pengisian_filling4_apv_"+aku+"_sm3Awal") + ", " +
                                        + prefManager.catchFloat("pengisian_filling4_tekAkhir") + ", " + prefManager.catchFloat("pengisian_filling4_tempAkhir") + ", " + prefManager.catchFloat("pengisian_filling4_pressAkhir")
                                        + ", " + prefManager.catchFloat("pengisian_filling4_sm3Akhir") + ",  '" + keterangan.getText().toString() + "' )";

                                preparedStatement = con.prepareStatement(query);
                                preparedStatement.executeUpdate();

                                query = "insert into tp.master_rak (nomor_rak, keterangan, tanggal) values ('" + prefManager.catchString("pengisian_filling4_apv_"+aku+"_noRak") + "', '" + prefManager.catchString("pengisian_filling4_keterangan") +
                                        "', convert(datetime, '" + prefManager.catchString("pengisian_filling4_timeAkhir") + "', 103))";
                                preparedStatement = con.prepareStatement(query);
                                preparedStatement.executeUpdate();

                                query = "insert into tp.master_filling (id_filling, moda_transport, compressor) values (4, 'APV', '" + statusCompressor + "')";
                                preparedStatement = con.prepareStatement(query);
                                preparedStatement.executeUpdate();

                                query = "insert into tp.master_tanggal (tanggal_awal, tanggal_akhir, jam_awal, jam_akhir, durasi) values (convert(date, '" + prefManager.catchString("pengisian_filling4_timeAwal") + "', 103), convert(date, '" + prefManager.catchString("pengisian_filling4_timeAkhir") + "', 103), "
                                        + "convert(datetime, convert(datetime, '" + prefManager.catchString("pengisian_filling4_timeAwal") + "', 103), 108), " + "convert(datetime, convert(datetime, '" + prefManager.catchString("pengisian_filling4_timeAkhir") + "', 103), 108), " +
                                        "convert(time, convert(datetime, '10/02/1998 " + stopwatch.getText().toString() + "', 103), 108))";
                                preparedStatement = con.prepareStatement(query);
                                preparedStatement.executeUpdate();

                                //query =
                                aku++;
                            }

                        } else if (signRakCrane >= 9 && signRakCrane <= 11) {
                            aku = 9;
                            while (aku != 12) {
                                //convert(varchar,convert(datetime,'2013-09-17 21:01:00.000'),103)
                                //CONVERT(datetime, '2014-11-30 23:59:59.997', 121);
                                query = "insert into tp.master_pressure values (" + prefManager.catchFloat("pengisian_filling4_crane_"+aku+"_tekAwal") + ", " + prefManager.catchFloat("pengisian_filling4_crane_"+aku+"_tempAwal") +
                                        ", " + prefManager.catchFloat("pengisian_filling4_crane_"+aku+"_pressAwal") + ", " + prefManager.catchFloat("pengisian_filling4_crane_"+aku+"_sm3Awal") + ", " +
                                        + prefManager.catchFloat("pengisian_filling4_tekAkhir") + ", " + prefManager.catchFloat("pengisian_filling4_tempAkhir") + ", " + prefManager.catchFloat("pengisian_filling4_pressAkhir")
                                        + ", " + prefManager.catchFloat("pengisian_filling4_sm3Akhir") + ",  '" + keterangan.getText().toString() + "' )";

                                preparedStatement = con.prepareStatement(query);
                                preparedStatement.executeUpdate();

                                query = "insert into tp.master_rak (nomor_rak, keterangan, tanggal) values ('" + prefManager.catchString("pengisian_filling4_crane_"+aku+"_noRak") + "', '" + prefManager.catchString("pengisian_filling4_keterangan") +
                                        "', convert(datetime, '" + prefManager.catchString("pengisian_filling4_timeAkhir") + "', 103))";
                                preparedStatement = con.prepareStatement(query);
                                preparedStatement.executeUpdate();

                                query = "insert into tp.master_filling (id_filling, moda_transport, compressor) values (4, 'HYAPCRANE', '" + statusCompressor + "')";
                                preparedStatement = con.prepareStatement(query);
                                preparedStatement.executeUpdate();

                                query = "insert into tp.master_tanggal (tanggal_awal, tanggal_akhir, jam_awal, jam_akhir, durasi) values (convert(date, '" + prefManager.catchString("pengisian_filling4_timeAwal") + "', 103), convert(date, '" + prefManager.catchString("pengisian_filling4_timeAkhir") + "', 103), "
                                        + "convert(datetime, convert(datetime, '" + prefManager.catchString("pengisian_filling4_timeAwal") + "', 103), 108), " + "convert(datetime, convert(datetime, '" + prefManager.catchString("pengisian_filling4_timeAkhir") + "', 103), 108), " +
                                        "convert(time, convert(datetime, '10/02/1998 " + stopwatch.getText().toString() + "', 103), 108))";
                                preparedStatement = con.prepareStatement(query);
                                preparedStatement.executeUpdate();

                                //query =
                                aku++;
                            }
                        }

                        else {
                            query = "insert into tp.master_pressure values (" + prefManager.catchFloat("pengisian_filling4_trail_12_tekAwal") + ", " + prefManager.catchFloat("pengisian_filling4_trail_12_tempAwal") +
                                    ", " + prefManager.catchFloat("pengisian_filling4_trail_12_pressAwal") + ", " + prefManager.catchFloat("pengisian_filling4_trail_12_sm3Awal") + ", " +
                                    + prefManager.catchFloat("pengisian_filling4_tekAkhir") + ", " + prefManager.catchFloat("pengisian_filling4_tempAkhir") + ", " + prefManager.catchFloat("pengisian_filling4_pressAkhir")
                                    + ", " + prefManager.catchFloat("pengisian_filling4_sm3Akhir") + ",  '" + keterangan.getText().toString() + "' )";

                            preparedStatement = con.prepareStatement(query);
                            preparedStatement.executeUpdate();

                            query = "insert into tp.master_rak (nomor_rak, keterangan, tanggal) values ('" + prefManager.catchString("pengisian_filling4_trail_12_noRak") + "', '" + prefManager.catchString("pengisian_filling4_keterangan") +
                                    "', convert(datetime, '" + prefManager.catchString("pengisian_filling4_timeAkhir") + "', 103))";
                            preparedStatement = con.prepareStatement(query);
                            preparedStatement.executeUpdate();

                            query = "insert into tp.master_filling (id_filling, moda_transport, compressor) values (4, 'TRAILER', '" + statusCompressor + "')";
                            preparedStatement = con.prepareStatement(query);
                            preparedStatement.executeUpdate();

                            query = "insert into tp.master_tanggal (tanggal_awal, tanggal_akhir, jam_awal, jam_akhir, durasi) values (convert(date, '" + prefManager.catchString("pengisian_filling4_timeAwal") + "', 103), convert(date, '" + prefManager.catchString("pengisian_filling4_timeAkhir") + "', 103), "
                                    + "convert(datetime, convert(datetime, '" + prefManager.catchString("pengisian_filling4_timeAwal") + "', 103), 108), " + "convert(datetime, convert(datetime, '" + prefManager.catchString("pengisian_filling4_timeAkhir") + "', 103), 108), " +
                                    "convert(time, convert(datetime, '10/02/1998 " + stopwatch.getText().toString() + "', 103), 108))";
                            preparedStatement = con.prepareStatement(query);
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


    public class GetLwc extends AsyncTask<String,String,String>
    {
        String z = "";

        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r)
        {
            progressBar.setVisibility(View.GONE);
            //Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
            double p0 = Double.valueOf(tekAwal.getText().toString());
            double t0 = Double.valueOf(tempAwal.getText().toString());
            double gravity = 0.65321;
            double co2 = 0.459;
            double n2 = 1.02;

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
                lwc =  Double.parseDouble(prefManager.catchString("pengisian_filling4_apv_"+signRakApv+"_lwc"));
            else if (signRakCrane >= 9 && signRakCrane <= 11)
                lwc =  Double.parseDouble(prefManager.catchString("pengisian_filling4_crane_"+signRakCrane+"_lwc"));
            else
                lwc =  Double.parseDouble(prefManager.catchString("pengisian_filling4_trail_12_lwc"));
            double p1 = lwc / 1000;

            double sm3 = p1*(p0+1.01325)/1.01325*(273+27)/(273+t0)*Math.pow(fpv, 2);
            pressAwal.setText(String.format("%.2f", sm3));
            if (signRakApv >= 1 && signRakApv <= 8)
                prefManager.saveFloat("pengisian_filling4_apv_"+signRakApv+"_pressAwal", Float.parseFloat(pressAwal.getText().toString()));
            else if (signRakCrane >= 9 && signRakCrane <= 11)
                prefManager.saveFloat("pengisian_filling4_crane_"+signRakCrane+"_pressAwal", Float.parseFloat(pressAwal.getText().toString()));
            else
                prefManager.saveFloat("pengisian_filling4_trail_"+signRakTrailer+"_pressAwal", Float.parseFloat(pressAwal.getText().toString()));

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
                            prefManager.saveString("pengisian_filling4_apv_" + signRakApv + "_lwc", rs.getString("LWC").toString());
                        else if (signRakCrane >= 9 && signRakCrane <= 11)
                            prefManager.saveString("pengisian_filling4_crane_" + signRakCrane + "_lwc", rs.getString("LWC").toString());
                        else
                            prefManager.saveString("pengisian_filling4_trail_12_lwc", rs.getString("LWC").toString());

                    }



                }
            } catch (Exception ex) {

                Toast.makeText(getActivity(), "kosong", Toast.LENGTH_SHORT).show();
            }

            return z;
        }
    }
}
