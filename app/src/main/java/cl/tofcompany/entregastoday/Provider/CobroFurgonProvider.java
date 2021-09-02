package cl.tofcompany.entregastoday.Provider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CobroFurgonProvider {
    DatabaseReference mDatabase;

    public CobroFurgonProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("CostoFurgon");
    }

    public DatabaseReference getCobroFurgon(){
        return mDatabase;
    }
}
