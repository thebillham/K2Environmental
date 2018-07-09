package nz.co.k2.k2e.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import nz.co.k2.k2e.ui.jobs.myjobs.JobsAdapter;
import nz.co.k2.k2e.ui.jobs.myjobs.JobsItemViewModel;
import nz.co.k2.k2e.ui.jobs.wfmjobs.WfmAdapter;
import nz.co.k2.k2e.ui.jobs.wfmjobs.WfmItemViewModel;

public final class BindingUtils {

    private BindingUtils() {
        // This class is not publicly instantiable
    }

    @BindingAdapter({"wfmAdapter"})
    public static void addWfmItems(RecyclerView recyclerView, List<WfmItemViewModel> wfmList) {
        WfmAdapter adapter = (WfmAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            if (!adapter.filteredList) {
                adapter.clearItems();
                adapter.addItems(wfmList);
            }
        }
    }

    @BindingAdapter({"jobsAdapter"})
    public static void addJobItems(RecyclerView recyclerView, List<JobsItemViewModel> jobList) {
        JobsAdapter jobsAdapter = (JobsAdapter) recyclerView.getAdapter();
        if (jobsAdapter != null) {
            jobsAdapter.clearItems();
            jobsAdapter.addItems(jobList);
        }
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }
}
