package nz.co.k2.k2e.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import nz.co.k2.k2e.ui.jobs.JobsAdapter;
import nz.co.k2.k2e.ui.jobs.JobsItemViewModel;
import nz.co.k2.k2e.ui.jobs.wfmjobs.WfmAdapter;
import nz.co.k2.k2e.ui.jobs.wfmjobs.WfmItemViewModel;

public final class BindingUtils {

    private BindingUtils() {
        // This class is not publicly instantiable
    }
//
//    @BindingAdapter({"adapter"})
//    public static void addBlogItems(RecyclerView recyclerView, List<BlogResponse.Blog> blogs) {
//        BlogAdapter adapter = (BlogAdapter) recyclerView.getAdapter();
//        if (adapter != null) {
//            adapter.clearItems();
//            adapter.addItems(blogs);
//        }
//    }

    @BindingAdapter({"wfmAdapter"})
    public static void addWfmItems(RecyclerView recyclerView, List<WfmItemViewModel> wfmList) {
        WfmAdapter adapter = (WfmAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.clearItems();
            adapter.addItems(wfmList);
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
//
//    @BindingAdapter({"adapter", "action"})
//    public static void addQuestionItems(SwipePlaceHolderView mCardsContainerView, List<QuestionCardData> mQuestionList, int mAction) {
//        if (mAction == MainViewModel.ACTION_ADD_ALL) {
//            if (mQuestionList != null) {
//                mCardsContainerView.removeAllViews();
//                for (QuestionCardData question : mQuestionList) {
//                    if (question != null && question.options != null && question.options.size() == 3) {
//                        mCardsContainerView.addView(new QuestionCard(question));
//                    }
//                }
//                ViewAnimationUtils.scaleAnimateView(mCardsContainerView);
//            }
//        }
//    }
//
    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).load(url).into(imageView);
    }
}
