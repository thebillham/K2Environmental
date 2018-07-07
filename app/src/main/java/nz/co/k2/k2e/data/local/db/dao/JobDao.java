package nz.co.k2.k2e.data.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import nz.co.k2.k2e.data.model.db.jobs.BaseJob;

@Dao
public interface JobDao {

    @Delete
    void delete(BaseJob job);

    @Query("SELECT * FROM jobs WHERE jobNumber LIKE :jobNumber LIMIT 1")
    BaseJob findByJobNumber(String jobNumber);

    @Query("SELECT * FROM jobs WHERE uuid LIKE :uuid LIMIT 1")
    BaseJob findByUuid(String uuid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(BaseJob job);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertAll(List<BaseJob> job);

    @Query("SELECT * FROM jobs")
    List<BaseJob> loadAll();

    @Query("SELECT * FROM jobs WHERE uuid IN (:uniqueIds)")
    List<BaseJob> loadAllByIds(List<Integer> uniqueIds);

    @Query("DELETE FROM jobs WHERE jobNumber = :jobNumber")
    void deleteJob(String jobNumber);

    @Query("DELETE FROM jobs")
    void deleteAll();

}
