package id.co.cng.spj.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.co.cng.spj.MenuUtamaActivity;
import id.co.cng.spj.R;
import id.co.cng.spj.utility.ConnectionHelper;
import id.co.cng.spj.utility.PrefManager;

public class Fragment_Customer extends Fragment {

    PrefManager prefManager;
    Connection con;

    AutoCompleteTextView textNamaCust;

    TextInputEditText
            textPressIn,textKetDecant,textPressOut1,textPressOut2,
            textKetPress,textPressOut3,textSettingPress,textKetInsturment,
            textSettingTemp1,textSettingTemp2,textTempAir,textKetPemanas,textTempGasOut,
            textKetTempGas,textTurbinMeter,textElectricVol,textPressStand,textTempStand,textKetStand,
            textKetSambung;
    SingleSelectToggleGroup //decant
            custIpKamera;
    SingleSelectToggleGroup //pressure reduce
            custRegulatorH,custSafetyValueH,
            custRegulatorL,custSafetyValueL;
    SingleSelectToggleGroup //instrument sys
            custRegulatorIns,custPressControl1,custPressControl2;
    SingleSelectToggleGroup //heating sys
            custAir,custHeater1,custHeater2,custTherm1,custTherm2;
    SingleSelectToggleGroup //standmeter
            custOliStand;
    SingleSelectToggleGroup //tubing
            custOliSambung;
    Button
            buttonKirimCust,
            buttonResetCust;
    ProgressBar progressBar;

    public Fragment_Customer(){

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        textNamaCust        = view.findViewById(R.id.text_customer_check_nama_customer);;
        textPressIn         = view.findViewById(R.id.text_pressure_inlet);
        textKetDecant       = view.findViewById(R.id.text_decant_manifold_keterangan);
        textPressOut1       = view.findViewById(R.id.text_pressure_outlet_high);
        textPressOut2       = view.findViewById(R.id.text_pressure_outlet_low);
        textKetPress        = view.findViewById(R.id.text_pressure_reduce_system_keterangan);
        textPressOut3       = view.findViewById(R.id.text_instrument_system_pressure_outlet);
        textSettingPress    = view.findViewById(R.id.text_instrument_system_setting_pressure);
        textKetInsturment   = view.findViewById(R.id.text_instrument_system_keterangan);
        textSettingTemp1    = view.findViewById(R.id.text_heating_system_setting_temp);
        textSettingTemp2    = view.findViewById(R.id.text_heating_system_setting_temp2);
        textTempAir         = view.findViewById(R.id.text_heating_system_temp_air);
        textKetPemanas      = view.findViewById(R.id.text_heating_system_keterangan);
        textTempGasOut      = view.findViewById(R.id.text_gas_outlet_temp);
        textKetTempGas      = view.findViewById(R.id.text_gas_outlet_keterangan);
        textTurbinMeter     = view.findViewById(R.id.text_turbin_flow);
        textElectricVol     = view.findViewById(R.id.text_electric_volume_correction);
        textPressStand      = view.findViewById(R.id.text_pressure_stand);
        textTempStand       = view.findViewById(R.id.text_temp_stand);
        textKetStand        = view.findViewById(R.id.text_stand_keterangan);
        textKetSambung      = view.findViewById(R.id.text_sambungan_pipa_keterangan);
        //decant
        custIpKamera         = view.findViewById(R.id.group_single_radiobutton_ip_camera);
        //pressure reduce
        custRegulatorH         = view.findViewById(R.id.group_single_radiobutton_regulator_high);
        custRegulatorL         = view.findViewById(R.id.group_single_radiobutton_regulator_low);
        custSafetyValueH         = view.findViewById(R.id.group_single_radiobutton_safety_value_high);
        custSafetyValueL         = view.findViewById(R.id.group_single_radiobutton_safety_value_low);
        //instrument sys
        custRegulatorIns    = view.findViewById(R.id.group_single_radiobutton_regulator_instrument);
        custPressControl1   = view.findViewById(R.id.group_single_radiobutton_instrument_system_pressure_control);
        custPressControl2   = view.findViewById(R.id.group_single_radiobutton_instrument_system_pressure_control2);
        //heating sys
        custAir= view.findViewById(R.id.group_single_radiobutton_heating_system_air);
        custHeater1= view.findViewById(R.id.group_single_radiobutton_heating_system_heater1);
        custHeater2= view.findViewById(R.id.group_single_radiobutton_heating_system_heater2);
        custTherm1= view.findViewById(R.id.group_single_radiobutton_heating_system_thermocontrol1);
        custTherm2= view.findViewById(R.id.group_single_radiobutton_heating_system_thermocontrol2);
        //standmeter
        custOliStand= view.findViewById(R.id.group_single_radiobutton_stand_oli_pelumas);
        //tubing
        custOliSambung= view.findViewById(R.id.group_single_radiobutton_tubing_oli_pelumas);


        progressBar         = view.findViewById(R.id.progressBar_customer);
        buttonKirimCust     = view.findViewById(R.id.button_customer_kirim);
        buttonResetCust     = view.findViewById(R.id.button_customer_reset);
        prefManager         = new PrefManager(getActivity());

        LoadPage();
////////////////////////////////////////////////////////////////////////////////////////////////////

        FillList fillList = new FillList();
        fillList.execute("");


//onclicklistener
        buttonKirimCust.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (textNamaCust.getText().toString().trim().equals("")) {
                    textNamaCust.setError("Tolong diisi!");
                    textNamaCust.requestFocus();
                } else if (textPressIn.getText().toString().trim().equals("")) {
                    textPressIn.setError("Tolong diisi!");
                    textPressIn.requestFocus();
                } else if (textPressOut1.getText().toString().trim().equals("")) {
                    textPressOut1.setError("Tolong diisi!");
                    textPressOut1.requestFocus();
                } else if (textPressOut2.getText().toString().trim().equals("")) {
                    textPressOut2.setError("Tolong diisi!");
                    textPressOut2.requestFocus();
                } else if (textPressOut3.getText().toString().trim().equals("")) {
                    textPressOut3.setError("Tolong diisi!");
                    textPressOut3.requestFocus();
                } else if (textSettingPress.getText().toString().trim().equals("")) {
                    textSettingPress.setError("Tolong diisi!");
                    textSettingPress.requestFocus();
                } else if (textSettingTemp1.getText().toString().trim().equals("")) {
                    textSettingTemp1.setError("Tolong diisi!");
                    textSettingTemp1.requestFocus();
                } else if (textSettingTemp2.getText().toString().trim().equals("")) {
                    textSettingTemp2.setError("Tolong diisi!");
                    textSettingTemp2.requestFocus();
                } else if (textTempAir.getText().toString().trim().equals("")) {
                    textTempAir.setError("Tolong diisi!");
                    textTempAir.requestFocus();
                } else if (textTempGasOut.getText().toString().trim().equals("")) {
                    textTempGasOut.setError("Tolong diisi!");
                    textTempGasOut.requestFocus();
                } else if (textTurbinMeter.getText().toString().trim().equals("")) {
                    textTurbinMeter.setError("Tolong diisi!");
                    textTurbinMeter.requestFocus();
                } else if (textElectricVol.getText().toString().trim().equals("")) {
                    textElectricVol.setError("Tolong diisi!");
                    textElectricVol.requestFocus();
                } else if (textPressStand.getText().toString().trim().equals("")) {
                    textPressStand.setError("Tolong diisi!");
                    textPressStand.requestFocus();
                } else if (textTempStand.getText().toString().trim().equals("")) {
                    textTempStand.setError("Tolong diisi!");
                    textTempStand.requestFocus();
                } else {
                    AddCust addCust = new AddCust();// this is the Asynctask, which is used to process in background to reduce load on app process
                    addCust.execute("");
                }
            }
        });

        buttonResetCust.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ResetCust resetCust = new ResetCust();
                resetCust.execute("");
            }
        });
        custIpKamera.check(R.id.choice_ip_camera_on);
        prefManager.saveString("cust_ip_kamera", "on");

        custIpKamera.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_ip_camera_off:
                        prefManager.saveString("cust_ip_kamera", "off");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_ip_camera_on:
                        prefManager.saveString("cust_ip_kamera", "on");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });
        custRegulatorH.check(R.id.choice_regulator_high_tidak_bocor);
        prefManager.saveString("cust_reg_h", "tidak bocor");

        custRegulatorH.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_regulator_high_bocor:
                        prefManager.saveString("cust_reg_h", "bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_regulator_high_tidak_bocor:
                        prefManager.saveString("cust_reg_h", "tidak bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        custSafetyValueH.check(R.id.choice_safety_value_high_tidak_bocor);
        prefManager.saveString("cust_safe_h", "tidak bocor");

        custSafetyValueH.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_safety_value_high_bocor:
                        prefManager.saveString("cust_safe_h", "bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_safety_value_high_tidak_bocor:
                        prefManager.saveString("cust_safe_h", "tidak bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        custRegulatorL.check(R.id.choice_regulator_low_tidak_bocor);
        prefManager.saveString("cust_reg_l", "tidak bocor");

        custRegulatorL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_regulator_low_bocor:
                        prefManager.saveString("cust_reg_l", "bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_regulator_low_tidak_bocor:
                        prefManager.saveString("cust_reg_l", "tidak bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        custSafetyValueL.check(R.id.choice_safety_value_low_tidak_bocor);
        prefManager.saveString("cust_safe_l", "tidak bocor");

        custSafetyValueL.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_safety_value_low_bocor:
                        prefManager.saveString("cust_safe_l", "bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_safety_value_low_tidak_bocor:
                        prefManager.saveString("cust_safe_l", "tidak bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });
        custRegulatorIns.check(R.id.choice_regulator_instrument_tidak_bocor);
        prefManager.saveString("cust_reg_ins", "tidak bocor");

        custRegulatorIns.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_regulator_instrument_bocor:
                        prefManager.saveString("cust_reg_ins", "bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_regulator_instrument_tidak_bocor:
                        prefManager.saveString("cust_reg_ins", "tidak bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        custPressControl1.check(R.id.choice_instrument_system_pressure_control_fungsi);
        prefManager.saveString("cust_press_con1", "fungsi");
        custPressControl1.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_instrument_system_pressure_control_tidak_fungsi:
                        prefManager.saveString("cust_press_con1", "tidak fungsi");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_instrument_system_pressure_control_fungsi:
                        prefManager.saveString("cust_press_con1", "fungsi");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        custPressControl2.check(R.id.choice_instrument_system_pressure_control2_fungsi);
        prefManager.saveString("cust_press_con2", "fungsi");
        custPressControl2.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_instrument_system_pressure_control2_tidak_fungsi:
                        prefManager.saveString("cust_press_con2", "tidak fungsi");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_instrument_system_pressure_control2_fungsi:
                        prefManager.saveString("cust_press_con2", "fungsi");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        custAir.check(R.id.choice_heating_system_air_normal);
        prefManager.saveString("cust_air", "normal");
        custAir.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_heating_system_air_tidak_normal:
                        prefManager.saveString("cust_air", "tidak normal");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_heating_system_air_normal:
                        prefManager.saveString("cust_air", "normal");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        custHeater1.check(R.id.choice_heating_system_heater1_tidak_bocor);
        prefManager.saveString("cust_heater1", "tidak bocor");
        custHeater1.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_heating_system_heater1_bocor:
                        prefManager.saveString("cust_heater1", "bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_heating_system_heater1_tidak_bocor:
                        prefManager.saveString("cust_heater1", "tidak bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        custHeater2.check(R.id.choice_heating_system_heater2_tidak_bocor);
        prefManager.saveString("cust_heater2", "tidak bocor");
        custHeater2.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_heating_system_heater2_bocor:
                        prefManager.saveString("cust_heater2", "bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_heating_system_heater2_tidak_bocor:
                        prefManager.saveString("cust_heater2", "tidak bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        custTherm1.check(R.id.choice_heating_system_thermocontrol1_fungsi);
        prefManager.saveString("cust_therm1", "fungsi");
        custTherm1.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_heating_system_thermocontrol1_tidak_fungsi:
                        prefManager.saveString("cust_therm1", "tidak fungsi");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_heating_system_thermocontrol1_fungsi:
                        prefManager.saveString("cust_therm1", "fungsi");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        custTherm2.check(R.id.choice_heating_system_thermocontrol2_fungsi);
        prefManager.saveString("cust_therm2", "fungsi");
        custTherm2.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_heating_system_thermocontrol2_tidak_fungsi:
                        prefManager.saveString("cust_therm2", "tidak fungsi");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_heating_system_thermocontrol2_fungsi:
                        prefManager.saveString("cust_therm2", "fungsi");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        custOliStand.check(R.id.choice_stand_oli_pelumas_normal);
        prefManager.saveString("cust_stand_oli", "normal");
        custOliStand.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_stand_oli_pelumas_tidak_normal:
                        prefManager.saveString("cust_stand_oli", "tidak normal");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_stand_oli_pelumas_normal:
                        prefManager.saveString("cust_stand_oli", "normal");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        custOliSambung.check(R.id.choice_tubing_oli_pelumas_tidak_bocor);
        prefManager.saveString("cust_tubing_oli", "tidak bocor");
        custOliSambung.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.choice_tubing_oli_pelumas_bocor:
                        prefManager.saveString("cust_tubing_oli", "bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_tidak_nyala);
                        break;
                    case R.id.choice_tubing_oli_pelumas_tidak_bocor:
                        prefManager.saveString("cust_tubing_oli", "tidak bocor");
                        //prefManager.saveInt("tb_cek_lampu_pos", R.id.choice_trailer_berangkat_cek_lampu_nyala);
                        break;

                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
        ((MenuUtamaActivity) getActivity())
                .setActionBarTitle("Customer Check");
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_customer_check, container, false);

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
            textNamaCust.setThreshold(1); //will start working from first character
            textNamaCust.setAdapter(adapter);

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ConnectionHelper cons = new ConnectionHelper();
                con = cons.connectionclass(prefManager.catchString("server"));        // Connect to database
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "select nama from tp.master_customer";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    //ArrayList data1 = new ArrayList();

                    while (rs.next()) {

                        prolist.add(rs.getString("nama"));
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



    private class AddCust extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        ProgressDialog progress = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute()
        {
            progress.setTitle("Loading");
            progress.setMessage("Harap tunggu");
            progress.setCanceledOnTouchOutside(false);
            progress.show();

            prefManager.saveString("namaCust"       ,textNamaCust       .getText().toString());
            prefManager.saveString("pressIn"        ,textPressIn        .getText().toString());
            prefManager.saveString("ketDecant"      ,textKetDecant      .getText().toString());
            prefManager.saveString("pressOut1"      ,textPressOut1      .getText().toString());
            prefManager.saveString("pressOut2"      ,textPressOut2      .getText().toString());
            prefManager.saveString("ketPress"       ,textKetPress       .getText().toString());
            prefManager.saveString("pressOut3"      ,textPressOut3      .getText().toString());
            prefManager.saveString("settingPress"   ,textSettingPress   .getText().toString());
            prefManager.saveString("ketInstrument"  ,textKetInsturment  .getText().toString());
            prefManager.saveString("settingTemp1"   ,textSettingTemp1   .getText().toString());
            prefManager.saveString("settingTemp2"   ,textSettingTemp2   .getText().toString());
            prefManager.saveString("tempAir"        ,textTempAir        .getText().toString());
            prefManager.saveString("ketPemanas"     ,textKetPemanas     .getText().toString());
            prefManager.saveString("tempGasOut"     ,textTempGasOut     .getText().toString());
            prefManager.saveString("ketTempGas"     ,textKetTempGas     .getText().toString());
            prefManager.saveString("turbinMeter"    ,textTurbinMeter    .getText().toString());
            prefManager.saveString("electricVol"    ,textElectricVol    .getText().toString());
            prefManager.saveString("pressStand"     ,textPressStand     .getText().toString());
            prefManager.saveString("tempStand"      ,textTempStand      .getText().toString());
            prefManager.saveString("ketStand"       ,textKetStand       .getText().toString());
            prefManager.saveString("ketSambung"     ,textKetSambung     .getText().toString());

            //progressBar.setVisibility(View.VISIBLE);
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
                    //z = "Check Your Internet Access!";
                    z = "Check Your Internet Access!";
                } else {
                    String query = "select id_customer from tp.master_customer where nama = '" + textNamaCust.getText() + "'";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    while (rs.next())
                        prefManager.saveString("idCust", rs.getString("id_customer"));



                    query = "insert into tp.cek_decant_manifold values ( " + prefManager.catchString("idCust") + ", '" +
                            prefManager.catchString("namaCust") + "', " +
                            prefManager.catchString("pressIn") + ", '" +
                            prefManager.catchString("cust_ip_kamera") + "', '" +
                            prefManager.catchString("ketDecant") + "')";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    query = "insert into tp.cek_prs values( " + prefManager.catchString("idCust") + ", '" +
                            prefManager.catchString("namaCust") + "', '" +
                            prefManager.catchString("cust_reg_h") + "', " +
                            textPressOut1.getText() + ", '" +
                            prefManager.catchString("cust_safe_h") + "', '" +
                            prefManager.catchString("cust_reg_l") + "', " +
                            textPressOut2.getText() + ", '" +
                            prefManager.catchString("cust_safe_l") + "', '" +
                            prefManager.catchString("ketPress") + "')";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    query = "insert into tp.cek_instrumen_system values( " + prefManager.catchString("idCust") + ", '" +
                            prefManager.catchString("namaCust") + "', '" +
                            prefManager.catchString("cust_reg_ins") + "', " +
                            textPressOut3.getText() + ", '" +
                            prefManager.catchString("cust_press_con1") + "', " +//
                            textSettingPress.getText() + ", '" +
                            prefManager.catchString("cust_press_con2") + "', '" +
                            prefManager.catchString("ketInstrument") + "')";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    query = "insert into tp.cek_heating_system values( " + prefManager.catchString("idCust") + ", '" +
                            prefManager.catchString("namaCust") + "', '" +
                            prefManager.catchString("cust_air") + "', '" +
                            prefManager.catchString("cust_heater1") + "', '" +
                            prefManager.catchString("cust_heater2") + "', '" +
                            prefManager.catchString("cust_therm1") + "', " +
                            textSettingTemp1.getText() + ", '" +
                            prefManager.catchString("cust_therm2") + "', " +
                            textSettingTemp2.getText() + ", " +
                            textTempAir.getText() + ", '" +
                            textKetPemanas.getText() + "')";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    query = "insert into tp.cek_temperature_gas_outlet values( " + prefManager.catchString("idCust") + ", '" +
                            prefManager.catchString("namaCust") + "', " +
                            textTempGasOut.getText() + ", '" +
                            textKetTempGas.getText() + "')";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    query = "insert into tp.cek_standmeter values( " + prefManager.catchString("idCust") + ", '" +
                            prefManager.catchString("namaCust") + "', " +
                            textTurbinMeter.getText() + ", '" +
                            prefManager.catchString("cust_stand_oli") + "', " +
                            textElectricVol.getText() + ", " +
                            textPressStand.getText() + ", " +
                            textTempStand.getText() + ", '" +
                            textKetStand.getText() + "')";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    query = "insert into tp.cek_sambungan_pipa values( " + prefManager.catchString("idCust") + ", '" +
                            prefManager.catchString("namaCust") + "', '" +
                            prefManager.catchString("cust_tubing_oli") + "', '" +
                            textKetSambung.getText() + "')";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
//                    textPressStand     .getText().toString());
//                    prefManager.saveString("tempStand"      ,textTempStand      .getText().toString());
//                    prefManager.saveString("ketStand"       ,textKetStand       .getText().toString());


                    z= "berhasil ditambahkan";

                }
            } catch (Exception ex) {
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();

            }
            return z;
        }
    }

    private void LoadPage() {
        textNamaCust        .setText(prefManager.catchString("namaCust"));
        textPressIn         .setText(prefManager.catchString("pressIn"));
        textKetDecant       .setText(prefManager.catchString("ketDecant"));
        textPressOut1       .setText(prefManager.catchString("pressOut1"));
        textPressOut2       .setText(prefManager.catchString("pressOut2"));
        textKetPress        .setText(prefManager.catchString("ketPress"));
        textPressOut3       .setText(prefManager.catchString("pressOut3"));
        textSettingPress    .setText(prefManager.catchString("settingPress"));
        textKetInsturment   .setText(prefManager.catchString("ketInstrument"));
        textSettingTemp1    .setText(prefManager.catchString("settingTemp1"));
        textSettingTemp2    .setText(prefManager.catchString("settingTemp2"));
        textTempAir         .setText(prefManager.catchString("tempAir"));
        textKetPemanas      .setText(prefManager.catchString("ketPemanas"));
        textTempGasOut      .setText(prefManager.catchString("tempGasOut"));
        textKetTempGas      .setText(prefManager.catchString("ketTempGas"));
        textTurbinMeter     .setText(prefManager.catchString("turbinMeter"));
        textElectricVol     .setText(prefManager.catchString("electricVol"));
        textPressStand      .setText(prefManager.catchString("pressStand"));
        textTempStand       .setText(prefManager.catchString("tempStand"));
        textKetStand        .setText(prefManager.catchString("ketStand"));
        textKetSambung      .setText(prefManager.catchString("ketSambung"));
    }

    private class ResetCust extends AsyncTask<String,String,String>
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
            prefManager.deleteKey("namaCust");
            prefManager.deleteKey("pressIn");
            prefManager.deleteKey("ketDecant");
            prefManager.deleteKey("pressOut1");
            prefManager.deleteKey("pressOut2");
            prefManager.deleteKey("ketPress");
            prefManager.deleteKey("pressOut3");
            prefManager.deleteKey("settingPress");
            prefManager.deleteKey("ketInstrument");
            prefManager.deleteKey("settingTemp1");
            prefManager.deleteKey("settingTemp2");
            prefManager.deleteKey("tempAir");
            prefManager.deleteKey("ketPemanas");
            prefManager.deleteKey("tempGasOut");
            prefManager.deleteKey("ketTempGas");
            prefManager.deleteKey("turbinMeter");
            prefManager.deleteKey("electricVol");
            prefManager.deleteKey("pressStand");
            prefManager.deleteKey("tempStand");
            prefManager.deleteKey("ketStand");
            prefManager.deleteKey("ketSambung");
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