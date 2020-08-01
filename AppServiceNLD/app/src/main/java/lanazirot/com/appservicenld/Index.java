package lanazirot.com.appservicenld;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Index extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //Charge for Username
    String username;

    private ImageView foton;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference ref;

    private TextView correo;
    private TextView nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View profil = navigationView.getHeaderView(0);
        foton =(ImageView) profil.findViewById(R.id.fotoini);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        correo = (TextView) navigationView.getHeaderView(0).findViewById(R.id.correoNav);
        nombreUsuario = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nombreusuario);
        correo.setText(auth.getCurrentUser().getEmail());
        //lo bueno
        ref = FirebaseDatabase.getInstance().getReference("Usuarios").child(user.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nombre = dataSnapshot.child("nombre").getValue().toString();
                String url = dataSnapshot.child("pp_url").getValue().toString();

                nombreUsuario.setText(nombre);
                Glide.with(getApplicationContext()).load(url).fitCenter().into(foton);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nombre = dataSnapshot.child("nombre").getValue().toString();
                String url = dataSnapshot.child("pp_url").getValue().toString();

                nombreUsuario.setText(nombre);
                Glide.with(getApplicationContext()).load(url).fitCenter().into(foton);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
          //  super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inicio, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //inicio
        if (id == R.id.nav_camera) {
                Intent intent = new Intent(getApplicationContext(), Works.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
                setTitle("Perfil");
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,new Profile()).commit();
        } else if (id == R.id.nav_slideshow) {
                setTitle("Secciones");
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,new Sections()).commit();
        } else if (id == R.id.nav_manage) {
            setTitle("Añadir un servicio");
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,new Add()).commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.salir) {
            salir();
        }else if(id==R.id.mis_servicios){
            Intent intent = new Intent(getApplicationContext(), OwnServices.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void salir(){



       new AlertDialog.Builder(this).setTitle("Salir de AppService").setMessage("Estas a punto de cerrar sesion").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               auth.signOut();
               finish();
               Intent intent =new Intent(Index.this, Login.class);
               startActivity(intent);
           }
       }).setNegativeButton(android.R.string.no, null).show();


    }


}
