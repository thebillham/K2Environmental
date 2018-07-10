package nz.co.k2.k2e.data.remote;


import android.util.Log;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.gson.Gson;
import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.json.JSONArray;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import nz.co.k2.k2e.data.model.db.WfmJob;
import nz.co.k2.k2e.data.model.db.jobs.BaseJob;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class AppApiHelper implements ApiHelper {
//
//    private ApiHeader mApiHeader;
//

    private Retrofit retrofit;

    @Inject
    public AppApiHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiEndPoint.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Override
    public Single<List<WfmJob>> getWfmApiCall(String jobNumber) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_WFM + "job.php")
                .addQueryParameter("apiKey",ApiEndPoint.API_KEY)
                .addQueryParameter("job",jobNumber)
                .build()
                .getObjectListSingle(WfmJob.class);
    }

    @Override
    public void apiCreateJob(BaseJob baseJob) {
        Gson gson = new Gson();
        String baseJobJSON = gson.toJson(baseJob);
        Log.d("BenD", "Creating job from API: " + baseJobJSON);
        Rx2AndroidNetworking.post(ApiEndPoint.ENDPOINT_JOB + "create.php")
                .addQueryParameter("apiKey", ApiEndPoint.API_KEY)
                .addQueryParameter("job", baseJobJSON)
                .build()
//                .getObjectSingle(Long.class);
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("BenD", response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                    }
                });
    }

    @Override
    public Single<BaseJob> apiGetJobByUuid(String uuid) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_JOB + "read.php")
                .addQueryParameter("apiKey",ApiEndPoint.API_KEY)
                .addQueryParameter("uuid",uuid)
                .build()
                .getObjectSingle(BaseJob.class);
    }

    @Override
    public Single<BaseJob> apiGetJobByJobNumber(String jobNumber) {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_JOB + "read.php")
                .addQueryParameter("apiKey",ApiEndPoint.API_KEY)
                .addQueryParameter("jobNumber",jobNumber)
                .build()
                .getObjectSingle(BaseJob.class);
    }
//
//    @Override
//    public Single<List<WfmJob>> getWfmApiCall(String apiKey, String jobNumber) {
//        return getWfmApiCall(apiKey, jobNumber);
//    }


//    public ApiHeader getApiHeader() { return mApiHeader; }
}
