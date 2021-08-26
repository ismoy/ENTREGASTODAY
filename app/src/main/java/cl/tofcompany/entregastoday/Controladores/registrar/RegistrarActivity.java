package cl.tofcompany.entregastoday.Controladores.registrar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Pattern;

import cl.tofcompany.entregastoday.Controladores.MainActivity;
import cl.tofcompany.entregastoday.Provider.AuthProvider;
import cl.tofcompany.entregastoday.Provider.UsuarioProvider;
import cl.tofcompany.entregastoday.R;
import cl.tofcompany.entregastoday.modelos.Usuarios;

public class RegistrarActivity extends AppCompatActivity {
    private EditText txtnombre;
    private EditText txtapellido;
    private EditText txtrut;
    private EditText txtphone;
    private EditText txtpassword;
    private EditText txtemail;
    private ProgressDialog mDialog;
    private AuthProvider mAuthProvider;
    private UsuarioProvider mUsuarioProvider;
    private FirebaseAuth fAuth;
    Button mregistrar;
    ImageView marrowbackregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        init();

    }



    private void init(){
        txtnombre = findViewById(R.id.nombre);
        txtapellido = findViewById(R.id.apellido);
        txtrut = findViewById(R.id.rut);
        txtphone = findViewById(R.id.phone);
        txtpassword = findViewById(R.id.password1);
        txtemail = findViewById(R.id.email);
        mDialog = new ProgressDialog(this);
        mAuthProvider = new AuthProvider();
        mUsuarioProvider = new UsuarioProvider();
        fAuth = FirebaseAuth.getInstance();
        mregistrar = findViewById(R.id.registrar);
        mregistrar.setOnClickListener(v -> clickRegister());
        marrowbackregister = findViewById(R.id.arrowbackregister);
        marrowbackregister.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrarActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private void clickRegister() {
        final String nombre = txtnombre.getText().toString().trim();
        final String apellido = txtapellido.getText().toString().trim();
        final String rut = txtrut.getText().toString().trim();
        final String phone = txtphone.getText().toString().trim();
        final String password = txtpassword.getText().toString().trim();
        final String email = txtemail.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)) {
            txtnombre.setError("Requerido");
            txtnombre.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            return;
        }
        if (!validarletrasynumero(nombre)) {
            txtnombre.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            txtnombre.setError("Solo letra y numero");
            txtnombre.setHint("No permite caracter especiales");
            return;
        }

        if (TextUtils.isEmpty(apellido)) {
            txtapellido.setError("Requerido");
            txtapellido.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            return;
        }

        //Validamos que solo pueden igresar letra
        if (!validarletras(apellido)) {
            txtapellido.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            txtapellido.setError("Solo letra");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            txtemail.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            txtemail.setError("requerido");
            return;
        }
        if (!validaremail(email)) {
            txtemail.setError("invalida");
            txtemail.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            return;
        }
        if (TextUtils.isEmpty(rut)) {
            txtrut.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            txtrut.setError("Requerido");
            return;
        }


        if (TextUtils.isEmpty(phone)) {
            txtphone.setError("Requerido");
            txtphone.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            return;
        }
        if (!isValidPhoneNumber(phone)) {
            txtphone.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            txtphone.setError("Ingrese un numero celular valido");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            txtpassword.setError("Requerido");
            txtpassword.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            return;
        }

        if (password.length() < 6) {
            txtpassword.setError("Ingrese un paswword seguro");
            txtpassword.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));

        }else {
            mDialog.setMessage("Cargando...");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            register(nombre, apellido,email,rut,phone,password);
        }
    }

    void register(final String nombre,String apellido,String email,String rut,final String phone,String password) {

        mAuthProvider.register(email , password).addOnCompleteListener(task -> {
            mDialog.dismiss();
            if (task.isSuccessful()) {
                String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                Usuarios usuarios = new Usuarios(id,nombre,apellido, email,rut,phone,password);
                create(usuarios);
                FirebaseUser user = fAuth.getCurrentUser();
                fAuth.setLanguageCode("es");
                assert user != null;
                user.sendEmailVerification().addOnCompleteListener(task1 -> Toast.makeText(RegistrarActivity.this , "verifica su correo" , Toast.LENGTH_LONG).show()).addOnFailureListener(e -> {

                    String TAG = "";
                    Log.d(TAG , "error" + e.getMessage());
                });


            } else {
                //mensaje en case de error
                Toast.makeText(RegistrarActivity.this , "Error" + Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_LONG).show();
            }
        });
    }
    //metodo para crear los clientes en firebase
    void create(Usuarios usuarios){
        mUsuarioProvider.create(usuarios).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Intent intent = new Intent(RegistrarActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else {
                //mensaje de error
                Toast.makeText(RegistrarActivity.this, "Error" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    //Validacion regex para los campos
    public static boolean validarletras (String datos){
        return datos.matches("[a-zA-Z ]*");
    }
    public static boolean validarletrasynumero(String datos){
        return datos.matches("[a-zA-Z-0-9 ]*");
    }

    //Validacion Patterns para el campo email
    private boolean validaremail (String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null) {
            return false;
        }else {
            if (target.length() < 11 || target.length() > 13)
            { return false;
            }else {
                return android.util.Patterns.PHONE.matcher(target).matches();
            }
        }
    }

}