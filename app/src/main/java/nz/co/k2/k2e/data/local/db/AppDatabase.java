package nz.co.k2.k2e.data.local.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import nz.co.k2.k2e.data.local.db.dao.JobDao;
import nz.co.k2.k2e.data.local.db.dao.WfmJobDao;
import nz.co.k2.k2e.data.model.db.WfmJob;
import nz.co.k2.k2e.data.model.db.jobs.BaseJob;

@Database(entities = {WfmJob.class, BaseJob.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WfmJobDao wfmJobDao();
    public abstract JobDao jobDao();
}
