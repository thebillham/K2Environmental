package nz.co.k2.k2e.ui.jobs.wfmjobs;

public class WfmEmptyItemViewModel {

    private final WfmEmptyItemViewModelListener mListener;

    public WfmEmptyItemViewModel(WfmEmptyItemViewModelListener listener) {
        this.mListener = listener;
    }

    public void onRetryClick() {
        mListener.onRetryClick();
    }

    public interface WfmEmptyItemViewModelListener {

        void onRetryClick();
    }
}
