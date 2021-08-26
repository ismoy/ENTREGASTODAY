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

  /*  public Task<Void> update(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUsername());
        map.put("firstname", user.getFirstname());
        map.put("lastname", user.getLastname());
        map.put("email", user.getEmail());
        map.put("phone", user.getPhone());
        map.put("saldomoncash", user.getSaldomoncash());
        map.put("saldotopup", user.getSaldotopup());
        map.put("role", user.getRole());
        map.put("estado", user.getEstado());
        map.put("image", user.getImage());
        map.put("password",user.getPassword());
        return mDatabase.child(user.getId()).updateChildren(map);
    }*/

}
