package cl.tofcompany.entregastoday.Provider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CobroBicicletaProvider {
    DatabaseReference mDatabase;

    public CobroBicicletaProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("CostoBicicleta");
    }

    public DatabaseReference getCobroBicicleta(){
        return mDatabase;
    }
}
