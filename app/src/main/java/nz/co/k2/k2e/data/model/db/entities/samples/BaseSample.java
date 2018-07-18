package nz.co.k2.k2e.data.model.db.entities.samples;

import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;

import nz.co.k2.k2e.data.model.db.entities.surveyitems.Room;
import nz.co.k2.k2e.data.model.db.tasks.SiteVisit;

public class BaseSample {
    @Expose
    @PrimaryKey
    @NonNull
    public String uuid;

    // Don't return Site Visit with SQL, need to check first whether the Site Visit object on the Remote DB is the same as the one on the Local DB
    // Look into this, could be easier to bundle it up in php...

    @Expose
    public String jobNumber;

    public String siteVisitUuid;

    @Expose
    public String sampleid;

    @Expose
    public String itemUuid;

    @Expose
    public String room;

    @Expose
    public String item;

    @Expose
    public String lastModified;

    @Expose
    public String samplePhotoMidShot;

    @Expose
    public String samplePhotoCloseUp;

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getSiteVisitUuid() {
        return siteVisitUuid;
    }

    public void setSiteVisitUuid(String siteVisitUuid) {
        this.siteVisitUuid = siteVisitUuid;
    }

    public String getSampleid() {
        return sampleid;
    }

    public void setSampleid(String sampleid) {
        this.sampleid = sampleid;
    }

    public String getItemUuid() {
        return itemUuid;
    }

    public void setItemUuid(String itemUuid) {
        this.itemUuid = itemUuid;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getSamplePhotoMidShot() {
        return samplePhotoMidShot;
    }

    public void setSamplePhotoMidShot(String samplePhotoMidShot) {
        this.samplePhotoMidShot = samplePhotoMidShot;
    }

    public String getSamplePhotoCloseUp() {
        return samplePhotoCloseUp;
    }

    public void setSamplePhotoCloseUp(String samplePhotoCloseUp) {
        this.samplePhotoCloseUp = samplePhotoCloseUp;
    }
}
