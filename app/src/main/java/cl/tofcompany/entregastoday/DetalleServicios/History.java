package cl.tofcompany.entregastoday.DetalleServicios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import cl.tofcompany.entregastoday.Controladores.Adapter.HistoryBookingClientAdapter;
import cl.tofcompany.entregastoday.Provider.AuthProvider;
import cl.tofcompany.entregastoday.R;
import cl.tofcompany.entregastoday.modelos.ClientBooking;
import cl.tofcompany.entregastoday.modelos.HistoryBooking;

public class History extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private HistoryBookingClientAdapter mAdapter;
    private AuthProvider mAuthProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Init();
    }
    private void Init(){

        mRecyclerView = findViewById(R.id.recyclerviewhistoryclient);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuthProvider = new AuthProvider();
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("ClientBooking")
                .orderByChild("idClient")
                .equalTo(mAuthProvider.getId());
        FirebaseRecyclerOptions<ClientBooking> options = new FirebaseRecyclerOptions.Builder<ClientBooking>()
                .setQuery(query, ClientBooking.class)
                .build();
        mAdapter = new HistoryBookingClientAdapter(options, History.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}