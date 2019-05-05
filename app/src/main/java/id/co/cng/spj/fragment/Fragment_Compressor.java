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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.widget.Switch;
import android.widget.TextView;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.co.cng.spj.MenuUtamaActivity;
import id.co.cng.spj.R;
import id.co.cng.spj.utility.ConnectionHelper;
import id.co.cng.spj.utility.PrefManager;

public class Fragment_Compressor extends Fragment {

    private Spinner spin4,spin5;
    private String[] comp_comp = {
            "Compressor 1 Waru",
            "Compressor 2 Waru",
            "Compressor 3 Waru",
            "Compressor 4 Waru",
            "Compressor 5 Waru"
    };
    private String[] comp_shift = {
            "P2(07:00AM-03:00PM)",
            "S2(03:00AM-11:00PM)",
            "M2(11:00PM-07:00AM)"
    };


    PrefManager prefManager;
    Connection con;

    TextInputEditText jam_op, rpm, temp_ambient, crankcase_add, lubrication_add, condensate,
            gas_in1, gas_out1, gas_out2, gas_out3, gas_out4,
            gas_stage, gas_sta1, gas_sta2, gas_sta3, gas_sta4,
            volt, ampere, cosq, mrs, mrs_tek, mrs_temp, mrs_stand, comp_keterangan;

    Button button_kirim_comp, reset;

    ProgressBar progressBar;

    Switch switchCompressorOil, switchPompaOil, switchCrankcaseOil, switchTekananOil,
            switchGasDalamTekanan, switchBeltTension, switchPsv, switchPanelKontrol,
            switchSuaraMesin, switchVibrasi, switchKebocoran, switchKebersihan;
    TextView textCompressorOil, textPompaOil, textCrankcaseOil, textTekananOil, textGasDalamTekanan, textBeltTension,
            textPsv, textPanelKontrol, textSuaraMesin, textVibrasi, textKebocoran, textKebersihan;

    String switchOn = "Yes";
    String switchOff = "No ";

    private int[] listInputTextInt = {
            R.id.text_compressor_operating_hours,
            R.id.text_compressor_rpm,
            R.id.text_compressor_ambient_temperature,
            R.id.text_compressor_crankcase_oil_add,
            R.id.text_compressor_lubrication_oil_add,
            R.id.text_compressor_condensate,
            R.id.text_compressor_gas_inlet_pressure_stage1,
            R.id.text_compressor_gas_outlet_pressure_stage_1,
            R.id.text_compressor_gas_outlet_pressure_stage_2,
            R.id.text_compressor_gas_outlet_pressure_stage_3,
            R.id.text_compressor_gas_outlet_pressure_stage_4,
            R.id.text_compressor_gas_temperature_stage,
            R.id.text_compressor_gas_temperature_stage_1,
            R.id.text_compressor_gas_temperature_stage_2,
            R.id.text_compressor_gas_temperature_stage_3,
            R.id.text_compressor_gas_temperature_stage_4,
            R.id.text_compressor_volt,
            R.id.text_compressor_ampere,
            R.id.text_compressor_cos,
            R.id.text_compressor_mrs,
            R.id.text_compressor_tekanan_bar,
            R.id.text_compressor_temp,
            R.id.text_compressor_stand
    };
    TextInputEditText listInputText;

    View view;

    public Fragment_Compressor() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ((MenuUtamaActivity) getActivity())
                .setActionBarTitle("Cek Compressor");


        jam_op=view.findViewById(R.id.text_compressor_operating_hours);
        rpm=view.findViewById(R.id.text_compressor_rpm);
        temp_ambient=view.findViewById(R.id.text_compressor_ambient_temperature);
        crankcase_add=view.findViewById(R.id.text_compressor_crankcase_oil_add);
        lubrication_add=view.findViewById(R.id.text_compressor_lubrication_oil_add);
        condensate=view.findViewById(R.id.text_compressor_condensate);

        gas_in1=view.findViewById(R.id.text_compressor_gas_inlet_pressure_stage1);
        gas_out1=view.findViewById(R.id.text_compressor_gas_outlet_pressure_stage_1);
        gas_out2=view.findViewById(R.id.text_compressor_gas_outlet_pressure_stage_2);
        gas_out3=view.findViewById(R.id.text_compressor_gas_outlet_pressure_stage_3);
        gas_out4=view.findViewById(R.id.text_compressor_gas_outlet_pressure_stage_4);

        gas_stage=view.findViewById(R.id.text_compressor_gas_temperature_stage);
        gas_sta1=view.findViewById(R.id.text_compressor_gas_temperature_stage_1);
        gas_sta2=view.findViewById(R.id.text_compressor_gas_temperature_stage_2);
        gas_sta3=view.findViewById(R.id.text_compressor_gas_temperature_stage_3);
        gas_sta4=view.findViewById(R.id.text_compressor_gas_temperature_stage_4);

        volt=view.findViewById(R.id.text_compressor_volt);
        ampere=view.findViewById(R.id.text_compressor_ampere);
        cosq=view.findViewById(R.id.text_compressor_cos);
        mrs=view.findViewById(R.id.text_compressor_mrs);
        mrs_tek=view.findViewById(R.id.text_compressor_tekanan_bar);
        mrs_temp=view.findViewById(R.id.text_compressor_temp);
        mrs_stand=view.findViewById(R.id.text_compressor_stand);
        comp_keterangan=view.findViewById(R.id.text_compressor_keterangan);

        switchCompressorOil = view.findViewById(R.id.switch_compressor_engine_compressor_oil);
        textCompressorOil = view.findViewById(R.id.textView_compressor_engine_compressor_oil);
        switchPompaOil = view.findViewById(R.id.switch_compressor_pompa_oli);
        textPompaOil = view.findViewById(R.id.textView_compressor_pompa_oli);
        switchCrankcaseOil = view.findViewById(R.id.switch_compressor_crankcase_oli);
        textCrankcaseOil = view.findViewById(R.id.textView_compressor_crankcase_oli);
        switchTekananOil = view.findViewById(R.id.switch_compressor_tekanan_oli);
        textTekananOil = view.findViewById(R.id.textView_compressor_tekanan_oli);
        switchGasDalamTekanan = view.findViewById(R.id.switch_compressor_gas_dalam_tekanan);
        textGasDalamTekanan = view.findViewById(R.id.textView_compressor_gas_dalam_tekanan);
        switchBeltTension = view.findViewById(R.id.switch_compressor_belt);
        textBeltTension = view.findViewById(R.id.textView_compressor_belt);
        switchPsv = view.findViewById(R.id.switch_compressor_psv);
        textPsv = view.findViewById(R.id.textView_compressor_psv);
        switchPanelKontrol = view.findViewById(R.id.switch_compressor_panel);
        textPanelKontrol = view.findViewById(R.id.textView_compressor_panel);
        switchSuaraMesin = view.findViewById(R.id.switch_compressor_suara);
        textSuaraMesin = view.findViewById(R.id.textView_compressor_suara);
        switchVibrasi = view.findViewById(R.id.switch_compressor_vibrasi);
        textVibrasi = view.findViewById(R.id.textView_compressor_vibrasi);
        switchKebocoran = view.findViewById(R.id.switch_compressor_bocor);
        textKebocoran = view.findViewById(R.id.textView_compressor_bocor);
        switchKebersihan = view.findViewById(R.id.switch_compressor_clean);
        textKebersihan = view.findViewById(R.id.textView_compressor_clean);

        progressBar = view.findViewById(R.id.progressBar_compressor);
        button_kirim_comp=view.findViewById(R.id.button_compressor_kirim);
        reset = view.findViewById(R.id.button_compressor_reset);
        prefManager = new PrefManager(getActivity());
////////////////////////////////////////////////////////////////////////////////////////////////////

        catchStatusCompressor();

        switchCompressorOil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textCompressorOil.setText(switchOn);
                } else {
                    textCompressorOil.setText(switchOff);
                }
            }
        });

        if (switchCompressorOil.isChecked()) {
            textCompressorOil.setText(switchOn);
        } else {
            textCompressorOil.setText(switchOff);
        }

        // For switch button 7



        switchPompaOil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textPompaOil.setText(switchOn);
                } else {
                    textPompaOil.setText(switchOff);
                }
            }
        });

        if (switchPompaOil.isChecked()) {
            textPompaOil.setText(switchOn);
        } else {
            textPompaOil.setText(switchOff);
        }

        // For switch button 8


        switchCrankcaseOil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textCrankcaseOil.setText(switchOn);
                } else {
                    textCrankcaseOil.setText(switchOff);
                }
            }
        });

        if (switchCrankcaseOil.isChecked()) {
            textCrankcaseOil.setText(switchOn);
        } else {
            textCrankcaseOil.setText(switchOff);
        }

        // For switch button 9


        switchTekananOil.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textTekananOil.setText(switchOn);
                } else {
                    textTekananOil.setText(switchOff);
                }
            }
        });

        if (switchTekananOil.isChecked()) {
            textTekananOil.setText(switchOn);
        } else {
            textTekananOil.setText(switchOff);
        }

        // For switch button 10



        switchGasDalamTekanan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textGasDalamTekanan.setText(switchOn);
                } else {
                    textGasDalamTekanan.setText(switchOff);
                }
            }
        });

        if (switchGasDalamTekanan.isChecked()) {
            textGasDalamTekanan.setText(switchOn);
        } else {
            textGasDalamTekanan.setText(switchOff);
        }

        // For switch button 11



        switchBeltTension.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textBeltTension.setText(switchOn);
                } else {
                    textBeltTension.setText(switchOff);
                }
            }
        });

        if (switchBeltTension.isChecked()) {
            textBeltTension.setText(switchOn);
        } else {
            textBeltTension.setText(switchOff);
        }

        // For switch button 12



        switchPsv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textPsv.setText(switchOn);
                } else {
                    textPsv.setText(switchOff);
                }
            }
        });

        if (switchPsv.isChecked()) {
            textPsv.setText(switchOn);
        } else {
            textPsv.setText(switchOff);
        }

        // For switch button 13



        switchPanelKontrol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textPanelKontrol.setText(switchOn);
                } else {
                    textPanelKontrol.setText(switchOff);
                }
            }
        });

        if (switchPanelKontrol.isChecked()) {
            textPanelKontrol.setText(switchOn);
        } else {
            textPanelKontrol.setText(switchOff);
        }

        // For switch button 14



        switchSuaraMesin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textSuaraMesin.setText(switchOn);
                } else {
                    textSuaraMesin.setText(switchOff);
                }
            }
        });

        if (switchSuaraMesin.isChecked()) {
            textSuaraMesin.setText(switchOn);
        } else {
            textSuaraMesin.setText(switchOff);
        }

        // For switch button 15



        switchVibrasi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textVibrasi.setText(switchOn);
                } else {
                    textVibrasi.setText(switchOff);
                }
            }
        });

        if (switchVibrasi.isChecked()) {
            textVibrasi.setText(switchOn);
        } else {
            textVibrasi.setText(switchOff);
        }

        // For switch button 16



        switchKebocoran.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textKebocoran.setText(switchOn);
                } else {
                    textKebocoran.setText(switchOff);
                }
            }
        });

        if (switchKebocoran.isChecked()) {
            textKebocoran.setText(switchOn);
        } else {
            textKebocoran.setText(switchOff);
        }

        // For switch button 17



        switchKebersihan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textKebersihan.setText(switchOn);
                } else {
                    textKebersihan.setText(switchOff);
                }
            }
        });

        if (switchKebersihan.isChecked()) {
            textKebersihan.setText(switchOn);
        } else {
            textKebersihan.setText(switchOff);
        }

        button_kirim_comp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (jam_op.getText().toString().trim().equals("")) {
                    jam_op.setError("Tolong diisi!");
                    jam_op.requestFocus();
                }
//                else if (rpm.getText().toString().trim().equals("")) {
//                    rpm.setError("Tolong diisi!");
//                    rpm.requestFocus();
//                }
//                else if (temp_ambient.getText().toString().trim().equals("")) {
//                    temp_ambient.setError("Tolong diisi!");
//                    temp_ambient.requestFocus();
//                }
//                else if (crankcase_add.getText().toString().trim().equals("")) {
//                    crankcase_add.setError("Tolong diisi!");
//                    crankcase_add.requestFocus();
//                }
//                else if (lubrication_add.getText().toString().trim().equals("")) {
//                    lubrication_add.setError("Tolong diisi!");
//                    lubrication_add.requestFocus();
//                }
//                else if (condensate.getText().toString().trim().equals("")) {
//                    condensate.setError("Tolong diisi!");
//                    condensate.requestFocus();
//                }
//                else if (gas_in1.getText().toString().trim().equals("")) {
//                    gas_in1.setError("Tolong diisi!");
//                    gas_in1.requestFocus();
//                }
//                else if (gas_out1.getText().toString().trim().equals("")) {
//                    gas_out1.setError("Tolong diisi!");
//                    gas_out1.requestFocus();
//                }
//                else if (gas_out2.getText().toString().trim().equals("")) {
//                    gas_out2.setError("Tolong diisi!");
//                    gas_out2.requestFocus();
//                }
//                else if (gas_out3.getText().toString().trim().equals("")) {
//                    gas_out3.setError("Tolong diisi!");
//                    gas_out3.requestFocus();
//                }
//                else if (gas_out4.getText().toString().trim().equals("")) {
//                    gas_out4.setError("Tolong diisi!");
//                    gas_out4.requestFocus();
//                }
//                else if (gas_stage.getText().toString().trim().equals("")) {
//                    gas_stage.setError("Tolong diisi!");
//                    gas_stage.requestFocus();
//                }
//                else if (gas_sta1.getText().toString().trim().equals("")) {
//                    gas_sta1.setError("Tolong diisi!");
//                    gas_sta1.requestFocus();
//                }
//                else if (gas_sta2.getText().toString().trim().equals("")) {
//                    gas_sta2.setError("Tolong diisi!");
//                    gas_sta2.requestFocus();
//                }
//                else if (gas_sta3.getText().toString().trim().equals("")) {
//                    gas_sta3.setError("Tolong diisi!");
//                    gas_sta3.requestFocus();
//                }
//                else if (gas_sta4.getText().toString().trim().equals("")) {
//                    gas_sta4.setError("Tolong diisi!");
//                    gas_sta4.requestFocus();
//                }
//                else if (volt.getText().toString().trim().equals("")) {
//                    volt.setError("Tolong diisi!");
//                    volt.requestFocus();
//                }
//                else if (ampere.getText().toString().trim().equals("")) {
//                    ampere.setError("Tolong diisi!");
//                    ampere.requestFocus();
//                }
//                else if (cosq.getText().toString().trim().equals("")) {
//                    cosq.setError("Tolong diisi!");
//                    cosq.requestFocus();
//                }
//                else if (mrs.getText().toString().trim().equals("")) {
//                    mrs.setError("Tolong diisi!");
//                    mrs.requestFocus();
//                }
//                else if (mrs_tek.getText().toString().trim().equals("")) {
//                    mrs_tek.setError("Tolong diisi!");
//                    mrs_tek.requestFocus();
//                }
//                else if (mrs_temp.getText().toString().trim().equals("")) {
//                    mrs_temp.setError("Tolong diisi!");
//                    mrs_temp.requestFocus();
//                }
//                else if (mrs_stand.getText().toString().trim().equals("")) {
//                    mrs_stand.setError("Tolong diisi!");
//                    mrs_stand.requestFocus();
//                }
//                else if (comp_keterangan.getText().toString().trim().equals("")) {
//                    comp_keterangan.setError("Tolong diisi!");
//                    comp_keterangan.requestFocus();
//                }
                else {
                    short i;
                    for (i = 0; i <= 22; i++) {
                        listInputText = view.findViewById(listInputTextInt[i]);
                        if (listInputText.getText().toString().contentEquals("")) {
                            listInputText.setText("0");
                        }
                    }
                    AddCompressor addCompressor = new AddCompressor();// this is the Asynctask, which is used to process in background to reduce load on app process
                    addCompressor.execute("");
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ResetCompressor resetCompressor = new ResetCompressor();
                resetCompressor.execute("");
            }
        });

        // For switch button 6

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.fragment_compressor, container, false);
        view = inflater.inflate(R.layout.fragment_compressor, container, false);
//spin4
        spin4 = view.findViewById(R.id.spinner_compressor_use);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, comp_comp);

        // mengeset Array Adapter tersebut ke Spinner
        spin4.setAdapter(adapter4);

        // mengeset listener untuk mengetahui saat item dipilih
        spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                prefManager.saveString("cek_comp_compressor", adapter4.getItem(i));
                //Toast.makeText(getActivity(), "Selected " + adapter4.getItem(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//spin4
        spin5 = (Spinner) view.findViewById(R.id.spinner_compressor_shift);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, comp_shift);

        // mengeset Array Adapter tersebut ke Spinner
        spin5.setAdapter(adapter5);

        // mengeset listener untuk mengetahui saat item dipilih
        spin5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
//                spin5.setSelection(2);
                prefManager.saveString("cek_comp_shift", adapter5.getItem(i));
                //Toast.makeText(getActivity(), "Selected " + adapter5.getItem(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    public class AddCompressor extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        ProgressDialog progress = new ProgressDialog(getActivity());
        short i;

        @Override
        protected void onPreExecute()
        {
            progress.setTitle("Loading");
            progress.setMessage("Harap tunggu");
            progress.show();


            prefManager.saveString("cek_comp_jam_op",jam_op.getText().toString());
            prefManager.saveString("cek_comp_rpm",rpm.getText().toString());
            prefManager.saveString("cek_comp_temp_ambient",temp_ambient.getText().toString());
            prefManager.saveString("cek_comp_crankcase_add",crankcase_add.getText().toString());
            prefManager.saveString("cek_comp_lubrication_add",lubrication_add.getText().toString());
            prefManager.saveString("cek_comp_condensate",condensate.getText().toString());
            prefManager.saveString("cek_comp_gas_in1",gas_in1.getText().toString());
            prefManager.saveString("cek_comp_gas_out1",gas_out1.getText().toString());
            prefManager.saveString("cek_comp_gas_out2",gas_out2.getText().toString());
            prefManager.saveString("cek_comp_gas_out3",gas_out3.getText().toString());
            prefManager.saveString("cek_comp_gas_out4",gas_out4.getText().toString());
            prefManager.saveString("cek_comp_gas_stage",gas_stage.getText().toString());
            prefManager.saveString("cek_comp_gas_sta1",gas_sta1.getText().toString());
            prefManager.saveString("cek_comp_gas_sta2",gas_sta2.getText().toString());
            prefManager.saveString("cek_comp_gas_sta3",gas_sta3.getText().toString());
            prefManager.saveString("cek_comp_gas_sta4",gas_sta4.getText().toString());
            prefManager.saveString("cek_comp_volt",volt.getText().toString());
            prefManager.saveString("cek_comp_ampere",ampere.getText().toString());
            prefManager.saveString("cek_comp_cosq",cosq.getText().toString());
            prefManager.saveString("cek_comp_mrs",mrs.getText().toString());
            prefManager.saveString("cek_comp_mrs_tek",mrs_tek.getText().toString());
            prefManager.saveString("cek_comp_mrs_temp",mrs_temp.getText().toString());
            prefManager.saveString("cek_comp_mrs_stand",mrs_stand.getText().toString());
            prefManager.saveString("cek_comp_comp_keterangan",comp_keterangan.getText().toString());

            prefManager.saveBoolean("cek_comp_compressor_oil", switchCompressorOil.isChecked());
            if(switchCompressorOil.isChecked())
                prefManager.saveString("cek_comp_compressor_oil_string", "Yes");
            else
                prefManager.saveString("cek_comp_compressor_oil_string", "No");

            prefManager.saveBoolean("cek_comp_pompa_oil", switchPompaOil.isChecked());
            if (switchPompaOil.isChecked())
                prefManager.saveString("cek_comp_pompa_oil_string", "Yes");
            else
                prefManager.saveString("cek_comp_pompa_oil_string", "No");

            prefManager.saveBoolean("cek_comp_crankcase_oil", switchCrankcaseOil.isChecked());
            if (switchCrankcaseOil.isChecked())
                prefManager.saveString("cek_comp_crankcase_oil_string", "Yes");
            else
                prefManager.saveString("cek_comp_crankcase_oil_string", "No");

            prefManager.saveBoolean("cek_comp_tekanan_oil", switchTekananOil.isChecked());
            if (switchTekananOil.isChecked())
                prefManager.saveString("cek_comp_tekanan_oil_string", "Yes");
            else
                prefManager.saveString("cek_comp_tekanan_oil_string", "No");

            prefManager.saveBoolean("cek_comp_gas_dalam_tekanan", switchGasDalamTekanan.isChecked());
            if (switchGasDalamTekanan.isChecked())
                prefManager.saveString("cek_comp_gas_dalam_tekanan_string", "Yes");
            else
                prefManager.saveString("cek_comp_gas_dalam_tekanan_string", "No");

            prefManager.saveBoolean("cek_comp_belt_tension", switchBeltTension.isChecked());
            if (switchBeltTension.isChecked())
                prefManager.saveString("cek_comp_belt_tension_string", "Yes");
            else
                prefManager.saveString("cek_comp_belt_tension_string", "No");

            prefManager.saveBoolean("cek_comp_psv", switchPsv.isChecked());
            if (switchPsv.isChecked())
                prefManager.saveString("cek_comp_psv_string", "Yes");
            else
                prefManager.saveString("cek_comp_psv_string", "No");

            prefManager.saveBoolean("cek_comp_panel_kontrol", switchPanelKontrol.isChecked());
            if (switchPanelKontrol.isChecked())
                prefManager.saveString("cek_comp_panel_kontrol_string", "Yes");
            else
                prefManager.saveString("cek_comp_panel_kontrol_string", "No");

            prefManager.saveBoolean("cek_comp_suara_mesin", switchSuaraMesin.isChecked());
            if (switchSuaraMesin.isChecked())
                prefManager.saveString("cek_comp_suara_mesin_string", "Yes");
            else
                prefManager.saveString("cek_comp_suara_mesin_string", "No");

            prefManager.saveBoolean("cek_comp_vibrasi", switchVibrasi.isChecked());
            if (switchVibrasi.isChecked())
                prefManager.saveString("cek_comp_vibrasi_string", "Yes");
            else
                prefManager.saveString("cek_comp_vibrasi_string", "No");

            prefManager.saveBoolean("cek_comp_kebocoran", switchKebocoran.isChecked());
            if (switchKebocoran.isChecked())
                prefManager.saveString("cek_comp_kebocoran_string", "Yes");
            else
                prefManager.saveString("cek_comp_kebocoran_string", "No");

            prefManager.saveBoolean("cek_comp_kebersihan", switchKebersihan.isChecked());
            if (switchKebersihan.isChecked())
                prefManager.saveString("cek_comp_kebersihan_string", "Yes");
            else
                prefManager.saveString("cek_comp_gas_kebersihan_string", "No");

            //Toast.makeText(getActivity(), prefManager.catchString("pengisian_filling1_timeAwal"), Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            String date_time = simpleDateFormat.format(calendar.getTime());

            prefManager.saveString("tanggal_kirim_compressor", date_time);

        }

        @Override
        protected void onPostExecute(String r)
        {


            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
            prefManager.deleteKey("cek_comp_compressor_oil_string");
            prefManager.deleteKey("cek_comp_pompa_oil_string");
            prefManager.deleteKey("cek_comp_crankcase_oil_string");
            prefManager.deleteKey("cek_comp_tekanan_oil_string");
            prefManager.deleteKey("cek_comp_gas_dalam_tekanan_string");
            prefManager.deleteKey("cek_comp_belt_tension_string");
            prefManager.deleteKey("cek_comp_psv_string");
            prefManager.deleteKey("cek_comp_panel_kontrol_string");
            prefManager.deleteKey("cek_comp_suara_mesin_string");
            prefManager.deleteKey("cek_comp_vibrasi_string");
            prefManager.deleteKey("cek_comp_kebocoran_string");
            prefManager.deleteKey("cek_comp_kebersihan_string");

            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }
        @Override
        protected String doInBackground(String... params) {
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


                    query = "insert into tp.cek_compressor values ( " + prefManager.catchString("id_karyawan_spj") + ", '" +
                            prefManager.catchString("nama_karyawan_spj") + "', '" +
                            prefManager.catchString("cek_comp_compressor") +
                            "', convert (datetime, '" + prefManager.catchString("tanggal_kirim_compressor") + "', 103), " +
                            "convert(datetime, convert(datetime, '" + prefManager.catchString("tanggal_kirim_compressor") + "', 103), 108),  " +
                            "convert(time, convert(datetime, '10/02/1998 " + jam_op.getText().toString() + ":00:00', 103), 108), '"  +
                            prefManager.catchString("cek_comp_shift") + "', '" +
                            prefManager.catchString("cek_comp_compressor_oil_string") + "', '" +
                            prefManager.catchString("cek_comp_pompa_oil_string") + "', '" +
                            prefManager.catchString("cek_comp_crankcase_oil_string") + "', '" +
                            prefManager.catchString("cek_comp_tekanan_oil_string") + "', '" +
                            prefManager.catchString("cek_comp_gas_dalam_tekanan_string") + "' ,'" +
                            prefManager.catchString("cek_comp_belt_tension_string") + "', " +
                            prefManager.catchString("cek_comp_rpm") + ", " +
                            prefManager.catchString("cek_comp_temp_ambient") + ", " +
                            prefManager.catchString("cek_comp_crankcase_add") + ", " + //knkjn kj
                            prefManager.catchString("cek_comp_lubrication_add") + ", " +
                            prefManager.catchString("cek_comp_condensate") + ", " +
                            prefManager.catchString("cek_comp_gas_in1") + ", " +
                            prefManager.catchString("cek_comp_gas_out1") + ", " +
                            prefManager.catchString("cek_comp_gas_out2") + ", " +
                            prefManager.catchString("cek_comp_gas_out3") + ", " +
                            prefManager.catchString("cek_comp_gas_out4") + ", " +
                            prefManager.catchString("cek_comp_gas_stage") + ", " +
                            prefManager.catchString("cek_comp_gas_sta1") + ", "  +
                            prefManager.catchString("cek_comp_gas_sta2") + ", " +
                            prefManager.catchString("cek_comp_gas_sta3") + ", " +
                            prefManager.catchString("cek_comp_gas_sta4") + ", " +
                            prefManager.catchString("cek_comp_volt") + ", " +
                            prefManager.catchString("cek_comp_ampere") + ", " +
                            prefManager.catchString("cek_comp_cosq") + ", '" +
                            prefManager.catchString("cek_comp_psv_string") + "' ,'" +
                            prefManager.catchString("cek_comp_panel_kontrol_string") + "' ,'" +
                            prefManager.catchString("cek_comp_suara_mesin_string") + "' ,'" +
                            prefManager.catchString("cek_comp_vibrasi_string") + "' ,'" +
                            prefManager.catchString("cek_comp_kebocoran_string") + "' ,'" +
                            prefManager.catchString("cek_comp_kebersihan_string") + "')";
//                    preparedStatement = con.prepareStatement(query);
//                    preparedStatement.executeUpdate();

                    query = "insert into tp.cek_mrs values ( " + prefManager.catchString("id_karyawan_spj") + ", '" +
                            prefManager.catchString("nama_karyawan_spj") + "', " +
                            prefManager.catchString("cek_comp_mrs") + ", " +
                            prefManager.catchString("cek_comp_mrs_tek") + ", " +
                            prefManager.catchString("cek_comp_mrs_temp") + ", " +
                            prefManager.catchString("cek_comp_mrs_stand") + ", '" +
                            prefManager.catchString("cek_comp_keterangan") + "')";
//                    preparedStatement = con.prepareStatement(query);
//                    preparedStatement.executeUpdate();
                    z = "Added Successfully";

                    }

            } catch (Exception ex) {

                z = ex.getMessage();
                //keterangan.setText(ex.getMessage());
            }
            return z;
        }
    }

    public class ResetCompressor extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);

            prefManager.saveString("cek_comp_jam_op","");
            prefManager.saveString("cek_comp_rpm","");
            prefManager.saveString("cek_comp_temp_ambient","");
            prefManager.saveString("cek_comp_crankcase_add","");
            prefManager.saveString("cek_comp_lubrication_add","");
            prefManager.saveString("cek_comp_condensate","");
            prefManager.saveString("cek_comp_gas_in1","");
            prefManager.saveString("cek_comp_gas_out1","");
            prefManager.saveString("cek_comp_gas_out2","");
            prefManager.saveString("cek_comp_gas_out3","");
            prefManager.saveString("cek_comp_gas_out4","");
            prefManager.saveString("cek_comp_gas_stage","");
            prefManager.saveString("cek_comp_gas_sta1","");
            prefManager.saveString("cek_comp_gas_sta2","");
            prefManager.saveString("cek_comp_gas_sta3","");
            prefManager.saveString("cek_comp_gas_sta4","");
            prefManager.saveString("cek_comp_volt","");
            prefManager.saveString("cek_comp_ampere","");
            prefManager.saveString("cek_comp_cosq","");
            prefManager.saveString("cek_comp_mrs","");
            prefManager.saveString("cek_comp_mrs_tek","");
            prefManager.saveString("cek_comp_mrs_temp","");
            prefManager.saveString("cek_comp_mrs_stand","");
            prefManager.saveString("cek_comp_keterangan","");

            prefManager.saveBoolean("cek_comp_compressor_oil", false);
            prefManager.saveBoolean("cek_comp_pompa_oil", false);
            prefManager.saveBoolean("cek_comp_crankcase_oil", false);
            prefManager.saveBoolean("cek_comp_tekanan_oil", false);
            prefManager.saveBoolean("cek_comp_gas_dalam_tekanan", false);
            prefManager.saveBoolean("cek_comp_belt_tension", false);
            prefManager.saveBoolean("cek_comp_psv", false);
            prefManager.saveBoolean("cek_comp_panel_kontrol", false);
            prefManager.saveBoolean("cek_comp_suara_mesin", false);
            prefManager.saveBoolean("cek_comp_vibrasi", false);
            prefManager.saveBoolean("cek_comp_kebocoran", false);
            prefManager.saveBoolean("cek_comp_kebersihan", false);


            catchStatusCompressor();



            //Toast.makeText(getActivity(), prefManager.catchString("pengisian_filling1_timeAwal"), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String r)
        {
            progressBar.setVisibility(View.GONE);
            //Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            return "";
        }
    }


    private void catchStatusCompressor() {
        jam_op.setText(prefManager.catchString("cek_comp_jam_op"));
        rpm.setText(prefManager.catchString("cek_comp_rpm"));
        temp_ambient.setText(prefManager.catchString("cek_comp_temp_ambient"));
        crankcase_add.setText(prefManager.catchString("cek_comp_crankcase_add"));
        lubrication_add.setText(prefManager.catchString("cek_comp_lubrication_add"));
        condensate.setText(prefManager.catchString("cek_comp_condensate"));

        gas_in1.setText(prefManager.catchString("cek_comp_gas_in1"));
        gas_out1.setText(prefManager.catchString("cek_comp_gas_out1"));
        gas_out2.setText(prefManager.catchString("cek_comp_gas_out2"));
        gas_out3.setText(prefManager.catchString("cek_comp_gas_out3"));
        gas_out4.setText(prefManager.catchString("cek_comp_gas_out4"));

        gas_stage.setText(prefManager.catchString("cek_comp_gas_stage"));
        gas_sta1.setText(prefManager.catchString("cek_comp_gas_sta1"));
        gas_sta2.setText(prefManager.catchString("cek_comp_gas_sta2"));
        gas_sta3.setText(prefManager.catchString("cek_comp_gas_sta3"));
        gas_sta4.setText(prefManager.catchString("cek_comp_gas_sta4"));

        volt.setText(prefManager.catchString("cek_comp_volt"));
        ampere.setText(prefManager.catchString("cek_comp_ampere"));
        cosq.setText(prefManager.catchString("cek_comp_cosq"));
        mrs.setText(prefManager.catchString("cek_comp_mrs"));
        mrs_tek.setText(prefManager.catchString("cek_comp_mrs_tek"));
        mrs_temp.setText(prefManager.catchString("cek_comp_mrs_temp"));
        mrs_stand.setText(prefManager.catchString("cek_comp_mrs_stand"));
        comp_keterangan.setText(prefManager.catchString("cek_comp_comp_keterangan"));

        switchCompressorOil.setChecked(prefManager.catchBoolean("cek_comp_compressor_oil"));
        switchPompaOil.setChecked(prefManager.catchBoolean("cek_comp_pompa_oil"));
        switchCrankcaseOil.setChecked(prefManager.catchBoolean("cek_comp_crankcase_oil"));
        switchTekananOil.setChecked(prefManager.catchBoolean("cek_comp_tekanan_oil"));
        switchGasDalamTekanan.setChecked(prefManager.catchBoolean("cek_comp_gas_dalam_tekanan"));
        switchBeltTension.setChecked(prefManager.catchBoolean("cek_comp_belt_tension"));
        switchPsv.setChecked(prefManager.catchBoolean("cek_comp_psv"));
        switchPanelKontrol.setChecked(prefManager.catchBoolean("cek_comp_panel_kontrol"));
        switchSuaraMesin.setChecked(prefManager.catchBoolean("cek_comp_suara_mesin"));
        switchVibrasi.setChecked(prefManager.catchBoolean("cek_comp_vibrasi"));
        switchKebocoran.setChecked(prefManager.catchBoolean("cek_comp_kebocoran"));
        switchKebersihan.setChecked(prefManager.catchBoolean("cek_comp_kebersihan"));
    }

}