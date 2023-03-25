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
import br.unoeste.appmymusics.db.dal.GeneroDAL;

public class CategoriaActivity extends AppCompatActivity {
    private ListView lvCategoria;
    private Button btConfirmar;
    private EditText etGenero;
    private LinearLayout linearLayout;
    private FloatingActionButton fabNovaCategoria; //1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        lvCategoria = findViewById(R.id.lvCategoria);
        linearLayout = findViewById(R.id.linearLayout);
        btConfirmar=findViewById(R.id.btConfirmarMusica);
        etGenero=findViewById(R.id.etMusica);
        fabNovaCategoria=findViewById(R.id.fabNovaCategoria); //2
        carregarListView("");
        linearLayout.setVisibility(View.GONE);
        btConfirmar.setOnClickListener(e->{
            cadastrarGenero();
            linearLayout.setVisibility(View.GONE);
        });
        fabNovaCategoria.setOnClickListener(e->{ //3
            linearLayout.setVisibility(View.VISIBLE);
            etGenero.setText("");
            etGenero.requestFocus();
        });
        //configurando o click longo para deletar categoria
        lvCategoria.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //slide 89 Snackbar
                Snackbar sbar;
                sbar = Snackbar.make(view, "Deseja excluir categoria?", Snackbar.LENGTH_LONG);
                sbar.show(); //exibe o snackbar
                //definindo uma ação para o snackbar
                sbar.setAction("Excluir ?", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // apagar o item do listview AQUI: int i que veio por parametro na nossa função onItemLongClick
                    }
                });
                return true;
            }
        });
    }

    private void carregarListView(String s) {
        ArrayList<Genero> generos=new GeneroDAL(this).get("");
        ArrayAdapter<Genero> adapter=new ArrayAdapter<Genero>(this, android.R.layout.simple_list_item_1,generos);
        lvCategoria.setAdapter(adapter);
    }

    private void cadastrarGenero() {

        GeneroDAL dal = new GeneroDAL(this);
        Genero genero=new Genero(etGenero.getText().toString());
        dal.salvar(genero);
        ArrayList<Genero> generos=new GeneroDAL(this).get("");
        ArrayAdapter<Genero> adapter=new ArrayAdapter<Genero>(this,
                android.R.layout.simple_list_item_1,generos);
        lvCategoria.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.itncategoria:
                // cadastrar nova categoria
                linearLayout.setVisibility(View.VISIBLE);
                etGenero.setText("");
                etGenero.requestFocus();
                break;
            case R.id.itvoltar:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2,menu);

        MenuItem searchItem = menu.findItem(R.id.itemSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {//quando clica no icone
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {//conforme vai digitando ja filtra
                carregarListView("gen_nome like '%"+s+"%'");
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}