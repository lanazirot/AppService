package lanazirot.com.appservicenld;

import android.content.Context;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Services extends AppCompatActivity {


    private Bundle bundleDatosAnteriores;
    private String categoria;
    private TextView bannerCategoria;
    private RecyclerView recyclerViewServicios;
    private DatabaseReference refServicios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_category_work);
        getSupportActionBar().hide();

        recyclerViewServicios = (RecyclerView) findViewById(R.id.recycat);
        bundleDatosAnteriores = this.getIntent().getExtras();
        categoria = bundleDatosAnteriores.getString("categoria");
        bannerCategoria = (TextView) findViewById(R.id.bannercatego);
        refServicios = FirebaseDatabase.getInstance().getReference("Servicios");


        final LinearLayoutManager layoutManager =  new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewServicios.setHasFixedSize(true);
        recyclerViewServicios.setLayoutManager(layoutManager);

        bannerCategoria.setText(categoria);


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Service,ForCategoryWorkViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Service, ForCategoryWorkViewHolder>(Service.class, R.layout.blog_row, ForCategoryWorkViewHolder.class,
                refServicios.orderByChild("categoria").equalTo(categoria)) {
            @Override
            protected void populateViewHolder(final ForCategoryWorkViewHolder viewHolder, final Service model, final int position) {
                viewHolder.setTitulo(model.getNombre());
                viewHolder.setDesc(model.getDescripcion());
                viewHolder.setImagen(getApplicationContext(),model.getUrlimage());
                viewHolder.setTelefono(model.getTelefono());
                viewHolder.setDireccion(model.getDireccion());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       String post = getRef(position).getKey();
                        Intent intent = new Intent(getApplicationContext(), ForService.class);
                        intent.putExtra("llave",post);
                        startActivity(intent);
                    }
                });

            }

        };
        recyclerViewServicios.setAdapter(firebaseRecyclerAdapter);


    }

private static class ForCategoryWorkViewHolder extends  RecyclerView.ViewHolder {
    private View mView;
    public ForCategoryWorkViewHolder(final View itemView) {
        super(itemView);
        this.mView=itemView;

    }


    public void setTitulo(String titulo){
        TextView tituloServ = (TextView) mView.findViewById(R.id.titulo);
        tituloServ.setText(titulo);
    }
    public void setDesc(String desc){
        TextView descServ = (TextView) mView.findViewById(R.id.desc);
        descServ.setText(desc);
    }
    public void setImagen(Context ctx, String url){
        ImageView imgServ = (ImageView) mView.findViewById(R.id.imagen);
        Glide.with(ctx).load(url).into(imgServ);
    }
    public  void setTelefono(String telefono){
        TextView telefonoServ = (TextView) mView.findViewById(R.id.telefonox);
        telefonoServ.setText(telefono);
    }
    public void setDireccion(String direccion){
        TextView direServ = (TextView) mView.findViewById(R.id.direccionx);
        direServ.setText(direccion);
    }


}


}
