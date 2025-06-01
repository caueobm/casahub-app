package com.caueobm.casahub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.caueobm.casahub.model.Imovel;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Locale;
import com.caueobm.casahub.R;
import com.caueobm.casahub.model.Imovel;

import java.util.List;

public class ImovelAdapter extends RecyclerView.Adapter<ImovelAdapter.ImovelViewHolder> {

    private List<Imovel> imovelList;
    private OnImovelAction listener;

    public interface OnImovelAction {
        void onVerDetalhesClick(long imovelId); // Assumindo que o ID é um long. Ajuste se for int ou String.
    }
    public void setOnImovelActionListener(OnImovelAction listener) {
        this.listener = listener;
    }

    public ImovelAdapter(List<Imovel> imovelList) {
        this.imovelList = imovelList;
    }

    @NonNull
    @Override
    public ImovelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_imovel, parent, false);
        return new ImovelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImovelViewHolder holder, int position) {
        Imovel imovelAtual = imovelList.get(position);
        holder.tvTipo.setText(imovelAtual.getTipo());
        holder.tvEndereco.setText(imovelAtual.getEndereco());

        if (imovelAtual.getValor() != null) {
            holder.tvValor.setText(String.format(Locale.getDefault(), "Venda: R$ %,.2f", imovelAtual.getValor()));
            holder.tvValor.setVisibility(View.VISIBLE);
        } else if (imovelAtual.getValorAluguel() != null) {
            holder.tvValor.setText(String.format(Locale.getDefault(), "Aluguel: R$ %,.2f/mês", imovelAtual.getValorAluguel()));
            holder.tvValor.setVisibility(View.VISIBLE);
        } else {
            holder.tvValor.setText("Preço não disponível"); // Ou oculte: holder.tvValor.setVisibility(View.GONE);
            holder.tvValor.setVisibility(View.VISIBLE);
        }

        holder.btnVerDetalhes.setOnClickListener(v -> {
            if (listener != null && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                Imovel clickedImovel = imovelList.get(holder.getAdapterPosition());
                // Certifique-se que seu Imovel.java tem um metodo getId()
                // e que o tipo do ID (long, int, String) corresponde ao da interface
                listener.onVerDetalhesClick(clickedImovel.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return imovelList == null ? 0 : imovelList.size();
    }

    public static class ImovelViewHolder extends RecyclerView.ViewHolder {
        TextView tvTipo, tvEndereco, tvValor;
        Button btnVerDetalhes;

        public ImovelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTipo = itemView.findViewById(R.id.tvTipo);
            tvEndereco = itemView.findViewById(R.id.tvEndereco);
            tvValor = itemView.findViewById(R.id.tvValor);
            btnVerDetalhes = itemView.findViewById(R.id.btnVerDetalhes);
        }
    }

    // Metodo para atualizar a lista (boa prática se você for implementar "swipe to refresh" ou atualizações)
    public void setImoveis(List<Imovel> novosImoveis) {
        this.imovelList = novosImoveis;
        notifyDataSetChanged(); // Notifica o RecyclerView que os dados mudaram
    }
}
