package br.unoeste.appmymusics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import br.unoeste.appmymusics.db.bean.Genero;
import br.unoeste.appmymusics.db.bean.Musica;
import br.unoeste.appmymusics.db.dal.GeneroDAL;
import br.unoeste.appmymusics.db.dal.MusicaDAL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ListView lvMusicas;
    private LinearLayout llCadastro;
    private EditText etTitulo, etInterprete, etAno, etDuracao;
    private Spinner spinner;
    private Button btGravarMusica;
    private FloatingActionButton fabNovaMusica;
    private int EdicaoCod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Musica> musicas = new MusicaDAL(this).get("");
        MusicaDAL dal = new MusicaDAL(this);

        //Pegar por id
        lvMusicas = findViewById(R.id.lvMusicas);
        llCadastro = findViewById(R.id.llCadastro);
        etTitulo = findViewById(R.id.etTitulo);
        etInterprete = findViewById(R.id.etInterprete);
        etAno = findViewById(R.id.etAno);
        etDuracao = findViewById(R.id.etDuracao);

        spinner = findViewById(R.id.spinner);
        //Genero
        ArrayList<Genero> generos=new GeneroDAL(this).get("");
        ArrayAdapter adapterSpn = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generos);
        adapterSpn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpn);
        spinner.setOnItemSelectedListener(this);

        fabNovaMusica=findViewById(R.id.fabNovaMusica);
        btGravarMusica = findViewById(R.id.btGravarMusica);

        //Carregar lista de musicas
        CarregarMusicas("");

        //Deixar a tela de cadastro invisível
        llCadastro.setVisibility(View.INVISIBLE);

        btGravarMusica.setOnClickListener(e->{
            cadastrarMusica();
            llCadastro.setVisibility(View.INVISIBLE);
            LimparCampos();
        });

        fabNovaMusica.setOnClickListener(e->{ //3
            EdicaoCod = -1;
            llCadastro.setVisibility(View.VISIBLE);
            etAno.setText("");
            etTitulo.setText("");
            etTitulo.requestFocus();
            etInterprete.setText("");
            spinner.setOnItemSelectedListener(this);
            etDuracao.setText("");
        });

        lvMusicas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar sbar;
                sbar = Snackbar.make(view, "Deseja apagar a musica?", Snackbar.LENGTH_LONG);
                sbar.show();
                sbar.setAction("Apagar?", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // aqui apago o item do listview
                        int id = musicas.get(i).getId();
                        dal.apagar(id);
                        CarregarMusicas("");
                    }
                });
                return true;
            }
        });

        lvMusicas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                ArrayList<Musica> lista = new MusicaDAL(view.getContext()).get("");
                //Alterar aqui
                int idMusica = lista.get(i).getId();
                EdicaoCod = idMusica;
                llCadastro.setVisibility(View.VISIBLE);
                Musica musica = dal.get(EdicaoCod);
                etAno.setText(musica.getAno()+ "");
                etTitulo.setText(musica.getTitulo());
                etInterprete.setText((musica.getInterprete()));
                for (int x = 0; x < spinner.getCount(); x++) {
                    Genero gen = (Genero) spinner.getItemAtPosition(x);
                    if (musica.getGenero().getId()==gen.getId()) {
                        spinner.setSelection(x);
                        break;
                    }
                }
                etDuracao.setText(musica.getDuracao()+ "");
            }
        });

    }

    private void CarregarMusicas(String s) {
        ArrayList<Musica> musicas = new MusicaDAL(this).get(s);
        ArrayAdapter<Musica> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, musicas);
        lvMusicas.setAdapter(adapter);
    }

    private void cadastrarMusica() {
        MusicaDAL dal = new MusicaDAL(this);
        //int ano, String titulo, String interprete, Genero genero, double duracao
        if(EdicaoCod == -1) {
            Musica musica = new Musica(Integer.parseInt(etAno.getText().toString()) ,etTitulo.getText().toString(), etInterprete.getText().toString(), (Genero)spinner.getSelectedItem(), Float.parseFloat(etDuracao.getText().toString()));
            dal.salvar(musica);
            ArrayList<Musica> musicas = new MusicaDAL(this).get("");
            ArrayAdapter<Musica> adapter=new ArrayAdapter<Musica>(this,
                    android.R.layout.simple_list_item_1,musicas);
            lvMusicas.setAdapter(adapter);
        } else {
            Musica musica = new Musica(EdicaoCod ,Integer.parseInt(etAno.getText().toString()) ,etTitulo.getText().toString(), etInterprete.getText().toString(), (Genero)spinner.getSelectedItem(), Float.parseFloat(etDuracao.getText().toString()));
            dal.alterar(musica);
            ArrayList<Musica> musicas = new MusicaDAL(this).get("");
            ArrayAdapter<Musica> adapter=new ArrayAdapter<Musica>(this,
                    android.R.layout.simple_list_item_1,musicas);
            lvMusicas.setAdapter(adapter);
        }
    }

    private void LimparCampos()
    {
        etTitulo.setText("");
        etInterprete.setText("");
        etAno.setText("");
        etDuracao.setText("");
    }


    //Inserir menu na tela
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//alt ins - override - oncreate...
        getMenuInflater().inflate(R.menu.menu1, menu);//inflate pega xml do menu e transf em obj

        MenuItem searchItem = menu.findItem(R.id.searchItem2);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                CarregarMusicas("mus_titulo like '%"+s+"%'");
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //Item do menu selecionado e sua ação
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.itmusica:
                // cadastrar nova musica
                EdicaoCod = -1;
                llCadastro.setVisibility(View.VISIBLE);
                etAno.setText("");
                etInterprete.setText("");
                etInterprete.requestFocus();
                etInterprete.setText("");
                spinner.setOnItemSelectedListener(this);
                etDuracao.setText("");
                break;
            case R.id.itcategoria:
                Intent intent = new Intent(this,CategoriaActivity.class);
                startActivity(intent);
                break;
            case R.id.itfechar:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}