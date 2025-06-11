package com.caueobm.casahub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caueobm.casahub.R;
import com.caueobm.casahub.model.Cliente;

import java.util.List;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ClienteViewHolder> {

    private final List<Cliente> clientes;

    public ClientesAdapter(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cliente, parent, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder holder, int position) {
        Cliente cliente = clientes.get(position);
        holder.nome.setText(cliente.getNomeCliente());
        holder.email.setText(cliente.getEmailCliente());
        holder.telefone.setText(cliente.getTelefoneCliente());
        holder.tipoImovel.setText("Tipo: " + cliente.getTipoImovel()); // Preenche o tipo do imóvel
        holder.enderecoImovel.setText("Endereço: " + cliente.getEnderecoCompleto()); // Preenche o endereço do imóvel
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    static class ClienteViewHolder extends RecyclerView.ViewHolder {
        TextView nome, email, telefone, tipoImovel, enderecoImovel;

        public ClienteViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.tvNome);
            email = itemView.findViewById(R.id.tvEmail);
            telefone = itemView.findViewById(R.id.tvTelefone);
            tipoImovel = itemView.findViewById(R.id.tvTipoImovel); // Inicializa o novo TextView
            enderecoImovel = itemView.findViewById(R.id.tvEnderecoImovel); // Inicializa o novo TextView
        }
    }
}