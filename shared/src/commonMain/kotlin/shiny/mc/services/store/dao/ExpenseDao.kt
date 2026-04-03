package shiny.mc.services.store.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import shiny.mc.services.store.entity.Expense

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense ORDER BY title")
    fun getExpanses(): Flow<List<Expense>>

    @Insert
    suspend fun insert(entity: Expense)

    @Update
    suspend fun update(entity: Expense)

    @Delete
    suspend fun delete(entity: Expense)
}