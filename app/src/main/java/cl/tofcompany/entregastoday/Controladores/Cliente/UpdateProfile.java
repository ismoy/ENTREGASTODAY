package cl.tofcompany.entregastoday.Controladores.Cliente;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

import cl.tofcompany.entregastoday.Controladores.Home.ServicioEnvio;
import cl.tofcompany.entregastoday.Provider.AuthProvider;
import cl.tofcompany.entregastoday.Provider.ImagesProvider;
import cl.tofcompany.entregastoday.Provider.UsuarioProvider;
import cl.tofcompany.entregastoday.R;
import cl.tofcompany.entregastoday.Utils.FileUtil;
import cl.tofcompany.entregastoday.modelos.Usuarios;
import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {

    private CircleImageView miImageViewprofile;
    private Button mButtonupdate;
    private EditText mTextViewname;
    private EditText mTextViewemail;
    private EditText mtextviewapellido;
    private EditText mtextviewphone;
    private UsuarioProvider mUsuarioProvider;
    private AuthProvider mAuthProvider;
    private File mimagefile;
    private final int GALLERY_REQUEST = 1;
    private ProgressDialog mProgressDialog;
    private String mname;
    private String mapellido;
    private  String mphone;
    private String memail;
    private ImagesProvider mImageProvider;
    private ImageView mvolverupdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Init();
    }


    private void Init(){
        miImageViewprofile = findViewById(R.id.imageviewprofile);
        mTextViewname = findViewById(R.id.nombre);
        mTextViewemail = findViewById(R.id.email);
        mtextviewapellido = findViewById(R.id.apellido);
        mtextviewphone = findViewById(R.id.phone);
        mButtonupdate = findViewById(R.id.actualizar);
        mAuthProvider = new AuthProvider();
        mUsuarioProvider = new UsuarioProvider();
        mvolverupdate = findViewById(R.id.volverupdate);
        mImageProvider = new ImagesProvider("client_images");
        mProgressDialog = new ProgressDialog(this);
        getClientInfo();
        mButtonupdate.setOnClickListener(v ->
                updateProfile());
        miImageViewprofile.setOnClickListener(v ->
                openGallery());
        mvolverupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfile.this, ServicioEnvio.class);
                startActivity(intent);
            }
        });
    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== GALLERY_REQUEST && resultCode == RESULT_OK) {
            try {

                mimagefile = FileUtil.from(this, data.getData());
                miImageViewprofile.setImageBitmap(BitmapFactory.decodeFile(mimagefile.getAbsolutePath()));
            } catch(Exception e) {

            }
        }
    }

    private void getClientInfo() {

        mUsuarioProvider.getUsuarios(mAuthProvider.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("nombre").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    String apelido = dataSnapshot.child("apellido").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String image = "";
                    if (dataSnapshot.hasChild("image")) {
                        image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(miImageViewprofile);
                    }
                    mTextViewname.setText(name);
                    mTextViewemail.setText(email);
                    mtextviewapellido.setText(apelido);
                    mtextviewphone.setText(phone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void updateProfile() {
        mname = mTextViewname.getText().toString();
        mapellido = mtextviewapellido.getText().toString();
        mphone = mtextviewphone.getText().toString();
        if (!mname.equals("") && mimagefile != null) {
            mProgressDialog.setMessage("Espere un momento...");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
            saveImage();
        }
        else {
            Toast.makeText(this, "Ingresa la imagen", Toast.LENGTH_SHORT).show();
        }
    }
    private void saveImage() {
        mImageProvider.saveImage(UpdateProfile.this, mimagefile, mAuthProvider.getId()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    mImageProvider.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        String image = uri.toString();
                        Usuarios usuarios = new Usuarios();
                        usuarios.setNombre(mname);
                        usuarios.setImage(image);
                        usuarios.setPhone(mphone);
                        usuarios.setApellido(mapellido);
                        usuarios.setId(mAuthProvider.getId());
                        mUsuarioProvider.updateimage(usuarios).addOnSuccessListener(aVoid -> {
                            mProgressDialog.dismiss();
                            Toast.makeText(UpdateProfile.this, "Su informacion se actualizo correctamente", Toast.LENGTH_SHORT).show();
                        });
                    });
                }
                else {
                    Toast.makeText(UpdateProfile.this, "Hubo un error al subir la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}