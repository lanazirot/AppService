package lanazirot.com.appservicenld;

import android.content.Context;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Works extends AppCompatActivity {


    private RecyclerView blogTrabajos;
    private DatabaseReference refServicios;
    private FirebaseUser usuarioActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_trabajos);
        getSupportActionBar().hide();

        usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        refServicios = FirebaseDatabase.getInstance().getReference().child("Servicios");

        final LinearLayoutManager layoutManager =  new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);



        blogTrabajos = (RecyclerView) findViewById(R.id.trabajos);
        blogTrabajos.setHasFixedSize(true);
        blogTrabajos.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Service,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Service, BlogViewHolder>(Service.class, R.layout.blog_row, BlogViewHolder.class, refServicios) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Service model, int position) {
                viewHolder.setTitulo(model.getNombre());
                viewHolder.setDesc(model.getDescripcion());
                viewHolder.setImagen(getApplicationContext(),model.getUrlimage());
                viewHolder.setTelefono(model.getTelefono());
                viewHolder.setDireccion(model.getDireccion());
            }
        };
            blogTrabajos.setAdapter(firebaseRecyclerAdapter);
    }


    public static class BlogViewHolder extends  RecyclerView.ViewHolder{
        View mView;
        public BlogViewHolder(View itemView) {
            super(itemView);
            this.mView=itemView;
        }
        public void setTitulo(String titulo){
            TextView tituloServicio = (TextView) mView.findViewById(R.id.titulo);
            tituloServicio.setText(titulo);
        }
        public void setDesc(String desc){
            TextView descServicio = (TextView) mView.findViewById(R.id.desc);
            descServicio.setText(desc);
        }
        public void setImagen(Context ctx, String url){
            ImageView imgServicio = (ImageView) mView.findViewById(R.id.imagen);
            Glide.with(ctx).load(url).into(imgServicio);
        }
        public void setTelefono(String telefono){
            TextView telefonoServicio = (TextView) mView.findViewById(R.id.telefonox);
            telefonoServicio.setText(telefono);
        }
        public void setDireccion(String direccion){
            TextView direccionServicio = (TextView) mView.findViewById(R.id.direccionx);
            direccionServicio.setText(direccion);
        }
    }
}
