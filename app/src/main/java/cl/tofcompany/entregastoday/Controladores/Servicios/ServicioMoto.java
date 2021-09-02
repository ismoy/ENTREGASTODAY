package cl.tofcompany.entregastoday.Controladores.Servicios;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import cl.tofcompany.entregastoday.Controladores.Home.ServicioEnvio;
import cl.tofcompany.entregastoday.Controladores.MainActivity;
import cl.tofcompany.entregastoday.DetalleServicios.DetallesCotizacionServicioMoto;
import cl.tofcompany.entregastoday.DetalleServicios.History;
import cl.tofcompany.entregastoday.Provider.AuthProvider;
import cl.tofcompany.entregastoday.Provider.GeofireProvider;
import cl.tofcompany.entregastoday.Provider.UsuarioProvider;
import cl.tofcompany.entregastoday.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ServicioMoto extends AppCompatActivity implements OnMapReadyCallback {
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    private LatLng mCurrentLatLng;
    private GeofireProvider mGeofireProvider;
    private AuthProvider mAuthProvider;
    private boolean mIsFirstTime = true;
    private AutocompleteSupportFragment mAutocomplete;
    private AutocompleteSupportFragment mAutocompleteDestino;
    private PlacesClient mPlacesClient;
    private String mOrigin;
    private LatLng mOriginLatLng;
    private String mDetination;
    private LatLng mDestinationLatLng;
    private CardView mserviciomotos;
    private GoogleMap.OnCameraIdleListener mCameraListener;
    private Button mbtncotizar;
    private BottomAppBar mBottomAppBar;
    private ImageView mvolveralhome;
    private TextView mnombrebottomsheet;
    private TextView mapellidobottomsheet;
    private TextView  mrutbottomsheet;
    private TextView  memailbottomsheet;
    private TextView  mtelefonobottomsheet;
    private CircleImageView mpicturebottomsheet;
    private UsuarioProvider mUsuarioProvider;
    private EditText mmedidas;
    private CircleImageView mimagemaps;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    mCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(15f)
                                    .build()
                    ));
                    if (mIsFirstTime) {
                        mIsFirstTime = false;
                        limitSearch();
                    }
                    updateLocation();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_moto);
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mMapFragment != null;
        mMapFragment.getMapAsync(this);
        mGeofireProvider = new GeofireProvider("active_users");
        mAuthProvider = new AuthProvider();
        mUsuarioProvider = new UsuarioProvider();
        mserviciomotos = findViewById(R.id.serviciomotos);
        mvolveralhome = findViewById(R.id.volveralhome);
        mmedidas = findViewById(R.id.medidas);
        mimagemaps = findViewById(R.id.imagemaps);
        mbtncotizar = findViewById(R.id.btncotizar);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBKzVSHeQV0jtkr90a1tLxR8Mje9eQ224w");
        }
        mPlacesClient = Places.createClient(this);
        instanceAutocompleteOrigin();
        instanceAutocompleteDestination();
        onCameraMove();
        mbtncotizar.setOnClickListener(view ->SolicitandoCotizacion());
        mBottomAppBar = findViewById(R.id.bttomApp);
        setSupportActionBar(mBottomAppBar);
        mvolveralhome.setOnClickListener(v -> gotohome());
        infousers();

    }

    private void gotohome() {
        Intent intent = new Intent(ServicioMoto.this,ServicioEnvio.class);
        startActivity(intent);
    }

    private void SolicitandoCotizacion() {
        if (mOriginLatLng !=null && mDestinationLatLng != null){
            Intent intent = new Intent(ServicioMoto.this, DetallesCotizacionServicioMoto.class);
            intent.putExtra("origin_lat", mOriginLatLng.latitude);
            intent.putExtra("origin_lng", mOriginLatLng.longitude);
            intent.putExtra("destination_lat", mDestinationLatLng.latitude);
            intent.putExtra("destination_lng", mDestinationLatLng.longitude);
            intent.putExtra("origin", mOrigin);
            intent.putExtra("destination", mDetination);
            intent.putExtra("medidas", mmedidas.getText().toString());
            if (TextUtils.isEmpty(mmedidas.getText().toString().trim())){
                customToast(ServicioMoto.this,"Ingrese las medidas");

            }else {
                startActivity(intent);
            }
        }else {
            customToast(ServicioMoto.this,"Ingrese el lugar de origen y destino");
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnCameraIdleListener(mCameraListener);
        mLocationRequest = new LocationRequest();
        //mLocationRequest.setInterval(1000);
       /// mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5);
        startLocation();
    }
    private void updateLocation() {
        if (mAuthProvider.existSession() && mCurrentLatLng !=null){
            mGeofireProvider.savelocation(mAuthProvider.getId(),mCurrentLatLng);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActived()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    } else {
                        showAlertDialogNOGPS();
                    }
                } else {
                    checkLocationPermissions();
                }
            } else {
                checkLocationPermissions();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActived()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
        else if (requestCode == SETTINGS_REQUEST_CODE && !gpsActived()){
            showAlertDialogNOGPS();
        }
    }

    private void showAlertDialogNOGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor activa tu ubicacion para continuar")
                .setCancelable(false)
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }
    private boolean gpsActived() {
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }
        return isActive;
    }

    private void startLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActived()) {
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                }
                else {
                    showAlertDialogNOGPS();
                }
            }
            else {
                checkLocationPermissions();
            }
        } else {
            if (gpsActived()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            }
            else {
                showAlertDialogNOGPS();
            }
        }
    }


    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicacion requiere de los permisos de ubicacion " +
                                "para poder utilizarse")
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialogInterface, i) ->
                                ActivityCompat.requestPermissions(ServicioMoto.this,
                                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                        LOCATION_REQUEST_CODE))
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(ServicioMoto.this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }
    }

    private void limitSearch() {
        LatLng northSide = SphericalUtil.computeOffset(mCurrentLatLng, 5000, 0);
        LatLng southSide = SphericalUtil.computeOffset(mCurrentLatLng, 5000, 180);
        mAutocomplete.setCountry("CL");
        mAutocomplete.setLocationBias(RectangularBounds.newInstance(southSide, northSide));
        mAutocompleteDestino.setCountry("CL");
        mAutocompleteDestino.setLocationBias(RectangularBounds.newInstance(southSide, northSide));
    }


    private void onCameraMove() {
        mCameraListener = () -> {
            try {
                Geocoder geocoder = new Geocoder(ServicioMoto.this);
                mOriginLatLng = mMap.getCameraPosition().target;
                List<Address> addressList = geocoder.getFromLocation(mOriginLatLng.latitude, mOriginLatLng.longitude, 1);
                String city = addressList.get(0).getLocality();
                String country = addressList.get(0).getCountryName();
                String address = addressList.get(0).getAddressLine(0);
                mOrigin = address + " " + city;
                mAutocomplete.setText(address + " " + city);
            } catch (Exception e) {
            }
        };
    }

    private void instanceAutocompleteOrigin() {
        mAutocomplete = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.placeautocompleteorigin);
        assert mAutocomplete != null;
        mAutocomplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        mAutocomplete.setHint("Desde");
        mAutocomplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
               mOrigin = place.getName();
               mOriginLatLng = place.getLatLng();

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }
    private void instanceAutocompleteDestination() {
        mAutocompleteDestino = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.placeautocompletedestino);
        assert mAutocompleteDestino != null;
        mAutocompleteDestino.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        mAutocompleteDestino.setHint("Hasta");
        mAutocompleteDestino.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
               mDetination = place.getName();
               mDestinationLatLng = place.getLatLng();

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
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
                ServicioMoto.this,R.style.BottomSheetDialogTheme
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
        Intent intent = new Intent(ServicioMoto.this, MainActivity.class);
        startActivity(intent);
    }

    private void customToast(Context context, String mensaje){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast,(ViewGroup) findViewById(R.id.layoutcustomtoast));
        TextView txtmensaje = view.findViewById(R.id.mensajeerror);
        txtmensaje.setText(mensaje);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.NO_GRAVITY,0,-100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
    private void infousers(){
        mUsuarioProvider.getUsuarios(mAuthProvider.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String image = "";
                    if (snapshot.hasChild("image")){
                        image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(mimagemaps);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}