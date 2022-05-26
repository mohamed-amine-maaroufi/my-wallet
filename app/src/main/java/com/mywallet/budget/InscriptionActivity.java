package com.mywallet.budget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InscriptionActivity extends Activity {
    TextView txt_head;
    EditText edtname, edtmail, edtpassword;
    Button btn_Rg;
    AlertDialog.Builder builder;
    String strname, stremail, strpass;
    DatabaseHelper myDbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inscription);

        myDbase = new DatabaseHelper(InscriptionActivity.this);

        txt_head = findViewById(R.id.txthome_register);

        edtpassword = findViewById(R.id.edtpassword);
        edtmail = findViewById(R.id.edtemail);
        edtname = findViewById(R.id.edtnom);
        btn_Rg =  findViewById(R.id.btn_register);

        builder = new AlertDialog.Builder(InscriptionActivity.this);

        btn_Rg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InsertIntoSQLite();
            }
        });
    }

    private void InsertIntoSQLite() {

        strname = edtname.getText().toString();
        stremail = edtmail.getText().toString();
        strpass = edtpassword.getText().toString();

        Log.d("TAG", strname + " / " + stremail + " / " +  strpass);

        if (strname.equals("") || stremail.equals("") || strpass.equals("") ) {
            builder.setTitle("ERREUR");
            builder.setMessage("Merci de remplir toutes les champs...");
            displayAlert("input_error0");
        } else if (!stremail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            builder.setTitle("ERREUR");
            builder.setMessage("Email invalide ...");
            displayAlert("input_error_email");

        } else {

            if(myDbase.insertuserData(strname, stremail, strpass)){
                Intent accountsIntent = new Intent(InscriptionActivity.this, LoginActivity.class);
                emptyInputEditText();
                startActivity(accountsIntent);
            }

        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        edtmail.setText(null);
        edtpassword.setText(null);
        edtname.setText(null);
    }

    public void displayAlert(final String code) {
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (code.equals("input_error0")) {


                } else if (code.equals("reg_success")) {
                    finish();
                    startActivity(new Intent(InscriptionActivity.this,LoginActivity.class));

                } else if (code.equals("reg_failed")) {
                    edtname.setText("");
                    edtmail.setText("");
                    edtpassword.setText("");

                } else if (code.equals("input_error_email")) {

                    edtmail.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
