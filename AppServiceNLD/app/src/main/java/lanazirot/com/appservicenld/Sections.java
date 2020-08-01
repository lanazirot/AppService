package lanazirot.com.appservicenld;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Sections extends Fragment {

    private ListView sections;
    private String values[];




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewFragmentSecciones = inflater.inflate(R.layout.fragment_secciones, container, false);

        sections = (ListView) viewFragmentSecciones.findViewById(R.id.secciones);
        values = getResources().getStringArray(R.array.categorias);

        ArrayAdapter<String> adapterSections = new ArrayAdapter<String>(getContext(),R.layout.secciones_layout,values);

        sections.setAdapter(adapterSections);


        sections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentServiciosCategoria = new Intent(getContext(), Services.class);
                Bundle bundle = new Bundle();

                String categoria = (String) sections.getItemAtPosition(position);

                bundle.putString("categoria",categoria);

                intentServiciosCategoria.putExtras(bundle);

                startActivity(intentServiciosCategoria);

            }
        });

        return viewFragmentSecciones;
    }

}
