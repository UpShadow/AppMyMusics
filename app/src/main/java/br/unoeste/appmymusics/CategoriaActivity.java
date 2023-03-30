package br.unoeste.appmymusics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import br.unoeste.appmymusics.db.bean.Genero;
import br.unoeste.appmymusics.db.bean.Musica;
import br.unoeste.appmymusics.db.dal.GeneroDAL;
import br.unoeste.appmymusics.db.dal.MusicaDAL;

public class CategoriaActivity extends AppCompatActivity {

    private ListView lvCategoria;
    private LinearLayout linearLayout;
    private Button btConfirmar;
    private EditText etGenero;
    private FloatingActionButton fabNovaCategoria;
    private int EdicaoCod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);

        ArrayList<Genero> genero = new GeneroDAL(this).get("");
        GeneroDAL dal = new GeneroDAL(this);

        lvCategoria = findViewById(R.id.lvCategoria);
        linearLayout = findViewById(R.id.linearLayout);
        btConfirmar = findViewById(R.id.btConfirmar);
        etGenero = findViewById(R.id.etGenero);
        fabNovaCategoria = findViewById(R.id.fabNovaCategoria);

        CarregarLista("");

        linearLayout.setVisibility(View.INVISIBLE);

        btConfirmar.setOnClickListener(e->{
            cadastrarGenero();
            linearLayout.setVisibility(View.INVISIBLE);
        });

        fabNovaCategoria.setOnClickListener(e->{
            EdicaoCod = -1;
            linearLayout.setVisibility(View.VISIBLE);
            etGenero.setText("");
            etGenero.requestFocus();
        });

        lvCategoria.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar sbar;
                sbar = Snackbar.make(view, "Deseja apagar a categoria?", Snackbar.LENGTH_LONG);
                sbar.show();
                sbar.setAction("Apagar?", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // aqui apago o item do listview
                        int id = genero.get(i).getId();
                        dal.apagar(id);
                        CarregarLista("");
                    }
                });
                return true;
            }
        });

        lvCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                ArrayList<Genero> lista = new GeneroDAL(view.getContext()).get("");
                //Alterar aqui
                int idGenero = lista.get(i).getId();
                EdicaoCod = idGenero;
                linearLayout.setVisibility(View.VISIBLE);
                Genero genero = dal.get(EdicaoCod);
                etGenero.setText(genero.getNome());
            }
        });
    }

    private void CarregarLista(String s) {
        ArrayList<Genero> generos = new GeneroDAL(this).get(s);
        ArrayAdapter<Genero> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, generos);
        lvCategoria.setAdapter(adapter);
    }

    private void cadastrarGenero() {
        GeneroDAL dal = new GeneroDAL(this);
        if(EdicaoCod == -1) {
            Genero genero = new Genero(etGenero.getText().toString());
            dal.salvar(genero);
            //exibindo a lista novamente
            CarregarLista("");
        } else {
            Genero genero = new Genero(EdicaoCod ,etGenero.getText().toString());
            dal.alterar(genero);
            //exibindo a lista novamente
            CarregarLista("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        MenuItem searchItem = menu.findItem(R.id.searchItem);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                CarregarLista("gen_nome like '%"+s+"%'");
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.itncategoria:
                EdicaoCod = -1;
                linearLayout.setVisibility(View.VISIBLE);
                etGenero.setText("");
                etGenero.requestFocus();
                break;
            case R.id.itvoltar:
                onBackPressed();//volta
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}