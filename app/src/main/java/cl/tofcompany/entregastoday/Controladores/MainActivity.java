package cl.tofcompany.entregastoday.Controladores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Pattern;

import cl.tofcompany.entregastoday.Controladores.Home.ServicioEnvio;
import cl.tofcompany.entregastoday.Controladores.registrar.RegistrarActivity;
import cl.tofcompany.entregastoday.Provider.AuthProvider;
import cl.tofcompany.entregastoday.R;

public class MainActivity extends AppCompatActivity {

    private TextView iralregistro;
    private EditText memail;
    private TextInputEditText mpassword;
    private TextView molvidascontraseña;
    private Button mbtn_login;
    private ProgressDialog mDialog;
    private AuthProvider mAuthProvider;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();

    }

    private void Init() {
        iralregistro = findViewById(R.id.crearcuenta);
        memail = findViewById(R.id.email);
        mpassword = findViewById(R.id.password1);
        molvidascontraseña = findViewById(R.id.olvidascontraseña);
        mbtn_login = findViewById(R.id.login);
        mDialog = new ProgressDialog(this);
        mAuthProvider = new AuthProvider();
        fAuth = FirebaseAuth.getInstance();
        iralregistro.setOnClickListener(v -> {
            Intent intent = new Intent( MainActivity.this, RegistrarActivity.class);
            startActivity(intent);
        });
        mbtn_login.setOnClickListener(v -> Login());
    }



    private void Login() {
        String correo = memail.getText().toString();
        final String password = mpassword.getText().toString();

        if (TextUtils.isEmpty(correo)) {
            memail.setError("Requerido");
            memail.setHintTextColor(ContextCompat.getColor(this,R.color.design_default_color_error));
            return;
        }
        if (!validaremail(correo)) {
            memail.setError("Invalido");
            memail.setHintTextColor(ContextCompat.getColor(this,R.color.design_default_color_error));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mpassword.setError("Requerido");
            mpassword.setHintTextColor(ContextCompat.getColor(this,R.color.design_default_color_error));
            return;
        }
        if (password.length() < 6) {
            mpassword.setError("Tiene que tener 6 o mas");
            mpassword.setHintTextColor(ContextCompat.getColor(this,R.color.design_default_color_error));
        }else {
            mDialog.setMessage("Cargando...");
            mDialog.show();
            mDialog.setCanceledOnTouchOutside(false);
            mAuthProvider.login(correo, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (Objects.requireNonNull(fAuth.getCurrentUser()).isEmailVerified()) {
                        Intent intent = new Intent(MainActivity.this, ServicioEnvio.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(MainActivity.this, "Cuenta no verificada ", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Email o contraseña incorrecto", Toast.LENGTH_LONG).show();
                    mDialog.dismiss();
                }
            });


        }

    }

    //Validacion Patterns para el campo email
    private boolean validaremail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


}