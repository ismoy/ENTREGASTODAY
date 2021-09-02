package cl.tofcompany.entregastoday.Controladores.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import cl.tofcompany.entregastoday.Controladores.Cliente.Profile;
import cl.tofcompany.entregastoday.Controladores.Cliente.UpdateProfile;
import cl.tofcompany.entregastoday.Controladores.MainActivity;
import cl.tofcompany.entregastoday.Controladores.Servicios.ServicioAuto;
import cl.tofcompany.entregastoday.Controladores.Servicios.ServicioBicicleta;
import cl.tofcompany.entregastoday.Controladores.Servicios.ServicioFurgon;
import cl.tofcompany.entregastoday.Controladores.Servicios.ServicioMoto;
import cl.tofcompany.entregastoday.DetalleServicios.History;
import cl.tofcompany.entregastoday.Provider.AuthProvider;
import cl.tofcompany.entregastoday.Provider.UsuarioProvider;
import cl.tofcompany.entregastoday.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ServicioEnvio extends AppCompatActivity {
    private CardView mserviciomoto;
    private CardView mservicioauto;
    private CardView mserviciobicileta;
    private CardView mserviciofugon;
    private BottomAppBar mBottomAppBar;
    private TextView mnombrebottomsheet;
    private TextView mapellidobottomsheet;
    private TextView  mrutbottomsheet;
    private TextView  memailbottomsheet;
    private TextView  mtelefonobottomsheet;
    private CircleImageView mpicturebottomsheet;
    private UsuarioProvider mUsuarioProvider;
    private AuthProvider mAuthProvider;
    private ImageView mvolveralhome;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_envio);
        mBottomAppBar = findViewById(R.id.botomappbar);
        setSupportActionBar(mBottomAppBar);

        Init();
    }

    private void Init() {
        mserviciomoto = findViewById(R.id.serviciomoto);
        mservicioauto = findViewById(R.id.servicioauto);
        mserviciobicileta = findViewById(R.id.serviciobicicleta);
        mserviciofugon = findViewById(R.id.serviciofurgo);
        mvolveralhome = findViewById(R.id.volveralhome);
        mFloatingActionButton = findViewById(R.id.historial);
        mAuthProvider = new AuthProvider();
        mUsuarioProvider = new UsuarioProvider();
        mserviciomoto.setOnClickListener(v -> goToServicioMoto());
        mservicioauto.setOnClickListener(v -> goToServicioAuto());
        mserviciobicileta.setOnClickListener(v -> gotoServicioBicicleta());
        mserviciofugon.setOnClickListener(v -> goToServicioFurgon());
        mvolveralhome.setOnClickListener(v -> gotohome());
        mFloatingActionButton.setOnClickListener(v -> gotoHistory());


    }

    private void gotoHistory() {
        Intent intent = new Intent(ServicioEnvio.this, History.class);
        startActivity(intent);
    }

    private void gotohome() {
        recreate();
    }

    private void goToServicioFurgon() {
        Intent intent = new Intent(ServicioEnvio.this, ServicioFurgon.class);
        startActivity(intent);
    }

    private void gotoServicioBicicleta() {

        Intent intent = new Intent(ServicioEnvio.this, ServicioBicicleta.class);
        startActivity(intent);
    }

    private void goToServicioAuto() {
        Intent intent = new Intent(ServicioEnvio.this, ServicioAuto.class);
        startActivity(intent);
    }

    private void goToServicioMoto() {
        Intent intent = new Intent(ServicioEnvio.this, ServicioMoto.class);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuprofiles) {
            getInfoUsers();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void getInfoUsers() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                ServicioEnvio.this,R.style.BottomSheetDialogTheme
        );
        View bottomsheetview = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet_profile,
                        (LinearLayout)findViewById(R.id.botomsheetcontainer)
                );
        mUsuarioProvider.getUsuarios(mAuthProvider.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {

                mnombrebottomsheet = bottomsheetview.findViewById(R.id.nombrebottomsheet);
                mapellidobottomsheet = bottomsheetview.findViewById(R.id.apellidobottomsheet);
                memailbottomsheet = bottomsheetview.findViewById(R.id.emailbottomsheet);
                mrutbottomsheet = bottomsheetview.findViewById(R.id.rutbottomsheeet);
                mtelefonobottomsheet = bottomsheetview.findViewById(R.id.telefonobottomsheet);
                mpicturebottomsheet = bottomsheetview.findViewById(R.id.picturebottomsheetadmin);
                String firstname = snapshot.child("nombre").getValue().toString();
                String lastname = snapshot.child("apellido").getValue().toString();
                String email = snapshot.child("email").getValue().toString();
                String rut = snapshot.child("rut").getValue().toString();
                String telefono = snapshot.child("phone").getValue().toString();
                mnombrebottomsheet.setText(firstname);
                mapellidobottomsheet.setText(lastname);
                memailbottomsheet.setText(email);
                mrutbottomsheet.setText(rut);
                mtelefonobottomsheet.setText(telefono);
                String image = "";
                if (snapshot.hasChild("image")){
                    image = snapshot.child("image").getValue().toString();
                    Picasso.get().load(image).into(mpicturebottomsheet);
                }

                mpicturebottomsheet.setOnClickListener(v -> {
                    Intent intent = new Intent(ServicioEnvio.this, UpdateProfile.class);
                    startActivity(intent);
                });
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });

        bottomsheetview.findViewById(R.id.btnlogout).setOnClickListener(v -> {
            logout();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setContentView(bottomsheetview);
        bottomSheetDialog.show();
    }

    private void logout() {
        mAuthProvider.logout();
        Intent intent = new Intent(ServicioEnvio.this, MainActivity.class);
        startActivity(intent);
    }

}