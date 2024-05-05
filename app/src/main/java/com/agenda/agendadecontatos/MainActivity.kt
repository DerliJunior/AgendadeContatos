package com.agenda.agendadecontatos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.agenda.agendadecontatos.adapter.ContatoAdapter
import com.agenda.agendadecontatos.dao.UsuarioDao
import com.agenda.agendadecontatos.databinding.ActivityMainBinding
import com.agenda.agendadecontatos.formatter.PhoneFormatter
import com.agenda.agendadecontatos.formatter.PhoneFormatter.Companion.formatarTelefone
import com.agenda.agendadecontatos.model.CurrentUser
import com.agenda.agendadecontatos.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var contatoAdapter: ContatoAdapter
    private var currentUser: CurrentUser =
        CurrentUser("MockDerli", "MockJunior", "25", "11999716735", 1)
    private val _listaUsuarios = MutableLiveData<MutableList<Usuario>>()

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {


            withContext(Dispatchers.Main) {


                _listaUsuarios.observe(this@MainActivity) { listaUsuarios ->
                    val recyclerViewContatos = binding.recyclerViewContatos
                    recyclerViewContatos.layoutManager = LinearLayoutManager(this@MainActivity)
                    recyclerViewContatos.setHasFixedSize(true)
                    contatoAdapter = ContatoAdapter(this@MainActivity, listaUsuarios)
                    recyclerViewContatos.adapter = contatoAdapter
                    contatoAdapter.notifyDataSetChanged()


                    _listaUsuarios.value?.let { listaUsuarios ->
                        if (listaUsuarios.isNotEmpty()) {
                            currentUser.id = listaUsuarios[0].uid
                            currentUser.nome = listaUsuarios[0].nome
                            currentUser.sobrenome = listaUsuarios[0].sobrenome
                            currentUser.idade = listaUsuarios[0].idade
                            currentUser.telefone = listaUsuarios[0].celular
                        }
                    }

                    binding.textNomeSobrenome.text = "${currentUser.nome} ${currentUser.sobrenome}"
                    binding.textTelefone.text = formatarTelefone(currentUser.telefone)
                }
            }
        }

        binding.btCadastrar.setOnClickListener {
            val navegarTelaCadastro = Intent(this, CadastrarUsuario::class.java)

            startActivity(navegarTelaCadastro)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch {
            getContatos()

            withContext(Dispatchers.Main) {

                _listaUsuarios.observe(this@MainActivity) { listaUsuarios ->
                    val recyclerViewContatos = binding.recyclerViewContatos
                    recyclerViewContatos.layoutManager = LinearLayoutManager(this@MainActivity)
                    recyclerViewContatos.setHasFixedSize(true)
                    contatoAdapter = ContatoAdapter(this@MainActivity, listaUsuarios)
                    recyclerViewContatos.adapter = contatoAdapter
                    contatoAdapter.notifyDataSetChanged()


                    _listaUsuarios.value?.let { listaUsuarios ->
                        if (listaUsuarios.isNotEmpty()) {
                            currentUser.id = listaUsuarios[0].uid
                            currentUser.nome = listaUsuarios[0].nome
                            currentUser.sobrenome = listaUsuarios[0].sobrenome
                            currentUser.idade = listaUsuarios[0].idade
                            currentUser.telefone = listaUsuarios[0].celular
                        }
                    }

                    binding.textNomeSobrenome.text = "${currentUser.nome} ${currentUser.sobrenome} (Meu Número)"
                    binding.textTelefone.text = PhoneFormatter.formatarTelefone(currentUser.telefone)
                }
            }
        }
    }

    private fun getContatos() {
        usuarioDao = AppDatabase.getInstance(this).usuarioDao()

        val listaUsuarios: MutableList<Usuario> = usuarioDao.get()

        _listaUsuarios.postValue(listaUsuarios)
    }
}