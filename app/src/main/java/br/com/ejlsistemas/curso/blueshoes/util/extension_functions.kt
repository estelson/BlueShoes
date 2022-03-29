package br.com.ejlsistemas.curso.blueshoes.util

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText

private fun EditText.afterTextChanged(invokeValidation: (String) -> Unit) {

    this.addTextChangedListener(object: TextWatcher {
        /*
         * O callback invokeValidation passado como parâmetro tem a responsabilidade de invocar
         * o código de validação e apresentação de mensagem de erro, caso necessário
         */
        override fun afterTextChanged(content: Editable?) {
            invokeValidation(content.toString())
        }

        override fun beforeTextChanged(content: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(content: CharSequence?, start: Int, before: Int, count: Int) {

        }
    })

}

fun EditText.validate(validator: (String) -> Boolean, message: String) {
    this.afterTextChanged {
        this.error = if(validator(it)) {
            null
        } else {
            message
        }
    }
}

fun String.isValidEmail(): Boolean = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean = this.length > 5