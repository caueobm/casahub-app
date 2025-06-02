package com.caueobm.casahub.activity; // Ajuste seu package se necessário

import android.content.Intent;
import android.os.Bundle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.MenuItem; // Para o botão "Up" (voltar) na ActionBar
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.caueobm.casahub.R; // Importe o R do seu pacote
import com.caueobm.casahub.model.Imovel;
import com.caueobm.casahub.network.ImovelService;
import com.caueobm.casahub.network.RetrofitClient;

import java.util.Locale;


public class DetalheImovelActivity extends AppCompatActivity {

    public static final String EXTRA_IMOVEL_ID = "IMOVEL_ID"; // Chave consistente

    private ProgressBar progressBarDetalhe;
    private TextView tvDetalheError;
    // Declare todos os seus TextViews para os detalhes do imóvel
    private TextView tvDetalheTipo, tvDetalheEnderecoCompleto, tvDetalheCidadeEstadoCep, tvDetalheValor;
    private TextView tvDetalheDescricao, tvDetalheQuartos, tvDetalheBanheiros, tvDetalheMetragem;
    private TextView tvDetalheMobiliado, tvDetalheDisponivel, tvDetalheDataCadastro;

    private ImovelService imovelService;
    private long imovelIdRecebido = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_imovel);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detalhes do Imóvel");
        }

        // Inicializar Views
        progressBarDetalhe = findViewById(R.id.progressBarDetalhe);
        tvDetalheError = findViewById(R.id.tvDetalheError);
        tvDetalheTipo = findViewById(R.id.tvDetalheTipo);
        tvDetalheEnderecoCompleto = findViewById(R.id.tvDetalheEnderecoCompleto);
        tvDetalheCidadeEstadoCep = findViewById(R.id.tvDetalheCidadeEstadoCep);
        tvDetalheValor = findViewById(R.id.tvDetalheValor);
        tvDetalheDescricao = findViewById(R.id.tvDetalheDescricao);
        tvDetalheQuartos = findViewById(R.id.tvDetalheQuartos);
        tvDetalheBanheiros = findViewById(R.id.tvDetalheBanheiros);
        tvDetalheMetragem = findViewById(R.id.tvDetalheMetragem);
        tvDetalheMobiliado = findViewById(R.id.tvDetalheMobiliado);
        tvDetalheDisponivel = findViewById(R.id.tvDetalheDisponivel);
        tvDetalheDataCadastro = findViewById(R.id.tvDetalheDataCadastro);

        // Inicializar Retrofit Service
        imovelService = RetrofitClient.getClient().create(ImovelService.class);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_IMOVEL_ID)) {
            imovelIdRecebido = intent.getLongExtra(EXTRA_IMOVEL_ID, -1L);
        }

        if (imovelIdRecebido != -1) {
            Log.d("DetalheImovelActivity", "ID do Imóvel recebido: " + imovelIdRecebido);
            carregarDetalhesDoImovel(imovelIdRecebido); // Nome do método alterado para clareza
        }
    }

    private void carregarDetalhesDoImovel(long id) {
        progressBarDetalhe.setVisibility(View.VISIBLE);
        tvDetalheError.setVisibility(View.GONE);

        imovelService.getImovelPorId(id).enqueue(new Callback<Imovel>() {
            @Override
            public void onResponse(@NonNull Call<Imovel> call, @NonNull Response<Imovel> response) {
                progressBarDetalhe.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Imovel imovel = response.body();
                    preencherCampos(imovel);
                } else {
                    mostrarErro("Erro ao carregar os dados do imóvel.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Imovel> call, @NonNull Throwable t) {
                progressBarDetalhe.setVisibility(View.GONE);
                mostrarErro("Falha na conexão: " + t.getMessage());
            }
        });
    }

    private void preencherCampos(Imovel imovel) {
        tvDetalheTipo.setText(imovel.getTipo());
        tvDetalheEnderecoCompleto.setText(imovel.getEndereco());
        tvDetalheCidadeEstadoCep.setText(imovel.getCidade() + " - " + imovel.getEstado() + ", " + imovel.getCep());

        String valorFormatado = "R$ " + String.format(Locale.getDefault(), "%.2f",
                imovel.getValor() != null ? imovel.getValor() : imovel.getValorAluguel());
        tvDetalheValor.setText(valorFormatado);

        tvDetalheDescricao.setText(imovel.getDescricao());
        tvDetalheQuartos.setText(imovel.getNumeroQuartos() + " quarto(s)");
        tvDetalheBanheiros.setText(imovel.getNumeroBanheiros() + " banheiro(s)");
        tvDetalheMetragem.setText(imovel.getMetragem() + " m²");
        tvDetalheMobiliado.setText(imovel.isMobiliado() ? "Sim" : "Não");
        tvDetalheDisponivel.setText(imovel.isDisponivel() ? "Disponível" : "Indisponível");
        tvDetalheDataCadastro.setText("Cadastrado em: " + imovel.getDataCadastro());
    }

    private void mostrarErro(String mensagem) {
        tvDetalheError.setText(mensagem);
        tvDetalheError.setVisibility(View.VISIBLE);
    }

}