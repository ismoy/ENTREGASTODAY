package cl.tofcompany.entregastoday.Controladores.registrar;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Matcher;
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
        Init();

    }



    private void Init(){
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
            customToast(RegistrarActivity.this,"Por favor ingrese su Nombre");
            txtnombre.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            return;
        }
        if (!validarletras(nombre)) {
            txtnombre.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            customToast(RegistrarActivity.this,"El Nombre no debe tener números ni caracteres especiales #0-9,.*´=");
            return;
        }

        if (TextUtils.isEmpty(apellido)) {
            customToast(RegistrarActivity.this,"Por favor ingrese su Apellido");
            txtapellido.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            return;
        }

        //Validamos que solo pueden igresar letra
        if (!validarletras(apellido)) {
            txtapellido.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            customToast(RegistrarActivity.this,"El Apellido no debe tener números ni caracteres especiales #0-9,.*´=");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            txtemail.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            customToast(RegistrarActivity.this,"Por favor ingrese un correo electrónico");
            return;
        }
        if (!validaremail(email)) {
            customToast(RegistrarActivity.this,"Correo electrónico inválido");
            txtemail.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            return;
        }
        if (TextUtils.isEmpty(rut)){
            txtrut.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            customToast(RegistrarActivity.this,"Por favor ingrese su Rut");
            return;
        }
         if (!validarRut(rut)) {
             txtrut.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
             customToast(RegistrarActivity.this,"Por favor ingrese un Rut válido");
             return;
                }

        if (TextUtils.isEmpty(phone)) {
            customToast(RegistrarActivity.this,"Por favor ingrese un número de teléfono");
            txtphone.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            return;
        }
        if (!isValidPhoneNumber(phone)) {
            txtphone.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            customToast(RegistrarActivity.this,"Por favor ingrese un número de teléfono válido ");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            customToast(RegistrarActivity.this,"Por favor ingrese una contraseña");
            txtpassword.setHintTextColor(ContextCompat.getColor(this, R.color.design_default_color_error));
            return;
        }

        if (password.length() < 6) {
            customToast(RegistrarActivity.this,"La contraseña debe tener al menos 6 caracteres");
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
                user.sendEmailVerification().addOnCompleteListener(task1 -> customToastSuccess(RegistrarActivity.this , "Su cuenta ha sido creada con éxito. Revise su correo electrónico para activar la cuenta")).addOnFailureListener(e -> {

                    String TAG = "";
                    Log.d(TAG , "error" + e.getMessage());
                });


            } else {
                //mensaje en case de error
                customToast(RegistrarActivity.this,"Error" + Objects.requireNonNull(task.getException()).getMessage());
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
                customToast(RegistrarActivity.this,"Error" + Objects.requireNonNull(task.getException()).getMessage());
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

    private void customToast(Context context, String mensaje){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.layoutcustomtoast));
        TextView txtmensaje = view.findViewById(R.id.mensajeerror);
        txtmensaje.setText(mensaje);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.NO_GRAVITY,0,1000);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    private void customToastSuccess(Context context, String mensaje){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_success,(ViewGroup) findViewById(R.id.layoutcustomtoastsuccess));
        TextView txtmensaje = view.findViewById(R.id.mensajesuccess);
        txtmensaje.setText(mensaje);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.NO_GRAVITY,0,1000);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    //Validamos un formato para Rut
    public static Boolean validarRut(String rut) {
        Pattern pattern = Pattern.compile("^[0-9]+-[0-9kK]{1}$");
        Matcher matcher = pattern.matcher(rut);
        if (!matcher.matches()) return false;
        String[] stringRut = rut.split("-");
        return stringRut[1].toLowerCase().equals(RegistrarActivity.dv(stringRut[0]));
    }

    /*
     * Valida el dígito verificador
     */
    public static String dv(String rut) {
        Integer M = 0, S = 1, T = Integer.parseInt(rut);
        for (; T != 0; T = (int) Math.floor(T /= 10))
            S = (S + T % 10 * (9 - M++ % 6)) % 11;
        return (S > 0) ? String.valueOf(S - 1) : "k";
    }
}