package id.co.cng.spj.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import id.co.cng.spj.utility.ConnectionHelper;
import id.co.cng.spj.utility.PrefManager;

import id.co.cng.spj.R;

public class Fragment_Genset extends Fragment {
    PrefManager prefManager;

    private Spinner spin1,spin2;
    private String[] genset = {
            "GENSET 1-MARGO",
            "GENSET 2-MARGO"
    };
    private String[] shift_generator = {
            "P2(07:00AM-03:00PM)",
            "S2(03:00AM-11:00PM)",
            "M2(11:00PM-07:00AM)"
    };
//    please take a notes.
//    T stands for temperature and A stand for ampere
    Switch switchWaterLeak, switchOilLeaking, switchOilVolume, switchWaterVolume, switchVanBelt;
    TextView textWaterLeak, textOilLeaking, textOilVolume, textWaterVolume, textVanBelt;
    TextInputEditText jamOp, gasPressureBefore, gasPressureAfter, waterAdd, engineOilAdd;
    TextInputEditText powerBattery, rpm, voltage, ampere, standMeter, freq, ambienT; //t stands for temperature
    TextInputEditText turboT1, turboT2, exhaustT1, exhaustT2, oilFilterT1, oilFilterT2, waterT;
    TextInputEditText oilPress, currentA1, currentA2, currentA3, powerCableT1, powerCableT2;
    TextInputEditText powerCableT3, troubleshooting;
    Button kirimGenerator, resetGenerator;
    ProgressBar progressBar;

    String switchOn  = "Yes";
    String switchOff = "No ";

    private int[] inputEditTextConvertInt = {
            R.id.text_generator_running_hours,
            R.id.text_generator_inlet_gas_before,
            R.id.text_generator_inlet_gas_after,
            R.id.text_generator_water_add,
            R.id.text_generator_engine_oil_add,
            R.id.text_generator_power_battery,
            R.id.text_generator_rpm_machine,
            R.id.text_generator_ampere,
            R.id.text_generator_stand,
            R.id.text_generator_frequency,
            R.id.text_generator_temp_ambient,
            R.id.text_generator_temp_turbo_1,
            R.id.text_generator_temp_turbo_2,
            R.id.text_generator_temp_exhaust_1,
            R.id.text_generator_temp_exhaust_2,
            R.id.text_generator_temp_oil_filter_1,
            R.id.text_generator_temp_oil_filter_2,
            R.id.text_generator_water_temp,
            R.id.text_generator_oil_pressure,
            R.id.text_generator_current_1,
            R.id.text_generator_current_2,
            R.id.text_generator_current_3,
            R.id.text_generator_temp_power_1,
            R.id.text_generator_temp_power_2,
            R.id.text_generator_temp_power_3

    };

    TextInputEditText inputEditTextConvert;
    View view;
    short i;

    public Fragment_Genset() {
        // Required empty public constructor
    }
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefManager = new PrefManager(getActivity());

        // For switch button 18
        switchWaterLeak = view.findViewById(R.id.switch_generator_water_leak);
        textWaterLeak = view.findViewById(R.id.textView_generator_water_leak);
        switchOilLeaking = view.findViewById(R.id.switch_generator_oil_leaking);
        textOilLeaking = view.findViewById(R.id.textView_generator_oil_leaking);
        switchOilVolume = view.findViewById(R.id.switch_generator_oil_volume);
        textOilVolume = view.findViewById(R.id.textView_generator_oil_volume);
        switchWaterVolume = view.findViewById(R.id.switch_generator_water_volume);
        textWaterVolume = view.findViewById(R.id.textView_generator_water_volume);
        switchVanBelt = view.findViewById(R.id.switch_generator_van_belt);
        textVanBelt = view.findViewById(R.id.textView_generator_van_belt);

//        gasPressureBefore, gasPressureAfter, waterAdd, engineOilAdd;
        jamOp = view.findViewById(R.id.text_generator_running_hours);
        gasPressureBefore = view.findViewById(R.id.text_generator_inlet_gas_before);
        gasPressureAfter = view.findViewById(R.id.text_generator_inlet_gas_after);
        waterAdd = view.findViewById(R.id.text_generator_water_add);
        engineOilAdd = view.findViewById(R.id.text_generator_engine_oil_add);
//        powerBattery, rpm, voltage, ampere, standMeter, freq, ambienT;
        powerBattery = view.findViewById(R.id.text_generator_power_battery);
        rpm = view.findViewById(R.id.text_generator_rpm_machine);

        voltage = view.findViewById(R.id.text_generator_volt);
        ampere = view.findViewById(R.id.text_generator_ampere);
        standMeter = view.findViewById(R.id.text_generator_stand);
        freq = view.findViewById(R.id.text_generator_frequency);
        ambienT = view.findViewById(R.id.text_generator_temp_ambient);
//        turboT1, turboT2, exhaustT1, exhaustT2, waterT, oilPress;
        turboT1 = view.findViewById(R.id.text_generator_temp_turbo_1);
        turboT2 = view.findViewById(R.id.text_generator_temp_turbo_2);
        exhaustT1 = view.findViewById(R.id.text_generator_temp_exhaust_1);
        exhaustT2 = view.findViewById(R.id.text_generator_temp_exhaust_2);
        oilFilterT1 = view.findViewById(R.id.text_generator_temp_oil_filter_1);
        oilFilterT2 = view.findViewById(R.id.text_generator_temp_oil_filter_2);
        waterT = view.findViewById(R.id.text_generator_water_temp);
//        oilPress, currentA1, currentA2, currentA3, powerCableT1, PowerCableT2;
        oilPress = view.findViewById(R.id.text_generator_oil_pressure);
        currentA1 = view.findViewById(R.id.text_generator_current_1);
        currentA2 = view.findViewById(R.id.text_generator_current_2);
        currentA3 = view.findViewById(R.id.text_generator_current_3);
        powerCableT1 = view.findViewById(R.id.text_generator_temp_power_1);
        powerCableT2 = view.findViewById(R.id.text_generator_temp_power_2);
//        powerCableT3, troubleshooting, keteranganGenset;
        powerCableT3 = view.findViewById(R.id.text_generator_temp_power_3);
        //////////////////////////////////////////////////////////////////
        troubleshooting = view.findViewById(R.id.text_generator_troubleshoot);
        //keteranganGenset = view.findViewById(R.id.text_generator_);
        kirimGenerator = view.findViewById(R.id.button_generator_kirim);
        resetGenerator = view.findViewById(R.id.button_generator_reset);
        progressBar = view.findViewById(R.id.progressBar_genset);

//        gasPressureBefore, gasPressureAfter, waterAdd, engineOilAdd;


        LoadPage();


        switchWaterLeak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textWaterLeak.setText(switchOn);
                    prefManager.saveString("generator_water_leak", switchOn);
                } else {
                    textWaterLeak.setText(switchOff);
                    prefManager.saveString("generator_water_leak", switchOff);
                }
            }
        });

        if (switchWaterLeak.isChecked()) {
            textWaterLeak.setText(switchOn);
        } else {
            textWaterLeak.setText(switchOff);
        }

        // For switch button 19



        switchOilLeaking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textOilLeaking.setText(switchOn);
                    prefManager.saveString("generator_oil_leak", switchOn);
                } else {
                    textOilLeaking.setText(switchOff);
                    prefManager.saveString("generator_oil_leak", switchOff);
                }
            }
        });

        if (switchOilLeaking.isChecked()) {
            textOilLeaking.setText(switchOn);
        } else {
            textOilLeaking.setText(switchOff);
        }

        // For switch button 20



        switchOilVolume.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textOilVolume.setText(switchOn);
                    prefManager.saveString("generator_oil_volume", switchOn);
                } else {
                    textOilVolume.setText(switchOff);
                    prefManager.saveString("generator_oil_volume", switchOff);
                }
            }
        });

        if (switchOilVolume.isChecked()) {
            textOilVolume.setText(switchOn);
        } else {
            textOilVolume.setText(switchOff);
        }

        // For switch button 21



        switchWaterVolume.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textWaterVolume.setText(switchOn);
                    prefManager.saveString("generator_water_volume", switchOn);
                } else {
                    textWaterVolume.setText(switchOff);
                    prefManager.saveString("generator_water_volume", switchOff);
                }
            }
        });

        if (switchWaterVolume.isChecked()) {
            textWaterVolume.setText(switchOn);
        } else {
            textWaterVolume.setText(switchOff);
        }

        // For switch button 22



        switchVanBelt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textVanBelt.setText(switchOn);
                    prefManager.saveString("generator_vanbelt", switchOn);
                } else {
                    textVanBelt.setText(switchOff);
                    prefManager.saveString("generator_vanbelt", switchOff);
                }
            }
        });

        if (switchVanBelt.isChecked()) {
            textVanBelt.setText(switchOn);
        } else {
            textVanBelt.setText(switchOff);
        }

        spin1 = view.findViewById(R.id.spinner_generator_generator);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, genset);

        // mengeset Array Adapter tersebut ke Spinner
        spin1.setAdapter(adapter);
        spin1.setSelection(prefManager.catchInt("generator_generator_pos"));

        // mengeset listener untuk mengetahui saat item dipilih
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                //Toast.makeText(getActivity(), "Selected " + adapter.getItem(i), Toast.LENGTH_SHORT).show();
                prefManager.saveString("generator_generator", adapter.getItem(i));
                prefManager.saveInt("generator_generator_pos", i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//spin shift
        spin2 = view.findViewById(R.id.spinner_generator_shift);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, shift_generator);

        // mengeset Array Adapter tersebut ke Spinner
        spin2.setAdapter(adapter1);
        spin2.setSelection(prefManager.catchInt("generator_shift_pos"));
        // mengeset listener untuk mengetahui saat item dipilih
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                //Toast.makeText(getActivity(), "Selected " + adapter1.getItem(i), Toast.LENGTH_SHORT).show();
                prefManager.saveString("generator_shift", adapter1.getItem(i));
                prefManager.saveInt("generator_shift_pos", i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        kirimGenerator.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (jamOp.getText().toString().trim().equals("")) {
                    jamOp.setError("Tolong diisi!");
                    jamOp.requestFocus();
                }
                else if (rpm.getText().toString().trim().equals("")) {
                    rpm.setError("Tolong diisi!");
                    rpm.requestFocus();
                }
                else if (ampere.getText().toString().trim().equals("")) {
                    ampere.setError("Tolong diisi!");
                    ampere.requestFocus();
                }
                else {

                    for (i = 0; i <= 24; i++) {
                        inputEditTextConvert = view.findViewById(inputEditTextConvertInt[i]);
                        if (inputEditTextConvert.getText().toString().contentEquals("")) {
                            inputEditTextConvert.setText("0");
                        }

                    }
                    KirimGenset kirimGenset = new KirimGenset();
                    kirimGenset.execute("");
                }
            }
        });

        resetGenerator.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ResetGenset resetGenset = new ResetGenset();
                resetGenset.execute("");

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genset, container, false);

        //spin genset

        return view;
    }

    private void LoadPage() {
        switchWaterLeak.setChecked(prefManager.catchBoolean("genset_waterleak"));
        switchOilLeaking.setChecked(prefManager.catchBoolean("genset_oilleaking"));
        switchOilVolume.setChecked(prefManager.catchBoolean("genset_oilvolume"));
        switchWaterVolume.setChecked(prefManager.catchBoolean("genset_watervolume"));
        switchVanBelt.setChecked(prefManager.catchBoolean("genset_vanbelt"));

        jamOp.setText(prefManager.catchString("generator_jamop"));
        gasPressureBefore.setText(prefManager.catchString("generator_gas_pressure_before"));
        gasPressureAfter.setText(prefManager.catchString("generator_gas_pressure_after"));
        waterAdd.setText(prefManager.catchString("generator_water_add"));
        engineOilAdd.setText(prefManager.catchString("generator_engine_oil_add"));
//        powerBattery, rpm, voltage, ampere, standMeter, freq, ambienT;
        powerBattery.setText(prefManager.catchString("generator_power_battery"));
        rpm.setText(prefManager.catchString("generator_rpm"));
        voltage.setText(prefManager.catchString("generator_voltage"));
        ampere.setText(prefManager.catchString("generator_ampere"));
        standMeter.setText(prefManager.catchString("generator_stand"));
        freq.setText(prefManager.catchString("generator_freq"));
        ambienT.setText(prefManager.catchString("generator_ambien_temp"));
//      turboT1, turboT2, exhaustT1, exhaustT2, oilFilterT1, oilFilterT2, waterT;
        turboT1.setText(prefManager.catchString("generator_turbo1_temp"));
        turboT2.setText(prefManager.catchString("generator_turbo2_temp"));
        exhaustT1.setText(prefManager.catchString("generator_exhaust1_temp"));
        exhaustT2.setText(prefManager.catchString("generator_exhaust2_temp"));
        oilFilterT1.setText(prefManager.catchString("generator_oil_filter1_temp"));
        oilFilterT2.setText(prefManager.catchString("generator_oil_filter2_temp"));
        waterT.setText(prefManager.catchString("generator_water_temp"));
//        oilPress, currentA1, currentA2, currentA3, powerCableT1, powerCableT2;
        oilPress.setText(prefManager.catchString("generator_oil_press"));
        currentA1.setText(prefManager.catchString("generator_current_a1"));
        currentA2.setText(prefManager.catchString("generator_current_a2"));
        currentA3.setText(prefManager.catchString("generator_current_a3"));
        powerCableT1.setText(prefManager.catchString("generator_power1_temp"));
        powerCableT2.setText(prefManager.catchString("generator_power2_temp"));
        powerCableT3.setText(prefManager.catchString("generator_power3_temp"));
        troubleshooting.setText(prefManager.catchString("generator_troubleshooting"));
    }

    private class KirimGenset extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        ProgressDialog progress = new ProgressDialog(getActivity());
        Connection con;


        @Override
        protected void onPreExecute()
        {
//            progress.setTitle("Loading");
//            progress.setMessage("Harap tunggu");
//            progress.show();
            progressBar.setVisibility(View.VISIBLE);



//            jamOp.setText(prefManager.catchString("generator_jamop"));
            prefManager.saveString("generator_jamop", String.valueOf(jamOp.getText()));
//            gasPressureBefore.setText(prefManager.catchString("generator_gas_pressure_before"));
            prefManager.saveString("generator_gas_pressure_before", String.valueOf(gasPressureBefore.getText()));
//            gasPressureAfter.setText(prefManager.catchString("generator_gas_pressure_after"));
            prefManager.saveString("generator_gas_pressure_after", String.valueOf(gasPressureAfter.getText()));
//            waterAdd.setText(prefManager.catchString("generator_water_add"));
            prefManager.saveString("generator_water_add", String.valueOf(waterAdd.getText()));
//            engineOilAdd.setText(prefManager.catchString("generator_engine_oil_add"));
            prefManager.saveString("generator_engine_oil_add", String.valueOf(engineOilAdd.getText()));
//            powerBattery.setText(prefManager.catchString("generator_power_battery"));
            prefManager.saveString("generator_power_battery", String.valueOf(powerBattery.getText()));
//            rpm.setText(prefManager.catchString("generator_rpm"));
            prefManager.saveString("generator_rpm", String.valueOf(rpm.getText()));
//            voltage.setText(prefManager.catchString("generator_voltage"));
            prefManager.saveString("generator_voltage", String.valueOf(voltage.getText()));
            prefManager.saveString("generator_ampere", String.valueOf(ampere.getText()));
//            standMeter.setText(prefManager.catchString("generator_stand"));
            prefManager.saveString("generator_stand", String.valueOf(standMeter.getText()));
//            freq.setText(prefManager.catchString("generator_freq"));
            prefManager.saveString("generator_freq", String.valueOf(freq.getText()));
//            ambienT.setText(prefManager.catchString("generator_ambien_temp"));
            prefManager.saveString("generator_ambien_temp", String.valueOf(ambienT.getText()));
//            turboT1.setText(prefManager.catchString("generator_turbo1_temp"));
            prefManager.saveString("generator_turbo1_temp", String.valueOf(turboT1.getText()));
//            turboT2.setText(prefManager.catchString("generator_turbo2_temp"));
            prefManager.saveString("generator_turbo2_temp", String.valueOf(turboT2.getText()));
//            exhaustT1.setText(prefManager.catchString("generator_exhaust1_temp"));
            prefManager.saveString("generator_exhaust1_temp", String.valueOf(exhaustT1.getText()));
//            exhaustT2.setText(prefManager.catchString("generator_exhaust2_temp"));
            prefManager.saveString("generator_exhaust2_temp", String.valueOf(exhaustT2.getText()));
//            oilFilterT1.setText(prefManager.catchString("generator_oil_filter1_temp"));
            prefManager.saveString("generator_oil_filter1_temp", String.valueOf(oilFilterT1.getText()));
//            oilFilterT2.setText(prefManager.catchString("generator_oil_filter2_temp"));
            prefManager.saveString("generator_oil_filter2_temp", String.valueOf(oilFilterT2.getText()));
//            waterT.setText(prefManager.catchString("generator_water_temp"));
            prefManager.saveString("generator_water_temp", String.valueOf(waterT.getText()));
//            oilPress.setText(prefManager.catchString("generator_oil_press"));
            prefManager.saveString("generator_oil_press", String.valueOf(oilPress.getText()));
//            currentA1.setText(prefManager.catchString("generator_current_a1"));
            prefManager.saveString("generator_current_a1", String.valueOf(currentA1.getText()));
//            currentA2.setText(prefManager.catchString("generator_current_a2"));
            prefManager.saveString("generator_current_a2", String.valueOf(currentA2.getText()));
//            currentA3.setText(prefManager.catchString("generator_current_a3"));
            prefManager.saveString("generator_current_a3", String.valueOf(currentA3.getText()));
//            powerCableT1.setText(prefManager.catchString("generator_power1_temp"));
            prefManager.saveString("generator_power1_temp", String.valueOf(powerCableT1.getText()));
//            powerCableT2.setText(prefManager.catchString("generator_power2_temp"));
            prefManager.saveString("generator_power2_temp", String.valueOf(powerCableT2.getText()));
//            powerCableT3.setText(prefManager.catchString("generator_power3_temp"));
            prefManager.saveString("generator_power3_temp", String.valueOf(powerCableT3.getText()));
//            troubleshooting.setText(prefManager.catchString("generator_troubleshooting"));
            prefManager.saveString("generator_troubleshooting", String.valueOf(troubleshooting.getText()));
//            keteranganGenset.setText(prefManager.catchString("generator_keterangan"));


            prefManager.saveBoolean("genset_waterleak", switchWaterLeak.isChecked());
            prefManager.saveBoolean("genset_oilleaking", switchOilLeaking.isChecked());
            prefManager.saveBoolean("genset_oilvolume", switchOilVolume.isChecked());
            prefManager.saveBoolean("genset_watervolume", switchWaterVolume.isChecked());
            prefManager.saveBoolean("genset_vanbelt", switchVanBelt.isChecked());

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            String date_time = simpleDateFormat.format(calendar.getTime());

            prefManager.saveString("tanggal_kirim_genset", date_time);




            //Toast.makeText(getActivity(), prefManager.catchString("pengisian_filling1_timeAwal"), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String r)
        {
//            progress.dismiss();
            progressBar.setVisibility(View.GONE);
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
//[id_cek_genset] [int] IDENTITY(1,1) NOT NULL,
//	[id_karyawan] [int] NULL,
//	[nama_karyawan] [varchar](50) NULL,
//	[Generator] [varchar](100) NULL,
//	[tanggal] [date] NULL,
//	[waktu] [time](7) NULL,
//	[jam_operasi] [time](7) NULL,
//	[shift] [varchar](100) NULL,
//	[water_leaking] [varchar](4) NULL,
//	[oil_leaking] [varchar](4) NULL,
//	[volume_oil] [varchar](4) NULL,
//	[water_volume] [varchar](4) NULL,
//	[van_belt] [varchar](4) NULL,
//	[inlet_before] [float] NULL,
//	[inlet_after] [float] NULL,
//	[water_added] [float] NULL,
//	[oil_added] [float] NULL,
//	[volt_accu] [float] NULL,
//	[RPM] [float] NULL,
//	[voltage] [float] NULL,
//	[ampere] [float] NULL,
//	[turbine] [float] NULL,
//	[frequency] [float] NULL,
//	[ambient] [float] NULL,
//	[turbo_1] [float] NULL,
//	[turbo_2] [float] NULL,
//	[exhaust_1] [float] NULL,
//	[exhaust_2] [float] NULL,
//	[oil_filter_1] [float] NULL,
//	[oil_filter_2] [float] NULL,
//	[water_temperature] [float] NULL,
//	[oil_pressure] [float] NULL,
//	[amp_L1] [float] NULL,
//	[amp_L2] [float] NULL,
//	[amp_L3] [float] NULL,
//	[C_L1] [float] NULL,
//	[C_L2] [float] NULL,
//	[C_L3] [float] NULL,
//	[Troubleshooting] [varchar](100) NULL,
//	[keterangan] [varchar](50) NULL,
                    //String query;
//                    [volume_oil] [varchar](4) NULL,
////	[water_volume] [varchar](4) NULL,
////	[van_belt] [varchar](4) NULL,
////	[inlet_before] [float] NULL,
////	[inlet_after] [float] NULL,
////	[water_added] [float] NULL,
////	[oil_added] [float] NULL,
////	[volt_accu] [float] NULL,
                    PreparedStatement preparedStatement;

                    String query = "insert into tp.cek_genset values ( " + prefManager.catchString("id_karyawan_spj") + ", '" +
                            prefManager.catchString("nama_karyawan_spj") + "', '" +
                            prefManager.catchString("generator_generator") + "', " +
                            "convert (datetime, '" + prefManager.catchString("tanggal_kirim_genset") + "', 103), " +
                            "convert(datetime, convert(datetime, '" + prefManager.catchString("tanggal_kirim_genset") + "', 103), 108),  " +
                            prefManager.catchString("generator_jamop") + ", '"  +
                            prefManager.catchString("generator_shift") + "', '" + prefManager.catchString("generator_water_leak") + "', '" +  prefManager.catchString("generator_oil_leak") + "', '" +
                            prefManager.catchString("generator_oil_volume") + "', '" + prefManager.catchString("generator_water_volume") + "', '" + prefManager.catchString("generator_vanbelt") + "' ," +
                            prefManager.catchString("generator_gas_pressure_before") + ", " + prefManager.catchString("generator_gas_pressure_after") + ", " + prefManager.catchString("generator_water_add") + ", " + //knkjn kj
                            prefManager.catchString("generator_engine_oil_add") + ", " + prefManager.catchString("generator_power_battery") + ", " + prefManager.catchString("generator_rpm") + ", " +
                            prefManager.catchString("generator_voltage") + ", " + prefManager.catchString("generator_ampere") + ", " + prefManager.catchString("generator_stand") + ", " +
                            prefManager.catchString("generator_freq") + ", " + prefManager.catchString("generator_ambien_temp") + ", " + prefManager.catchString("generator_turbo1_temp") + ", "  +
                            prefManager.catchString("generator_turbo2_temp") + ", " + prefManager.catchString("generator_exhaust1_temp") + ", " + prefManager.catchString("generator_exhaust2_temp") + ", " +
                            prefManager.catchString("generator_oil_filter1_temp") + ", " + prefManager.catchString("generator_oil_filter2_temp") + ", " + prefManager.catchString("generator_water_temp") + ", " +
                            prefManager.catchString("generator_oil_press") + ", " + prefManager.catchString("generator_current_a1") + ", " + prefManager.catchString("generator_current_a2") + ", " +
                            prefManager.catchString("generator_current_a3") + ", " + prefManager.catchString("generator_power1_temp") + ", " + prefManager.catchString("generator_power2_temp") + ", " +
                            prefManager.catchString("generator_power3_temp") + ", '" + prefManager.catchString("generator_troubleshooting") + "', '')";
                    preparedStatement = con.prepareStatement(query);
                    //preparedStatement.executeUpdate();

                    z = "Added Successfully";
                }

            } catch (Exception ex) {

                z = ex.getMessage();
                //keterangan.setText(ex.getMessage());
            }
            return z;
        }
    }

    private class ResetGenset extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        ProgressDialog progress = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute()
        {
            progress.setTitle("Loading");
            progress.setMessage("Harap tunggu");
            progress.setCancelable(false);
            progress.show();

//            jamOp.setText(prefManager.catchString("generator_jamop"));
            prefManager.deleteKey("generator_jamop");
//            gasPressureBefore.setText(prefManager.catchString("generator_gas_pressure_before"));
            prefManager.deleteKey("generator_gas_pressure_before");
//            gasPressureAfter.setText(prefManager.catchString("generator_gas_pressure_after"));
            prefManager.deleteKey("generator_gas_pressure_after");
//            waterAdd.setText(prefManager.catchString("generator_water_add"));
            prefManager.deleteKey("generator_water_add");
//            engineOilAdd.setText(prefManager.catchString("generator_engine_oil_add"));
            prefManager.deleteKey("generator_engine_oil_add");
//            powerBattery.setText(prefManager.catchString("generator_power_battery"));
            prefManager.deleteKey("generator_power_battery");
//            rpm.setText(prefManager.catchString("generator_rpm"));
            prefManager.deleteKey("generator_rpm");
//            voltage.setText(prefManager.catchString("generator_voltage"));
            prefManager.deleteKey("generator_voltage");
            prefManager.deleteKey("generator_ampere");
//            standMeter.setText(prefManager.catchString("generator_stand"));
            prefManager.deleteKey("generator_stand");
//            freq.setText(prefManager.catchString("generator_freq"));
            prefManager.deleteKey("generator_freq");
//            ambienT.setText(prefManager.catchString("generator_ambien_temp"));
            prefManager.deleteKey("generator_ambien_temp");
//            turboT1.setText(prefManager.catchString("generator_turbo1_temp"));
            prefManager.deleteKey("generator_turbo1_temp");
//            turboT2.setText(prefManager.catchString("generator_turbo2_temp"));
            prefManager.deleteKey("generator_turbo2_temp");
//            exhaustT1.setText(prefManager.catchString("generator_exhaust1_temp"));
            prefManager.deleteKey("generator_exhaust1_temp");
//            exhaustT2.setText(prefManager.catchString("generator_exhaust2_temp"));
            prefManager.deleteKey("generator_exhaust2_temp");
//            oilFilterT1.setText(prefManager.catchString("generator_oil_filter1_temp"));
            prefManager.deleteKey("generator_oil_filter1_temp");
//            oilFilterT2.setText(prefManager.catchString("generator_oil_filter2_temp"));
            prefManager.deleteKey("generator_oil_filter2_temp");
//            waterT.setText(prefManager.catchString("generator_water_temp"));
            prefManager.deleteKey("generator_water_temp");
//            oilPress.setText(prefManager.catchString("generator_oil_press"));
            prefManager.deleteKey("generator_oil_press");
//            currentA1.setText(prefManager.catchString("generator_current_a1"));
            prefManager.deleteKey("generator_current_a1");
//            currentA2.setText(prefManager.catchString("generator_current_a2"));
            prefManager.deleteKey("generator_current_a2");
//            currentA3.setText(prefManager.catchString("generator_current_a3"));
            prefManager.deleteKey("generator_current_a3");
//            powerCableT1.setText(prefManager.catchString("generator_power1_temp"));
            prefManager.deleteKey("generator_power1_temp");
//            powerCableT2.setText(prefManager.catchString("generator_power2_temp"));
            prefManager.deleteKey("generator_power2_temp");
//            powerCableT3.setText(prefManager.catchString("generator_power3_temp"));
            prefManager.deleteKey("generator_power3_temp");
//            troubleshooting.setText(prefManager.catchString("generator_troubleshooting"));
            prefManager.deleteKey("generator_troubleshooting");
//            keteranganGenset.setText(prefManager.catchString("generator_keterangan"));

            prefManager.deleteKey("genset_waterleak");
            prefManager.deleteKey("genset_oilleaking");
            prefManager.deleteKey("genset_oilvolume");
            prefManager.deleteKey("genset_watervolume");
            prefManager.deleteKey("genset_vanbelt");

            LoadPage();
            //Toast.makeText(getActivity(), prefManager.catchString("pengisian_filling1_timeAwal"), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String r)
        {
            progress.dismiss();
            //Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            z = "reset berhasil";
            return z;
        }
    }
}
