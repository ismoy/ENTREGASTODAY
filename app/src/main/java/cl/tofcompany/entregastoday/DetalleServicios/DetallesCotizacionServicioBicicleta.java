package cl.tofcompany.entregastoday.DetalleServicios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import cl.tofcompany.entregastoday.Controladores.Home.ServicioEnvio;
import cl.tofcompany.entregastoday.Provider.AuthProvider;
import cl.tofcompany.entregastoday.Provider.ClientBookingProvider;
import cl.tofcompany.entregastoday.Provider.CobroBicicletaProvider;
import cl.tofcompany.entregastoday.Provider.GoogleApiProvider;
import cl.tofcompany.entregastoday.Provider.HistoryBookingProvider;
import cl.tofcompany.entregastoday.Provider.UsuarioProvider;
import cl.tofcompany.entregastoday.R;
import cl.tofcompany.entregastoday.Utils.DecodePoints;
import cl.tofcompany.entregastoday.Utils.GmailSender;
import cl.tofcompany.entregastoday.modelos.ClientBooking;
import cl.tofcompany.entregastoday.modelos.CobroBicicleta;
import cl.tofcompany.entregastoday.modelos.HistoryBooking;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesCotizacionServicioBicicleta extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mmap;
    private SupportMapFragment mapFragment;
    private double mExtraOriginLat;
    private double mExtraOriginLng;
    private double mExtraDestinationLat;
    private double mExtraDestinationLng;
    private String mExtraOrigin;
    private String mExtraDestination;
    private LatLng mOriginLatLng;
    private LatLng mDestinationLatLng;
    private GoogleApiProvider mGoogleApiProvider;
    private List mpolyllineList;
    private PolylineOptions mPolylineOptions;
    private TextView mtextViewOrigin, mtextViewDestino, mtextViewTiempo, mtextViewprice;
    private ClientBookingProvider mClientBookingProvider;
    private AuthProvider mAuthProvider;
    private UsuarioProvider mUsuarioProvider;
    private Button mbtnsolicitarahora;
    private HistoryBooking mHistoryBooking;
    private HistoryBookingProvider mHistoryBookingProvider;
    private CobroBicicletaProvider mCobroBicicletaProvider;
    private TextView mtvemail;
    private String subject = "Tienes Una Nueva Solicitud ";
    private GmailSender.GMailSender sender;
    private String user = "entregastoday@gmail.com";
    private String password = "emailentregastoday";
    private String correocliente;
    private String name;
    private String firstname;
    private String run;
    private String phone;
    private String distancias ;
    private String duracion;
    private double precio;
    private double distaciasparse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_cotizacion_servicio_bicicleta);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        mExtraOriginLat = getIntent().getDoubleExtra("origin_lat", 0);
        mExtraOriginLng = getIntent().getDoubleExtra("origin_lng", 0);
        mExtraDestinationLat = getIntent().getDoubleExtra("destination_lat", 0);
        mExtraDestinationLng = getIntent().getDoubleExtra("destination_lng", 0);
        mExtraOrigin = getIntent().getStringExtra("origin");
        mExtraDestination = getIntent().getStringExtra("destination");
        mOriginLatLng = new LatLng(mExtraOriginLat, mExtraOriginLng);
        mDestinationLatLng = new LatLng(mExtraDestinationLat, mExtraDestinationLng);
        mGoogleApiProvider = new GoogleApiProvider(this);
        mClientBookingProvider = new ClientBookingProvider();
        mAuthProvider = new AuthProvider();
        mUsuarioProvider = new UsuarioProvider();
        mtextViewOrigin = findViewById(R.id.txtorigin);
        mtextViewDestino = findViewById(R.id.txtdestination);
        mtextViewTiempo = findViewById(R.id.textviewtime);
        mtextViewprice = findViewById(R.id.textviewprice);
        mtextViewOrigin.setText(mExtraOrigin);
        mtextViewDestino.setText(mExtraDestination);
        mClientBookingProvider = new ClientBookingProvider();
        mHistoryBookingProvider = new HistoryBookingProvider();
        mCobroBicicletaProvider = new CobroBicicletaProvider();
        mbtnsolicitarahora = findViewById(R.id.btnsolicitarahora);
        sender = new GmailSender.GMailSender(user,password);
        mbtnsolicitarahora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LLEGADAKM", " " + distaciasparse);
                if (distaciasparse>10 ){
                    new SweetAlertDialog(DetallesCotizacionServicioBicicleta.this,SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Por la distancia de su ubicacion no esta disponible el servicio de Bicicleta. " +
                                    " Su distancia es " + distancias)
                            .setConfirmClickListener(sweetAlertDialog -> {
                                sweetAlertDialog.setCancelable(false);
                                sweetAlertDialog.setCanceledOnTouchOutside(false);
                                sweetAlertDialog.setConfirmText("OK");
                                Intent intent = new Intent(DetallesCotizacionServicioBicicleta.this, ServicioEnvio.class);
                                startActivity(intent);
                            })
                            .show();
                }else {
                    new MyAsyncClassb().execute();
                }
            }
        });
        mtvemail = findViewById(R.id.tvemail);

        mUsuarioProvider.getUsuarios(mAuthProvider.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String email = snapshot.child("email").getValue().toString();
                    String nombre = snapshot.child("nombre").getValue().toString();
                    String apellido = snapshot.child("apellido").getValue().toString();
                    String rut = snapshot.child("rut").getValue().toString();
                    String telefono = snapshot.child("phone").getValue().toString();
                    name = nombre;
                    firstname = apellido;
                    run = rut;
                    phone = telefono;
                    correocliente = email;
                    Log.d("EMAIL", " " +correocliente);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    class MyAsyncClassb extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            pDialog =  ProgressDialog.show(DetallesCotizacionServicioBicicleta.this
                    ,"Please Wait..","Enviando Solicitud...",true,false);


        }

        @Override

        protected Void doInBackground(Void... mApi) {
            try {

                // Add subject, Body, your mail Id, and receiver mail Id.
                sender.sendMail(subject, "Tienes una nueva solicitud " +"\n"+
                        " Nombre: " + name+ "\n"+
                        " Apellido: " + firstname+"\n"+
                        " Rut: " + run+"\n"+
                        " Telefono: " + phone+"\n"+
                        " Email: " + correocliente+"\n"+
                        " Desde: " + mExtraOrigin+"\n"+
                        " Hata: " + mExtraDestination+"\n"+
                        " Distancia: " + distancias+"\n"+
                        " Duration: " + duracion+"\n"+
                        " Costo: " + precio + " CLP "+"\n"+
                        " Tipo Servicio: " + " SERVICIO DE BICICLETA "+"\n"+
                        " Este es un correo electrónico generado automáticamente. Por favor no responder. ", correocliente, user);
                Log.d("send", "done");
            }
            catch (Exception ex) {
                Log.d("exceptionsending", ex.toString());
            }
            return null;
        }

        @Override

        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            pDialog.cancel();
            /*AlertDialog.Builder builder = new AlertDialog.Builder(DetallesCotizacionServicioBicicleta.this);
            builder.setCancelable(false);
            builder.setTitle(Html.fromHtml("<font color='#509324'>Envío confirmado</font>"));
            builder.setMessage("Nuestro colaborador llegará a tu domicilio en 25 minutos aprox.\n" +
                    "Uno de nuestros agentes se comunicará para indicar nuestros medios de pago. ");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(DetallesCotizacionServicioBicicleta.this, ServicioEnvio.class);
                    startActivity(intent);
                }
            });
            builder.show();*/
            new SweetAlertDialog(DetallesCotizacionServicioBicicleta.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(String.valueOf(Html.fromHtml("<font color='#509324'>Envío confirmado</font>")))
                    .setContentText("Uno de nuestros agentes se comunicará contigo para coordinar la entrega y los medios de pago.\n" +
                            "¡Debes estar atento a tu teléfono!")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.setCanceledOnTouchOutside(false);
                        sweetAlertDialog.setConfirmText("OK");
                        Intent intent = new Intent(DetallesCotizacionServicioBicicleta.this,ServicioEnvio.class);
                        startActivity(intent);


                    })

                    .show();
                createsolicitudcliente();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mmap = googleMap;
        mmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mmap.getUiSettings().setZoomControlsEnabled(true);
        mmap.addMarker(new MarkerOptions().position(mOriginLatLng).title("Origen").icon(BitmapDescriptorFactory.fromResource(R.mipmap.mappin)));
        mmap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Destino").icon(BitmapDescriptorFactory.fromResource(R.mipmap.mappinblue)));
        mmap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(mOriginLatLng)
                        .zoom(15f)
                        .build()
        ));
        drawRoute();

    }

    private void drawRoute() {
        mGoogleApiProvider.getDirections(mOriginLatLng, mDestinationLatLng).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    assert response.body() != null;
                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");
                    mpolyllineList = DecodePoints.decodePoly(points);
                    mPolylineOptions = new PolylineOptions();
                    mPolylineOptions.color(Color.DKGRAY);
                    mPolylineOptions.width(13f);
                    mPolylineOptions.startCap(new SquareCap());
                    mPolylineOptions.jointType(JointType.ROUND);
                    mPolylineOptions.addAll(mpolyllineList);
                    mmap.addPolyline(mPolylineOptions);
                    JSONArray legs = route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject distance = leg.getJSONObject("distance");
                    JSONObject duration = leg.getJSONObject("duration_in_traffic");
                    String distanceText = distance.getString("text");
                    distancias = distanceText;
                    String durationText = duration.getString("text");
                    duracion = durationText;
                    mtextViewTiempo.setText(durationText + " " + distanceText);
                    String[] distanceAndKm = distanceText.split(" ");
                    double distancevalue = Double.parseDouble(distanceAndKm[0]);
                    distaciasparse = distancevalue;
                   /* String[] durationAndMins = durationText.split(" ");
                    double durationvalue = Double.parseDouble(durationAndMins[0]);*/
                    // calculatePrice(distancevalue,durationvalue);
                    calculatePrice(distancevalue);

                } catch (Exception e) {
                    Log.d("Error", "Error encontrado " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    private void calculatePrice(double distancevalue) {
        mCobroBicicletaProvider.getCobroBicicleta().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    CobroBicicleta cobroBicicleta = snapshot.getValue(CobroBicicleta.class);
                    assert cobroBicicleta != null;
                    double totalDistance = Math.round(distancevalue * cobroBicicleta.getKm());
                    mtextViewprice.setText(totalDistance + " CLP ");
                    precio = totalDistance;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createsolicitudcliente() {
        mGoogleApiProvider.getDirections(mOriginLatLng, mDestinationLatLng).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");
                    JSONArray legs = route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject distance = leg.getJSONObject("distance");
                    JSONObject duration = leg.getJSONObject("duration");
                    String km = distance.getString("text");
                    String time = duration.getString("text");
                    ClientBooking clientBooking = new ClientBooking(
                            mAuthProvider.getId(),
                            mExtraDestination,
                            mExtraOrigin,
                            time,
                            km,
                            mExtraOriginLat,
                            mExtraOriginLng,
                            mExtraDestinationLat,
                            mExtraDestinationLng,
                            precio,
                            name,
                            firstname,
                            run,
                            correocliente,
                            phone

                    );
                    mClientBookingProvider.create(clientBooking).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    });
                } catch (Exception e) {
                    Log.d("Error", "Error encontrado " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}