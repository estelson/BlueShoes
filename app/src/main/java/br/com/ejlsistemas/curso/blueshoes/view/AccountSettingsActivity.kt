package br.com.ejlsistemas.curso.blueshoes.view

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import br.com.ejlsistemas.curso.blueshoes.R
import br.com.ejlsistemas.curso.blueshoes.data.AccountSettingsItemsDataBase
import br.com.ejlsistemas.curso.blueshoes.domain.User
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_account_settings.*

class AccountSettingsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*
         * Colocando em tela o usuário conectado.
         * */
        val user = intent.getParcelableExtra<User>(User.KEY)
        tv_user_connected.text = String.format(
            "%s %s",
            getString(R.string.connected),
            user.name
        )

        initItems()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    /*
     * Método que inicializa a lista de itens de configurações
     * de conta.
     * */
    private fun initItems() {
        rv_account_settings_items.setHasFixedSize(false)

        val layoutManager = LinearLayoutManager(this)
        rv_account_settings_items.layoutManager = layoutManager

        val divider = DividerItemDecoration(this, layoutManager.getOrientation())
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.light_grey_divider_line)!!)
        rv_account_settings_items.addItemDecoration(divider)

        rv_account_settings_items.adapter = AccountSettingsItemsAdapter(AccountSettingsItemsDataBase.getItems(this))
    }

}