package br.com.ejlsistemas.curso.blueshoes.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import br.com.ejlsistemas.curso.blueshoes.R
import br.com.ejlsistemas.curso.blueshoes.data.NavMenuItemsDataBase
import br.com.ejlsistemas.curso.blueshoes.domain.NavMenuItem
import br.com.ejlsistemas.curso.blueshoes.domain.User
import br.com.ejlsistemas.curso.blueshoes.util.NavMenuItemDetailsLookup
import br.com.ejlsistemas.curso.blueshoes.util.NavMenuItemKeyProvider
import br.com.ejlsistemas.curso.blueshoes.util.NavMenuItemPredicate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.nav_header_user_logged.*
import kotlinx.android.synthetic.main.nav_header_user_not_logged.*
import kotlinx.android.synthetic.main.nav_menu.*

class MainActivity: AppCompatActivity() {

    lateinit var navMenu: NavMenuItemsDataBase

    val user = User(
        "Estelson Medeiros Pereira",
        R.drawable.user,
        true
    )

    lateinit var navMenuItems: List<NavMenuItem>
    lateinit var navMenuItemsLogged: List<NavMenuItem>
    lateinit var selectNavMenuItems: SelectionTracker<Long>
    lateinit var selectNavMenuItemsLogged: SelectionTracker<Long>

    companion object {
        const val FRAGMENT_TAG = "frag-tag"
        const val FRAGMENT_ID = "frag-id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        initNavMenu(savedInstanceState)

        initFragment()
    }

    private fun initFragment() {
        val supFrag = supportFragmentManager
        var fragment = supFrag.findFragmentByTag(FRAGMENT_TAG)

        /*
         * Se n??o for uma reconstru????o de atividade, ent??o n??o
         * haver?? um fragmento em mem??ria, ent??o busca-se o
         * inicial.
         * */
        if(fragment == null) {
            /*
             * Caso haja algum ID de fragmento em intent, ent??o
             * ?? este fragmento que deve ser acionado. Caso
             * contr??rio, abra o fragmento comum de in??cio.
             * */
            var fragId = intent?.getIntExtra(FRAGMENT_ID, 0)
            if(fragId == 0) {
                fragId = R.id.item_about
            }

            fragment = getFragment(fragId!!.toLong())
        }

        replaceFragment(fragment)
    }

    private fun getFragment(fragId: Long): Fragment {
        return when(fragId) {
            R.id.item_about.toLong() -> AboutFragment()
            R.id.item_contact.toLong() -> ContactFragment()
            R.id.item_privacy_policy.toLong() -> PrivacyPolicyFragment()

            else -> AboutFragment()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fl_fragment_container,
                fragment,
                FRAGMENT_TAG
            ).commit()
    }

    fun callLoginActivity(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    /**
     * Mudan??a de t??tulo na barra de t??tulo
     */
    fun updateToolbarTitleInFragment(titleStringId: Int) {
        toolbar.title = getString(titleStringId)
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        /**
         * Infla o menu. Adiciona itens a barra de topo, se ela estiver presente.
         */
        menuInflater.inflate(R.menu.main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /**
         * Lidar com cliques de itens da barra de a????o aqui.
         * A barra de a????o manipular?? automaticamente os
         * cliques no bot??o Home / Up, desde que seja
         * especificada uma atividade pai em AndroidManifest.xml.
         */
        when(item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        /*
         * Para manter o item de menu gaveta selecionado caso
         * haja reconstru????o de atividade.
         * */
        selectNavMenuItems.onSaveInstanceState(outState!!)
        selectNavMenuItemsLogged.onSaveInstanceState(outState)
    }

    /**
     * M??todo que inicializa a lista de itens de menu gaveta
     * que estar?? presente quando o usu??rio estiver ou n??o
     * conectado ao aplicativo.
     */
    private fun initNavMenuItems() {
        rv_menu_items.setHasFixedSize(false)
        rv_menu_items.layoutManager = LinearLayoutManager(this)
        rv_menu_items.adapter = NavMenuItemsAdapter(navMenuItems)

        initNavMenuItemsSelection()
    }

    /**
     * M??todo que inicializa a parte de lista de itens de menu
     * gaveta que estar?? presente somente quando o usu??rio
     * estiver conectado ao aplicativo.
     */
    private fun initNavMenuItemsLogged() {
        rv_menu_items_logged.setHasFixedSize(true)
        rv_menu_items_logged.layoutManager = LinearLayoutManager(this)
        rv_menu_items_logged.adapter = NavMenuItemsAdapter(navMenuItemsLogged)

        initNavMenuItemsLoggedSelection()
    }

    /*
     * M??todo respons??vel por inicializar o objeto de sele????o
     * de itens de menu gaveta, sele????o dos itens que aparecem
     * para usu??rio conectado ou n??o.
     * */
    private fun initNavMenuItemsSelection() {
        selectNavMenuItems = SelectionTracker.Builder<Long>(
            "id-selected-item",
            rv_menu_items,
            NavMenuItemKeyProvider(navMenuItems),
            NavMenuItemDetailsLookup(rv_menu_items),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(NavMenuItemPredicate(this)).build()

        selectNavMenuItems.addObserver(
            SelectObserverNavMenuItems {
                selectNavMenuItemsLogged.selection.filter {
                    selectNavMenuItemsLogged.deselect(it)
                }
            }
        )

        (rv_menu_items.adapter as NavMenuItemsAdapter).selectionTracker = selectNavMenuItems
    }

    /*
    * M??todo respons??vel por inicializar o objeto de sele????o
    * de itens de menu gaveta, sele????o dos itens que aparecem
    * somente quando o usu??rio est?? conectado ao app.
    * */
    private fun initNavMenuItemsLoggedSelection() {
        selectNavMenuItemsLogged = SelectionTracker.Builder<Long>(
            "id-selected-item-logged",
            rv_menu_items_logged,
            NavMenuItemKeyProvider(navMenuItemsLogged),
            NavMenuItemDetailsLookup(rv_menu_items_logged),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(NavMenuItemPredicate(this)).build()

        selectNavMenuItemsLogged.addObserver(
            SelectObserverNavMenuItems {
                selectNavMenuItems.selection.filter {
                    selectNavMenuItems.deselect(it)
                }
            }
        )

        (rv_menu_items_logged.adapter as NavMenuItemsAdapter).selectionTracker = selectNavMenuItemsLogged
    }

    /*
     * M??todo de inicializa????o do menu gaveta, respons??vel por
     * apresentar o cabe??alho e itens de menu de acordo com o
     * status do usu??rio (logado ou n??o).
     * */
    private fun initNavMenu(savedInstanceState: Bundle?) {
        navMenu = NavMenuItemsDataBase(this)
        navMenuItems = navMenu.items
        navMenuItemsLogged = navMenu.itemsLogged

        showHideNavMenuViews()

        initNavMenuItems()
        initNavMenuItemsLogged()

        if(savedInstanceState != null) {
            selectNavMenuItems.onRestoreInstanceState(savedInstanceState)
            selectNavMenuItemsLogged.onRestoreInstanceState(savedInstanceState)
        } else {
            /*
             * Verificando se h?? algum item ID em intent. Caso n??o,
             * utilize o ID do primeiro item.
             * */
            var fragId = intent?.getIntExtra(FRAGMENT_ID, 0)
            if(fragId == 0) {
                fragId = R.id.item_all_shoes
            }

            /*
             * O primeiro item do menu gaveta deve estar selecionado
             * caso n??o seja uma reinicializa????o de tela / atividade
             * ou o envio de um ID especifico de fragmento a ser aberto.
             * O primeiro item aqui ?? o de ID R.id.item_all_shoes.
             * */
            selectNavMenuItems.select(fragId!!.toLong())
        }
    }

    /*
     * Abrir da atividade SignUpActivity pelo menu gaveta (usu??rio n??o logado)
     * */
    fun callSignUpActivity( view: View ){
        val intent = Intent(this, SignUpActivity::class.java)

        startActivity( intent )
    }

    /*
     * Invoca o m??todo de navMenu que salva o ID do ??ltimo item de fragmento acionado
     * */
    private fun itemCallFragment(key: Long, callbackRemoveSelection: () -> Unit) {
        /*
         * Para garantir que somente um item de lista se
         * manter?? selecionado, ?? preciso acessar o objeto
         * de sele????o da lista de itens de usu??rio conectado
         * para ent??o remover qualquer poss??vel sele????o
         * ainda presente nela. Sempre haver?? somente um
         * item selecionado, mas infelizmente o m??todo
         * clearSelection() n??o estava respondendo como
         * esperado, por isso a estrat??gia a seguir.
         * */
        callbackRemoveSelection()

        navMenu.saveLastSelectedItemFragmentID(this, key)

        if(!navMenu.wasActivityItemFired(this)) {
            /*
             * Somente permite a real sele????o de um fragmento e o
             * fechamento do menu gaveta se o item de menu anterior
             * selecionado n??o tiver sido um que aciona uma atividade.
             * Caso contr??rio o fragmento j?? em tela deve continuar
             * e o menu gaveta deve permanecer aberto.
             * */
            val fragment = getFragment(key)
            replaceFragment(fragment)

            /*
             * Fechando o menu gaveta.
             * */
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            navMenu.saveIsActivityItemFired(this, false)
        }
    }

    private fun itemCallActivity(key: Long, callbackRemoveSelection: () -> Unit) {
        callbackRemoveSelection()

        lateinit var intent: Intent

        when(key) {
            R.id.item_settings.toLong() -> {
                intent = Intent(this, AccountSettingsActivity::class.java)
                intent.putExtra(User.KEY, user)
            }
        }

        navMenu.saveIsActivityItemFired(this, true)

        startActivity(intent)
    }

    /*
     * Alguns itens do menu gaveta de usu??rio conectado acionam
     * a abertura de uma atividade e n??o a abertura de um novo
     * fragmento, dessa forma o m??todo abaixo ser?? ??til em
     * l??gicas de neg??cio para informar quais s??o os itens que
     * acionam atividades.
     * */
    fun isActivityCallInMenu(key: Long) = when(key) {
        R.id.item_settings.toLong() -> true
        else -> false
    }

    /*
     * Respons??vel pelo preenchimento de dados no cabe??alho de menu quando com usu??rio conectado.
     * */
    private fun fillUserHeaderNavMenu() {
        if(user.status) { /* Conectado */
            iv_user.setImageResource(user.image)
            tv_user.text = user.name
        }
    }

    /*
     * M??todo respons??vel por esconder itens do menu gaveta de
     * acordo com o status do usu??rio (conectado ou n??o).
     * */
    private fun showHideNavMenuViews() {
        if(user.status) { /* Conectado */
            rl_header_user_not_logged.visibility = View.GONE
            fillUserHeaderNavMenu()
        } else {  /* N??o conectado */
            rl_header_user_logged.visibility = View.GONE
            v_nav_vertical_line.visibility = View.GONE
            rv_menu_items_logged.visibility = View.GONE
        }
    }

    inner class SelectObserverNavMenuItems(val callbackRemoveSelection: () -> Unit):
        SelectionTracker.SelectionObserver<Long>() {

        /*
         * M??todo respons??vel por permitir que seja poss??vel
         * disparar alguma a????o de acordo com a mudan??a de
         * status de algum item em algum dos objetos de sele????o
         * de itens de menu gaveta. Aqui vamos proceder com
         * alguma a????o somente em caso de item obtendo sele????o,
         * para item perdendo sele????o n??o haver?? processamento,
         * pois este status n??o importa na l??gica de neg??cio
         * deste m??todo.
         * */
        override fun onItemStateChanged(key: Long, selected: Boolean) {
            super.onItemStateChanged(key, selected)

            /*
             * Padr??o Cl??usula de Guarda para n??o seguirmos
             * com o processamento em caso de item perdendo
             * sele????o. O processamento posterior ao condicional
             * abaixo ?? somente para itens obtendo a sele????o,
             * selected = true.
             * */
            if(!selected) {
                return
            }


            if(isActivityCallInMenu(key)) {
                itemCallActivity(key, callbackRemoveSelection)
            } else {
                itemCallFragment(key, callbackRemoveSelection)
            }
        }

    }

    override fun onResume() {
        super.onResume()

        /*
         * Se o ??ltimo item de menu gaveta selecionado foi um
         * que aciona uma atividade, ent??o temos de colocar a
         * sele????o de item correta em menu gaveta, item que
         * estava selecionado antes do acionamento do item que
         * invoca uma atividade.
         * */
        if(navMenu.wasActivityItemFired(this)) {
            selectNavMenuItems.select(navMenu.getLastSelectedItemFragmentID(this))
        }
    }

}
