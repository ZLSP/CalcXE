package com.zlsp.calcxe.base

import com.zlsp.calcxe.main.MainContract

interface BaseInteractor {
    fun sendAction(state: MainContract.State, action: MainContract.Action)
}