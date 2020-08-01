package lanazirot.com.appservicenld;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


public class Profile extends Fragment {


    private FirebaseUser usuario;
    private DatabaseReference referenciaUsuarios;
    private Uri uriImagenUsuario;

    private TextView nombre,correo,ocupacion,servicio,tipo, serviciosPropios;
    private ImageView fotito;

    private ProgressDialog pdCargandoFoto;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        pdCargandoFoto = new ProgressDialog(getContext());
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        referenciaUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios").child(usuario.getUid());

        nombre = (TextView) v.findViewById(R.id.nombreperfil);
        correo = (TextView) v.findViewById(R.id.correoPerfil);
        ocupacion = (TextView) v.findViewById(R.id.ocupacionPerfil);
        servicio = (TextView) v.findViewById(R.id.servicioPerfil);
        tipo = (TextView) v.findViewById(R.id.tipousuarioPerfil);
        serviciosPropios = (TextView) v.findViewById(R.id.ownservices);


        serviciosPropios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OwnServices.class);
                startActivity(intent);
            }
        });


        fotito = (ImageView) v.findViewById(R.id.pp);


        llenarPerfil();


        fotito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarFoto();
            }
        });


        return v;
    }


    public void cambiarFoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode== RESULT_OK){
            uriImagenUsuario = data.getData();


            pdCargandoFoto.setMessage("Cargando tu foto...");
            pdCargandoFoto.setMessage("Estamos trabajando en ello...");
            pdCargandoFoto.show();
            pdCargandoFoto.setCancelable(false);

            fotito.setImageURI(uriImagenUsuario);


             StorageReference  storageRefFotoPerfil = FirebaseStorage.getInstance().getReference().child("user_pp").child(usuario.getUid());

            UploadTask task = storageRefFotoPerfil.putFile(uriImagenUsuario);

            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Uri uriUrlFotoPerfil = taskSnapshot.getDownloadUrl();

                    referenciaUsuarios.child("pp_url").setValue(uriUrlFotoPerfil.toString());

                    pdCargandoFoto.dismiss();

                }
            });




        }
    }

    public void llenarPerfil(){
        referenciaUsuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                llenarPerfil_task(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        referenciaUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                llenarPerfil_task(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void llenarPerfil_task(DataSnapshot dataSnapshot){
        String nombreCargado = dataSnapshot.child("nombre").getValue().toString();
        String correoCargado = dataSnapshot.child("correo").getValue().toString();
        String ocupacionCargada = dataSnapshot.child("ocupacion").getValue().toString();
        String servicioCargado = dataSnapshot.child("servicio").getValue().toString();
        String tipoUsuarioCargado = dataSnapshot.child("tipousuario").getValue().toString();

        String urlFotoPerfil = dataSnapshot.child("pp_url").getValue().toString();

        nombre.setText(nombreCargado);
        correo.setText(correoCargado);
        ocupacion.setText(ocupacionCargada);
        tipo.setText(tipoUsuarioCargado);
        servicio.setText(servicioCargado);

        Glide.clear(fotito);
        Glide.with(getActivity()).load(urlFotoPerfil).diskCacheStrategy(DiskCacheStrategy.ALL).into(fotito);
    }
}
