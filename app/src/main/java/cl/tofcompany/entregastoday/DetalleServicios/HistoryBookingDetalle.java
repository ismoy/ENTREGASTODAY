package cl.tofcompany.entregastoday.DetalleServicios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import cl.tofcompany.entregastoday.Controladores.Home.ServicioEnvio;
import cl.tofcompany.entregastoday.Provider.AuthProvider;
import cl.tofcompany.entregastoday.Provider.ClientBookingProvider;
import cl.tofcompany.entregastoday.R;
import cl.tofcompany.entregastoday.modelos.ClientBooking;
import cl.tofcompany.entregastoday.modelos.HistoryBooking;
import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryBookingDetalle extends AppCompatActivity {

    private ImageView mcircle_imagebackhistorybookingdetaill;
    private CircleImageView mcircle_imagehistorybookingdetaill;
    private TextView mtextvieworigenhistorybookingdetaillclient;
    private TextView mtextviewdestinationhistorybookingdetaillclient;
    private TextView mtextviewdistaciahistorybookingdetaillclient;
    private TextView mtextvietiempo;
    private TextView mtextviewcosto;
    private ClientBookingProvider mClientBookingProvider;
    private AuthProvider mAuthProvider;
    private String mIdExtra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_booking_detalle);
        Init();
    }

    private void Init() {
        mcircle_imagebackhistorybookingdetaill= findViewById(R.id.circle_imagebackhistorybookingdetaill);
        mcircle_imagehistorybookingdetaill = findViewById(R.id.circle_imagehistorybookingdetaill);
        mtextvieworigenhistorybookingdetaillclient = findViewById(R.id.textvieworigenhistorybookingdetaillclient);
        mtextviewdestinationhistorybookingdetaillclient = findViewById(R.id.textviewdestinationhistorybookingdetaillclient);
        mtextviewdistaciahistorybookingdetaillclient = findViewById(R.id.textviewdistaciahistorybookingdetaillclient);
        mtextvietiempo = findViewById(R.id.textvietiempo);
        mtextviewcosto = findViewById(R.id.textviewcosto);
        mAuthProvider = new AuthProvider();
        mClientBookingProvider = new ClientBookingProvider();
        mIdExtra = getIntent().getStringExtra("idHistoryBooking");
        mcircle_imagebackhistorybookingdetaill.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryBookingDetalle.this, ServicioEnvio.class);
            startActivity(intent);
        });
        getClientBooking();
    }

    private void getClientBooking() {
        mClientBookingProvider.getClientBooking(mIdExtra).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ClientBooking historyBooking = snapshot.getValue(ClientBooking.class);
                    mtextvieworigenhistorybookingdetaillclient.setText(historyBooking.getOrigin());
                    mtextviewdestinationhistorybookingdetaillclient.setText(historyBooking.getDestination());
                    mtextviewdistaciahistorybookingdetaillclient.setText(historyBooking.getKm());
                    mtextvietiempo.setText(historyBooking.getTime());

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}