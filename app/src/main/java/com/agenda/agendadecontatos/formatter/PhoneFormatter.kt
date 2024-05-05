package com.agenda.agendadecontatos.formatter

class PhoneFormatter {
    companion object {
        fun formatarTelefone(telefone: String): String {
            val numeros = telefone.replace("\\D".toRegex(), "")

            if (numeros.length == 11) {
                return "(${numeros.substring(0, 2)}) ${numeros.substring(2, 7)}-${numeros.substring(7)}"
            }

            if (numeros.length == 10) {
                return "(${numeros.substring(0, 2)}) ${numeros.substring(2, 6)}-${numeros.substring(6)}"
            }

            return "(${numeros.substring(0, 2)}) ${numeros.substring(2)}"
        }
    }


}