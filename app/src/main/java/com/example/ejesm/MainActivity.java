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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button IniciarBTN , registroBTN;
    EditText ET_CorreoELectronico ,ET_ContraseñaLogin;


    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IniciarBTN = findViewById(R.id.IniciarBTN);
        registroBTN = findViewById(R.id.registroBTN);
        ET_CorreoELectronico = findViewById(R.id.ET_CorreoELectronico);
        ET_ContraseñaLogin = findViewById(R.id.ET_ContraseñaLogin);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(MainActivity.this);
        dialog = new Dialog(MainActivity.this);




        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle("Login");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);





        IniciarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String correo = ET_CorreoELectronico.getText().toString();
                String contraseña = ET_ContraseñaLogin.getText().toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                    ET_CorreoELectronico.setError("Correo invalido");
                    ET_CorreoELectronico.setFocusable(true);
                }
                else if (contraseña.length()<6){
                    ET_ContraseñaLogin.setError("La contraseña debe se mayor o igual a 6");
                    ET_ContraseñaLogin.setFocusable(true);
                }
                else
                {
                 LOGINUSUARIO(correo,contraseña);
                }


            }
        });




        registroBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,
                        Registro.class));
            }
        });



    }

    private void LOGINUSUARIO(String correo, String contraseña) {
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                            assert user != null;
                            Toast.makeText(MainActivity.this, "Hola Bienvenido(a)"+user.getEmail(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progressDialog.dismiss();
                            Dialogo_NO_Iniciado();
                            //Toast.makeText(MainActivity.this, "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                
            }
        });

    }


    private void Dialogo_NO_Iniciado(){

        Button Ok_no_inicio;
        dialog.setContentView(R.layout.no_sesion);
        Ok_no_inicio = dialog.findViewById(R.id.Ok_no_inicio_id);

        Ok_no_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }



}