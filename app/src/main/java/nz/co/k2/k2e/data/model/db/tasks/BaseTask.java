package nz.co.k2.k2e.data.model.db.tasks;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import nz.co.k2.k2e.data.model.db.jobs.BaseJob;

// Base task
// Children include all types of tasks, surveys, bulk sampling, decontamination etc.
@Entity (tableName = "tasks", foreignKeys = @ForeignKey(
                entity = BaseJob.class,
                parentColumns = "uuid",
                childColumns = "job_uuid"))
public class BaseTask {
    @Expose
    @PrimaryKey
    public String uuid;

    @Expose
    public String job_uuid;
}
