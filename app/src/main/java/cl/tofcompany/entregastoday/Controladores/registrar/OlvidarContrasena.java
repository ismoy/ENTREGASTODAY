package cl.tofcompany.entregastoday.Controladores.registrar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import cl.tofcompany.entregastoday.Controladores.MainActivity;
import cl.tofcompany.entregastoday.Provider.AuthProvider;
import cl.tofcompany.entregastoday.R;

public class OlvidarContrasena extends AppCompatActivity {

    private String email;
    private EditText memail;
    private AuthProvider mAuthProvider;
    private ProgressDialog mDialog;
    FirebaseAuth mAuth;
    private Button mbtnconfirmar;
    private ImageView mvolverolvidar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvidar_contrasena);

        Init();
    }

    private void Init() {
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        memail = findViewById(R.id.emailfogot);
        mbtnconfirmar = findViewById(R.id.recuperar);
        mAuthProvider = new AuthProvider();
        mvolverolvidar = findViewById(R.id.volverolvidar);
        mbtnconfirmar.setOnClickListener(v -> {
            ActionResetPassword();
        });

        mvolverolvidar.setOnClickListener(v -> {
            Intent intent = new Intent(OlvidarContrasena.this,MainActivity.class);
            startActivity(intent);
        });

    }

    public void resetPassword() {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(OlvidarContrasena.this , "Se ha enviado un correo electrónico para restablecer su contraseña revise su bandeja de entrada" , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(OlvidarContrasena.this , "No se pudo enviar el correo electrónico para restablecer la contraseña" , Toast.LENGTH_LONG).show();
            }
            mDialog.dismiss();
        });
    }

    public void ActionResetPassword() {
        email = memail.getText().toString();
        if (!email.isEmpty()) {
            mDialog.setMessage("Cargando");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            resetPassword();
        } else {
            memail.setError("Por favor ingrese su correo electrónico");
        }

    }
}