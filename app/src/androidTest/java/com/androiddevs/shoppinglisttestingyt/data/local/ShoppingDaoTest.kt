package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest // i.e. the Unit Test. Use @MediumTest for IntegrationTest, @LargeTest for UI Test
class ShoppingDaoTest {

    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {

        // RAM-scoped database
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.shoppingDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runTest {

        val shoppingItem = ShoppingItem(
            "Hana", 1, 1f, "url", 1
        )
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()
        // This function will wait until the live data returns a value
        // Longer than two seconds? Throw exception.

        assertThat(allShoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runTest {
        val shoppingItem = ShoppingItem(
            "Hana", 1, 1f, "url", 1
        )
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItems).doesNotContain(shoppingItem)

    }

    @Test
    fun observeTotalPriceSum() = runTest {
        val shoppingItem1 = ShoppingItem("Hana", 2, 10f, "url", 1)
        val shoppingItem2 = ShoppingItem("Hana", 4, 5.5f, "url", 2)
        val shoppingItem3 = ShoppingItem("Hana", 0, 100f, "url", 3)
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()
        assertThat(totalPriceSum).isEqualTo(2 * 10f + 4 * 5.5f)
    }
}