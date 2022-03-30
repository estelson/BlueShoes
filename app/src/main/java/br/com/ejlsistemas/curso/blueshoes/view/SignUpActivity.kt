package br.com.ejlsistemas.curso.blueshoes.view

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.View
import br.com.ejlsistemas.curso.blueshoes.R
import br.com.ejlsistemas.curso.blueshoes.util.isValidEmail
import br.com.ejlsistemas.curso.blueshoes.util.isValidPassword
import br.com.ejlsistemas.curso.blueshoes.util.validate
import com.blankj.utilcode.util.ScreenUtils
import kotlinx.android.synthetic.main.content_sign_up.*

class SignUpActivity: FormEmailAndPasswordActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
         * Colocando configuração de validação de campo de email
         * para enquanto o usuário informa o conteúdo deste campo.
         * */
        et_email.validate(
            {
                it.isValidEmail()
            }, getString(R.string.invalid_email)
        )

        /*
         * Colocando configuração de validação de campo de senha
         * para enquanto o usuário informa o conteúdo deste campo.
         * */
        et_password.validate(
            {
                it.isValidPassword()
            }, getString(R.string.invalid_password)
        )

        /*
         * Colocando configuração de validação de campo de
         * confirmação de senha para enquanto o usuário informa o
         * conteúdo deste campo.
         * */
        et_confirm_password.validate(
            {
                /*
                 * O toString() em et_password.text.toString() é
                 * necessário, caso contrário a validação falha
                 * mesmo quando é para ser ok.
                 * */
                (et_password.text.isNotEmpty() && it.equals(et_password.text.toString())) || et_password.text.isEmpty()
            }, getString(R.string.invalid_confirmed_password)
        )

        et_confirm_password.setOnEditorActionListener(this)
    }

    override fun backEndFakeDelay() {
        backEndFakeDelay(false, getString(R.string.invalid_sign_up_email))
    }

    override fun getLayoutResourceID() = R.layout.content_sign_up


    override fun blockFields(status: Boolean) {
        et_email.isEnabled = !status
        et_password.isEnabled = !status
        et_confirm_password.isEnabled = !status
        bt_sign_up.isEnabled = !status
    }

    override fun isMainButtonSending(status: Boolean) {
        bt_sign_up.text = if(status) {
            getString(R.string.sign_up_going)
        } else {
            getString(R.string.sign_up)
        }
    }

    override fun isConstraintToSiblingView(isKeyBoardOpened: Boolean) = isKeyBoardOpened || ScreenUtils.isLandscape()
    override fun setConstraintsRelativeToSiblingView(constraintSet: ConstraintSet, privacyId: Int) {
        /*
         * Se o teclado virtual estiver aberto ou a tela
         * estiver em landscape, então mude a configuração
         * da View alvo (tv_privacy_policy) para ficar
         * vinculada a View acima dela (bt_sign_up).
         * */
        constraintSet.connect(
            privacyId,
            ConstraintLayout.LayoutParams.TOP,
            bt_sign_up.id,
            ConstraintLayout.LayoutParams.BOTTOM,
            (12 * ScreenUtils.getScreenDensity()).toInt()
        )
    }

    fun callLoginActivity(view: View) {
        finish()
    }

}