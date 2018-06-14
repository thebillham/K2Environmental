package nz.co.k2.k2e.data.remote;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import nz.co.k2.k2e.data.model.db.WfmJob;

// Declare all the functions that AppApiHelper can do

public interface ApiHelper {

    Observable<List<WfmJob>> getWfmApiCall(String jobNumber);

    //    ApiHeader getApiHeader();
}
