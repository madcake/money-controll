package shiny.mc.infrastructure.persistent.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import shiny.mc.infrastructure.persistent.room.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Insert
    suspend fun insert(categoryEntity: CategoryEntity): Long

    @Query("DELETE FROM category WHERE id = :id")
    suspend fun delete(id: Long)

    @Update
    suspend fun update(category: CategoryEntity)

    @Query("SELECT * FROM category WHERE id = :id")
    fun getCategory(id: Long): Flow<CategoryEntity?>

    @Query("SELECT * FROM category WHERE title = :title")
    fun getCategory(title: String): Flow<CategoryEntity?>

    @Query("""
        SELECT * FROM
            category
        WHERE
            title LIKE '%' || :query || '%'
        ORDER BY title
    """)
    fun find(query: String): Flow<List<CategoryEntity>>
}