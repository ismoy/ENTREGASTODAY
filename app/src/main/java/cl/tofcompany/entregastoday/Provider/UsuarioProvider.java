package cl.tofcompany.entregastoday.Provider;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import cl.tofcompany.entregastoday.modelos.Usuarios;

public class UsuarioProvider {


    DatabaseReference mDatabase;

    public UsuarioProvider() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");

    }


    public Task<Void> create(Usuarios usuarios) {
        return mDatabase.child(usuarios.getId()).setValue(usuarios);
    }

    public DatabaseReference getUsuarios (String idUsuario) {
        return mDatabase.child(idUsuario);
    }



    public Task<Void> updateimage(Usuarios usuarios) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre", usuarios.getNombre());
        map.put("image", usuarios.getImage());
        map.put("phone", usuarios.getPhone());
        map.put("apellido", usuarios.getApellido());
        return mDatabase.child(usuarios.getId()).updateChildren(map);
    }


}
