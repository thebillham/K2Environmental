package nz.co.k2.k2e.data.model.db.tasks;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.List;

import nz.co.k2.k2e.data.model.db.users.User;

public class SiteVisit {
    @Expose
    @PrimaryKey
    @NonNull
    public String uuid;

    @Expose
    public String baseTaskUuid;

    @Ignore
    public BaseTask baseTask;

    @Expose
    public String user_sitevisitUuid;

    @Ignore
    public List<User> sitePersonnel;

    @Expose
    public Date sitevisitDate;

    @Expose
    public String sitevisitType;

    @Expose
    public Integer stage;

    @Expose
    public Integer attempt;

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    public String getBaseTaskUuid() {
        return baseTaskUuid;
    }

    public void setBaseTaskUuid(String baseTaskUuid) {
        this.baseTaskUuid = baseTaskUuid;
    }

    public BaseTask getBaseTask() {
        return baseTask;
    }

    public void setBaseTask(BaseTask baseTask) {
        this.baseTask = baseTask;
    }

    public String getUser_sitevisitUuid() {
        return user_sitevisitUuid;
    }

    public void setUser_sitevisitUuid(String user_sitevisitUuid) {
        this.user_sitevisitUuid = user_sitevisitUuid;
    }

    public List<User> getSitePersonnel() {
        return sitePersonnel;
    }

    public void setSitePersonnel(List<User> sitePersonnel) {
        this.sitePersonnel = sitePersonnel;
    }

    public Date getSitevisitDate() {
        return sitevisitDate;
    }

    public void setSitevisitDate(Date sitevisitDate) {
        this.sitevisitDate = sitevisitDate;
    }

    public String getSitevisitType() {
        return sitevisitType;
    }

    public void setSitevisitType(String sitevisitType) {
        this.sitevisitType = sitevisitType;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }
}
