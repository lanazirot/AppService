package lanazirot.com.appservicenld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {
    private EditText correoUsuario, contraUsuario, confirmarContraUsuario, nombreUsuario, ocupacionUsuario, edadUsuario;
    private TextView confirmaRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarme);
        getSupportActionBar().hide();

        correoUsuario = (EditText) findViewById(R.id.correo);
        contraUsuario = (EditText) findViewById(R.id.contra);
        confirmarContraUsuario = (EditText) findViewById(R.id.confirmacontra);
        nombreUsuario = (EditText) findViewById(R.id.nombre);
        edadUsuario = (EditText) findViewById(R.id.edad);
        confirmaRegistro = (TextView) findViewById(R.id.siguiente);
        confirmaRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarRegistro();
            }
        });
    }

    private boolean estaVacio(String s) {
        return TextUtils.isEmpty(s);
    }

    private void verificarRegistro() {
        if (!estaVacio(correoUsuario.getText().toString()) && !estaVacio(contraUsuario.getText().toString()) && !estaVacio(confirmarContraUsuario.getText().toString()) && !estaVacio(nombreUsuario.getText().toString()) && !estaVacio(edadUsuario.getText().toString())) {
            if(contraUsuario.getText().toString().equals(confirmarContraUsuario.getText().toString())){
                registro();
            }else{
                Toast.makeText(getApplicationContext(), "Las contrasenas no coinciden", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Disuclpa, creemos que falta algun dato", Toast.LENGTH_SHORT).show();
        }
    }
    private void registro() {
        finish();

        final String correo_i = correoUsuario.getText().toString().trim();
        final String contra_i = contraUsuario.getText().toString().trim();
        final String nombre_i = nombreUsuario.getText().toString().trim();
        final String edad_i = edadUsuario.getText().toString().trim();

        Intent intent = new Intent(SignUp.this, LaboralContext.class);
        Bundle bundle = new Bundle();
        bundle.putString("correo",correo_i);
        bundle.putString("nombre",nombre_i);
        bundle.putString("contra",contra_i);
        bundle.putString("edad",edad_i);

        intent.putExtras(bundle);
        startActivity(intent);
    }
}
