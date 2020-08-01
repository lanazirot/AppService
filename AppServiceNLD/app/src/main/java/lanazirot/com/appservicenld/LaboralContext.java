package lanazirot.com.appservicenld;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LaboralContext extends AppCompatActivity {


    private EditText ocupacion, servicio;
    private Spinner tipousuario;
    private TextView registro;
    private FirebaseAuth auth;
    private DatabaseReference refUsuarios;
    private Bundle bundleDatosAnteriores;


    private ProgressDialog pdCreandoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campo_laboral);
        getSupportActionBar().hide();
        pdCreandoUsuario = new ProgressDialog(this);
        ocupacion = (EditText) findViewById(R.id.ocupacion);
        servicio = (EditText) findViewById(R.id.servicio);
        tipousuario = (Spinner) findViewById(R.id.spinner);
        registro = (TextView) findViewById(R.id.listo);


        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroFinal();
            }
        });

        auth = FirebaseAuth.getInstance();
        refUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        bundleDatosAnteriores = this.getIntent().getExtras();
    }

    private boolean estaVacio(String s) {
        return TextUtils.isEmpty(s);
    }

    private void validar() {
        if (!estaVacio(ocupacion.getText().toString()) && !estaVacio(servicio.getText().toString())) {
            registroFinal();
        } else {
            Toast.makeText(getApplicationContext(), "Ayuda a tus clientes rellenando toda la informacion", Toast.LENGTH_SHORT).show();
        }
    }


    private void registroFinal() {


        pdCreandoUsuario.setTitle("AppService esta creando tu usuario");
        pdCreandoUsuario.setMessage("Estamos trabajando para crear tu nuevo usuario. ¡Ten paciencia!");
        pdCreandoUsuario.show();

        final String name = bundleDatosAnteriores.getString("nombre");
        final String age = bundleDatosAnteriores.getString("edad");

        final String occupation = ocupacion.getText().toString().trim();
        final String service = servicio.getText().toString().trim();
        final String type = tipousuario.getSelectedItem().toString();

        //Dos de auth
        final String email = bundleDatosAnteriores.getString("correo");
        final String pass = bundleDatosAnteriores.getString("contra");

        //RTDB

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String idUsuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    User usuario = new User(idUsuario, name, email, pass, age, occupation, service, type);

                    refUsuarios.child(idUsuario).setValue(usuario);
                    refUsuarios.child(idUsuario).child("pp_url").setValue("https://firebasestorage.googleapis.com/v0/b/appservicenld.appspot.com/o/perfil.png?alt=media&token=d2eeb923-3d3e-49b3-b84d-0ee8fd342144");

                    pdCreandoUsuario.dismiss();


                    Intent intentInicio = new Intent(getApplicationContext(), Index.class);
                    startActivity(intentInicio);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "AppService error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                Intent intentRegistrarme = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intentRegistrarme);
                finish();

            }
        });


    }
}


