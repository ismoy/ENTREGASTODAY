package cl.tofcompany.entregastoday.Provider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CobroMotocicletaProvider {
    DatabaseReference mDatabase;

    public CobroMotocicletaProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("CostoMotociclista");
    }
    public DatabaseReference getCobroMotociclista(){
        return mDatabase;
    }

}
