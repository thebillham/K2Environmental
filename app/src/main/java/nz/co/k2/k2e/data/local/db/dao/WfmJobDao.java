package nz.co.k2.k2e.data.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import nz.co.k2.k2e.data.model.db.WfmJob;

@Dao
public interface WfmJobDao {

    @Delete
    void delete(WfmJob wfmJob);

    @Query("SELECT * FROM wfm_jobs WHERE jobNumber LIKE :jobNumber LIMIT 1")
    WfmJob findByJobNumber(String jobNumber);

    @Query("SELECT * FROM wfm_jobs WHERE uid LIKE :id LIMIT 1")
    WfmJob findById(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(WfmJob wfmJob);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertAll(List<WfmJob> wfmJobs);

    @Query("SELECT * FROM wfm_jobs")
    List<WfmJob> loadAll();

    @Query("SELECT * FROM wfm_jobs WHERE uid IN (:uniqueIds)")
    List<WfmJob> loadAllByIds(List<Integer> uniqueIds);

    @Query("DELETE FROM wfm_jobs")
    void deleteAll();
}
