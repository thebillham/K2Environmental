package nz.co.k2.k2e.ui.jobs;

public class JobsEmptyItemViewModel {

    private final JobsEmptyItemViewModelListener mListener;

    public JobsEmptyItemViewModel(JobsEmptyItemViewModelListener listener) {
        this.mListener = listener;
    }

    public void onRetryClick() {
        mListener.onRetryClick();
    }

    public interface JobsEmptyItemViewModelListener {

        void onRetryClick();
    }
}
