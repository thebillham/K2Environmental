package nz.co.k2.k2e.data.remote;

import nz.co.k2.k2e.BuildConfig;

public final class ApiEndPoint {
    // TODO Get buildConfigs to work
//    public static final String ENDPOINT_OPEN_SOURCE = BuildConfig.BASE_URL + "/5926c34212000035026871cd";
    public static final String ENDPOINT_WFM = "http://api.k2.co.nz/GetJob.php";
    public static final String API_KEY = "BfqKcOR6tcMtFPCH7MrmHoaANEIJ5grs";
    private ApiEndPoint() {
        // This class is not publicly instantiable
    }
}
