package com.ne.project.astrosat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hendrix.pdfmyxml.PdfDocument;
import com.hendrix.pdfmyxml.viewRenderer.AbstractViewRenderer;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Documented;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.os.Build.*;
import static java.util.Calendar.MINUTE;

public class MainActivity extends Activity {
    //String ServerURL = "https://astrosat.000webhostapp.com/insert.php";
    public static final String ROOT_URL = "https://astrosat.000webhostapp.com/insert.php";
    File pdfFile;
    InputStream is = null;
    int code;
    LinearLayout ll_calc_desc, ll_calc_desc1, ll_calc_desc2, ll_calc_desc3, ll_calc_desc4, ll_calc_desc5;
    String str_target_name1, str_target_name2, str_target_name3, str_target_name4, str_target_name5;
    String str_RA1, str_RA2, str_RA3, str_RA4, str_RA5, str_Dec1, str_Dec2, str_Dec3, str_Dec4, str_Dec5;
    EditText et_targetName, et_targetName1, et_targetName2, et_targetName3, et_targetName4, et_targetName5;
    EditText et_RA, et_RA1, et_RA2, et_RA3, et_RA4, et_RA5;
    EditText et_DEC, et_DEC1, et_DEC2, et_DEC3, et_DEC4, et_DEC5;
    float daysBetween;
    String dateFormat = "yyyy/MM/dd";
    String TAG = "Permsission : ", startDate, endDate, startTime, endTime;
    EditText et_Dec, et_second, Et_targetName2;
    ImageView ivstart_dateCal, ivend_dateCal, ivstartTime, ivendTime;
    TextView tvstartDate, tvendDate, tvstartTime, tvEndTime, tvSetting;
    Button generate, saveSettings;

    RadioButton rb_no, rb_yes;
    RadioGroup radioGroup;
    Context context = this;
    String targetName, rA, dec, second, rbStatus, tname2;
    int i = 0, j = 5;
    ArrayList targetList = new ArrayList();
    LinearLayout linearLayout;
    Calendar c1 = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rb_no = findViewById(R.id.rb_no);
        rb_yes = findViewById(R.id.rb_yes);
        generate = findViewById(R.id.btn_generate);
        ivstart_dateCal = findViewById(R.id.iv_report_startDate_calender);
        ivend_dateCal = findViewById(R.id.iv_report_endDate_calender);
        tvstartDate = findViewById(R.id.tv_report_startDate);
        tvendDate = findViewById(R.id.tv_report_endDate);
        ivstartTime = findViewById(R.id.iv_report_startTime);
        ivendTime = findViewById(R.id.iv_report_endTime);
        tvSetting = findViewById(R.id.tv_settings);
        linearLayout = findViewById(R.id.ll_calc_desc);
        tvstartTime = findViewById(R.id.tv_report_startTime);
        tvEndTime = findViewById(R.id.tv_report_endTime);
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        tvstartDate.setText("01/01/2018");
        tvendDate.setText(dateString);
        Calendar mcurrentDate = Calendar.getInstance();
        final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
        final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
        final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(MINUTE);
        final int second = mcurrentTime.get(Calendar.SECOND);
        tvEndTime.setText(hour + ":" + minute + ":" + second);
        findAllIds();
        getCurrentTime();
        iv_report_startTime();
        iv_report_endTime();
        btn_reset();
        //    btn_generate();
        startDate();
        endDate();
        tvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingPopupBox();
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();


            }
        });

    }

    public void findAllIds() {


        ll_calc_desc = (LinearLayout) findViewById(R.id.ll_calc_desc);
        ll_calc_desc1 = (LinearLayout) findViewById(R.id.ll_calc_desc1);
        ll_calc_desc2 = (LinearLayout) findViewById(R.id.ll_calc_desc2);
        ll_calc_desc3 = (LinearLayout) findViewById(R.id.ll_calc_desc3);
        ll_calc_desc4 = (LinearLayout) findViewById(R.id.ll_calc_desc4);
        ll_calc_desc5 = (LinearLayout) findViewById(R.id.ll_calc_desc5);


        et_targetName = (EditText) findViewById(R.id.et_targetName);
        et_targetName1 = (EditText) findViewById(R.id.et_targetName1);
        et_targetName2 = (EditText) findViewById(R.id.et_targetName2);
        et_targetName3 = (EditText) findViewById(R.id.et_targetName3);
        et_targetName4 = (EditText) findViewById(R.id.et_targetName4);
        et_targetName5 = (EditText) findViewById(R.id.et_targetName5);


        et_RA = (EditText) findViewById(R.id.et_RA);
        et_RA1 = (EditText) findViewById(R.id.et_RA1);
        et_RA2 = (EditText) findViewById(R.id.et_RA2);
        et_RA3 = (EditText) findViewById(R.id.et_RA3);
        et_RA4 = (EditText) findViewById(R.id.et_RA4);
        et_RA5 = (EditText) findViewById(R.id.et_RA5);

        et_DEC = (EditText) findViewById(R.id.et_DEC);
        et_DEC1 = (EditText) findViewById(R.id.et_DEC1);
        et_DEC2 = (EditText) findViewById(R.id.et_DEC2);
        et_DEC3 = (EditText) findViewById(R.id.et_DEC3);
        et_DEC4 = (EditText) findViewById(R.id.et_DEC4);
        et_DEC5 = (EditText) findViewById(R.id.et_DEC5);

    }

    private void settingPopupBox() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.settings);
        dialog.setCancelable(true);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.settings, null, false);
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(view);
        rb_no = view.findViewById(R.id.rb_no);
        rb_yes = view.findViewById(R.id.rb_yes);
        radioGroup = view.findViewById(R.id.radioGroup);
        et_second = view.findViewById(R.id.et_secInterval);
        saveSettings = view.findViewById(R.id.btn_saveSettings);
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_no.isChecked()) {
                    rbStatus = "no";
                } else {
                    rbStatus = "yes";
                }

                second = et_second.getText().toString();
                dialog.dismiss();
            }
        });
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();

        /*  rb_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                rb_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rb_no.setChecked(false);
                    }
                });*/
    }

    private void endDate() {
        ivend_dateCal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // To show current date in the datepicker
                final Calendar mcurrentDate = Calendar.getInstance();
                final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
                final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
                final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_MONTH)};

                DatePickerDialog mDatePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "yyyy/MM/dd"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                        //iv_calender.setText(sdf.format(myCalendar.getTime()));
                        tvendDate.setText(sdf.format(myCalendar.getTime()));
                        endDate = sdf.format(myCalendar.getTime());
                        c2.set(selectedyear, selectedmonth, selectedday);
                        mDay[0] = selectedday;
                        mMonth[0] = selectedmonth;
                        mYear[0] = selectedyear;
                    }
                }, mYear[0], mMonth[0], mDay[0]);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });
    }

    public void iv_report_startTime() {
        ivstartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(MINUTE);
                final int second = mcurrentTime.get(Calendar.SECOND);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvstartTime.setText(selectedHour + " : " + selectedMinute + " : " + second);
                        startTime = selectedHour + "/" + selectedMinute + "/" + second;

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    public void iv_report_endTime() {
        ivendTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(MINUTE);
                final int second = mcurrentTime.get(Calendar.SECOND);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        tvEndTime.setText(selectedHour + " : " + selectedMinute + " : " + second);
                        endTime = selectedHour + "" + selectedMinute + "" + second;
                    }
                }, hour, minute, true);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

    }

    public void btn_reset() {

    }

    public void onAddField(View view) {
        i++;
        if (i == 1) {
            ll_calc_desc1.setVisibility(View.VISIBLE);
            j = 1;
        } else if (i == 2) {
            ll_calc_desc2.setVisibility(View.VISIBLE);
            j = 2;
        } else if (i == 3) {
            ll_calc_desc3.setVisibility(View.VISIBLE);
            j = 3;
        } else if (i == 4) {
            ll_calc_desc4.setVisibility(View.VISIBLE);
            j = 4;
        } else if (i == 5) {
            ll_calc_desc5.setVisibility(View.VISIBLE);
            j = 5;
        }


        /*
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.add_new_field, null);
        Et_targetName2 = rowView.findViewById(R.id.et_targetName_field);
        tname2 = Et_targetName2.getText().toString();
        Toast.makeText(this, "" + tname2, Toast.LENGTH_SHORT).show();
        //targetList.add(Et_targetName2.getText().toString());
        linearLayout.addView(rowView, linearLayout.getChildCount());
        */
    }

    public void onDelete(View view) {
        j--;
        if (j == 5) {
            ll_calc_desc5.setVisibility(View.GONE);
            i = 0;
        } else if (j == 4) {
            ll_calc_desc4.setVisibility(View.GONE);
            i = 0;

        } else if (j == 3) {
            ll_calc_desc3.setVisibility(View.GONE);
            i = 0;
        } else if (j == 2) {
            ll_calc_desc2.setVisibility(View.GONE);
            i = 0;
        } else if (j == 1) {
            ll_calc_desc1.setVisibility(View.GONE);
            i = 0;
        } else if (j == 1) {
            ll_calc_desc1.setVisibility(View.GONE);
            i = 0;
        } else if (j == 0) {
            ll_calc_desc5.setVisibility(View.GONE);
            i = 0;
        } else if (j == 5) {
            ll_calc_desc1.setVisibility(View.GONE);
            i = 0;
        }
        //    linearLayout.removeView((View) view.getParent());
    }

    public void btn_generate(View view) {

        // String target_name=et_targetName.getText().toString().trim();
        if (et_targetName.getText().toString().trim().equals(" ")) {
            et_targetName.setError("Required Field");

        }

        save();

    }

    private void getCurrentTime() {
    }

    private void startDate() {
        ivstart_dateCal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // To show current date in the datepicker
                Calendar mcurrentDate = Calendar.getInstance();
                final int[] mYear = {mcurrentDate.get(Calendar.YEAR)};
                final int[] mMonth = {mcurrentDate.get(Calendar.MONTH)};
                final int[] mDay = {mcurrentDate.get(Calendar.DAY_OF_YEAR)};

                DatePickerDialog mDatePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        Calendar myCalendar = Calendar.getInstance();
                        myCalendar.set(Calendar.YEAR, selectedyear);
                        myCalendar.set(Calendar.MONTH, selectedmonth);
                        myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                        String myFormat = "YYYY/MM/dd"; //Change as you need
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                        //iv_calender.setText(sdf.format(myCalendar.getTime()));
                        tvstartDate.setText(sdf.format(myCalendar.getTime()));
                        startDate = sdf.format(myCalendar.getTime());
                        mDay[0] = selectedday;
                        mMonth[0] = selectedmonth;
                        mYear[0] = selectedyear;
                        c1.set(selectedyear, selectedmonth, selectedday);
                    }
                }, mYear[0], mMonth[0], mDay[0]);
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();

            }
        });
    }

    private boolean save()
    {


        AbstractViewRenderer page = new AbstractViewRenderer(context, R.layout.graph_view) {
            private String _text;

            public void setText(String text) {
                _text = text;
            }

            @Override
            protected void initView(View view)
            {

                GraphView graph = (GraphView) view.findViewById(R.id.report_graph);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, 1),
                        new DataPoint(1, 5),
                        new DataPoint(2, 3)
                });
                graph.addSeries(series);

            }
        };
        PdfDocument doc = new PdfDocument(this);

// add as many pages as you have
        doc.addPage(page);
        doc.setRenderWidth(2115);
        doc.setRenderHeight(1500);
        doc.setOrientation(PdfDocument.A4_MODE.LANDSCAPE);
        doc.setProgressTitle(R.string.reportprogressTitle);
        doc.setProgressMessage(R.string.reportProgressText);
        doc.setFileName("test");
        doc.setSaveDirectory(this.getExternalFilesDir(null));
        doc.setInflateOnMainThread(false);
        doc.setListener(new PdfDocument.Callback() {
            @Override
            public void onComplete(File file) {
                Toast.makeText(MainActivity.this, "pdf2 generated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();

            }
        });

        doc.createPdf(this);
// you can reuse the bitmap if you want
        page.setReuseBitmap(true);

        String TAG = "Permsission : ";
        if (VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                File root_text = Environment.getExternalStorageDirectory();
                try {
                    File folder = new File(Environment.getExternalStorageDirectory() + "/Astrosat");
                    boolean success = true;

                    if (!folder.exists()) {
                        success = folder.mkdir();
                    }
                    BufferedWriter fwv = new BufferedWriter(new FileWriter(new File("/sdcard/Astrosat/Source.txt"), true));
                    if (root_text.canWrite()) {
                        String space = "\t";
                        str_target_name1 = et_targetName1.getText().toString();
                        str_target_name2 = et_targetName2.getText().toString();
                        str_target_name3 = et_targetName3.getText().toString();
                        str_target_name4 = et_targetName4.getText().toString();
                        str_target_name5 = et_targetName5.getText().toString();
                        str_RA1 = et_RA1.getText().toString();
                        str_RA2 = et_RA2.getText().toString();
                        str_RA3 = et_RA3.getText().toString();
                        str_RA4 = et_RA4.getText().toString();
                        str_RA5 = et_RA5.getText().toString();
                        str_Dec1 = et_DEC1.getText().toString();
                        str_Dec2 = et_DEC2.getText().toString();
                        str_Dec3 = et_DEC3.getText().toString();
                        str_Dec4 = et_DEC4.getText().toString();
                        str_Dec5 = et_DEC5.getText().toString();

                        fwv.write("\n" + space + et_targetName.getText().toString());
                        fwv.write(space + et_RA.getText().toString());
                        fwv.write(space + et_DEC.getText().toString());

                        if (str_target_name1 != null) {

                            String url = "/sdcard/Astrosat/" + str_target_name1.toString();
                            BufferedWriter fw1 = new BufferedWriter(new FileWriter(new File(url + "Source.txt"), true));
                            try {
                                fw1.write("\n" + space + str_target_name1.toString());
                                fw1.write(space + str_RA1);
                                fwv.write("sdfghjk");
                                fw1.write(space + str_Dec1);
                                fw1.close();
                            } catch (FileNotFoundException f) {
                                f.printStackTrace();
                                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
                            }

                        }
                        if (str_RA1 != null) {
                        }
                        if (str_Dec1 != null) {
                        }

                        if (str_target_name2 != null) {
                            String url = "/sdcard/Astrosat/" + str_target_name2.toString();
                            BufferedWriter fw2 = new BufferedWriter(new FileWriter(new File(url + "Source.txt"), true));

                            fw2.write("\n" + space + str_target_name2.toString());
                            fw2.write(space + str_RA2.toString());
                            fw2.write(space + str_Dec2.toString());
                            fw2.close();
                        }
                        if (str_RA2 != null) {
                        }
                        if (str_Dec2 != null) {
                        }

                        if (str_target_name3 != null) {
                            String url = "/sdcard/Astrosat/" + str_target_name3;
                            BufferedWriter fw3 = new BufferedWriter(new FileWriter(new File(url + "Source.txt"), true));
                            fw3.write("\n" + space + str_target_name3);
                            fw3.write(space + str_RA3);
                            fw3.write(space + str_Dec1);
                            fw3.close();
                        }
                        if (str_RA3 != null) {
                        }
                        if (str_Dec3 != null) {
                        }

                        if (str_target_name4 != null) {
                            String url = "/sdcard/Astrosat/" + str_target_name2;
                            BufferedWriter fw4 = new BufferedWriter(new FileWriter(new File(url + "Source.txt"), true));
                            fw4.write("\n" + space + str_target_name4);
                            fw4.write(space + str_RA4);
                            fw4.write(space + str_Dec4);
                            fw4.close();
                        }
                        if (str_RA4 != null) {
                        }
                        if (str_Dec4 != null) {
                        }
                        if (str_target_name5 != null) {
                            String url = "/sdcard/Astrosat/" + str_target_name2;
                            BufferedWriter fw5 = new BufferedWriter(new FileWriter(new File(url + "Source.txt"), true));
                            fw5.write("\n" + space + str_target_name5);
                            fw5.write(space + str_RA5);
                            fw5.write(space + str_Dec5);
                            fw5.close();
                        }
                        if (str_RA5 != null) {
                        }
                        if (str_Dec5 != null) {
                        }

                        fwv.close();
                    }
                    BufferedWriter fwv1 = new BufferedWriter(new FileWriter(new File("/sdcard/Astrosat/Avis3_Config.txt"), true));
                    if (root_text.canWrite()) {
                        String space = "\t";
                        fwv1.write(" \n" + space + startDate);
                        fwv1.write("\n" + space + endDate);
                        fwv1.write("\n" + space + startTime);
                        fwv1.write(" \n" + space + endTime);
                        fwv1.write("\n" + space + second);
                        fwv1.write("\n" + space + rbStatus);
                        fwv1.close();
                    }
                    BufferedWriter fwv2 = new BufferedWriter(new FileWriter(new File("/sdcard/Astrosat/Report.txt"), true));
                    if (root_text.canWrite()) {

                        long millis1 = c1.getTimeInMillis();
                        long millis2 = c2.getTimeInMillis();

                        // Calculate difference in milliseconds
                        long diff = millis1 - millis2;

                        // Calculate difference in seconds
                        long diffSeconds = diff / 1000;

                        // Calculate difference in minutes
                        long diffMinutes = diff / (60 * 1000);

                        // Calculate difference in hours
                        long diffHours = diff / (60 * 60 * 1000);

                        String space = "\t";
                        // Calculate difference in days
                        long diffDays = diff / (24 * 60 * 60 * 1000);
                        DateFormat df = DateFormat.getTimeInstance();
                        df.setTimeZone(TimeZone.getTimeZone("ut"));
                        String utTime = df.format(new Date());
                        fwv2.write("\n StartDate" + space + "" + startDate);
                        fwv2.write("\n StartUT" + space + utTime);
                        fwv2.write(" \n" + " " + "SpanDays" + space + diffDays);
                        fwv2.write("\n SecInterval" + space + second);
                        fwv2.close();
                    }


                } catch (Exception e) {
                    Log.e("MODEL", "ERROR: " + e.toString());
                }
            } else {

                Toast.makeText(MainActivity.this, "Revoke", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        //Here we will handle the http request to insert user to mysql db
        //Creating a RestAdapter
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL) //Setting the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface

        RegisterAPI api = adapter.create(RegisterAPI.class);

        //Defining the method insertuser of our interface
        api.insertUser
                (

                        //Passing the values by getting it from editTexts
                        et_targetName.getText().toString(),
                        et_targetName1.getText().toString(),
                        et_targetName2.getText().toString(),
                        et_targetName3.getText().toString(),
                        et_targetName4.getText().toString(),
                        et_targetName5.getText().toString(),
                        et_targetName4.getText().toString(),
                        et_targetName5.getText().toString(),
                        //Creating an anonymous callback
                        new Callback<Response>() {
                            @Override
                            public void success(Response result, Response response) {
                                //On success we will read the server's output using bufferedreader
                                //Creating a bufferedreader object
                                BufferedReader reader = null;

                                //An string to store output from the server
                                String output = "";

                                try {
                                    //Initializing buffered reader
                                    reader = new BufferedReader(new InputStreamReader(result.getBody().in()));

                                    //Reading the output in the string
                                    output = reader.readLine();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                //Displaying the output as a toast
                                Toast.makeText(MainActivity.this, "Generated Successfully", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                //If any error occured displaying the error as toast
                                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                );

        return true;
    }


    public void reset(View view) {
        et_targetName.setText("");
        et_RA.setText("");
        et_DEC.setText("");

        et_targetName1.setText("");
        et_RA1.setText("");
        et_DEC1.setText("");

        et_targetName2.setText("");
        et_RA2.setText("");
        et_DEC2.setText("");

        et_targetName3.setText("");
        et_RA3.setText("");
        et_DEC3.setText("");


        et_targetName4.setText("");
        et_RA4.setText("");
        et_DEC4.setText("");

        et_targetName5.setText("");
        et_RA5.setText("");
        et_DEC5.setText("");

        tvstartDate.setText("01/01/2018");
        tvendDate.setText("30/03/2019");

        tvstartTime.setText("12:00:00");
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(MINUTE);
        final int second = mcurrentTime.get(Calendar.SECOND);
        tvEndTime.setText(hour + ":" + minute + ":" + second);


    }
}

