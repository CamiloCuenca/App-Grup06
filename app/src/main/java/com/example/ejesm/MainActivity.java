package com.example.ejesm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button IniciarBTN , registroBTN;
    EditText ET_CorreoELectronico ,ET_ContraseñaLogin;
    AwesomeValidation  awesomeValidation;
    FirebaseAuth firebaseAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IniciarBTN = findViewById(R.id.IniciarBTN);
        registroBTN = findViewById(R.id.registroBTN);
        ET_CorreoELectronico = findViewById(R.id.ET_CorreoELectronico);
        ET_ContraseñaLogin = findViewById(R.id.ET_ContraseñaLogin);
        firebaseAuth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.ET_CorreoELectronico, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this, R.id.ET_Nombre, ".{6,}", R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.ET_ContraseñaLogin, ".{6,}", R.string.invalid_password);

        registroBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Registro.class);
                startActivity(i);
            }
        });

        IniciarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(awesomeValidation.validate()){

                    String mail = ET_CorreoELectronico.getText().toString();
                    String pass = ET_ContraseñaLogin.getText().toString();

                    firebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                               iraHome();
                            }else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                dameToastdeerror(errorCode);
                            }



                        }
                    });

                }

            }
        });


    }

    private void iraHome() {
        Intent i = new Intent(this,HomeActivity.class);
        i.putExtra("mail",ET_CorreoELectronico.getText().toString());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    private void dameToastdeerror(String errorCode) {
    }
}


