package com.agenda.agendadecontatos.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agenda.agendadecontatos.AppDatabase
import com.agenda.agendadecontatos.AtualizarUsuario
import com.agenda.agendadecontatos.dao.UsuarioDao
import com.agenda.agendadecontatos.databinding.ContatoItemBinding
import com.agenda.agendadecontatos.formatter.PhoneFormatter.Companion.formatarTelefone
import com.agenda.agendadecontatos.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContatoAdapter(
    private val context: Context, private val listaUsuarios: MutableList<Usuario>
) : RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        val itemLista = ContatoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ContatoViewHolder(itemLista)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        val hasNomeLongo =
            listaUsuarios[position].nome.split(" ").size > 1 || listaUsuarios [position].nome.length > 13

        if (hasNomeLongo) {
            holder.txtNome.text = "${listaUsuarios[position].nome.substring(0,9)}..."
        }else if((listaUsuarios[position].nome.length + listaUsuarios[position].sobrenome.length) > 13)  {
            holder.txtNome.text = "${listaUsuarios[position].nome} ${listaUsuarios[position].sobrenome.substring(0, 1)}."
        }else {
            holder.txtNome.text =
                "${listaUsuarios[position].nome} ${listaUsuarios[position].sobrenome}"

        }
        
        holder.txtCelular.text = formatarTelefone(listaUsuarios[position].celular)

        holder.btAtualizar.setOnClickListener {
            val intent = Intent(context, AtualizarUsuario::class.java)
            intent.putExtra("nome", listaUsuarios[position].nome)
            intent.putExtra("sobrenome", listaUsuarios[position].sobrenome)
            intent.putExtra("idade", listaUsuarios[position].idade)
            intent.putExtra("celular", listaUsuarios[position].celular)
            intent.putExtra("uid", listaUsuarios[position].uid)
            context.startActivity(intent)
        }

        holder.btDeletar.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                val usuario = listaUsuarios[position]
                val usuarioDao: UsuarioDao = AppDatabase.getInstance(context).usuarioDao()
                usuarioDao.deletar(usuario.uid)
                listaUsuarios.remove(usuario)

                withContext(Dispatchers.Main) {
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount() = listaUsuarios.size

    inner class ContatoViewHolder(binding: ContatoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val txtNome = binding.txtNome
        val txtCelular = binding.txtCelular
        val btAtualizar = binding.btAtualizar
        val btDeletar = binding.btDeletar
    }
}