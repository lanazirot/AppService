package lanazirot.com.appservicenld;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class Login extends AppCompatActivity {
    RelativeLayout relay1,relay2;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            relay1.setVisibility(View.VISIBLE);
            relay2.setVisibility(View.VISIBLE);
        }
    };
    public static boolean hayInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
    
    private FirebaseAuth authUsuario;
    private static ConnectivityManager managerConnectivity;

    private ProgressDialog pdVerificandoDatos;
    private TextView login;
    private EditText correo,contra;
    private TextView registrarme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //Relays
        relay1 = (RelativeLayout) findViewById(R.id.relay1);
        relay2 = (RelativeLayout) findViewById(R.id.relay2);

        //Fields
        correo = (EditText) findViewById(R.id.editText2);
        contra = (EditText) findViewById(R.id.editText);
        login = (TextView) findViewById(R.id.textView);
        authUsuario = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });
        pdVerificandoDatos =  new ProgressDialog(this);
        registrarme = (TextView) findViewById(R.id.registrarme);
        if(!hayInternet(this)){
            Toast.makeText(this, "Advertencia: Usted no cuenta con ninguna red activa", Toast.LENGTH_LONG).show();
        }
        registrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirRegistro();
            }
        });
        handler.postDelayed(runnable,2000);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(authUsuario.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(), Index.class);
            startActivity(intent);
        }
    }
    private void abrirRegistro(){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }
    private void logIn(){
        String correo = this.correo.getText().toString().trim();
        String password = contra.getText().toString().trim();
        if(TextUtils.isEmpty(correo)){
            Toast.makeText(this, "Falta llenar el campo correo", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Falta llenar el campo contrasena", Toast.LENGTH_LONG).show();
            return;
        }
        pdVerificandoDatos.setMessage("Verificando datos..");
        pdVerificandoDatos.show();
        authUsuario.signInWithEmailAndPassword(correo,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Bienvenid@", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), Index.class);
                        startActivity(intent);

                    }else{
                        if(!hayInternet(Login.this)){
                            Toast.makeText(getApplicationContext(), "Advertencia: Usted no cuenta con ninguna red activa", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Por favor, verifique que sus datos sean correctos", Toast.LENGTH_LONG).show();
                        }

                    }
                    pdVerificandoDatos.dismiss();
            }
        });
    }

}
