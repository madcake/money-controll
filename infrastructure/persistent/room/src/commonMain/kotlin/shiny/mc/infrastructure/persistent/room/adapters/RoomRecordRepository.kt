package shiny.mc.infrastructure.persistent.room.adapters

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import shiny.mc.core.adapters.RecordRepository
import shiny.mc.core.dto.Record
import shiny.mc.infrastructure.persistent.room.dao.RecordDao
import shiny.mc.infrastructure.persistent.room.entity.toDto
import shiny.mc.infrastructure.persistent.room.entity.toEntity

class RoomRecordRepository(
    private val recordDao: RecordDao,
) : RecordRepository {

    override suspend fun addRecord(record: Record) {
        addRecords(listOf(record))
    }

    override suspend fun addRecords(records: List<Record>) {
        recordDao.insert(records.toEntity())
    }

    override fun getRecords(): Flow<List<Record>> {
        return recordDao.getRecords().toDto()
    }

    override fun getRecord(recordId: String): Flow<Record?> {
        return recordDao.getRecord(recordId)
            .map { it?.toDto() }
    }

    override fun getRecords(categoryId: Long): Flow<List<Record>> {
        return recordDao.getRecords(categoryId).toDto()
    }

    override fun getRecords(
        month: Int,
        year: Int,
    ): Flow<List<Record>> {
        return recordDao.getRecords(month, year).toDto()
    }

    override suspend fun updateRecord(record: Record) {
        recordDao.update(record.toEntity())
    }

    override suspend fun hasRecord(recordId: String): Boolean {
        return recordDao.hasRecord(recordId)
    }

    override suspend fun removeRecord(recordId: String) {
        recordDao.delete(recordId)
    }
}