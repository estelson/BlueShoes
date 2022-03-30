package br.com.ejlsistemas.curso.blueshoes.domain

import br.com.ejlsistemas.curso.blueshoes.view.FormActivity

class AccountSettingItem(
    val label: String,
    val description: String,
    val activityClass: Class<out FormActivity>
)