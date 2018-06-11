package nz.co.k2.k2e.data.model.db.jobs;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.annotations.Expose;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import nz.co.k2.k2e.data.model.db.WfmJob;

@Entity (tableName = "jobs")
public class BaseJob {
    @Expose
    @PrimaryKey
    @NonNull
    public String uuid;

    @Expose
    public String jobNumber;

    @Expose
    public String address;

    @Expose
    public String clientName;

    @Expose
    public String jobType;

    @Expose
    public String lastModified;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

}
