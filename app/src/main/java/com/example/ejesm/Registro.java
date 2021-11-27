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

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    Button CancelarBTN ,RegistarBTN;
    EditText ET_Nombre,ET_Correo,ET_Contraseña,ET_Contraseña_confirmada;

    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;
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
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.ET_Correo, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this, R.id.ET_Nombre, ".{6,}", R.string.invalid_name);
        awesomeValidation.addValidation(this, R.id.ET_Contraseña, ".{6,}", R.string.invalid_password);


        RegistarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail = ET_Correo.getText().toString();
                String pass = ET_Contraseña.getText().toString();
                String name = ET_Nombre.getText().toString();

                if (awesomeValidation.validate()) {
                    firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Registro.this, "Usuario creado con exito", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                dameToastdeerror(errorCode);
                            }
                        }
                    });
                } else {
                    Toast.makeText(Registro.this, "Completa todos los datos..!!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

        private void dameToastdeerror(String error) {

            switch (error) {

                case "ERROR_INVALID_CUSTOM_TOKEN":
                    Toast.makeText(Registro.this, "El formato del token personalizado es incorrecto. Por favor revise la documentación", Toast.LENGTH_LONG).show();
                    break;

                case "ERROR_CUSTOM_TOKEN_MISMATCH":
                    Toast.makeText(Registro.this, "El token personalizado corresponde a una audiencia diferente.", Toast.LENGTH_LONG).show();
                    break;

                case "ERROR_INVALID_CREDENTIAL":
                    Toast.makeText(Registro.this, "La credencial de autenticación proporcionada tiene un formato incorrecto o ha caducado.", Toast.LENGTH_LONG).show();
                    break;

                case "ERROR_INVALID_EMAIL":
                    Toast.makeText(Registro.this, "La dirección de correo electrónico está mal formateada.", Toast.LENGTH_LONG).show();
                    ET_Correo.setError("La dirección de correo electrónico está mal formateada.");
                    ET_Correo.requestFocus();
                    break;

                case "ERROR_WRONG_PASSWORD":
                    Toast.makeText(Registro.this, "La contraseña no es válida o el usuario no tiene contraseña.", Toast.LENGTH_LONG).show();
                    ET_Contraseña.setError("la contraseña es incorrecta ");
                    ET_Contraseña.requestFocus();
                    ET_Contraseña.setText("");
                    break;

                case "ERROR_USER_MISMATCH":
                    Toast.makeText(Registro.this, "Las credenciales proporcionadas no corresponden al usuario que inició sesión anteriormente..", Toast.LENGTH_LONG).show();
                    break;

                case "ERROR_REQUIRES_RECENT_LOGIN":
                    Toast.makeText(Registro.this,"Esta operación es sensible y requiere autenticación reciente. Inicie sesión nuevamente antes de volver a intentar esta solicitud.", Toast.LENGTH_LONG).show();
                    break;

                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                    Toast.makeText(Registro.this, "Ya existe una cuenta con la misma dirección de correo electrónico pero diferentes credenciales de inicio de sesión. Inicie sesión con un proveedor asociado a esta dirección de correo electrónico.", Toast.LENGTH_LONG).show();
                    break;

                case "ERROR_EMAIL_ALREADY_IN_USE":
                    Toast.makeText(Registro.this, "La dirección de correo electrónico ya está siendo utilizada por otra cuenta..   ", Toast.LENGTH_LONG).show();
                    ET_Correo.setError("La dirección de correo electrónico ya está siendo utilizada por otra cuenta.");
                    ET_Correo.requestFocus();
                    break;

                case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                    Toast.makeText(Registro.this, "Esta credencial ya está asociada con una cuenta de usuario diferente.", Toast.LENGTH_LONG).show();
                    break;

                case "ERROR_USER_DISABLED":
                    Toast.makeText(Registro.this, "La cuenta de usuario ha sido inhabilitada por un administrador..", Toast.LENGTH_LONG).show();
                    break;

                case "ERROR_USER_TOKEN_EXPIRED":
                    Toast.makeText(Registro.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                    break;

                case "ERROR_USER_NOT_FOUND":
                    Toast.makeText(Registro.this, "No hay ningún registro de usuario que corresponda a este identificador. Es posible que se haya eliminado al usuario.", Toast.LENGTH_LONG).show();
                    break;

                case "ERROR_INVALID_USER_TOKEN":
                    Toast.makeText(Registro.this, "La credencial del usuario ya no es válida. El usuario debe iniciar sesión nuevamente.", Toast.LENGTH_LONG).show();
                    break;

                case "ERROR_OPERATION_NOT_ALLOWED":
                    Toast.makeText(Registro.this, "Esta operación no está permitida. Debes habilitar este servicio en la consola.", Toast.LENGTH_LONG).show();
                    break;

                case "ERROR_WEAK_PASSWORD":
                    Toast.makeText(Registro.this, "La contraseña proporcionada no es válida..", Toast.LENGTH_LONG).show();
                    ET_Contraseña.setError("La contraseña no es válida, debe tener al menos 6 caracteres");
                    ET_Contraseña.requestFocus();
                    break;

            }

        }



}

