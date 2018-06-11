package nz.co.k2.k2e.data.model.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import nz.co.k2.k2e.data.model.db.jobs.BaseJob;

@Entity(tableName = "superrooms", foreignKeys = @ForeignKey(
        entity = BaseJob.class,
        parentColumns = "uuid",
        childColumns = "job_uuid"))
public class SuperRoom {
    @Expose
    @PrimaryKey
    public String uuid;

    @Expose
    public String job_uuid;
}
