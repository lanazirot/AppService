package lanazirot.com.appservicenld;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForService extends AppCompatActivity {


    private String llave;
    private String padre;

    private TextView call;

    private ImageView fotod;

    private TextView nombre,desc,direccion,telefono;

    private String imgurl;

    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_work_sheet);
        getSupportActionBar().hide();

        llave = getIntent().getExtras().get("llave").toString();
        ref = FirebaseDatabase.getInstance().getReference("Servicios").child(llave);
        nombre = (TextView) findViewById(R.id.nombrego);
        desc =(TextView) findViewById(R.id.descgo);
        direccion = (TextView) findViewById(R.id.dirgo);
        telefono = (TextView) findViewById(R.id.telgo);
        fotod = (ImageView) findViewById(R.id.fotogo);
        call = (TextView) findViewById(R.id.call);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String n = dataSnapshot.child("nombre").getValue().toString();
                    String d = dataSnapshot.child("descripcion").getValue().toString();
                    String t = dataSnapshot.child("telefono").getValue().toString();
                    String dd = dataSnapshot.child("direccion").getValue().toString();
                    String u = dataSnapshot.child("urlimage").getValue().toString();
                    nombre.setText(n);
                    desc.setText(d);
                    direccion.setText(dd);
                    telefono.setText(t);

                    Glide.with(getApplicationContext()).load(u).into(fotod);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarPorTelefono();
            }
        });
    }
    public void llamarPorTelefono(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+telefono.getText().toString()));

        if(ActivityCompat.checkSelfPermission(ForService.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"Woo",Toast.LENGTH_SHORT).show();
            return;
        }else{
            startActivity(intent);
        }
    }
}
