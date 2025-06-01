package com.caueobm.casahub.activity; // Ajuste seu package

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Opcional, para ActionBar/Toolbar

import com.caueobm.casahub.R;
import com.caueobm.casahub.model.Imovel;
import com.caueobm.casahub.network.ImovelService;
import com.caueobm.casahub.network.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalheImovelActivity extends AppCompatActivity {

    public static final String EXTRA_IMOVEL_ID = "extra_imovel_id"; // Constante para a chave do ID

    private TextView tvTipo, tvEnderecoCompleto, tvCidadeEstadoCep, tvValor, tvDescricao;
    private TextView tvQuartos;

}