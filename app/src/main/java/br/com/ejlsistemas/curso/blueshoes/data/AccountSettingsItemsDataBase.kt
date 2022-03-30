package br.com.ejlsistemas.curso.blueshoes.data

import android.content.Context
import br.com.ejlsistemas.curso.blueshoes.R
import br.com.ejlsistemas.curso.blueshoes.domain.AccountSettingItem

class AccountSettingsItemsDataBase {

    companion object {
        fun getItems(context: Context) = listOf(
            AccountSettingItem(
                context.getString(R.string.setting_item_profile),
                context.getString(R.string.setting_item_profile_desc)
            ),

            AccountSettingItem(
                context.getString(R.string.setting_item_login),
                context.getString(R.string.setting_item_login_desc)
            ),

            AccountSettingItem(
                context.getString(R.string.setting_item_address),
                context.getString(R.string.setting_item_address_desc)
            ),
            
            AccountSettingItem(
                context.getString(R.string.setting_item_credit_cards),
                context.getString(R.string.setting_item_credit_cards_desc)
            )
        )
    }

}