package nz.co.k2.k2e.data.model.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import nz.co.k2.k2e.data.model.db.jobs.BaseJob;

@Entity (tableName = "rooms", foreignKeys = {
        @ForeignKey(
            entity = BaseJob.class,
            parentColumns = "uuid",
            childColumns = "job_uuid"),

        @ForeignKey(
            entity = SuperRoom.class,
            parentColumns = "uuid",
            childColumns = "superroom_uuid")})

public class Room {
    @Expose
    @PrimaryKey
    public String uuid;

    @Expose
    public String job_uuid;

    @Expose
    public String superroom_uuid;
}
