package id.co.cng.spj.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import id.co.cng.spj.R;

public class FragmentTwo extends Fragment {

    private Spinner spin3;
    private String[] shift_pln = {
            "P2(07:00AM-03:00PM)",
            "S2(03:00AM-11:00PM)",
            "M2(11:00PM-07:00AM)"
    };

    public FragmentTwo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);

//spin shift pln
        spin3 = (Spinner) view.findViewById(R.id.spinner_pln_shift);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, shift_pln);

        // mengeset Array Adapter tersebut ke Spinner
        spin3.setAdapter(adapter3);

        // mengeset listener untuk mengetahui saat item dipilih
        spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // memunculkan toast + value Spinner yang dipilih (diambil dari adapter)
                Toast.makeText(getActivity(), "Selected " + adapter3.getItem(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }
}
