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

    public String siteVisitUuid;

    @Expose
    public String sampleid;

    @Expose
    public String itemUuid;

    @Expose
    public Room room;

    @Expose
    public String item;

    @Expose
    public String lastModified;
}
