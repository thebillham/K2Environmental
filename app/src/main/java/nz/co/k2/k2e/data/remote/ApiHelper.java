package nz.co.k2.k2e.data.remote;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import nz.co.k2.k2e.data.model.db.WfmJob;
import nz.co.k2.k2e.data.model.db.jobs.BaseJob;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Declare all the functions that AppApiHelper can do

public interface ApiHelper {
    // WFM API
    Single<List<WfmJob>> getWfmApiCall(String jobNumber);

    // Job API
    void apiCreateJob(BaseJob baseJob);
    Single<BaseJob> apiGetJobByUuid(String uuid);
    Single<BaseJob> apiGetJobByJobNumber(String jobNumber);

//    @GET("wfm/")
//    Single<List<WfmJob>> getWfmApiCall (@Query("apiKey") String apiKey
//                                        @Query("job") String jobNumber);
}
