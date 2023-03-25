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
    private ListView lvMusica;
    private Button btConfirmar;
    private EditText etAno;
    private EditText etMusica;
    private EditText etInterprete;
    private Spinner spnGenero;
    private EditText etDuracao;
    private LinearLayout linearLayout;
    private FloatingActionButton fabNovaMusica; //1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvMusica = findViewById(R.id.lvMusica);
        linearLayout = findViewById(R.id.linearLayout);
        btConfirmar = findViewById(R.id.btConfirmarMusica);
        etAno = findViewById(R.id.etAno);
        etMusica = findViewById(R.id.etMusica);
        etInterprete = findViewById(R.id.etInterprete);
        spnGenero = findViewById(R.id.spnGenero);
        //Genero
        ArrayList<Genero> generos=new GeneroDAL(this).get("");
        ArrayAdapter adapterSpn = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generos);
        adapterSpn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGenero.setAdapter(adapterSpn);
        spnGenero.setOnItemSelectedListener(this);
        etDuracao = findViewById(R.id.etDuracao);
        fabNovaMusica=findViewById(R.id.fabNovaMusica); //2
        carregarListView("");
        linearLayout.setVisibility(View.GONE);
        btConfirmar.setOnClickListener(e->{
            cadastrarMusica();
            linearLayout.setVisibility(View.GONE);
        });
        fabNovaMusica.setOnClickListener(e->{ //3
            linearLayout.setVisibility(View.VISIBLE);
            etAno.setText("");
            etMusica.setText("");
            etMusica.requestFocus();
            etInterprete.setText("");
            spnGenero.setOnItemSelectedListener(this);
            etDuracao.setText("");
        });
        //configurando o click longo para deletar musica
        lvMusica.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //slide 89 Snackbar
                Snackbar sbar;
                sbar = Snackbar.make(view, "Deseja excluir a musica?", Snackbar.LENGTH_LONG);
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
        ArrayList<Musica> musicas = new MusicaDAL(this).get("");
        ArrayAdapter<Musica> adapter=new ArrayAdapter<Musica>(this, android.R.layout.simple_list_item_1,musicas);
        lvMusica.setAdapter(adapter);
    }

    private void cadastrarMusica() {
        MusicaDAL dal = new MusicaDAL(this);
        //int ano, String titulo, String interprete, Genero genero, double duracao
        Musica musica = new Musica(Integer.parseInt(etAno.getText().toString()) ,etMusica.getText().toString(), etInterprete.getText().toString(), (Genero)spnGenero.getSelectedItem(), Float.parseFloat(etDuracao.getText().toString()));
        dal.salvar(musica);
        ArrayList<Musica> musicas = new MusicaDAL(this).get("");
        ArrayAdapter<Musica> adapter=new ArrayAdapter<Musica>(this,
                android.R.layout.simple_list_item_1,musicas);
        lvMusica.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.itmusica:
                // cadastrar nova musica
                linearLayout.setVisibility(View.VISIBLE);
                etAno.setText("");
                etMusica.setText("");
                etMusica.requestFocus();
                etInterprete.setText("");
                spnGenero.setOnItemSelectedListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);

        MenuItem searchItem = menu.findItem(R.id.itemSearchMusics);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {//quando clica no icone
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {//conforme vai digitando ja filtra
                carregarListView("mus_titulo like '%"+s+"%'");
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}