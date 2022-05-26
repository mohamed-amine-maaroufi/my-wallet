package com.mywallet.budget;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
    TextView sign_up_btn;
    Button lg_btn;
    EditText edtmail, edtpass;
    String strusername, strpass;
    AlertDialog.Builder builder;
    DatabaseHelper myDbase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        myDbase = new DatabaseHelper(this);

        lg_btn = findViewById(R.id.lgbtn);
        edtmail = findViewById(R.id.edtmail);
        edtpass = findViewById(R.id.edtpass);
        sign_up_btn = findViewById(R.id.sign_up_btn);

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, InscriptionActivity.class);
                startActivity(i);
            }
        });

        lg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyFromSQLite();
            }
        });
    }

    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edtmail.setText("");
                edtpass.setText("");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void verifyFromSQLite() {
        builder = new AlertDialog.Builder(LoginActivity.this);
        strusername = edtmail.getText().toString();
        strpass = edtpass.getText().toString();
        if (strusername.equals("") || strpass.equals("")) {
            builder.setTitle("Erreur");
            displayAlert("Remplir tous les champs...");
        } else {
            if (myDbase.checkUser(strusername.trim()
                    , strpass.trim())) {
                Intent accountsIntent = new Intent(LoginActivity.this, MainActivity.class);
                //accountsIntent.putExtra("EMAIL", strusername.trim());
                emptyInputEditText();
                startActivity(accountsIntent);
            }else {
                builder.setTitle("Erreur");
                displayAlert("Informations non valides...");
            }
        }


    }
    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        edtmail.setText(null);
        edtpass.setText(null);
    }
}
