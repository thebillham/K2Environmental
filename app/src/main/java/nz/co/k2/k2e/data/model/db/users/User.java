package nz.co.k2.k2e.data.model.db.users;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity
public class User {
    @Expose
    @PrimaryKey
    public String uuid;

    @Expose
    public String displayName;

    @Expose
    public String email;

    @Expose
    public String profilePictureUrl;

}
