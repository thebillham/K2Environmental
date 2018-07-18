package nz.co.k2.k2e.data.local.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import nz.co.k2.k2e.data.model.db.entities.samples.AsbestosBulkSample;

@Dao
public interface AsbestosBulkSampleDao {
    @Delete
    void delete(AsbestosBulkSample bulkSample);

    @Query("SELECT * FROM samples_asbestos_bulk WHERE uuid LIKE :uuid LIMIT 1")
    AsbestosBulkSample getSampleByUuid(String uuid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(AsbestosBulkSample bulkSample);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertAll(List<AsbestosBulkSample> bulkSamples);

    @Query("SELECT * FROM samples_asbestos_bulk")
    List<AsbestosBulkSample> loadAll();

    @Query("SELECT * FROM samples_asbestos_bulk WHERE jobNumber = :jobNumber")
    List<AsbestosBulkSample> loadAllByJobNumber(String jobNumber);
}
