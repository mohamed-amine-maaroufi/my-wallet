package com.mywallet.budget;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class add_income extends AppCompatActivity {

    DatabaseHelper myDbincome;
    EditText editDateincome, editNotesincome;
    String category;
    String account;
    String myListPreference;
    String noteCheck;
    Calendar myCalendar;
    Double FormatedVal;
    long RawincomeValue;
    boolean isCategoryInserted;
    boolean isDateInserted;
    boolean isAmountInserted;
    boolean isAccountInserted;
    DecimalFormat formatter;
    CurrencyEditText editAmountincome;
    CurrencyEditText cet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        Toolbar toolbar =  findViewById(R.id.toolbar_income);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_add_income));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
        myListPreference = sharedPreferences.getString("CurrencyType", "DT");

        myCalendar = Calendar.getInstance();
        cet = new CurrencyEditText(this, null);
        myDbincome = new DatabaseHelper(this);

        editAmountincome =  findViewById(R.id.amount_expense);
        editDateincome=  findViewById(R.id.date_income);
        editNotesincome =  findViewById(R.id.notes_income);

        if (Objects.equals(myListPreference, "$")){
            editAmountincome.setLocale(Locale.US);
        }else if (Objects.equals(myListPreference, "DT")){
            editAmountincome.setLocale(new Locale("fr", "TN"));
        }else if (Objects.equals(myListPreference, "€")){
            editAmountincome.setLocale(Locale.FRANCE);
        }


        AddData();


        Spinner spinner;
        ArrayAdapter<CharSequence> adapter;

        spinner =  findViewById(R.id.categorydropdown_income);
        adapter = ArrayAdapter.createFromResource(this, R.array.incomecategory_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(id == 0){
                    isCategoryInserted = false;
                }
                else{
                    category = (parent.getItemAtPosition(position)).toString();
                    isCategoryInserted = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Spinner accounts;
        spinner = findViewById(R.id.accounts);
        adapter = ArrayAdapter.createFromResource(this, R.array.account, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(id == 0){
                    isAccountInserted = false;
                }
                else{
                    account = (parent.getItemAtPosition(position)).toString();
                    isAccountInserted = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        editDateincome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(add_income.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        editDateincome.setText(sdf.format(myCalendar.getTime()));
        isDateInserted = true;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(add_income.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void AddData(){
        FloatingActionButton fab2 =  findViewById(R.id.fab_tick_income);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                formatter = new DecimalFormat("0.00");
                RawincomeValue = editAmountincome.getRawValue();
                FormatedVal = (double) RawincomeValue/100;

                isAmountInserted = RawincomeValue > 0;

                noteCheck = editNotesincome.getText().toString().trim();

                if(noteCheck.isEmpty())
                {
                    noteCheck = noteCheck.replace("", "No Note Inserted");
                }
                else
                {
                    //EditText is not empty
                }

                if(isCategoryInserted && isDateInserted && isAmountInserted && isAccountInserted) {
                    myDbincome.insertincomeData(
                            FormatedVal,
                            editDateincome.getText().toString(),
                            noteCheck,
                            category,
                            account);

                    Toast.makeText(add_income.this,"Revenu ajouté",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(add_income.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(add_income.this,"Revenu non enregistré \nAssurez-vous que toutes les données ont été insérées",Toast.LENGTH_LONG).show();
            }
        });

    }
}



