package com.agenda.agendadecontatos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.agenda.agendadecontatos.dao.UsuarioDao
import com.agenda.agendadecontatos.databinding.ActivityCadastrarUsuarioBinding
import com.agenda.agendadecontatos.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastrarUsuario : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarUsuarioBinding
    private lateinit var usuarioDao: UsuarioDao
    private val listaUsuarios: MutableList<Usuario> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastrarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btCadastrar.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                getContatos()

                val nome = binding.editNome.text.toString()
                val sobrenome = binding.editSobrenome.text.toString()
                val idade = binding.editIdade.text.toString()
                val celular = binding.editCelular.text.toString()
                var contatoExistente: String = ""
                var mensagem = 0

                if (nome.isEmpty() || sobrenome.isEmpty() || idade.isEmpty() || celular.isEmpty()) {
                    mensagem = 2
                }

                if (mensagem != 2) {

                    if(listaUsuarios.isNotEmpty()){
                        for (usuario in listaUsuarios) {
                            if (usuario.celular == celular) {
                                mensagem = 1
                                contatoExistente = usuario.nome
                            }
                        }
                    }
                }

                if (mensagem == 0) {
                    cadastrar(nome, sobrenome, idade, celular)
                }

                withContext(Dispatchers.Main) {
                    if (mensagem == 0) {
                        Toast.makeText(
                            applicationContext,
                            "Sucesso ao cadastrar usuário!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    else if (mensagem == 1) {
                        Toast.makeText(
                            applicationContext,
                            "Contato já existe!\n " +
                                    "Nome do contato: $contatoExistente", Toast.LENGTH_SHORT
                        ).show()

                    }
                    else if (mensagem == 2) {
                        Toast.makeText(
                            applicationContext,
                            "Preencha todos os campos!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }

    private fun cadastrar(nome: String, sobrenome: String, idade: String, celular: String) {
        val usuario = Usuario(nome, sobrenome, idade, celular)
        println(usuario)
        usuarioDao = AppDatabase.getInstance(this).usuarioDao()
        usuarioDao.inserir(usuario)
    }

    private fun getContatos() {
        usuarioDao = AppDatabase.getInstance(this).usuarioDao()
        val listaUsuariosGet: MutableList<Usuario> = usuarioDao.get()

        for (usuario in listaUsuariosGet) {
            listaUsuarios.add(usuario)
        }
    }
}