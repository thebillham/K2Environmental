package nz.co.k2.k2e.ui.jobs.myjobs;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.ItemJobsEmptyViewBinding;
import nz.co.k2.k2e.databinding.ItemJobsViewBinding;
import nz.co.k2.k2e.ui.base.BaseViewHolder;
import nz.co.k2.k2e.ui.jobs.wfmjobs.WfmFragment;

public class JobsAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    public static final int VIEW_TYPE_EMPTY = 0;

    public static final int VIEW_TYPE_NORMAL = 1;

    private List<JobsItemViewModel> mJobsResponseList;

    private final List<JobsItemViewModel> mJobsCache;

    private JobsAdapterListener mListener;

    private boolean multiSelect = false;
    private ArrayList<Integer> selectedItems = new ArrayList<Integer>();

    public JobsViewModel mJobsViewModel;

    public JobsAdapter(List<JobsItemViewModel> mJobsResponseList, JobsViewModel jobsViewModel) {
        this.mJobsResponseList = mJobsResponseList;
        this.mJobsCache = new ArrayList<>();
        this.mJobsViewModel = jobsViewModel;
    }

    @Override
    public int getItemCount() {
        if (!mJobsResponseList.isEmpty()) {
            return mJobsResponseList.size();
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!mJobsResponseList.isEmpty()) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.itemView.setOnLongClickListener(v -> {
            ((AppCompatActivity)v.getContext()).startSupportActionMode(actionModeCallbacks);
            selectItem(position);
            return true;
        });
        holder.itemView.setOnClickListener(v -> {
            if (multiSelect) {
                selectItem(position);
            } else {
                mListener.onJobClick(mJobsResponseList.get(position).jobNumber.get());
            }
        });
        holder.onBind(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                ItemJobsViewBinding JobsViewBinding = ItemJobsViewBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new JobsViewHolder(JobsViewBinding);
            case VIEW_TYPE_EMPTY:
            default:
                ItemJobsEmptyViewBinding emptyViewBinding = ItemJobsEmptyViewBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new EmptyViewHolder(emptyViewBinding);
        }
    }

    public void addItems(List<JobsItemViewModel> repoList) {
        mJobsResponseList.clear();
        mJobsResponseList.addAll(repoList);
        mJobsCache.clear();
        mJobsCache.addAll(repoList);
        notifyDataSetChanged();
    }

    public void updateItems(List<JobsItemViewModel> repoList) {
        mJobsResponseList.clear();
        mJobsResponseList.addAll(repoList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mJobsResponseList.clear();
    }

    public void setListener(JobsAdapterListener listener) {
        this.mListener = listener;
    }

    public interface JobsAdapterListener {
        void onJobClick(String jobNumber);
        void onRetryClick();
    }

    public class EmptyViewHolder extends BaseViewHolder implements JobsEmptyItemViewModel.JobsEmptyItemViewModelListener {

        private final ItemJobsEmptyViewBinding mBinding;

        public EmptyViewHolder(ItemJobsEmptyViewBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            JobsEmptyItemViewModel emptyItemViewModel = new JobsEmptyItemViewModel(this);
            mBinding.setViewModel(emptyItemViewModel);
        }

        @Override
        public void onRetryClick() {
            mListener.onRetryClick();
        }
    }

    public class JobsViewHolder extends BaseViewHolder implements View.OnClickListener {

        private final ItemJobsViewBinding mBinding;

        public JobsViewHolder(ItemJobsViewBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            final JobsItemViewModel mJobsItemViewModel = mJobsResponseList.get(position);
            mBinding.setViewModel(mJobsItemViewModel);

            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mBinding.executePendingBindings();
            if (selectedItems.contains(position)) {
                mJobsItemViewModel.setGrey();
            } else {
                mJobsItemViewModel.setWhite();
            }
        }

        public void onJobClick(String jobNumber) { mListener.onJobClick(jobNumber);}

        @Override
        public void onClick(View v) {
            // Add Jobs Job to My Jobs
        }
    }

    /*
    http://blog.teamtreehouse.com/contextual-action-bars-removing-items-recyclerview
     */
    private ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            menu.add("Delete");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            for (int pos : selectedItems){
                mJobsViewModel.deleteJob(mJobsResponseList.get(pos).getJobNumber());
                mJobsCache.remove(pos);
                updateItems(mJobsCache);
                notifyDataSetChanged();
//                mJobsResponseList.remove(pos);
            }
//            updateItems(mJobsCache);
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
        }
    };

    private void selectItem(Integer item) {
        if (multiSelect) {
            final JobsItemViewModel mJobsItemViewModel = mJobsResponseList.get(item);
            if (selectedItems.contains(item)) {
                selectedItems.remove(item);
                mJobsItemViewModel.setWhite();
            } else {
                selectedItems.add(item);
                mJobsItemViewModel.setGrey();
            }
        }
    }
}