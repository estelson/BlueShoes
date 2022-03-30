package br.com.ejlsistemas.curso.blueshoes.view

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.View
import br.com.ejlsistemas.curso.blueshoes.R
import br.com.ejlsistemas.curso.blueshoes.util.isValidEmail
import br.com.ejlsistemas.curso.blueshoes.util.isValidPassword
import br.com.ejlsistemas.curso.blueshoes.util.validate
import com.blankj.utilcode.util.ScreenUtils
import kotlinx.android.synthetic.main.content_login.*

class LoginActivity: FormEmailAndPasswordActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        val intent = Intent(
            this,
            SignUpActivity::class.java
        )

        startActivity(intent)
    }

    override fun isAbleToCallChangePrivacyPolicyConstraints() = ScreenUtils.isPortrait()

    override fun backEndFakeDelay() {
        backEndFakeDelay(false, getString(R.string.invalid_login))
    }

    override fun getLayoutResourceID() = R.layout.content_login

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

    override fun isConstraintToSiblingView(isKeyBoardOpened: Boolean) = isKeyBoardOpened
    override fun setConstraintsRelativeToSiblingView(constraintSet: ConstraintSet, privacyId: Int) {
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
    }

}
