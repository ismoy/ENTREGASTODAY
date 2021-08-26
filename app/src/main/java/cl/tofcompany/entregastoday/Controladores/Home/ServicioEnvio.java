package cl.tofcompany.entregastoday.Controladores.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cl.tofcompany.entregastoday.Controladores.Servicios.ServicioAuto;
import cl.tofcompany.entregastoday.Controladores.Servicios.ServicioBicicleta;
import cl.tofcompany.entregastoday.Controladores.Servicios.ServicioFurgon;
import cl.tofcompany.entregastoday.Controladores.Servicios.ServicioMoto;
import cl.tofcompany.entregastoday.R;

public class ServicioEnvio extends AppCompatActivity {
    private CardView mserviciomoto;
    private CardView mservicioauto;
    private CardView mserviciobicileta;
    private CardView mserviciofugon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_envio);

        Init();
    }

    private void Init() {
        mserviciomoto = findViewById(R.id.serviciomoto);
        mservicioauto = findViewById(R.id.servicioauto);
        mserviciobicileta = findViewById(R.id.serviciobicicleta);
        mserviciofugon = findViewById(R.id.serviciofurgo);

        mserviciomoto.setOnClickListener(v -> goToServicioMoto());
        mservicioauto.setOnClickListener(v -> goToServicioAuto());
        mserviciobicileta.setOnClickListener(v -> gotoServicioBicicleta());
        mserviciofugon.setOnClickListener(v -> goToServicioFurgon());
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
}