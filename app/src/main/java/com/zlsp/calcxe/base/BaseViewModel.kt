package com.zlsp.calcxe.base

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost

abstract class BaseViewModel<State : MVI.State, Effect : MVI.Effect, Event : MVI.Event> :
    ContainerHost<State, Effect>, ViewModel()
{

    protected abstract val containerHost: Container<State, Effect>

    override val container: Container<State, Effect>
        get() = containerHost

    abstract fun sendEvent(event: Event)
}