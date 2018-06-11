package nz.co.k2.k2e.data.remote;


import com.rx2androidnetworking.Rx2AndroidNetworking;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import nz.co.k2.k2e.data.model.db.WfmJob;

@Singleton
public class AppApiHelper implements ApiHelper {
//
//    private ApiHeader mApiHeader;
//
    @Inject
    public AppApiHelper() {

    }

    @Override
    public Single<List<WfmJob>> getWfmApiCall(String jobNumber) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_WFM)
                .addQueryParameter("apiKey",ApiEndPoint.API_KEY)
                .addQueryParameter("job",jobNumber)
                .build()
                .getObjectListSingle(WfmJob.class);
    }

//    public ApiHeader getApiHeader() { return mApiHeader; }
}
