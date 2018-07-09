package nz.co.k2.k2e.ui.jobs.wfmjobs;

public interface WfmNavigator {
    void onItemClick(String jobNumber);
    void onJobLoaded();
    void handleError(Throwable throwable);
}
