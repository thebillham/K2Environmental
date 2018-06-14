package nz.co.k2.k2e.ui.jobs.wfmjobs;


import java.util.List;

import io.reactivex.Single;
import nz.co.k2.k2e.data.model.db.WfmJob;

public interface WfmHelper {
    Single<List<WfmJob>> getWfmList(Boolean forceRefresh);
}
