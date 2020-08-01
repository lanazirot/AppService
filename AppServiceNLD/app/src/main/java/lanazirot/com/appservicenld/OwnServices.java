package lanazirot.com.appservicenld;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OwnServices extends AppCompatActivity {


    private RecyclerView blogServiciosPropios;
    private FirebaseUser usuarioActual;
    private DatabaseReference refServiciosDelUsuarioActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_services);
        getSupportActionBar().hide();


        blogServiciosPropios = (RecyclerView) findViewById(R.id.ownr);
        usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        refServiciosDelUsuarioActual = FirebaseDatabase.getInstance().getReference("Usuarios").child(usuarioActual.getUid()).child("servicios");

        final LinearLayoutManager layoutManager =  new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        blogServiciosPropios.setHasFixedSize(true);
        blogServiciosPropios.setLayoutManager(layoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Service,OwnServicesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Service, OwnServicesViewHolder>(Service.class, R.layout.blog_row, OwnServicesViewHolder.class, refServiciosDelUsuarioActual) {
            @Override
            protected void populateViewHolder(OwnServicesViewHolder viewHolder, Service model, final int position) {
                viewHolder.setTitulo(model.getNombre());
                viewHolder.setDesc(model.getDescripcion());
                viewHolder.setImagen(getApplicationContext(),model.getUrlimage());
                viewHolder.setTelefono(model.getTelefono());
                viewHolder.setDireccion(model.getDireccion());



                viewHolder.v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        new AlertDialog.Builder(OwnServices.this).setTitle("Tu servicio").setMessage("¿Que deseas hacer?.").setNeutralButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference refServiciosUsuario = FirebaseDatabase.getInstance().getReference("Servicios").child(getRef(position).getKey());
                                refServiciosUsuario.removeValue();
                                DatabaseReference refServicioSeleccionado = FirebaseDatabase.getInstance().getReference("Usuarios").child(usuarioActual.getUid()).child("servicios").child(getRef(position).getKey());
                                refServicioSeleccionado.removeValue();
                            }
                        }).setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final Intent intent = new Intent(OwnServices.this, UpdateService.class);


                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Servicios").child(getRef(position).getKey());

                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final String id = getRef(position).getKey();
                                        final String nombre = dataSnapshot.child("nombre").getValue().toString();
                                        final String desc =  dataSnapshot.child("descripcion").getValue().toString();
                                        final String dir = dataSnapshot.child("direccion").getValue().toString();
                                        final String tel =  dataSnapshot.child("telefono").getValue().toString();
                                        final String catego =  dataSnapshot.child("categoria").getValue().toString();

                                        intent.putExtra("id",id);
                                        intent.putExtra("nombre",nombre);
                                        intent.putExtra("desc",desc);
                                        intent.putExtra("tel",tel);
                                        intent.putExtra("dir",dir);
                                        intent.putExtra("catego",catego);

                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) { }
                                });

                            }
                        }).show();

                        return false;
                    }
                });

            }

        };
        blogServiciosPropios.setAdapter(firebaseRecyclerAdapter);
    }


    public static class OwnServicesViewHolder extends  RecyclerView.ViewHolder{
        private View v;
        public OwnServicesViewHolder(View itemView) {
            super(itemView);
            v=itemView;
        }
        public void setTitulo(String titulo){
            TextView tituloServicePropio = (TextView) v.findViewById(R.id.titulo);
            tituloServicePropio.setText(titulo);
        }
        public void setDesc(String desc){
            TextView descServicePropio = (TextView) v.findViewById(R.id.desc);
            descServicePropio.setText(desc);
        }
        public void setImagen(Context ctx, String url){
            ImageView imgServicePropio = (ImageView) v.findViewById(R.id.imagen);
            Glide.with(ctx).load(url).into(imgServicePropio);
        }
        public  void setTelefono(String telefono){
            TextView telefonoServicePropio = (TextView) v.findViewById(R.id.telefonox);
            telefonoServicePropio.setText(telefono);
        }
        public void setDireccion(String direccion){
            TextView direccionServicePropio = (TextView) v.findViewById(R.id.direccionx);
            direccionServicePropio.setText(direccion);
        }


    }

}

