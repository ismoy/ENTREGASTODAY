package cl.tofcompany.entregastoday.Controladores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Pattern;

import cl.tofcompany.entregastoday.Controladores.Home.ServicioEnvio;
import cl.tofcompany.entregastoday.Controladores.registrar.OlvidarContrasena;
import cl.tofcompany.entregastoday.Controladores.registrar.RegistrarActivity;
import cl.tofcompany.entregastoday.Provider.AuthProvider;
import cl.tofcompany.entregastoday.R;

public class MainActivity extends AppCompatActivity {

    private TextView iralregistro;
    private EditText memail;
    private TextInputEditText mpassword;
    private TextView molvidascontrasena;
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
        molvidascontrasena = findViewById(R.id.olvidascontrasena);
        mbtn_login = findViewById(R.id.login);
        mDialog = new ProgressDialog(this);
        mAuthProvider = new AuthProvider();
        fAuth = FirebaseAuth.getInstance();
        iralregistro.setOnClickListener(v -> {
            Intent intent = new Intent( MainActivity.this, RegistrarActivity.class);
            startActivity(intent);
        });
        mbtn_login.setOnClickListener(v -> Login());
        molvidascontrasena.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OlvidarContrasena.class);
            startActivity(intent);
        });
    }



    private void Login() {
        String correo = memail.getText().toString();
        final String password = mpassword.getText().toString();

        if (TextUtils.isEmpty(correo)) {
           customToast(MainActivity.this,"Se requiere un correo electrónico");
            memail.setHintTextColor(ContextCompat.getColor(this,R.color.design_default_color_error));
            return;
        }
        if (!validaremail(correo)) {
           customToast(MainActivity.this,"Correo electrónico inválido");
            memail.setHintTextColor(ContextCompat.getColor(this,R.color.design_default_color_error));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            customToast(MainActivity.this,"Se requiere una contraseña");
            mpassword.setHintTextColor(ContextCompat.getColor(this,R.color.design_default_color_error));
            return;
        }
        if (password.length() < 6) {
            customToast(MainActivity.this,"La contraseña debe tener al menos 6 caracteres");
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
                        mDialog.dismiss();
                        customToast(MainActivity.this,"Cuenta no verificada. Por favor revise su correo electrónico");

                    }
                } else {
                    mDialog.dismiss();
                    customToast(MainActivity.this,"Problema al iniciar sesión, verifique su correo electrónico y contraseña");
                }
            });


        }

    }

    //Validacion Patterns para el campo email
    private boolean validaremail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void customToast(Context context, String mensaje){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.layoutcustomtoast));
        TextView txtmensaje = view.findViewById(R.id.mensajeerror);
        txtmensaje.setText(mensaje);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.NO_GRAVITY,0,400);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

}