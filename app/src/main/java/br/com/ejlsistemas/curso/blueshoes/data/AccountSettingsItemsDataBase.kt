package br.com.ejlsistemas.curso.blueshoes.data

import android.content.Context
import br.com.ejlsistemas.curso.blueshoes.R
import br.com.ejlsistemas.curso.blueshoes.domain.AccountSettingItem
import br.com.ejlsistemas.curso.blueshoes.view.ConfigProfileActivity
import br.com.ejlsistemas.curso.blueshoes.view.config.connectiondata.ConfigConnectionDataActivity

class AccountSettingsItemsDataBase {

    companion object {
        fun getItems(context: Context) = listOf(
            AccountSettingItem(
                context.getString(R.string.setting_item_profile),
                context.getString(R.string.setting_item_profile_desc),
                ConfigProfileActivity::class.java
            ),

            AccountSettingItem(
                context.getString(R.string.setting_item_login),
                context.getString(R.string.setting_item_login_desc),
                ConfigConnectionDataActivity::class.java
            ),

            AccountSettingItem(
                context.getString(R.string.setting_item_address),
                context.getString(R.string.setting_item_address_desc),
                ConfigProfileActivity::class.java
            ),

            AccountSettingItem(
                context.getString(R.string.setting_item_credit_cards),
                context.getString(R.string.setting_item_credit_cards_desc),
                ConfigProfileActivity::class.java
            )
        )
    }

}