package com.zlsp.calcxe.base

sealed interface MVI {
    interface Event : MVI
    interface State : MVI
    interface Effect : MVI
}