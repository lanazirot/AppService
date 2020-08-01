package lanazirot.com.appservicenld;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


public class Add extends Fragment {


    private EditText nombre,direccion,telefono,descripcion;
    private ImageView foto;
    private Button confirm;
    private Uri uriFotografia;
    private Spinner categoria;

    private DatabaseReference dbRefServicios, dbRefUsuarios;
    private StorageReference initRef;
    private FirebaseUser user;

    private boolean haEntrado = true;

    private ProgressDialog pdCreando;

    private String file;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_add, container, false);
        nombre = (EditText) v.findViewById(R.id.nombre);
        direccion = (EditText) v.findViewById(R.id.direccion);
        telefono = (EditText) v.findViewById(R.id.telefono);
        descripcion = (EditText) v.findViewById(R.id.descripcion);
        categoria = (Spinner) v.findViewById(R.id.categoria);

        foto = (ImageView) v.findViewById(R.id.foto);

        Button pick = (Button) v.findViewById(R.id.pick);
        confirm = (Button) v.findViewById(R.id.confirmar);

        dbRefServicios = FirebaseDatabase.getInstance().getReference("Servicios");
        dbRefUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        initRef = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        pdCreando = new ProgressDialog(getContext());

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               validForm();
            }
        });



        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 27:
                        new AlertDialog.Builder(getContext()).setTitle("Lo sentimos").setMessage("AppService se disculpa por no tener tu servicio" +
                                " dentro de nuestras categorias. Ayudanos a mejorar en la seccion 'Ayuda' dentro del menu.").setNeutralButton("Confirmar",null).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        return v;
    }

    public void validForm(){
        if(TextUtils.isEmpty(nombre.getText())||TextUtils.isEmpty(direccion.getText())||TextUtils.isEmpty(telefono.getText())||TextUtils.isEmpty(descripcion.getText())|| uriFotografia ==null){
            new AlertDialog.Builder(getContext()).setTitle("Whoops!").setMessage("Tu nuevo servicio debe contener todos los campos.").setNeutralButton("De acuerdo",null).show();
            return;
        }else{
            registrarServicio();
        }

    }



    public void pickImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == 1 && resultCode== RESULT_OK){
             uriFotografia = data.getData();
             foto.setImageURI(uriFotografia);
             haEntrado =true;
             foto.setOnLongClickListener(new View.OnLongClickListener() {
                 @Override
                 public boolean onLongClick(View v) {

                        if(haEntrado){
                            new AlertDialog.Builder(getContext()).setTitle("Tu fotografia").setMessage("Eliminar tu fotografia").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    foto.setImageURI(null);
                                    uriFotografia =null;
                                    haEntrado = false;
                                }
                            }).setNegativeButton(android.R.string.no, null).show();
                        }
                         return false;
                     }
             });
         }
    }
    public void registrarServicio(){

        pdCreando.setTitle("Vamos a agregar tu nuevo servicio");
        pdCreando.setMessage("Estamos trabajando en ello...");
        pdCreando.show();



        Service servicio = new Service(nombre.getText().toString(), direccion.getText().toString(),
                telefono.getText().toString(), descripcion.getText().toString(),
                categoria.getSelectedItem().toString(), user.getUid().toString());

        dbRefServicios.child(user.getUid()+nombre.getText().toString()).setValue(servicio);
        dbRefUsuarios.child(user.getUid()).child("servicios").child(user.getUid()+nombre.getText().toString()).setValue(servicio);

        StorageReference refUser = FirebaseStorage.getInstance().getReference().child(user.getUid()+"GOTO="+nombre.getText().toString());

        UploadTask uploadTaskAdd = refUser.putFile(uriFotografia);

        uploadTaskAdd.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri download = taskSnapshot.getDownloadUrl();
                dbRefServicios.child(user.getUid()+nombre.getText().toString()).child("urlimage").setValue(download.toString());
                dbRefUsuarios.child(user.getUid()).child("servicios").child(user.getUid()+nombre.getText().toString()).child("urlimage").setValue(download.toString());


                pdCreando.dismiss();

                limpiarFields();
            }
        });




    }
    public void limpiarFields(){
        haEntrado =false;
        nombre.setText("");
        direccion.setText("");
        telefono.setText("");
        descripcion.setText("");
        foto.setImageURI(null);


        new AlertDialog.Builder(getContext()).setTitle("De parte de AppService...").setMessage("Hemos agregado tu nuevo servicio. Esperamos que tu servicio tenga exito dentro de la plataforma y tus " +
                "clientes sean complacidos.").setNeutralButton("Gracias", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();

    }


}
