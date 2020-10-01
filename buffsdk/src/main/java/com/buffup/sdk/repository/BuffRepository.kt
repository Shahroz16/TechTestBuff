package com.buffup.sdk.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.buffup.sdk.model.Buff
import com.buffup.sdk.network.BuffApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/**
 * Its the source of buffs, all logic regarding buff storage and access goes here.
 */
class BuffRepository {

    private val buffApiService = BuffApiService
    val buffsLiveData: LiveData<Buff> by lazy { loadBuffs().asLiveData() }

    fun loadBuffs(): Flow<Buff> = flow {
        // numbers 1..5 every given 30 secs
        for (i in 1..5) {
            loadBuff(id = i)
                .collect {
                    emit(it)
                    delay(30000)
                }
        }
    }


    fun loadBuff(id: Int): Flow<Buff> = flow {
        emit(buffApiService.getBuff(id))
    }

}