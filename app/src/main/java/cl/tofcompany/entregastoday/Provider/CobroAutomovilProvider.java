package cl.tofcompany.entregastoday.Provider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CobroAutomovilProvider {
    DatabaseReference mDatabase;

    public CobroAutomovilProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("CostoAutomovil");
    }

    public DatabaseReference getCobroAutomovil(){
        return mDatabase;
    }
}
