package com.buffup.sdk

import com.buffup.sdk.network.BuffError
import com.buffup.sdk.repository.BuffRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RepositoryUnitTest {

    @Mock
    val repository: BuffRepository = BuffRepository()

    @Test
    fun check_if_all_buffs_are_received() = runBlocking {

        val buffsList = repository.loadBuffs().toList()
        assertEquals(buffsList.size, 5)
    }

    @Test
    fun check_if_single_buff_is_loaded_successfully() = runBlocking {

        val buff = repository.loadBuff(1)
        buff.collect {
            assertNotNull(it.buffResult)
        }
    }

    @Test
    fun check_if_single_buff_is_loaded_with_failure() = runBlocking {

        // buff doesn't exist
        val buff = repository.loadBuff(-1)
        buff.collect {
            assertTrue(it.buffError is BuffError.BuffNotFound)
        }
    }

}
