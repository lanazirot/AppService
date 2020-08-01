package lanazirot.com.appservicenld;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateService extends AppCompatActivity {


    private Spinner tipoServicio;

    private EditText nombreServicio;
    private EditText descripcionServicio;
    private EditText telefonoServicio;
    private EditText direccionServicio;
    private String idServicio;


   private TextView registrarServicio;

    private FirebaseUser usuarioActual;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_servicio);
        getSupportActionBar().hide();

        usuarioActual = FirebaseAuth.getInstance().getCurrentUser();



        tipoServicio = (Spinner) findViewById(R.id.modspin);

        nombreServicio = (EditText) findViewById(R.id.nomedit);
        descripcionServicio = (EditText) findViewById(R.id.descedit);
        telefonoServicio = (EditText) findViewById(R.id.teledit);
        direccionServicio =(EditText) findViewById(R.id.diredit);


        nombreServicio.setHint(getIntent().getStringExtra("nombre"));
        descripcionServicio.setHint(getIntent().getStringExtra("desc"));
        telefonoServicio.setHint(getIntent().getStringExtra("tel"));
        direccionServicio.setHint(getIntent().getStringExtra("dir"));



        idServicio = getIntent().getStringExtra("id");


        registrarServicio = (TextView) findViewById(R.id.okay);


        registrarServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmar();
            }
        });


    }

    private void confirmar(){
        if(TextUtils.isEmpty(nombreServicio.getText())||TextUtils.isEmpty(descripcionServicio.getText())||TextUtils.isEmpty(telefonoServicio.getText())||TextUtils.isEmpty(direccionServicio.getText())){
            new AlertDialog.Builder(UpdateService.this).setTitle("Whoops!").setMessage("Tu servicio debe contener todos los campos.").setNeutralButton("Confirmar",null).show();
            return;
        }else{
            modificar();
        }
    }

    private void modificar(){




        DatabaseReference refServicios = FirebaseDatabase.getInstance().getReference("Servicios").child(idServicio);
        DatabaseReference refServiciosDelUsario = FirebaseDatabase.getInstance().getReference("Usuarios").child(usuarioActual.getUid()).child("servicios").child(idServicio);


        refServicios.child("categoria").setValue(tipoServicio.getSelectedItem().toString());
        refServicios.child("nombre").setValue(nombreServicio.getText().toString());
        refServicios.child("telefono").setValue(telefonoServicio.getText().toString());
        refServicios.child("descripcion").setValue(descripcionServicio.getText().toString());
        refServicios.child("direccion").setValue(direccionServicio.getText().toString());

        refServiciosDelUsario.child("categoria").setValue(tipoServicio.getSelectedItem().toString());
        refServiciosDelUsario.child("nombre").setValue(nombreServicio.getText().toString());
        refServiciosDelUsario.child("telefono").setValue(telefonoServicio.getText().toString());
        refServiciosDelUsario.child("descripcion").setValue(descripcionServicio.getText().toString());
        refServiciosDelUsario.child("direccion").setValue(direccionServicio.getText().toString());




        Intent intentWorks = new Intent(getApplicationContext(), Works.class);
        startActivity(intentWorks);



    }

}
