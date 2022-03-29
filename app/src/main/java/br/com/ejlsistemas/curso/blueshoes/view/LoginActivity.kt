package br.com.ejlsistemas.curso.blueshoes.view

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.View
import android.widget.Toast
import br.com.ejlsistemas.curso.blueshoes.R
import br.com.ejlsistemas.curso.blueshoes.util.isValidEmail
import br.com.ejlsistemas.curso.blueshoes.util.isValidPassword
import br.com.ejlsistemas.curso.blueshoes.util.validate
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ScreenUtils
import kotlinx.android.synthetic.main.content_form.*
import kotlinx.android.synthetic.main.content_login.*
import kotlinx.android.synthetic.main.text_view_privacy_policy_login.*

class LoginActivity:
    FormActivity(),
    KeyboardUtils.OnSoftInputChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
         * Colocando a View de um arquivo XML como View filha
         * do item indicado no terceiro argumento.
         * */
        View.inflate(
            this,
            R.layout.content_login,
            fl_form
        )

        /*
        * Hackcode para que a imagem de background do layout não
        * se ajuste de acordo com a abertura do teclado de
        * digitação. Caso utilizando o atributo
        * android:background, o ajuste ocorre, desconfigurando o
        * layout.
        * */
        window.setBackgroundDrawableResource(R.drawable.bg_activity)

        /*
         * Colocando configuração de validação de campo de email
         * para enquanto o usuário informa o conteúdo deste campo.
         * */
        et_email.validate({ it.isValidEmail() }, getString(R.string.invalid_email))

        /*
         * Colocando configuração de validação de campo de senha
         * para enquanto o usuário informa o conteúdo deste campo.
         * */
        et_password.validate({ it.isValidPassword() }, getString(R.string.invalid_password))

        et_password.setOnEditorActionListener(this)

        /*
        * Com a API KeyboardUtils conseguimos de maneira
        * simples obter o status atual do teclado virtual (aberto /
        * fechado) e assim prosseguir com algoritmos de ajuste de
        * layout.
        * */
        KeyboardUtils.registerSoftInputChangedListener(this, this)
    }

    /*
     * Listeners de clique dos links da tela de login
     * */
    fun callForgotPasswordActivity(view: View) {
        val intent = Intent(
            this,
            ForgotPasswordActivity::class.java
        )

        startActivity(intent)
    }

    fun callSignUpActivity(view: View) {
        Toast.makeText(this, "TODO: callSignUpActivity()", Toast.LENGTH_SHORT).show()
    }

    fun callPrivacyPolicyFragment(view: View) {
        val intent = Intent(this, MainActivity::class.java)

        /*
         * Para saber qual fragmento abrir quando a
         * MainActivity voltar ao foreground.
         * */
        intent.putExtra(MainActivity.FRAGMENT_ID, R.id.item_privacy_policy)

        /*
         * Removendo da pilha de atividades a primeira
         * MainActivity aberta (e a LoginActivity), para
         * deixar somente a nova MainActivity com uma nova
         * configuração de fragmento aberto.
         * */
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        startActivity(intent)
    }

    override fun onDestroy() {
        KeyboardUtils.unregisterSoftInputChangedListener(this)

        super.onDestroy()
    }

    override fun onSoftInputChanged(height: Int) {
        if(ScreenUtils.isPortrait()) {
            changePrivacyPolicyConstraints(KeyboardUtils.isSoftInputVisible(this))
        }
    }

    override fun mainAction(view: View?) { /* Antigo login() */
        blockFields(true)
        isMainButtonSending(true)
        showProxy(true)

        backEndFakeDelay(false, getString(R.string.invalid_login))
    }

    /*
     * Necessário para que os campos de formulário não possam
     * ser acionados depois de enviados os dados.
     * */
    override fun blockFields(status: Boolean) {
        et_email.isEnabled = !status
        et_password.isEnabled = !status
        bt_login.isEnabled = !status
    }

    override fun isMainButtonSending(status: Boolean) { /* Antigo isSignInGoing() */
        bt_login.text = if(status) {
            getString(R.string.sign_in_going)
        } else {
            getString(R.string.sign_in)
        }
    }

    /*
     * Muda o rótulo do botão de login de acordo com o status
     * do envio de dados de login.
     * */
    private fun isSignInGoing(status: Boolean) {
        bt_login.text = if(status) {
            getString(R.string.sign_in_going) /* Entrando... */
        } else {
            getString(R.string.sign_in) /* Entrar */
        }
    }

    /*
     *Muda a configuração do TextView de políticas de provacidade quando o teclado está aberto
     * */
    /* AndroidUtilCode API */
    private fun changePrivacyPolicyConstraints(isKeyBoardOpened: Boolean) {
        val privacyId = tv_privacy_policy.id
        val parent = tv_privacy_policy.parent as ConstraintLayout
        val constraintSet = ConstraintSet()

        /*
         * Definindo a largura e a altura da View em
         * mudança de constraints, caso contrário ela
         * fica com largura e altura em 0dp.
         * */
        constraintSet.constrainWidth(privacyId, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        constraintSet.constrainHeight(privacyId, ConstraintLayout.LayoutParams.WRAP_CONTENT)

        /*
         * Centralizando a View horizontalmente no ConstraintLayout.
         * */
        constraintSet.centerHorizontally(privacyId, ConstraintLayout.LayoutParams.PARENT_ID)

        if(isKeyBoardOpened) {
            /*
             * Se o teclado virtual estiver aberto, então
             * mude a configuração da View alvo
             * (tv_privacy_policy) para ficar vinculada a
             * View acima dela (tv_sign_up).
             * */
            constraintSet.connect(
                privacyId,
                ConstraintLayout.LayoutParams.TOP,
                tv_sign_up.id,
                ConstraintLayout.LayoutParams.BOTTOM,
                (12 * ScreenUtils.getScreenDensity()).toInt()
            )
        } else {
            /*
             * Se o teclado virtual estiver fechado, então
             * mude a configuração da View alvo
             * (tv_privacy_policy) para ficar vinculada ao
             * fundo do ConstraintLayout ancestral.
             * */
            constraintSet.connect(
                privacyId,
                ConstraintLayout.LayoutParams.BOTTOM,
                ConstraintLayout.LayoutParams.PARENT_ID,
                ConstraintLayout.LayoutParams.BOTTOM
            )
        }

        constraintSet.applyTo(parent)
    }

}
