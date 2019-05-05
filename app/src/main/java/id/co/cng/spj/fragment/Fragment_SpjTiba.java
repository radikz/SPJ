package id.co.cng.spj.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import id.co.cng.spj.MenuUtamaActivity;
import id.co.cng.spj.R;
import id.co.cng.spj.utility.ConnectionHelper;
import id.co.cng.spj.utility.PrefManager;

public class Fragment_SpjTiba extends Fragment {

    private Connection con;
    private PrefManager prefManager;
    private AppCompatAutoCompleteTextView customer;
    private EditText tanggal;
    private ProgressBar progressBar;
    ListView listView;

    public Fragment_SpjTiba(){}
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MenuUtamaActivity) getActivity())
                .setActionBarTitle("SPJ Kembali");

        prefManager = new PrefManager(getActivity());
        customer = view.findViewById(R.id.edittext_spj_tiba_search_nama);
        tanggal = view.findViewById(R.id.edittext_spj_tiba_kalender);
        listView = view.findViewById(R.id.list_spj);
        progressBar = view.findViewById(R.id.progressBar_spj_tiba);


        customer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //isSelectedText = true;
                LoadSelection loadSelection = new LoadSelection();
                loadSelection.execute("");
            }

        });

        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something here
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tanggal.setText( dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                LoadSelection loadSelection = new LoadSelection();
                                loadSelection.execute("");
                            }
                        }, mYear, mMonth, mDay);
                datePicker.show();

            }

        });


        LoadPage loadPage = new LoadPage();
        loadPage.execute("");

//


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_spj_tiba, container, false);

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

    private class LoadPage extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        ProgressDialog progress = new ProgressDialog(getActivity());
        List<String> listCustomer = new ArrayList<String>();
        List<Map<String, String>> prolist  = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute()
        {
            progress.setTitle("Loading");
            progress.setMessage("Harap tunggu");
            progress.setCanceledOnTouchOutside(false);
            progress.show();

        }

        @Override
        protected void onPostExecute(String r)
        {
            progress.dismiss();
            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();


            ArrayAdapter<String> customerAdapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, listCustomer);
            customer.setThreshold(1);
            customer.setAdapter(customerAdapter);

            //list
            String[] from = { "A", "B", "C" };
            int[] views = { R.id.spj_tiba_list_trip, R.id.spj_tiba_list_customer, R.id.spj_tiba_list_tanggal };
            final SimpleAdapter ADA = new SimpleAdapter(getActivity(),

                    prolist, R.layout.list_item, from, views);
            listView.setAdapter(ADA);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override

                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>)

                            ADA.getItem(arg2);
                    customer.setText((String) obj.get("B"));
                    tanggal.setText((String) obj.get("C"));
                    prefManager.saveString("spj_tiba_trip", (String) obj.get("A"));
                    Fragment fragment;
                    fragment = new Fragment_SpjBerangkat2();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
                //     qty.setText(qtys);                }
            });

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

                    String query = "select nama from tp.master_customer";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet rs =  preparedStatement.executeQuery();
                    while (rs.next()) {
                        listCustomer.add(rs.getString("nama"));
                    }

                    query = "select trip, tujuan, tanggal_antar from tp.spj_berangkat";
                    preparedStatement = con.prepareStatement(query);
                    rs = preparedStatement.executeQuery();

                    //ArrayList data1 = new ArrayList();

                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString("trip"));
                        datanum.put("B", rs.getString("tujuan"));
                        datanum.put("C", rs.getString("tanggal_antar"));
                        prolist.add(datanum);
                    }

                    z= "berhasil diload";

                }
            } catch (Exception ex) {
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();

            }
            return z;
        }
    }

    private class LoadSelection extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
//        ProgressDialog progress = new ProgressDialog(getActivity());
        List<String> listCustomer = new ArrayList<String>();
        List<Map<String, String>> prolist  = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute()
        {
//            progress.setTitle("Loading");
//            progress.setMessage("Harap tunggu");
//            progress.setCanceledOnTouchOutside(false);
//            progress.show();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r)
        {
//            progress.dismiss();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();


            ArrayAdapter<String> customerAdapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, listCustomer);
            customer.setThreshold(1);
            customer.setAdapter(customerAdapter);

            //list
            String[] from = { "A", "B", "C" };
            int[] views = { R.id.spj_tiba_list_trip, R.id.spj_tiba_list_customer, R.id.spj_tiba_list_tanggal };
            final SimpleAdapter ADA = new SimpleAdapter(getActivity(),

                    prolist, R.layout.list_item, from, views);
            listView.setAdapter(ADA);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override

                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>)

                            ADA.getItem(arg2);
                    customer.setText((String) obj.get("B"));
                    tanggal.setText((String) obj.get("C"));
                    prefManager.saveString("spj_tiba_trip", (String) obj.get("A"));
                    Fragment fragment;
                    fragment = new Fragment_SpjBerangkat2();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
                //     qty.setText(qtys);                }
            });

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

                    String query = "select nama from tp.master_customer";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet rs =  preparedStatement.executeQuery();
                    while (rs.next()) {
                        listCustomer.add(rs.getString("nama"));
                    }
                    if (tanggal.getText().toString().equals("") || customer.getText().toString().equals(""))
                        query = "select trip, tujuan, tanggal_antar from tp.spj_berangkat where tujuan='" +
                                customer.getText() + "' or tanggal_antar=" +
                                "convert (datetime, '" + tanggal.getText() + "', 103) ";
                    else
                        query = "select trip, tujuan, tanggal_antar from tp.spj_berangkat where tujuan='" +
                            customer.getText() + "' and tanggal_antar=" +
                            "convert (datetime, '" + tanggal.getText() + "', 103) ";

                    preparedStatement = con.prepareStatement(query);
                    rs = preparedStatement.executeQuery();

                    //ArrayList data1 = new ArrayList();

                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString("trip"));
                        datanum.put("B", rs.getString("tujuan"));
                        datanum.put("C", rs.getString("tanggal_antar"));
                        prolist.add(datanum);
                    }

                    z= "berhasil diload";

                }
            } catch (Exception ex) {
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();

            }
            return z;
        }
    }
}