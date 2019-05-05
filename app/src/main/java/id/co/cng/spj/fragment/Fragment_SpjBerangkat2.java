    package id.co.cng.spj.fragment;

    import android.annotation.SuppressLint;
    import android.app.DatePickerDialog;
    import android.app.Dialog;
    import android.app.ProgressDialog;
    import android.app.TimePickerDialog;
    import android.content.ActivityNotFoundException;
    import android.graphics.Color;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.support.annotation.Nullable;
    import android.support.design.widget.TextInputEditText;
    import android.support.v4.app.DialogFragment;
    import android.support.v4.app.Fragment;
    import android.support.v4.app.FragmentTransaction;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.AutoCompleteTextView;
    import android.widget.Button;
    import android.widget.DatePicker;
    import android.widget.EditText;
    import android.widget.FrameLayout;
    import android.widget.ProgressBar;
    import android.widget.RadioButton;
    import android.widget.TimePicker;
    import android.widget.Toast;

    import com.itextpdf.text.BaseColor;
    import com.itextpdf.text.Chunk;
    import com.itextpdf.text.Document;
    import com.itextpdf.text.DocumentException;
    import com.itextpdf.text.Element;
    import com.itextpdf.text.Font;
    import com.itextpdf.text.Image;
    import com.itextpdf.text.PageSize;
    import com.itextpdf.text.Paragraph;
    import com.itextpdf.text.Phrase;
    import com.itextpdf.text.pdf.BaseFont;
    import com.itextpdf.text.pdf.PdfPCell;
    import com.itextpdf.text.pdf.PdfPTable;
    import com.itextpdf.text.pdf.PdfWriter;
    import com.itextpdf.text.pdf.draw.LineSeparator;
    import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;

    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.List;

    import id.co.cng.spj.MenuUtamaActivity;
    import id.co.cng.spj.R;
    import id.co.cng.spj.utility.ConnectionHelper;
    import id.co.cng.spj.utility.FileUtils;
    import id.co.cng.spj.utility.PrefManager;

    import static android.view.View.GONE;

    public class Fragment_SpjBerangkat2 extends Fragment {

        PrefManager prefManager;
        Connection con;
        AutoCompleteTextView
                bTrip2,bDriver2,bAsistDriver2,bHeadTruck2,bNoPol2,bTujuan2,bPlantKembali2,
                //berangkat
                bKmAkhir,
                //horeka
                bTekTurbin,bTempTurbin,bMeterTurbin,
                //lain
                bKomplain,bKeterangan2;
        EditText bTanggalAntar2,bTanggalKembali,bTibaCust,bDariCust,bTibaPlant;
        AutoCompleteTextView nomorRak;
        EditText tekAkhir, tekAkhirCustomer, corrVol, uncorrVol;
        // trailer
        //EditText nomorRak, tekAkhir, tekAkhirCustomer;
        Button
                button_ubah_spj_simpan,
                button_ubah_spj_berangkat,
                button_kirim_spj_berangkat;
        ProgressBar progressBar;
        short signRakApv, signRakCrane, signRakTrailer;
        short pos = 1;

        private Button btnDateAntar,btnDateKembali,
                       btnTimeTibaCust,btnTimeDariCust,btnTimeTibaPlant;
        private EditText txtDateAntar,txtDateKembali,
                         txtTimeTibaCust,txtTimeDariCust,txtTimeTibaPlant;

        private int[] listRak = {
                0,
                R.id.act_2_choice_1,
                R.id.act_2_choice_2,
                R.id.act_2_choice_3,
                R.id.act_2_choice_4,
                R.id.act_2_choice_5,
                R.id.act_2_choice_6,
                R.id.act_2_choice_7,
                R.id.act_2_choice_8,
                R.id.act_2_choice_9,
                R.id.act_2_choice_10,
                R.id.act_2_choice_11,
                R.id.act_2_choice_12

        };

        private com.nex3z.togglebuttongroup.button.CircularToggle cobaRak;
        private View view;

        public Fragment_SpjBerangkat2(){}
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState)
        {
            bTrip2           = view.findViewById(R.id.text_spj_berangkat_2_trip);
            bDriver2         = view.findViewById(R.id.text_spj_berangkat_2_driver);
            bAsistDriver2    = view.findViewById(R.id.text_spj_berangkat_2_asisten);
            bHeadTruck2      = view.findViewById(R.id.text_spj_berangkat_2_head_truck);
            bNoPol2          = view.findViewById(R.id.text_spj_berangkat_2_nopol);
            bTujuan2         = view.findViewById(R.id.text_spj_berangkat_2_tujuan);
            bPlantKembali2   = view.findViewById(R.id.text_spj_berangkat_2_plant_kembali);
            bTanggalAntar2   = view.findViewById(R.id.text_spj_berangkat_2_date);

            bTanggalKembali  = view.findViewById(R.id.text_spj_berangkat_2_tanggal_kembali);
            bTibaCust        = view.findViewById(R.id.text_spj_berangkat_2_tiba_customer);
            bDariCust        = view.findViewById(R.id.text_spj_berangkat_2_dari_customer);
            bTibaPlant       = view.findViewById(R.id.text_spj_berangkat_2_tiba_plant);
            bKmAkhir         = view.findViewById(R.id.text_spj_berangkat_2_km_akhir);

            nomorRak         = view.findViewById(R.id.text_spj_berangkat_2_id_rak);
            tekAkhir         = view.findViewById(R.id.text_spj_berangkat_2_tek_akhir);
            tekAkhirCustomer = view.findViewById(R.id.text_spj_berangkat_2_tek_sampai);
            corrVol          = view.findViewById(R.id.text_spj_berangkat_2_corrected);
            uncorrVol          = view.findViewById(R.id.text_spj_berangkat_2_uncorrected);

            bTekTurbin       = view.findViewById(R.id.text_spj_berangkat_2_tek_turbin);
            bTempTurbin      = view.findViewById(R.id.text_spj_berangkat_2_temp_turbin);
            bMeterTurbin     = view.findViewById(R.id.text_spj_berangkat_2_meter_turbin);

            bKomplain        = view.findViewById(R.id.text_spj_berangkat_2_komplain);
            bKeterangan2      = view.findViewById(R.id.text_spj_berangkat_2_keterangan);

            final SingleSelectToggleGroup selectModa = view.findViewById(R.id.act_2_group_single_radiobutton);

            final SingleSelectToggleGroup selectRakCrane = view.findViewById(R.id.act_2_group_single_radiobutton_frame_hya);
            final SingleSelectToggleGroup selectRakApv = view.findViewById(R.id.act_2_group_single_radiobutton_frame_apv);

            final com.nex3z.togglebuttongroup.button.CircularToggle rak1 = view.findViewById(R.id.act_2_choice_1);
            final com.nex3z.togglebuttongroup.button.CircularToggle rak9 = view.findViewById(R.id.act_2_choice_9);

            final FrameLayout frameApv = view.findViewById(R.id.act_2_frame_apv);
            final FrameLayout frameCrane = view.findViewById(R.id.act_2_frame_crane);
            final FrameLayout frameTrailer = view.findViewById(R.id.act_2_frame_trailer);

            progressBar = view.findViewById(R.id.progressBar_spj_berangkat_2);
            button_ubah_spj_berangkat=view.findViewById(R.id.button_spj_berangkat_2_ubah);
            button_kirim_spj_berangkat=view.findViewById(R.id.button_spj_berangkat_2_kirim);
            button_ubah_spj_simpan=view.findViewById(R.id.button_spj_berangkat_2_selesai);
            prefManager = new PrefManager(getActivity());

////////////////////////////////////////////////////////////////////////////////////////////////////
            //
            // plnshift.(prefManager.catchString("spj_berangkat_2_shift"));

            LoadSPJB loadSPJB = new LoadSPJB();
            loadSPJB.execute("");

            nomorRak.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    //isSelectedText = true;
                    tekAkhir.requestFocus();
                }

            });

//onclicklistener
            button_kirim_spj_berangkat.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if (bTrip2.getText().toString().trim().equals("")) {
                        bTrip2.setError("Tolong diisi!");
                        bTrip2   .requestFocus();
                    } else if (bDriver2.getText().toString().trim().equals("")) {
                        bDriver2.setError("Tolong diisi!");
                        bDriver2.requestFocus();
                    } else if (bAsistDriver2.getText().toString().trim().equals("")) {
                        bAsistDriver2.setError("Tolong diisi!");
                        bAsistDriver2.requestFocus();
                    } else if (bHeadTruck2.getText().toString().trim().equals("")) {
                        bHeadTruck2.setError("Tolong diisi!");
                        bHeadTruck2.requestFocus();
                    } else if (bNoPol2.getText().toString().trim().equals("")) {
                        bNoPol2.setError("Tolong diisi!");
                        bNoPol2.requestFocus();
                    } else if (bTujuan2.getText().toString().trim().equals("")) {
                        bTujuan2.setError("Tolong diisi!");
                        bTujuan2.requestFocus();
                    } else if (bTanggalAntar2.getText().toString().trim().equals("")) {
                        bTanggalAntar2.setError("Tolong diisi!");
                        bTanggalAntar2.requestFocus();



                    } else if (bTanggalKembali.getText().toString().trim().equals("")) {
                        bTanggalKembali.setError("Tolong diisi!");
                        bTanggalKembali.requestFocus();
                    } else if (bTibaCust.getText().toString().trim().equals("")) {
                        bTibaCust.setError("Tolong diisi!");
                        bTibaCust.requestFocus();
                    } else if (bDariCust.getText().toString().trim().equals("")) {
                        bDariCust.setError("Tolong diisi!");
                        bDariCust.requestFocus();
                    } else if (bTibaPlant.getText().toString().trim().equals("")) {
                        bTibaPlant.setError("Tolong diisi!");
                        bTibaPlant.requestFocus();
                    } else if (bKmAkhir.getText().toString().trim().equals("")) {
                        bKmAkhir.setError("Tolong diisi!");
                        bKmAkhir.requestFocus();
                    } else {
                        AddSPJB addSPJB = new AddSPJB();// this is the Asynctask, which is used to process in background to reduce load on app process
                        addSPJB.execute("");
                    }
                }
            });

            button_ubah_spj_berangkat.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    Fragment fragment;
                    fragment = new Fragment_UbahSpjBerangkat();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
            });

            button_ubah_spj_simpan.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
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

            txtDateKembali = (EditText)view.findViewById(R.id.text_spj_berangkat_2_tanggal_kembali);
            txtDateKembali.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DialogFragment pickerFragment = new SelectDateFragment();
                    pickerFragment.show(getFragmentManager(),"DatePicker");
                }
            });
            btnDateKembali = (Button)view.findViewById(R.id.button_spj_berangkat_2_tanggal_kembali);
            btnDateKembali.setOnClickListener(new View.OnClickListener() {
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
                                    txtDateKembali.setText( dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                }
                            }, mYear, mMonth, mDay);
                    datePicker.show();
                }
            });

            //return view;
//Time Picker
            //tibacust
            txtTimeTibaCust = (EditText)view.findViewById(R.id.text_spj_berangkat_2_tiba_customer);
            txtTimeTibaCust.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment pickerFragment = new SelectDateFragment();
                    pickerFragment.show(getFragmentManager(),"TimePicker");
                }
            });
            btnTimeTibaCust = (Button)view.findViewById(R.id.button_spj_berangkat_2_tiba_customer);
            btnTimeTibaCust.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    int mHour = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);
                    // Launch Time Picker Dialog

                    TimePickerDialog timePicker = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    txtTimeTibaCust.setText( hourOfDay + ":" + minute);
                                }
                            }, mHour, mMinute, false);
                    timePicker.show();
                }
            });

            txtTimeDariCust = (EditText)view.findViewById(R.id.text_spj_berangkat_2_dari_customer);
            txtTimeDariCust.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DialogFragment pickerFragment = new SelectDateFragment();
                    pickerFragment.show(getFragmentManager(),"TimePicker");
                }
            });
            btnTimeDariCust = (Button)view.findViewById(R.id.button_spj_berangkat_2_dari_customer);
            btnTimeDariCust.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    int mHour = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);
                    // Launch Time Picker Dialog

                    TimePickerDialog timePicker = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    txtTimeDariCust.setText( hourOfDay + ":" + minute);
                                }
                            }, mHour, mMinute, false);
                    timePicker.show();
                }
            });
            txtTimeTibaPlant = (EditText)view.findViewById(R.id.text_spj_berangkat_2_tiba_plant);
            txtTimeTibaPlant.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DialogFragment pickerFragment = new SelectDateFragment();
                    pickerFragment.show(getFragmentManager(),"TimePicker");
                }
            });
            btnTimeTibaPlant = (Button)view.findViewById(R.id.button_spj_berangkat_2_tiba_plant);
            btnTimeTibaPlant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    int mHour = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);
                    // Launch Time Picker Dialog

                    TimePickerDialog timePicker = new TimePickerDialog(getActivity(),
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    txtTimeTibaPlant.setText( hourOfDay + ":" + minute);
                                }
                            }, mHour, mMinute, false);
                    timePicker.show();
                }
            });

            super.onViewCreated(view, savedInstanceState);
            ((MenuUtamaActivity) getActivity())
                    .setActionBarTitle("SPJ - Berangkat");

            fungsiReset();

            selectRakApv.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.act_2_choice_1 :
                            signRakApv = 1;
                            fungsiCatchValue(signRakApv);

                            //fungsiCatchApv();
                            //magicPengubahZero();
                            nomorRak.requestFocus();

                            break;

                        case R.id.act_2_choice_2 :
                            signRakApv = 2;
                            fungsiCatchValue(signRakApv);

                            //fungsiCatchApv();
                            //magicPengubahZero();
                            nomorRak.requestFocus();

                            break;

                        case R.id.act_2_choice_3 :
                            signRakApv = 3;

                            fungsiCatchValue(signRakApv);
                            //fungsiCatchApv();
                            //magicPengubahZero();
                            nomorRak.requestFocus();
                            break;

                        case R.id.act_2_choice_4 :
                            signRakApv = 4;

                            fungsiCatchValue(signRakApv);

                            pos = signRakApv;
                            //fungsiCatchApv();
                            //magicPengubahZero();
                            nomorRak.requestFocus();
                            break;

                        case R.id.act_2_choice_5 :
                            signRakApv = 5;

                            fungsiCatchValue(signRakApv);
                            //fungsiCatchApv();
                            //magicPengubahZero();
                            nomorRak.requestFocus();
                            break;

                        case R.id.act_2_choice_6 :
                            signRakApv = 6;

                            fungsiCatchValue(signRakApv);
                            //fungsiCatchApv();
                            //magicPengubahZero();
                            nomorRak.requestFocus();
                            break;

                        case R.id.act_2_choice_7 :
                            signRakApv = 7;

                            fungsiCatchValue(signRakApv);
                            //fungsiCatchApv();
                            //magicPengubahZero();
                            nomorRak.requestFocus();
                            break;

                        case R.id.act_2_choice_8 :
                            signRakApv = 8;

                            fungsiCatchValue(signRakApv);
                            //fungsiCatchApv();
                            //magicPengubahZero();
                            nomorRak.requestFocus();
                            break;
                    }
                }
            });

            selectRakCrane.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.act_2_choice_9 :
                            signRakCrane = 9;
                            Toast.makeText(getActivity(), "apv: "+ String.valueOf(signRakCrane) + " pos: " + String.valueOf(pos), Toast.LENGTH_SHORT).show();

                            fungsiCatchValue(signRakCrane);
                            nomorRak.requestFocus();

                            break;
                        case R.id.act_2_choice_10 :

                            signRakCrane = 10;
                            Toast.makeText(getActivity(), "apv: "+ String.valueOf(signRakCrane) + " pos: " + String.valueOf(pos), Toast.LENGTH_SHORT).show();

                            fungsiCatchValue(signRakCrane);
                            nomorRak.requestFocus();

                            break;
                        case R.id.act_2_choice_11 :
                            signRakCrane = 11;
                            Toast.makeText(getActivity(), "apv: "+ String.valueOf(signRakCrane) + " pos: " + String.valueOf(pos), Toast.LENGTH_SHORT).show();

                            fungsiCatchValue(signRakCrane);

                            //pos = signRakCrane;
                            nomorRak.requestFocus();

                            break;


                    }
                }
            });

            selectModa.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.act_2_choice_apv :

                            Toast.makeText(getActivity(), "apv: "+ String.valueOf(signRakCrane) + " pos: " + String.valueOf(pos), Toast.LENGTH_SHORT).show();
//                        signRakCrane = 1;
                            signRakApv = 1;
                            signRakTrailer = 0;
                            signRakCrane = 0;
                            fungsiCatchValue(signRakApv);

                            rak1.setChecked(true);

                            nomorRak.requestFocus();


                            frameApv.setVisibility(View.VISIBLE);
                            frameCrane.setVisibility(GONE);
                            frameTrailer.setVisibility(GONE);

                            //frameApv.requestFocus();

//                        crane.setVisibility(View.GONE);
//                        trailer.setVisibility(View.GONE);
//                        Toast.makeText(getActivity(), "1!", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.act_2_choice_crane :
//
//                        signRakApv = 9;
                            Toast.makeText(getActivity(), "apv: "+ String.valueOf(signRakCrane) + " pos: " + String.valueOf(pos), Toast.LENGTH_SHORT).show();

                            signRakCrane = 9;
                            signRakApv = 0;
                            signRakTrailer = 0;

                            rak9.setChecked(true);

                            //hide and show frame layout
                            frameApv.setVisibility(GONE);
                            frameCrane.setVisibility(View.VISIBLE);
                            frameTrailer.setVisibility(GONE);

                            //frameCrane.requestFocus();
                            fungsiCatchValue(signRakCrane);

//                        Toast.makeText(getActivity(), "2!", Toast.LENGTH_SHORT).show();
                            break;

                        case R.id.act_2_choice_trailer :

                            signRakTrailer = 12;
                            signRakCrane = 0;
                            signRakApv = 0;
//
                            frameApv.setVisibility(GONE);
                            frameCrane.setVisibility(GONE);
                            frameTrailer.setVisibility(View.VISIBLE);

                            fungsiCatchValue(signRakTrailer);

                            nomorRak.requestFocus();

//                        Toast.makeText(getActivity(), "3!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.fragment_spj_berangkat2, container, false);

            return view;
        }
/*        public void saveTask()
        {
            String description = tfDescription.getText().toString();
            String date = txtDate.getText().toString();
        }
*/
//Date
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
//Time
        @SuppressLint("ValidFragment")
        public class SelectTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                final Calendar calendar = Calendar.getInstance();
                int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute = calendar.get(Calendar.MINUTE);
                return new TimePickerDialog(getActivity(), this, mHour, mMinute, false);
            }

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                populateSetTime(hourOfDay, minute);
            }
            public void populateSetTime(int hourOfDay, int minute) {
                //set the timee here
                populateSetTime(hourOfDay,minute);
            }
        }

        private void fungsiCatchValue(short rak) {


            prefManager.saveString("spj_berangkat_2_" + pos + "_nomorRak", nomorRak.getText().toString());
            prefManager.saveString("spj_berangkat_2_" + pos + "_tekAkhir", tekAkhir.getText().toString());
            prefManager.saveString("spj_berangkat_2_" + pos + "_tekAkhirCustomer", tekAkhirCustomer.getText().toString());
            prefManager.saveString("spj_berangkat_2_" + pos + "_corrVol", corrVol.getText().toString());
            prefManager.saveString("spj_berangkat_2_" + pos + "_uncorrVol", uncorrVol.getText().toString());
            Toast.makeText(getActivity(), "apv: "+ String.valueOf(signRakApv) + " pos: " + String.valueOf(pos), Toast.LENGTH_SHORT).show();

            if (prefManager.catchString("spj_berangkat_2_" + pos + "_nomorRak").equals("") ||
                    prefManager.catchString("spj_berangkat_2_" + pos + "_tekAkhir").equals("") ||
                    prefManager.catchString("spj_berangkat_2_" + pos + "_tekAkhirCustomer").equals("") ||
                    prefManager.catchString("spj_berangkat_2_" + pos + "_corrVol").equals("") ||
                    prefManager.catchString("spj_berangkat_2_" + pos + "_uncorrVol").equals("")) {
                cobaRak = view.findViewById(listRak[pos]);
                cobaRak.setBackgroundColor(Color.RED);
                //((com.nex3z.togglebuttongroup.button.CircularToggle) view.findViewById(pos)).setBackgroundColor(Color.RED);
            } else {
                cobaRak = view.findViewById(listRak[pos]);
                cobaRak.setBackgroundColor(Color.GREEN);
            }

            nomorRak.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    //isSelectedText = true;

                    //tekAwal.requestFocus();
                    short i = 1;
                    while (i != 13) {
                        if (String.valueOf(nomorRak.getText()).contentEquals(prefManager.catchString("spj_berangkat_2_"+i+"_nomorRak"))) {
                            nomorRak.setError("Rak " + String.valueOf(nomorRak.getText()) + " sudah digunakan");
                            break;
                        }

                        i++;
                    }

                }

            });

            pos = rak;


            nomorRak.setText(prefManager.catchString("spj_berangkat_2_"+rak+"_nomorRak"));
            tekAkhir.setText(prefManager.catchString("spj_berangkat_2_"+rak+"_tekAkhir"));
            tekAkhirCustomer.setText(prefManager.catchString("spj_berangkat_2_"+rak+"_tekAkhirCustomer"));
            corrVol.setText(prefManager.catchString("spj_berangkat_2_"+rak+"_corrVol"));
            uncorrVol.setText(prefManager.catchString("spj_berangkat_2_"+rak+"_uncorrVol"));
        }

        private void fungsiReset() {
            short i = 1;
            nomorRak.setText(prefManager.catchString(""));
            tekAkhir.setText(prefManager.catchString(""));
            tekAkhirCustomer.setText(prefManager.catchString(""));
            corrVol.setText(prefManager.catchString(""));
            uncorrVol.setText(prefManager.catchString(""));
            try {
                while (i != 13) {
                    prefManager.deleteKey("spj_berangkat_2_" + i + "_nomorRak");
                    prefManager.deleteKey("spj_berangkat_2_" + i + "_tekAkhir");
                    prefManager.deleteKey("spj_berangkat_2_" + i + "_tekAkhirCustomer");
                    prefManager.deleteKey("spj_berangkat_2_" + i + "_corrVol");
                    prefManager.deleteKey("spj_berangkat_2_" + i + "_uncorrVol");
                    cobaRak = view.findViewById(listRak[i]);
                    cobaRak.setBackgroundColor(Color.RED);
                    i++;
                }
            } catch (Exception ex) {
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        public void createPdf(String dest) {

            if (new File(dest).exists()) {
                new File(dest).delete();
            }

            try {
                /**
                 * Creating Document
                 */
                Document document = new Document();

                // Location to save
                PdfWriter.getInstance(document, new FileOutputStream(dest));

                // Open to write
                document.open();

                // Document Settings
                document.setPageSize(PageSize.A4);
                document.addCreationDate();
                document.addAuthor("Radikz");
                document.addCreator("Radikz");

                /**
                 * How to USE FONT....
                 */
                BaseFont urName = BaseFont.createFont("assets/fonts/brandon_bold.otf", "UTF-8", BaseFont.EMBEDDED);

                // LINE SEPARATOR
                LineSeparator lineSeparator = new LineSeparator();
                lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

                Image img = Image.getInstance(FileUtils.getAppPath(getActivity()) + "cne.png");
                //img.setAbsolutePosition(450f, 9f);
                img.setAlignment(Image.LEFT);
                img.setXYRatio(30);
                img.setWidthPercentage(30);

                PdfPTable table;
                PdfPCell cell;

                table = new PdfPTable(10);
                table.setWidthPercentage(99);
//                table.setTotalWidth(600);
                table.setHorizontalAlignment(Element.ALIGN_CENTER);

                cell = new PdfPCell(img, false);
                cell.setFixedHeight(40);
                cell.setBorder(0);
                cell.setRowspan(2);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "  PT. CITRA NUSANTARA ENERGI")));

//                cell.setFixedHeight(10);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setBorder(0);
//                cell.setPaddingBottom(10);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setColspan(9);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "  Plant Waru")));
                cell.setFixedHeight(10);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setVerticalAlignment(Element.ALIGN_TOP);
//                cell.setPaddingTop(10);
                cell.setColspan(9);
                cell.setBorder(0);
                table.addCell(cell);
                document.add(table);

                table = new PdfPTable(4);
                table.setWidthPercentage(99);
//                table.setTotalWidth(600);
                table.setHorizontalAlignment(Element.ALIGN_CENTER);

                //kolom judul
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "")));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "SURAT PERINTAH JALAN")));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "")));
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "")));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Pengantaran CNG")));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(0);
                cell.setColspan(2);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "")));
                cell.setBorder(0);
                table.addCell(cell);

//                prefManager.saveString("spj_berangkat_2_trip2", rs.getString("trip"));
//                prefManager.saveString("spj_berangkat_2_driver2",rs.getString("driver"));
//                prefManager.saveString("spj_berangkat_2_adriver2",rs.getString("asisten_driver"));
//                prefManager.saveString("spj_berangkat_2_ht2",rs.getString("headtruck"));
//                prefManager.saveString("spj_berangkat_2_nopol2", rs.getString("nomor_polisi"));
//                prefManager.saveString("spj_berangkat_2_tujuan2", rs.getString("tujuan"));
//                prefManager.saveString("spj_berangkat_2_plant_kembali2",rs.getString("plant_kembali"));
//                prefManager.saveString("spj_berangkat_2_tanggal_antar2", rs.getString("tanggal_antar"));
//
//                prefManager.saveString("spj_berangkat_listRak", rs.getString("nomor_rak"));
//                prefManager.saveString("spj_berangkat_listTekAwal", rs.getString("tekanan_awal"));

                //kolom1
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "TRIP")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_trip2"))));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Head Truck")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_ht2"))));
                table.addCell(cell);

                //kolom2
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Driver")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_driver2"))));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Nopol")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_nopol2"))));
                table.addCell(cell);

                //kolom3
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Asisten")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_adriver2")))); // nama asisten
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setColspan(2);
                table.addCell(cell);

                //KOLOM 4
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "BERANGKAT")));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "KEMBALI")));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setColspan(2);
                table.addCell(cell);

                //KOLOM5
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Tujuan")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_tujuan2"))));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_plant_kembali2"))));
                table.addCell(cell);

                //kolom6
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Tanggal Antar")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_tanggal_antar2"))));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Tanggal Kembali")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_tanggal_kembali"))));
                table.addCell(cell);

                //kolom7
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Jam Brkt dr Plant")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, String.format("%.8s", prefManager.catchString("spj_berangkat_jamDariPlant")))));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Jam Tiba di Cust")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_tiba_cust"))));
                table.addCell(cell);

                //kolom8
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Jam Brkt dr Cust")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_dari_cust"))));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Jam Tiba di Plant")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_tiba_plant"))));
                table.addCell(cell);

                //kolom9
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Trailer Antar")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_listRak"))));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Trailer Balik")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_listRak"))));
                table.addCell(cell);

                //kolom10
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Tekanan Awal")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f,prefManager.catchString("spj_berangkat_listTekAwal"))));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Tekanan Akhir")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_listTekAkhir"))));
                table.addCell(cell);

                //kolom11
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Tekanan Sampai Customer")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_listTekAkhirCustomer"))));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setColspan(2);
                table.addCell(cell);

                //kolom12
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "KM Awal")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_kmAwal"))));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "KM Akhir")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_km_akhir"))));
                table.addCell(cell);

                //kolom13
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Corrected Vol")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_listCorrVol"))));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Uncorrected Vol")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_listUncorrVol"))));
                table.addCell(cell);

                //kolom14
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Temperature Turbin")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_temp_turbin"))));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Turbin Meter")));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_meter_turbin"))));
                table.addCell(cell);

                //dispatcher kolom
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Dispatcher,")));
                cell.setBorder(0);
                cell.setPaddingTop(5);
                cell.setPaddingBottom(20);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Driver,")));
                cell.setBorder(0);
                cell.setPaddingTop(5);
                cell.setPaddingBottom(20);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Security,")));
                cell.setBorder(0);
                cell.setPaddingTop(5);
                cell.setPaddingBottom(20);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Customer,")));
                cell.setBorder(0);
                cell.setPaddingTop(5);
                cell.setPaddingBottom(20);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Adip Sugiarto")));
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_driver2"))));
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_berangkat_2_tujuan2"))));
                cell.setBorder(0);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Putih: Dispatcher")));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Merah: Security CNE")));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Hijau: Security CNE")));
                cell.setBorder(0);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Kuning: Customer")));
                cell.setBorder(0);
                table.addCell(cell);

//            String DOG = "path/to/resource/dog.png";
//
//            Image fox = new Image(ImageDataFactory.create(DOG));
                document.add(table);


                document.close();

                Toast.makeText(getActivity(), "Created... :)", Toast.LENGTH_SHORT).show();

                //FileUtils.openFile(getActivity(), new File(dest));

            } catch (IOException | DocumentException ie) {
                //LOGE("createPdf: Error " + ie.getLocalizedMessage());
                Toast.makeText(getActivity(), "createPdf: Error " + ie.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            } catch (ActivityNotFoundException ae) {
                Toast.makeText(getActivity(), "No application found to open this file.", Toast.LENGTH_SHORT).show();
            }
        }

        private Paragraph TextPdf(BaseFont nameFont, float size, String text) {
            Font mOrderDetailsTitleFont = new Font(nameFont, size, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDetailsTitleChunk = new Chunk(text, mOrderDetailsTitleFont);
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);

            return mOrderDetailsTitleParagraph;
        }

        private class LoadSPJB extends AsyncTask<String,String,String>
        {
            String z = "";
            Boolean isSuccess = false;
            ProgressDialog progress = new ProgressDialog(getActivity());
            List<String> listRak = new ArrayList<String>();

            @Override
            protected void onPreExecute()
            {
                progress.setTitle("Loading");
                progress.setMessage("Harap tunggu");
                progress.setCanceledOnTouchOutside(false);
                progress.show();

                bTrip2.setEnabled(false);
                bDriver2.setEnabled(false);
                bAsistDriver2.setEnabled(false);
                bHeadTruck2.setEnabled(false);
                bNoPol2.setEnabled(false);
                bTujuan2.setEnabled(false);
                bPlantKembali2.setEnabled(false);
                bTanggalAntar2.setEnabled(false);


            }

            @Override
            protected void onPostExecute(String r)
            {
                progress.dismiss();
                Toast.makeText(getActivity(), prefManager.catchString("spj_tiba_trip"), Toast.LENGTH_SHORT).show();

                bTrip2.setText(prefManager.catchString("spj_berangkat_2_trip2"));
                bDriver2.setText(prefManager.catchString("spj_berangkat_2_driver2"));
                bAsistDriver2.setText(prefManager.catchString("spj_berangkat_2_adriver2"));
                bHeadTruck2.setText(prefManager.catchString("spj_berangkat_2_ht2"));
                bNoPol2.setText(prefManager.catchString("spj_berangkat_2_nopol2"));
                bTujuan2.setText(prefManager.catchString("spj_berangkat_2_tujuan2"));
                bPlantKembali2.setText(prefManager.catchString("spj_berangkat_2_plant_kembali2"));
                bTanggalAntar2.setText(prefManager.catchString("spj_berangkat_2_tanggal_antar2"));

                ArrayAdapter<String> rakAdapter = new ArrayAdapter<String>
                        (getActivity(), android.R.layout.select_dialog_item, listRak);
                nomorRak.setThreshold(1);
                nomorRak.setAdapter(rakAdapter);

                RadioButton modaApv = view.findViewById(R.id.act_2_choice_apv);
                RadioButton modaCrane = view.findViewById(R.id.act_2_choice_crane);
                RadioButton modaTrailer = view.findViewById(R.id.act_2_choice_trailer);

                if (prefManager.catchString("spj_berangkat_2_jenis_moda2").equals("APV")) {

                    modaApv.setChecked(true);
                    modaCrane.setEnabled(false);
                    modaCrane.setBackgroundColor(Color.GRAY);
                    modaTrailer.setEnabled(false);
                    modaTrailer.setBackgroundColor(Color.GRAY);
                } else if (prefManager.catchString("spj_berangkat_2_jenis_moda2").equals("HYAPCRANE")) {
                    modaCrane.setChecked(true);
                    modaApv.setEnabled(false);
                    modaApv.setBackgroundColor(Color.GRAY);
                    modaTrailer.setEnabled(false);
                    modaTrailer.setBackgroundColor(Color.GRAY);
                } else {
                    modaTrailer.setChecked(true);
                    modaCrane.setEnabled(false);
                    modaCrane.setBackgroundColor(Color.GRAY);
                    modaApv.setEnabled(false);
                    modaApv.setBackgroundColor(Color.GRAY);
                }

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
                        String query = "select * from tp.spj_berangkat where trip = '" + prefManager.catchString("spj_tiba_trip") + "'";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        ResultSet rs = preparedStatement.executeQuery();
                        while (rs.next()) {
                            prefManager.saveString("spj_berangkat_2_trip2", rs.getString("trip"));
                            prefManager.saveString("spj_berangkat_2_driver2",rs.getString("driver"));
                            prefManager.saveString("spj_berangkat_2_adriver2",rs.getString("asisten_driver"));
                            prefManager.saveString("spj_berangkat_2_ht2",rs.getString("headtruck"));
                            prefManager.saveString("spj_berangkat_2_nopol2", rs.getString("nomor_polisi"));
                            prefManager.saveString("spj_berangkat_2_tujuan2", rs.getString("tujuan"));
                            prefManager.saveString("spj_berangkat_2_plant_kembali2",rs.getString("plant_kembali"));
                            prefManager.saveString("spj_berangkat_2_tanggal_antar2", rs.getString("tanggal_antar"));
                            prefManager.saveString("spj_berangkat_2_jenis_moda2", rs.getString("jenis_moda"));

                            prefManager.saveString("spj_berangkat_jamDariPlant", rs.getString("jam_berangkat"));
                            prefManager.saveString("spj_berangkat_kmAwal", rs.getString("km_awal"));
                            prefManager.saveString("spj_berangkat_listRak", rs.getString("nomor_rak"));
                            prefManager.saveString("spj_berangkat_listTekAwal", rs.getString("tekanan_awal"));
                        }

                        query = "select No_moda from tp.master_moda_transport";
                        preparedStatement = con.prepareStatement(query);
                        rs = preparedStatement.executeQuery();

                        //ArrayList data1 = new ArrayList();

                        while (rs.next()) {

                            listRak.add(rs.getString("No_moda"));
//                        your_array_list.add
                            //prolist.add(datanum);
                        }

                        z= "berhasil diload";

                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();

                }
                return z;
            }
        }

        private class AddSPJB extends AsyncTask<String,String,String>
        {
            String z = "";
            Boolean isSuccess = false;
            String query;
            PreparedStatement preparedStatement;
            ResultSet rs;
            String rakBaru = "";
            String tekAkhirBaru = "";
            String tekAkhirCustomerBaru = "";
            String corrVolBaru = "";
            String uncorrVolBaru = "";

            @Override
            protected void onPreExecute() {
                short temp;
                prefManager.saveString("spj_berangkat_2_trip2", bTrip2.getText().toString());
                prefManager.saveString("spj_berangkat_2_driver2", bDriver2.getText().toString());
                prefManager.saveString("spj_berangkat_2_adriver2", bAsistDriver2.getText().toString());
                prefManager.saveString("spj_berangkat_2_ht2", bHeadTruck2.getText().toString());
                prefManager.saveString("spj_berangkat_2_nopol2", bNoPol2.getText().toString());
                prefManager.saveString("spj_berangkat_2_tujuan2", bTujuan2.getText().toString());
                prefManager.saveString("spj_berangkat_2_plant_kembali2", bPlantKembali2.getText().toString());
                prefManager.saveString("spj_berangkat_2_tanggal_antar2", bTanggalAntar2.getText().toString());


                prefManager.saveString("spj_berangkat_2_tanggal_kembali", bTanggalKembali.getText().toString());
                prefManager.saveString("spj_berangkat_2_tiba_cust", bTibaCust.getText().toString());
                prefManager.saveString("spj_berangkat_2_dari_cust", bDariCust.getText().toString());
                prefManager.saveString("spj_berangkat_2_tiba_plant", bTibaPlant.getText().toString());
                prefManager.saveString("spj_berangkat_2_km_akhir", bKmAkhir.getText().toString());
                prefManager.saveString("spj_berangkat_2_tek_turbin", bTekTurbin.getText().toString());
                prefManager.saveString("spj_berangkat_2_temp_turbin", bTempTurbin.getText().toString());
                prefManager.saveString("spj_berangkat_2_meter_turbin", bMeterTurbin.getText().toString());
                prefManager.saveString("spj_berangkat_2_komplain", bKomplain.getText().toString());
                prefManager.saveString("spj_berangkat_2_keterangan2", bKeterangan2.getText().toString());

                progressBar.setVisibility(View.VISIBLE);
                //Toast.makeText(getActivity(), prefManager.catchString("pengisian_filling1_timeAwal"), Toast.LENGTH_SHORT).show();


                if (signRakApv >= 1 && signRakApv <= 8) {
                    temp = 1;
                    while (temp != 9) {
                        rakBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_nomorRak") + "|";
                        tekAkhirBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_tekAkhir") + "|";
                        tekAkhirCustomerBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_tekAkhirCustomer") + "|";
                        corrVolBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_corrVol") + "|";
                        uncorrVolBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_uncorrVol") + "|";

                        temp++;
                    }
                } else if (signRakCrane >= 9 && signRakCrane <= 11) {
                    temp = 9;
                    while (temp != 12) {
                        rakBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_nomorRak") + "|";
                        tekAkhirBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_tekAkhir") + "|";
                        tekAkhirCustomerBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_tekAkhirCustomer") + "|";
                        corrVolBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_corrVol") + "|";
                        uncorrVolBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_uncorrVol") + "|";

                        temp++;
                    }
                } else {
                    temp = 12;
                    rakBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_nomorRak") ;
                    tekAkhirBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_tekAkhir");
                    tekAkhirCustomerBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_tekAkhirCustomer");
                    corrVolBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_corrVol");
                    uncorrVolBaru += prefManager.catchString("spj_berangkat_2_" + temp + "_uncorrVol");
                }
                prefManager.saveString("spj_berangkat_2_listRak", rakBaru);
                prefManager.saveString("spj_berangkat_2_listTekAkhir", tekAkhirBaru);
                prefManager.saveString("spj_berangkat_2_listTekAkhirCustomer", tekAkhirCustomerBaru);
                prefManager.saveString("spj_berangkat_2_listCorrVol", corrVolBaru);
                prefManager.saveString("spj_berangkat_2_listUncorrVol", uncorrVolBaru);
                Toast.makeText(getActivity(), rakBaru, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(String r)
            {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
                createPdf(FileUtils.getAppPath(getActivity()) + "spj.pdf");
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

                        File file = new File(FileUtils.getAppPath(getActivity()) + "spj.pdf");
                        FileInputStream input = new FileInputStream(file);
                        int len = (int) file.length();
                        query = "update tp.spj_berangkat set nomor_rak_baru = '" + rakBaru + "', " +
                                "tekanan_akhir = '" + tekAkhirBaru + "', " +
                                "tekanan_sampai_customer = '" + tekAkhirCustomerBaru + "', " +
                                "corrected_volume = '" + corrVolBaru + "', " +
                                "uncorrected_volume = '" + uncorrVolBaru + "', " +
                                "tanggal_kembali = " + "convert (datetime, '" + txtDateKembali.getText() + "', 103), " +
                                "jam_tiba_di_customer = convert(time, convert(datetime, '10/02/1998 " + txtTimeTibaCust.getText().toString() + "', 103), 108), " +
                                "jam_dari_customer = convert(time, convert(datetime, '10/02/1998 " + txtTimeDariCust.getText().toString() + "', 103), 108), " +
                                "jam_tiba_di_plant = convert(time, convert(datetime, '10/02/1998 " + txtTimeTibaPlant.getText().toString() + "', 103), 108), " +
                                "km_akhir = " + bKmAkhir.getText() + ", " +
                                "tekanan_turbin = " + bTekTurbin.getText() + ", " +
                                "temp_turbin = " + bTempTurbin.getText() + ", " +
                                "turbin_meter = " + bMeterTurbin.getText() + ", " +
                                "komplain = '" + bKomplain.getText() + "', " +
                                "keterangan_horeka = '" + bKeterangan2.getText() + "', " +
                                "pdf_upload = ? " +
                                "where trip ='" + prefManager.catchString("spj_tiba_trip") + "'";

                        preparedStatement = con.prepareStatement(query);
                        preparedStatement.setBinaryStream(1, input, len);
                        preparedStatement.executeUpdate();

//                        "', convert (datetime, '" + prefManager.catchString("tanggal_kirim_generator") + "', 103), "


                        z= "Berhasil Diubah";

                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();

                }
                return z;
            }
        }


    }