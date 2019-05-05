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
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import id.co.cng.spj.MenuUtamaActivity;
import id.co.cng.spj.R;
import id.co.cng.spj.utility.ConnectionHelper;

import id.co.cng.spj.utility.FileUtils;
import id.co.cng.spj.utility.PrefManager;

import static android.view.View.GONE;

public class Fragment_UbahSpjBerangkat extends Fragment {

    private Button btnDatePicker, btnTimePicker;
    private EditText txtDate, txtTime;
    //private TextInputEditText bTrip;
    private PrefManager prefManager;
    private Connection con;

    private AppCompatAutoCompleteTextView
            bTrip, bDriver, bAsistDriver, bHeadTruck, bNoPol, bTujuan, bPlantKembali;

    //berangkat
    private AppCompatAutoCompleteTextView bKmAwal, bKeterangan;
    // trailer
    private AppCompatAutoCompleteTextView nomorRak;
    private EditText tekAwal, tempAwal;
    private Button  buttonKirimSpjBerangkat, buttonResetSpjBerangkat;
    private Button selesai;
    short signRakApv, signRakCrane, signRakTrailer;
    short pos = 1;
    private com.nex3z.togglebuttongroup.button.CircularToggle cobaRak;
    ProgressBar progressBar;

    private int[] listRak = {
            0,
            R.id.act_choice_1,
            R.id.act_choice_2,
            R.id.act_choice_3,
            R.id.act_choice_4,
            R.id.act_choice_5,
            R.id.act_choice_6,
            R.id.act_choice_7,
            R.id.act_choice_8,
            R.id.act_choice_9,
            R.id.act_choice_10,
            R.id.act_choice_11,
            R.id.act_choice_12

    };

    private View view;

    public Fragment_UbahSpjBerangkat(){}
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MenuUtamaActivity) getActivity())
                .setActionBarTitle("SPJ - Ubah Berangkat");

        //bTrip = view.findViewById(R.id.text_spj_ubahBerangkat_trip);
//            kirim = view.findViewById(R.id.button_spj_ubahBerangkat_kirim);
//            reset = view.findViewById(R.id.button_spj_ubahBerangkat_reset);
        bTrip           = view.findViewById(R.id.text_spj_berangkat_trip);
        bDriver         = view.findViewById(R.id.text_spj_berangkat_driver);
        bAsistDriver    = view.findViewById(R.id.text_spj_berangkat_asisten);
        bHeadTruck      = view.findViewById(R.id.text_spj_berangkat_head_truck);
        bNoPol          = view.findViewById(R.id.text_spj_berangkat_nopol);
        bTujuan         = view.findViewById(R.id.text_spj_berangkat_tujuan);
        bPlantKembali   = view.findViewById(R.id.text_spj_berangkat_plant_kembali);
//
        nomorRak = view.findViewById(R.id.text_spj_berangkat_id_rak);
        tekAwal = view.findViewById(R.id.text_spj_berangkat_tek_awal);
        tempAwal = view.findViewById(R.id.text_spj_berangkat_temp_awal);
//
        bKmAwal         = view.findViewById(R.id.text_spj_berangkat_km_awal);
        bKeterangan     = view.findViewById(R.id.text_spj_berangkat_keterangan);
//
//            progressBar = view.findViewById(R.id.progressBar_spj_berangkat);
        buttonKirimSpjBerangkat = view.findViewById(R.id.button_spj_berangkat_kirim);
        selesai = view.findViewById(R.id.button_spj_berangkat_selesai);
        buttonResetSpjBerangkat = view.findViewById(R.id.button_spj_berangkat_reset);

        final SingleSelectToggleGroup selectModa = view.findViewById(R.id.act_group_single_radiobutton);

        final SingleSelectToggleGroup selectRakCrane = view.findViewById(R.id.act_group_single_radiobutton_frame_hya);
        final SingleSelectToggleGroup selectRakApv = view.findViewById(R.id.act_group_single_radiobutton_frame_apv);

        final com.nex3z.togglebuttongroup.button.CircularToggle rak1 = view.findViewById(R.id.act_choice_1);
        final com.nex3z.togglebuttongroup.button.CircularToggle rak9 = view.findViewById(R.id.act_choice_9);

        final FrameLayout frameApv = view.findViewById(R.id.act_frame_apv);
        final FrameLayout frameCrane = view.findViewById(R.id.act_frame_crane);
        final FrameLayout frameTrailer = view.findViewById(R.id.act_frame_trailer);

        prefManager = new PrefManager(getActivity());

        progressBar = view.findViewById(R.id.progressBar_spj_berangkat);

        bHeadTruck.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //isSelectedText = true;
                LoadNopol loadNopol = new LoadNopol();
                loadNopol.execute("");
            }

        });




        txtDate = (EditText)view.findViewById(R.id.text_spj_berangkat_date);
        txtDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment pickerFragment = new SelectDateFragment();
                pickerFragment.show(getFragmentManager(),"DatePicker");
            }
        });
        btnDatePicker = (Button)view.findViewById(R.id.button_spj_berangkat_date);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
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
                                txtDate.setText( dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePicker.show();
            }
        });

        //return view;

//Time Picker
        txtTime = (EditText)view.findViewById(R.id.text_spj_berangkat_time);
        txtTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment pickerFragment = new SelectDateFragment();
                pickerFragment.show(getFragmentManager(),"TimePicker");
            }
        });
        btnTimePicker = (Button)view.findViewById(R.id.button_spj_berangkat_time);
        btnTimePicker.setOnClickListener(new View.OnClickListener() {
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
                                txtTime.setText( hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePicker.show();
            }
        });

        buttonKirimSpjBerangkat.setText("UBAH");
        fungsiReset();


        LoadTrip loadTrip = new LoadTrip();
        loadTrip.execute("");

        LoadUbahBerangkat loadUbahBerangkat = new LoadUbahBerangkat();
        loadUbahBerangkat.execute("");



        selesai.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (signRakApv > 0 && signRakApv < 9) {
                    prefManager.saveString("spj_ubahBerangkat_" + signRakApv + "_noRak", nomorRak.getText().toString());
                    prefManager.saveString("spj_ubahBerangkat_" + signRakApv + "_tempAwal", tempAwal.getText().toString());
                    prefManager.saveString("spj_ubahBerangkat_" + signRakApv + "_tekAwal", tekAwal.getText().toString());

                } else if (signRakCrane >= 9 && signRakCrane <= 11) {
                    prefManager.saveString("spj_ubahBerangkat_" + signRakCrane + "_noRak", nomorRak.getText().toString());
                    prefManager.saveString("spj_ubahBerangkat_" + signRakCrane + "_tempAwal", tempAwal.getText().toString());
                    prefManager.saveString("spj_ubahBerangkat_" + signRakCrane + "_tekAwal", tekAwal.getText().toString());
                    
                } else if (signRakTrailer == 12) {
                    prefManager.saveString("spj_ubahBerangkat_12_noRak", nomorRak.getText().toString());
                    prefManager.saveString("spj_ubahBerangkat_12_tempAwal", tempAwal.getText().toString());
                    prefManager.saveString("spj_ubahBerangkat_12_tekAwal", tekAwal.getText().toString());
                    
                }


            }
        });
        
        buttonKirimSpjBerangkat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (bDriver.getText().toString().equals("")) {
                    bDriver.setError("Tolong Diisi");
                    bDriver.requestFocus();
                }
                else if (bAsistDriver.getText().toString().equals("")) {
                    bAsistDriver.setError("Tolong Diisi");
                    bAsistDriver.requestFocus();
                } else if (bHeadTruck.getText().toString().equals("")) {
                    bHeadTruck.setError("Tolong Diisi");
                    bHeadTruck.requestFocus();
                } else {
                    KirimSpjUbahBerangkat kirimSpjBerangkat = new KirimSpjUbahBerangkat();// this is the Asynctask, which is used to process in background to reduce load on app process
                    kirimSpjBerangkat.execute("");
                }


            }
        });

        buttonResetSpjBerangkat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

//                Fragment fragment;
//                fragment = new HomeFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, fragment);
//                ft.commit();
                fungsiReset();

            }
        });

        ///////////////////////////////////////////////////////////////

        nomorRak.setText(prefManager.catchString("spj_ubahBerangkat_"+1+"_nomorRak"));
        tekAwal.setText(prefManager.catchString("spj_ubahBerangkat_"+1+"_tekAwal"));
        tempAwal.setText(prefManager.catchString("spj_ubahBerangkat_"+1+"_tempAwal"));

        selectRakApv.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.act_choice_1 :
                        signRakApv = 1;
                        fungsiCatchValue(signRakApv);
                        nomorRak.requestFocus();

                        break;

                    case R.id.act_choice_2 :
                        signRakApv = 2;
                        fungsiCatchValue(signRakApv);
                        nomorRak.requestFocus();

                        break;

                    case R.id.act_choice_3 :
                        signRakApv = 3;
                        fungsiCatchValue(signRakApv);
                        nomorRak.requestFocus();
                        break;

                    case R.id.act_choice_4 :
                        signRakApv = 4;

                        fungsiCatchValue(signRakApv);
                        nomorRak.requestFocus();
                        break;

                    case R.id.act_choice_5 :
                        signRakApv = 5;

                        fungsiCatchValue(signRakApv);
                        nomorRak.requestFocus();
                        break;

                    case R.id.act_choice_6 :
                        signRakApv = 6;

                        fungsiCatchValue(signRakApv);
                        //magicPengubahZero();
                        nomorRak.requestFocus();
                        break;

                    case R.id.act_choice_7 :
                        signRakApv = 7;

                        fungsiCatchValue(signRakApv);
                        //magicPengubahZero();
                        nomorRak.requestFocus();
                        break;

                    case R.id.act_choice_8 :
                        signRakApv = 8;
                        fungsiCatchValue(signRakApv);
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
                    case R.id.act_choice_9 :
                        signRakCrane = 9;
                        fungsiCatchValue(signRakCrane);
                        nomorRak.requestFocus();

                        break;
                    case R.id.act_choice_10 :

                        signRakCrane = 10;
                        fungsiCatchValue(signRakCrane);
                        nomorRak.requestFocus();

                        break;
                    case R.id.act_choice_11 :
                        signRakCrane = 11;
                        fungsiCatchValue(signRakCrane);
                        nomorRak.requestFocus();

                        break;


                }
            }
        });

        selectModa.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.act_choice_apv :

                        //Toast.makeText(getActivity(), "apv: "+ String.valueOf(signRakCrane) + " pos: " + String.valueOf(pos), Toast.LENGTH_SHORT).show();
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

                    case R.id.act_choice_crane :
//
//                        signRakApv = 9;
                        //Toast.makeText(getActivity(), "apv: "+ String.valueOf(signRakCrane) + " pos: " + String.valueOf(pos), Toast.LENGTH_SHORT).show();

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

                    case R.id.act_choice_trailer :

                        signRakTrailer = 12;
                        signRakCrane = 0;
                        signRakApv = 0;
//
                        frameApv.setVisibility(GONE);
                        frameCrane.setVisibility(GONE);
                        frameTrailer.setVisibility(View.VISIBLE);

                        //frameTrailer.requestFocus();
                        fungsiCatchValue(signRakTrailer);

                        nomorRak.requestFocus();

//                        Toast.makeText(getActivity(), "3!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });



    }

    private void fungsiCatchValue(short rak) {


        prefManager.saveString("spj_ubahBerangkat_"+pos+"_nomorRak", String.valueOf(nomorRak.getText()));
        prefManager.saveString("spj_ubahBerangkat_"+pos+"_tekAwal", String.valueOf(tekAwal.getText()));
        prefManager.saveString("spj_ubahBerangkat_"+pos+"_tempAwal", String.valueOf(tempAwal.getText()));
        //Toast.makeText(getActivity(), "apv: "+ String.valueOf(signRakApv) + " pos: " + String.valueOf(pos), Toast.LENGTH_SHORT).show();

        if (prefManager.catchString("spj_ubahBerangkat_" + pos + "_nomorRak").equals("") ||
                prefManager.catchString("spj_ubahBerangkat_" + pos + "_tekAwal").equals("") ||
                prefManager.catchString("spj_ubahBerangkat_" + pos + "_tempAwal").equals("")) {
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
                    if (String.valueOf(nomorRak.getText()).contentEquals(prefManager.catchString("spj_ubahBerangkat_"+i+"_nomorRak"))) {
                        nomorRak.setError("Rak " + String.valueOf(nomorRak.getText()) + " sudah digunakan");
                        break;
                    }

                    i++;
                }

            }

        });

        pos = rak;


        nomorRak.setText(prefManager.catchString("spj_ubahBerangkat_"+rak+"_nomorRak"));
        tekAwal.setText(prefManager.catchString("spj_ubahBerangkat_"+rak+"_tekAwal"));
        tempAwal.setText(prefManager.catchString("spj_ubahBerangkat_"+rak+"_tempAwal"));
    }

    private void fungsiReset() {
        short i = 1;
        nomorRak.setText(prefManager.catchString(""));
        tekAwal.setText(prefManager.catchString(""));
        tempAwal.setText(prefManager.catchString(""));
        try {
            while (i != 13) {
                prefManager.deleteKey("spj_ubahBerangkat_" + i + "_nomorRak");
                prefManager.deleteKey("spj_ubahBerangkat_" + i + "_tekAwal");
                prefManager.deleteKey("spj_ubahBerangkat_" + i + "_tempAwal");
                cobaRak = view.findViewById(listRak[i]);
                cobaRak.setBackgroundColor(Color.RED);
                i++;
            }
        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void fungsiAwal () {
        short i = 1;
        try {
            while (i != 13) {
               if (prefManager.catchString("spj_ubahBerangkat_" + i + "_nomorRak").equals("") ||
                        prefManager.catchString("spj_ubahBerangkat_" + i + "_tekAwal").equals("") ||
                        prefManager.catchString("spj_ubahBerangkat_" + i + "_tempAwal").equals("")) {

                    cobaRak = view.findViewById(listRak[i]);
                    cobaRak.setBackgroundColor(Color.RED);
                    //((com.nex3z.togglebuttongroup.button.CircularToggle) view.findViewById(pos)).setBackgroundColor(Color.RED);
                } else {
                    cobaRak = view.findViewById(listRak[i]);
                    cobaRak.setBackgroundColor(Color.GREEN);

                }
                i++;
            }

        } catch (Exception ex) {
            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_spj_berangkat, container, false);

        // Date Picker

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

            //kolom1
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "TRIP")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, String.valueOf(bTrip.getText()))));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Head Truck")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, String.valueOf(bHeadTruck.getText()))));
            table.addCell(cell);

            //kolom2
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Driver")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, String.valueOf(bDriver.getText()))));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Nopol")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, String.valueOf(bNoPol.getText()))));
            table.addCell(cell);

            //kolom3
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Asisten")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, String.valueOf(bAsistDriver.getText())))); // nama asisten
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
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, String.valueOf(bTujuan.getText()))));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, String.valueOf(bPlantKembali.getText()))));
            table.addCell(cell);

            //kolom6
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Tanggal Antar")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, String.valueOf(txtDate.getText()))));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Tanggal Kembali")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table.addCell(cell);

            //kolom7
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Jam Brkt dr Plant")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, String.valueOf(txtTime.getText()))));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Jam Tiba di Cust")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table.addCell(cell);

            //kolom8
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Jam Brkt dr Cust")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Jam Tiba di Plant")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table.addCell(cell);

            //kolom9
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Trailer Antar")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, prefManager.catchString("spj_ubahBerangkat_listRak"))));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Trailer Balik")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table.addCell(cell);

            //kolom10
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Tekanan Awal")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f,prefManager.catchString("spj_ubahBerangkat_listTekAwal"))));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Tekanan Akhir")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table.addCell(cell);

            //kolom11
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Tekanan Sampai Customer")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setColspan(2);
            table.addCell(cell);

            //kolom12
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "KM Awal")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, String.valueOf(bKmAwal.getText()))));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "KM Akhir")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table.addCell(cell);

            //kolom13
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Corrected Vol")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Uncorrected Vol")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table.addCell(cell);

            //kolom14
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Temperature Turbin")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, "Turbin Meter")));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
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
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, String.valueOf(bDriver.getText()))));
            cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cell.setBorder(0);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(""));
            cell.setBorder(0);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(TextPdf(urName,9f, String.valueOf(bTujuan.getText()))));
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

    private class LoadTrip extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        ProgressDialog progress = new ProgressDialog(getActivity());
        List<String> listDriver = new ArrayList<String>();
        List<String> listAstDriver = new ArrayList<String>();
        List<String> listHeadTruck = new ArrayList<String>();
        List<String> listTujuan = new ArrayList<String>();
        List<String> listPlantKembali = new ArrayList<String>();
        List<String> listRak = new ArrayList<String>();

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
            bTrip.setText(String.format("%.2s", prefManager.catchString("spj_seq_taun"))  +
                    String.format("%.2s", prefManager.catchString("spj_seq_bulan")) + "-" +
                    String.format("%.2s", prefManager.catchString("spj_seq")));

            ArrayAdapter<String> driverAdapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, listDriver);
            bDriver.setThreshold(1);
            bDriver.setAdapter(driverAdapter);

            ArrayAdapter<String> asistenDriverAdapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, listAstDriver);
            bAsistDriver.setThreshold(1);
            bAsistDriver.setAdapter(asistenDriverAdapter);

            ArrayAdapter<String> headTruckAdapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, listHeadTruck);
            bHeadTruck.setThreshold(1);
            bHeadTruck.setAdapter(headTruckAdapter);

            ArrayAdapter<String> tujuanAdapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, listTujuan);
            bTujuan.setThreshold(1);
            bTujuan.setAdapter(tujuanAdapter);

            ArrayAdapter<String> plantKembaliAdapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, listPlantKembali);
            bPlantKembali.setThreshold(1);
            bPlantKembali.setAdapter(plantKembaliAdapter);

            ArrayAdapter<String> rakAdapter = new ArrayAdapter<String>
                    (getActivity(), android.R.layout.select_dialog_item, listRak);
            nomorRak.setThreshold(1);
            nomorRak.setAdapter(rakAdapter);
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
                    String query = "select current_value from sys.sequences where name = 'trip_seq'";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    while (rs.next())
                        prefManager.saveString("spj_seq", rs.getString("current_value"));

                    query = "select right(year(getdate()), 2) as taun";
                    preparedStatement = con.prepareStatement(query);
                    rs = preparedStatement.executeQuery();
                    while (rs.next())
                        prefManager.saveString("spj_seq_taun", rs.getString("taun"));

                    query = "select month(getdate()) as bulan";
                    preparedStatement = con.prepareStatement(query);
                    rs = preparedStatement.executeQuery();
                    while (rs.next())
                        prefManager.saveString("spj_seq_bulan", String.format("%02d", rs.getInt("bulan")));
                    //prefManager.saveString("spj_seq_bulan", rs.getString("bulan"));

                    query = "select * from tp.master_driver_asisten";
                    preparedStatement = con.prepareStatement(query);
                    rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        listDriver.add(rs.getString("nama_driver"));
                        listAstDriver.add(rs.getString("nama_asisten"));
                    }

                    query = "select * from tp.master_head_truck";
                    preparedStatement = con.prepareStatement(query);
                    rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        listHeadTruck.add(rs.getString("nama_head_truck"));
                    }

                    query = "select nama from tp.master_customer";
                    preparedStatement = con.prepareStatement(query);
                    rs =  preparedStatement.executeQuery();
                    while (rs.next()) {
                        listTujuan.add(rs.getString("nama"));
                        listPlantKembali.add(rs.getString("nama"));
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



    private class LoadUbahBerangkat extends AsyncTask<String,String,String>
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

        }

        @Override
        protected void onPostExecute(String r)
        {
            progress.dismiss();
            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
            bTrip.setText(prefManager.catchString("spj_ubahBerangkat_trip"));
            bDriver.setText(prefManager.catchString("spj_ubahBerangkat_driver"));
            bAsistDriver.setText(prefManager.catchString("spj_ubahBerangkat_adriver"));
            bHeadTruck.setText(prefManager.catchString("spj_ubahBerangkat_ht"));
            bNoPol.setText(prefManager.catchString("spj_ubahBerangkat_nopol"));
            bTujuan.setText(prefManager.catchString("spj_ubahBerangkat_tujuan"));
            bPlantKembali.setText(prefManager.catchString("spj_ubahBerangkat_plant_kembali"));
            txtDate.setText(prefManager.catchString("spj_ubahBerangkat_tanggal_antar"));

            //String test = "ku | hj";

            StringTokenizer parsingRak = new StringTokenizer(prefManager.catchString("spj_ubahBerangkat_nomorRak"), "|");
            Short count;
            if (prefManager.catchString("spj_ubahBerangkat_jenisModa").equals("APV")) count = 1;
            else if (prefManager.catchString("spj_ubahBerangkat_jenisModa").equals("HYAPCRANE")) count = 9;
            else count = 12;
            while (parsingRak.hasMoreTokens()) {
                //
                //anu += parsingRak.nextToken(); spj_ubahBerangkat_12_nomorRak
                prefManager.saveString("spj_ubahBerangkat_"+count+"_nomorRak", parsingRak.nextToken());
                count++;
            }

            StringTokenizer parsingTekAwal = new StringTokenizer(prefManager.catchString("spj_ubahBerangkat_tekAwal"), "|");
            if (prefManager.catchString("spj_ubahBerangkat_jenisModa").equals("APV")) count = 1;
            else if (prefManager.catchString("spj_ubahBerangkat_jenisModa").equals("HYAPCRANE")) count = 9;
            else count = 12;
            while (parsingTekAwal.hasMoreTokens()) {
                //
                //anu += parsingRak.nextToken();
                prefManager.saveString("spj_ubahBerangkat_"+count+"_tekAwal", parsingTekAwal.nextToken());
                count++;
            }

            StringTokenizer parsingTempAwal = new StringTokenizer(prefManager.catchString("spj_ubahBerangkat_tekAwal"), "|");
            if (prefManager.catchString("spj_ubahBerangkat_jenisModa").equals("APV")) count = 1;
            else if (prefManager.catchString("spj_ubahBerangkat_jenisModa").equals("HYAPCRANE")) count = 9;
            else count = 12;
            while (parsingTempAwal.hasMoreTokens()) {
                //
                //anu += parsingRak.nextToken();
                prefManager.saveString("spj_ubahBerangkat_"+count+"_tempAwal", parsingTempAwal.nextToken());
                count++;
            }
            nomorRak.setText(prefManager.catchString("spj_ubahBerangkat_1_nomorRak"));
            tekAwal.setText(prefManager.catchString("spj_ubahBerangkat_1_tekAwal"));
            tempAwal.setText(prefManager.catchString("spj_ubahBerangkat_1_tempAwal"));

            txtTime.setText(String.format("%.8s", prefManager.catchString("spj_ubahBerangkat_jamBerangkat")));
            bKmAwal.setText(prefManager.catchString("spj_ubahBerangkat_kmAwal"));
            bKeterangan.setText(prefManager.catchString("spj_ubahBerangkat_keterangan"));

            if (prefManager.catchString("spj_ubahBerangkat_jenis_moda").equals("APV")) {
                RadioButton moda = view.findViewById(R.id.act_choice_apv);
                moda.setChecked(true);
            } else if (prefManager.catchString("spj_ubahBerangkat_jenis_moda").equals("HYAPCRANE")) {
                RadioButton moda = view.findViewById(R.id.act_choice_crane);
                moda.setChecked(true);
            } else {
                RadioButton moda = view.findViewById(R.id.act_choice_trailer);
                moda.setChecked(true);
            }

            fungsiAwal();

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
                    String query = "select * from tp.spj_berangkat where trip ='" + prefManager.catchString("spj_tiba_trip") + "'";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        prefManager.saveString("spj_ubahBerangkat_trip", rs.getString("trip"));
                        prefManager.saveString("spj_ubahBerangkat_driver", rs.getString("driver"));
                        prefManager.saveString("spj_ubahBerangkat_adriver", rs.getString("asisten_driver"));
                        prefManager.saveString("spj_ubahBerangkat_ht", rs.getString("headtruck"));
                        prefManager.saveString("spj_ubahBerangkat_nopol", rs.getString("nomor_polisi"));
                        prefManager.saveString("spj_ubahBerangkat_tujuan", rs.getString("tujuan"));
                        prefManager.saveString("spj_ubahBerangkat_plant_kembali", rs.getString("plant_kembali"));
                        prefManager.saveString("spj_ubahBerangkat_tanggal_antar", rs.getString("tanggal_antar"));
                        prefManager.saveString("spj_ubahBerangkat_jenis_moda", rs.getString("jenis_moda"));

                        //trailer
                        prefManager.saveString("spj_ubahBerangkat_jenisModa", rs.getString("jenis_moda"));
                        prefManager.saveString("spj_ubahBerangkat_nomorRak", rs.getString("nomor_rak"));
                        prefManager.saveString("spj_ubahBerangkat_tekAwal", rs.getString("tekanan_awal"));
                        prefManager.saveString("spj_ubahBerangkat_tempAwal", rs.getString("temp_awal"));

                        prefManager.saveString("spj_ubahBerangkat_jamBerangkat", rs.getString("jam_berangkat"));
                        prefManager.saveString("spj_ubahBerangkat_kmAwal", rs.getString("km_awal"));
                        prefManager.saveString("spj_ubahBerangkat_keterangan", rs.getString("keterangan"));
                    }

                    z= "berhasil diload";

                }
            } catch (Exception ex) {
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();

            }
            return z;
        }
    }

    private class KirimSpjUbahBerangkat extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;
        String query;
        PreparedStatement preparedStatement;
        ResultSet rs;
        String rakBaru = "";
        String tekAwalBaru = "";
        String tempAwalBaru = "";
        String moda = "";

        @Override
        protected void onPreExecute()
        {
//                progress.setTitle("Loading");
//                progress.setMessage("Harap tunggu");
//                progress.setCanceledOnTouchOutside(false);
//                progress.show();
            progressBar.setVisibility(View.VISIBLE);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
            String date_time = simpleDateFormat.format(calendar.getTime());

            prefManager.saveString("tanggal_kirim_spj_ubahBerangkat", date_time);

            //////////////////////////////////////////////////
            short temp;
            if (signRakApv >= 1 && signRakApv <= 8) {
                temp = 1;
                while (temp != 9) {
                    rakBaru += prefManager.catchString("spj_ubahBerangkat_" + temp + "_nomorRak") + " | ";
                    tekAwalBaru += prefManager.catchString("spj_ubahBerangkat_" + temp + "_tekAwal") + " | ";
                    tempAwalBaru += prefManager.catchString("spj_ubahBerangkat_" + temp + "_tempAwal") + " | ";

                    temp++;
                }
                moda = "APV";

            } else if (signRakCrane >= 9 && signRakCrane <= 11) {
                temp = 9;
                while (temp != 12) {
                    rakBaru += prefManager.catchString("spj_ubahBerangkat_" + temp + "_nomorRak") + " | ";
                    tekAwalBaru += prefManager.catchString("spj_ubahBerangkat_" + temp + "_tekAwal") + " | ";
                    tempAwalBaru += prefManager.catchString("spj_ubahBerangkat_" + temp + "_tempAwal") + " | ";

                    temp++;
                }
                moda = "HYAPCRANE";
            } else {
                temp = 12;
                rakBaru += prefManager.catchString("spj_ubahBerangkat_" + temp + "_nomorRak") + " | ";
                tekAwalBaru += prefManager.catchString("spj_ubahBerangkat_" + temp + "_tekAwal") + " | ";
                tempAwalBaru += prefManager.catchString("spj_ubahBerangkat_" + temp + "_tempAwal") + " | ";
                moda = "TRAILER";
            }

        }

        @Override
        protected void onPostExecute(String r)
        {
            //progress.dismiss();
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
            prefManager.saveString("spj_ubahBerangkat_trip", bTrip.getText().toString());
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
                    //String query = "select top 1 * from tp.kalorigas order by id_kalorigas desc";
                    query = "select id_head_truck from tp.master_head_truck where nama_head_truck ='" +
                            bHeadTruck.getText() + "'";
                    preparedStatement = con.prepareStatement(query);
                    rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        prefManager.saveString("spj_ubahBerangkat_id_headtruck", rs.getString("id_head_truck"));
                    }

                    query = "select id_driver_asisten from tp.master_driver_asisten where nama_asisten ='" +
                            bAsistDriver.getText() + "'";
                    preparedStatement = con.prepareStatement(query);
                    rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        prefManager.saveString("spj_ubahBerangkat_id_asisten", rs.getString("id_driver_asisten"));
                    }

                    File file = new File("/storage/emulated/0/SPJ/spj.pdf");
                    FileInputStream input = new FileInputStream(file);
                    int len = (int) file.length();
//                    trip, id_driver_asisten,driver,asisten_driver," +
//                    "id_headtruck,headtruck,nomor_polisi,tujuan,plant_kembali,tanggal_antar,jenis_moda,nomor_rak," +
//                            "tekanan_awal,temp_awal,jam_berangkat,km_awal,keterangan, pdf_upload

                    query = "update tp.spj_berangkat set id_driver_asisten =" + prefManager.catchString("spj_ubahBerangkat_id_asisten") + ", " +
                            "driver = '" + bDriver.getText() + "', " +
                            "asisten_driver ='" + bAsistDriver.getText() + "', " +
                            "id_headtruck =" + prefManager.catchString("spj_ubahBerangkat_id_headtruck") + ", " +
                            "headtruck ='" + bHeadTruck.getText() + "', " +
                            "nomor_polisi ='" + bNoPol.getText() + "', " +
                            "tujuan ='" + bTujuan.getText() + "', " +
                            "plant_kembali ='" + bPlantKembali.getText() + "', " +
                            "tanggal_antar =" + "convert (datetime, '" + txtDate.getText() + "', 103), " +
                            "jenis_moda ='" + moda + "', " +
                            "nomor_rak ='" + rakBaru + "', " +
                            "tekanan_awal ='" + tekAwalBaru + "', " +
                            "temp_awal ='" + tempAwalBaru + "', " +
                            "jam_berangkat = convert(time, convert(datetime, '10/02/1998 " + txtTime.getText() + "', 103), 108), " +
                            "km_awal =" + bKmAwal.getText() + ", " +
                            "keterangan ='" + bKeterangan.getText() + "' " +
                            "where trip ='1904-209'";

                    preparedStatement = con.prepareStatement(query);
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

    private class LoadNopol extends AsyncTask<String,String,String>
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

        }

        @Override
        protected void onPostExecute(String r)
        {
            progress.dismiss();
            Toast.makeText(getActivity(), r, Toast.LENGTH_SHORT).show();
            bNoPol.setText(prefManager.catchString("spj_ubahBerangkat_nopol"));
            bTujuan.requestFocus();
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
                    String query = "select no_pol from tp.master_head_truck where nama_head_truck ='" +
                            bHeadTruck.getText() + "' ";

                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        prefManager.saveString("spj_ubahBerangkat_nopol", rs.getString("no_pol"));
                    }




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
}