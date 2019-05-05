package id.co.cng.spj.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import id.co.cng.spj.R;

public class FragmentOne extends Fragment {

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

    Switch switchButton18, switchButton19, switchButton20, switchButton21, switchButton22;
    TextView textView18, textView19, textView20, textView21, textView22;

    String switchOn  = "Yes";
    String switchOff = "No ";

    public FragmentOne() {
        // Required empty public constructor
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Generator Check");

        // For switch button 18
        switchButton18 = (Switch) view.findViewById(R.id.switch_generator_water_leak);
        textView18 = (TextView) view.findViewById(R.id.textView_generator_water_leak);

        switchButton18.setChecked(false);
        switchButton18.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textView18.setText(switchOn);
                } else {
                    textView18.setText(switchOff);
                }
            }
        });

        if (switchButton18.isChecked()) {
            textView18.setText(switchOn);
        } else {
            textView18.setText(switchOff);
        }

        // For switch button 19
        switchButton19 = (Switch) view.findViewById(R.id.switch_generator_oil_leaking);
        textView19 = (TextView) view.findViewById(R.id.textView_generator_oil_leaking);

        switchButton19.setChecked(false);
        switchButton19.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textView19.setText(switchOn);
                } else {
                    textView19.setText(switchOff);
                }
            }
        });

        if (switchButton19.isChecked()) {
            textView19.setText(switchOn);
        } else {
            textView19.setText(switchOff);
        }

        // For switch button 20
        switchButton20 = (Switch) view.findViewById(R.id.switch_generator_oil_volume);
        textView20 = (TextView) view.findViewById(R.id.textView_generator_oil_volume);

        switchButton20.setChecked(false);
        switchButton20.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textView20.setText(switchOn);
                } else {
                    textView20.setText(switchOff);
                }
            }
        });

        if (switchButton20.isChecked()) {
            textView20.setText(switchOn);
        } else {
            textView20.setText(switchOff);
        }

        // For switch button 21
        switchButton21 = (Switch) view.findViewById(R.id.switch_generator_water_volume);
        textView21 = (TextView) view.findViewById(R.id.textView_generator_water_volume);

        switchButton21.setChecked(false);
        switchButton21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textView21.setText(switchOn);
                } else {
                    textView21.setText(switchOff);
                }
            }
        });

        if (switchButton21.isChecked()) {
            textView21.setText(switchOn);
        } else {
            textView21.setText(switchOff);
        }

        // For switch button 22
        switchButton22 = (Switch) view.findViewById(R.id.switch_generator_van_belt);
        textView22 = (TextView) view.findViewById(R.id.textView_generator_van_belt);

        switchButton22.setChecked(false);
        switchButton22.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    textView22.setText(switchOn);
                } else {
                    textView22.setText(switchOff);
                }
            }
        });

        if (switchButton22.isChecked()) {
            textView22.setText(switchOn);
        } else {
            textView22.setText(switchOff);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        //spin genset
        spin1 = (Spinner) view.findViewById(R.id.spinner_generator_generator);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, genset);

        // mengeset Array Adapter tersebut ke Spinner
        spin1.setAdapter(adapter);

        // mengeset listener untuk mengetahui saat item dipilih
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                Toast.makeText(getActivity(), "Selected " + adapter.getItem(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//spin shift
        spin2 = (Spinner) view.findViewById(R.id.spinner_generator_shift);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, shift_generator);

        // mengeset Array Adapter tersebut ke Spinner
        spin2.setAdapter(adapter1);

        // mengeset listener untuk mengetahui saat item dipilih
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                Toast.makeText(getActivity(), "Selected " + adapter1.getItem(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
}
