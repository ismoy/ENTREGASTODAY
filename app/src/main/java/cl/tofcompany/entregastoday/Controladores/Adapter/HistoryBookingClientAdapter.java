package cl.tofcompany.entregastoday.Controladores.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import cl.tofcompany.entregastoday.DetalleServicios.HistoryBookingDetalle;
import cl.tofcompany.entregastoday.Provider.AuthProvider;
import cl.tofcompany.entregastoday.Provider.UsuarioProvider;
import cl.tofcompany.entregastoday.R;
import cl.tofcompany.entregastoday.modelos.ClientBooking;
import cl.tofcompany.entregastoday.modelos.HistoryBooking;
import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryBookingClientAdapter extends FirebaseRecyclerAdapter<ClientBooking, HistoryBookingClientAdapter.ViewHolder> {

    private Context mContext;
    private UsuarioProvider mUsuarioProvider;
    private AuthProvider mAuthProvider;

    public HistoryBookingClientAdapter(@NonNull @NotNull FirebaseRecyclerOptions<ClientBooking> options, Context context) {
        super(options);
        //iniciamos los variables
        mContext = context;
        mAuthProvider = new AuthProvider();
        mUsuarioProvider = new UsuarioProvider();
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull HistoryBookingClientAdapter.ViewHolder holder, int position, @NonNull @NotNull ClientBooking clientBooking) {
        String id = getRef(position).getKey();
        holder.textViewOrigen.setText(clientBooking.getOrigin());
        holder.textViewDestination.setText(clientBooking.getDestination());
        holder.textViewName.setText(clientBooking.getNombre());
        holder.mtextviewcostohistory.setText((int) clientBooking.getPrice() + " CLP ");
        holder.mview.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, HistoryBookingDetalle.class);
            intent.putExtra("idHistoryBooking", id);
            mContext.startActivity(intent);
        });

        mUsuarioProvider.getUsuarios(mAuthProvider.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String image = "";
                    if (snapshot.hasChild("image")) {
                        image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(holder.imageViewuser);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_history_booking,parent,false);

        return new ViewHolder(view);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewName;
        private TextView textViewOrigen;
        private TextView textViewDestination;
        private CircleImageView imageViewuser;
        private TextView mtextviewcostohistory;
        private View mview;
        //proviene de firebaserecycleadapter
        public ViewHolder(@NonNull @NotNull View view) {
            super(view);
            mview = view;
            textViewName = view.findViewById(R.id.textviewnamehistory);
            textViewOrigen = view.findViewById(R.id.textvieworigenhistory);
            textViewDestination = view.findViewById(R.id.textviewdestinationhistory);
            imageViewuser = view.findViewById(R.id.imageviewuserhistory);
            mtextviewcostohistory = view.findViewById(R.id.textviewcostohistory);

        }
    }

}
