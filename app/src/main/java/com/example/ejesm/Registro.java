package com.example.ejesm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    Button CancelarBTN ,RegistarBTN;
    EditText ET_Nombre,ET_Correo,ET_Contraseña,ET_Contraseña_confirmada;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        CancelarBTN = findViewById(R.id.CancelarBTN);
        RegistarBTN = findViewById(R.id.RegistarBTN);
        ET_Nombre = findViewById(R.id.ET_Nombre);
        ET_Correo = findViewById(R.id.ET_Correo);
        ET_Contraseña = findViewById(R.id.ET_Contraseña);



        firebaseAuth = firebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();
        assert  actionBar != null;
        actionBar.setTitle("Registro");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        CancelarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registro.this,MainActivity.class));
            }
        });

        RegistarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String nombre = ET_Nombre.getText().toString();
                String correo = ET_Correo.getText().toString();
                String contraseña = ET_Contraseña.getText().toString();


                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {

                    ET_Correo.setError("Correo no válido");
                    ET_Correo.setFocusable(true);
                }else if (contraseña.length()<6){
                    ET_Contraseña.setError("Contraseña debe ser mayor a 6 caracteres");
                    ET_Contraseña.setFocusable(true);
                }else{
                    Registrar(correo,contraseña);
                }


            }
        });
    }

    private void Registrar(String correo, String contraseña) {
         firebaseAuth.createUserWithEmailAndPassword(correo,contraseña)
                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             FirebaseUser user = firebaseAuth.getCurrentUser();
                             assert user != null;
                             String uid = user.getUid();
                             String correo = ET_Correo.getText().toString();
                             String contraseña = ET_Contraseña.getText().toString();
                             String nombre = ET_Nombre.getText().toString();

                             HashMap<Object, String> DatosUsuario = new HashMap<>();
                             DatosUsuario.put("uid", uid);
                             DatosUsuario.put("correo", correo);
                             DatosUsuario.put("contraseña", contraseña);

                             FirebaseDatabase database = FirebaseDatabase.getInstance();

                             DatabaseReference reference = database.getReference("AppGrupo06");

                             reference.child(uid).setValue(DatosUsuario);
                             Toast.makeText(Registro.this, "Se registró exitosamente", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(Registro.this,MainActivity.class));
                         } else {
                             Toast.makeText(Registro.this, "Algo ha salido mal", Toast.LENGTH_SHORT).show();
                         }
                     }
                 }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(Registro.this, e.getMessage(), Toast.LENGTH_SHORT).show();
             }
         });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}

