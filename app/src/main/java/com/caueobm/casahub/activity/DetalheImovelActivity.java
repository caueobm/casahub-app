package com.caueobm.casahub.activity; // Ajuste seu package se necessário

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.View;

import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.caueobm.casahub.R;
import com.caueobm.casahub.ui.BaseActivity;
import com.caueobm.casahub.util.TokenManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.caueobm.casahub.MainActivity;
import com.caueobm.casahub.model.Imovel;
import com.caueobm.casahub.network.ImovelService;
import com.caueobm.casahub.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class DetalheImovelActivity extends BaseActivity {

    public static final String EXTRA_IMOVEL_ID = "IMOVEL_ID"; // Chave consistente

    private ProgressBar progressBar; // Refatorado: progressBarDetalhe -> progressBar
    private TextView tvError;       // Refatorado: tvDetalheError -> tvError

    // Declare todos os seus TextViews para os detalhes do imóvel
    private TextView tvTipo, tvEnderecoCompleto, tvCidadeEstadoCep, tvValor; // Refatorado
    private TextView tvDescricao, tvQuartos, tvBanheiros, tvMetragem;     // Refatorado
    private TextView tvMobiliado, tvDisponivel, tvDataCadastro;            // Refatorado

    private MaterialButton btnMarcarInteresse;
    private ImovelService imovelService;
    private TokenManager tokenManager;
    private long imovelIdRecebido = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_imovel);
        configurarToolbar();

        // Inicializar o TokenManager
        tokenManager = new TokenManager(getApplicationContext());

        // 1. Inicializar Views
        progressBar = findViewById(R.id.progressBar);
        tvError = findViewById(R.id.tvError);
        tvTipo = findViewById(R.id.tvTipo);
        tvEnderecoCompleto = findViewById(R.id.tvEnderecoCompleto);
        tvCidadeEstadoCep = findViewById(R.id.tvCidadeEstadoCep);
        tvValor = findViewById(R.id.tvValor);
        tvDescricao = findViewById(R.id.tvDescricao);
        tvQuartos = findViewById(R.id.tvQuartos);
        tvBanheiros = findViewById(R.id.tvBanheiros);
        tvMetragem = findViewById(R.id.tvMetragem);
        tvMobiliado = findViewById(R.id.tvMobiliado);
        tvDisponivel = findViewById(R.id.tvDisponivel);
        tvDataCadastro = findViewById(R.id.tvDataCadastro);

        btnMarcarInteresse = findViewById(R.id.btnMarcarInteresse);

        btnMarcarInteresse.setOnClickListener(v -> {
            if (!tokenManager.isLoggedIn()) {
                Toast.makeText(this, "Você precisa estar logado para marcar interesse", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DetalheImovelActivity.this, LoginActivity.class);
                intent.putExtra("redirectAfterLogin", true);
                startActivity(intent);
                return;
            }

            registrarInteresseNoImovel(imovelIdRecebido);
        });
        // Adicionando funcao do botao voltar
        Button btnVoltar = findViewById(R.id.btnVoltarTelaInicial);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalheImovelActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        // 2. Inicializar Retrofit Service
        imovelService = RetrofitClient.getClient(getApplicationContext()).create(ImovelService.class);

        // 3. Obter o ID do imóvel do Intent e carregar os detalhes
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_IMOVEL_ID)) {
            imovelIdRecebido = intent.getLongExtra(EXTRA_IMOVEL_ID, -1L); // -1L como valor padrão
        }

        if (imovelIdRecebido != -1) {
            Log.d("DetalheImovelActivity", "ID do Imóvel recebido: " + imovelIdRecebido);
            carregarDetalhesDoImovel(imovelIdRecebido);
        } else {
            Log.e("DetalheImovelActivity", "ID do Imóvel não recebido ou inválido.");
            mostrarErro("Erro: ID do imóvel não encontrado."); // Usa seu método mostrarErro
        }
    }

    private void carregarDetalhesDoImovel(long id) {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);

        // imovelService deve estar inicializado
        // imovelService = RetrofitClient.getClient().create(ImovelService.class);

        imovelService.getImovelPorId(id).enqueue(new Callback<Imovel>() { // Use o 'id' recebido
            @Override
            public void onResponse(@NonNull Call<Imovel> call, @NonNull Response<Imovel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Imovel imovel = response.body();
                    preencherCampos(imovel); // Seu método para mostrar os dados na UI
                } else {
                    Log.e("DetalheImovelActivity", "Erro na resposta da API: " + response.code() + " - " + response.message());
                    mostrarErro("Erro ao carregar os dados do imóvel. Código: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Imovel> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("DetalheImovelActivity", "Falha na requisição: ", t);
                mostrarErro("Falha na conexão: " + t.getMessage());
            }
        });
    }

    private void preencherCampos(Imovel imovel) {
        tvTipo.setText(imovel.getTipo());
        tvEnderecoCompleto.setText(imovel.getEndereco());
        tvCidadeEstadoCep.setText(imovel.getCidade() + " - " + imovel.getEstado() + ", " + imovel.getCep());

        String valorFormatado = "R$ " + String.format(Locale.getDefault(), "%.2f",
                imovel.getValor() != null ? imovel.getValor() : imovel.getValorAluguel());
        tvValor.setText(valorFormatado);

        tvDescricao.setText(imovel.getDescricao());
        tvQuartos.setText(imovel.getNumeroQuartos() + " quarto(s)");
        tvBanheiros.setText(imovel.getNumeroBanheiros() + " banheiro(s)");
        tvMetragem.setText(imovel.getMetragem() + " m²");
        tvMobiliado.setText(imovel.isMobiliado() ? "Sim" : "Não");
        tvDisponivel.setText(imovel.isDisponivel() ? "Disponível" : "Indisponível");
        tvDataCadastro.setText("Cadastrado em: " + imovel.getDataCadastro());
    }

    private void mostrarErro(String mensagem) {
        tvError.setText(mensagem);
        tvError.setVisibility(View.VISIBLE);
    }

    private void registrarInteresseNoImovel(long imovelId) {
        imovelService.registrarInteresse(imovelId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetalheImovelActivity.this, "Interesse registrado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetalheImovelActivity.this, "Falha ao registrar interesse: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("DetalheImovel", "Erro: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DetalheImovelActivity.this, "Erro na requisição: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("DetalheImovel", "Falha na API", t);
            }
        });
    }

}