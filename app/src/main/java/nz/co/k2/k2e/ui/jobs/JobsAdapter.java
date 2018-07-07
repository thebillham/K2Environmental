package nz.co.k2.k2e.ui.jobs;

import android.graphics.Color;
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

import javax.inject.Inject;

import nz.co.k2.k2e.databinding.ItemJobsEmptyViewBinding;
import nz.co.k2.k2e.databinding.ItemJobsViewBinding;
import nz.co.k2.k2e.ui.base.BaseViewHolder;

public class JobsAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    // TODO find out why adapters aren't injecting ViewModels
    public static final int VIEW_TYPE_EMPTY = 0;

    public static final int VIEW_TYPE_NORMAL = 1;

    private List<JobsItemViewModel> mJobsResponseList;

    private final List<JobsItemViewModel> mJobsCache;

    private JobsAdapterListener mListener;

    private boolean multiSelect = false;
    private ArrayList<Integer> selectedItems = new ArrayList<Integer>();

    @Inject
    public JobsViewModel mJobsViewModel;

    public JobsAdapter(List<JobsItemViewModel> mJobsResponseList) {
        this.mJobsResponseList = mJobsResponseList;
        this.mJobsCache = new ArrayList<>();
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
            selectItem(position);
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
        Log.d("BenD", "Additems " + repoList.size());
        mJobsResponseList.clear();
        mJobsResponseList.addAll(repoList);
        mJobsCache.clear();
        mJobsCache.addAll(repoList);
        Log.d("BenD", "Cache size is " + mJobsCache.size());
        notifyDataSetChanged();
    }

    public void updateItems(List<JobsItemViewModel> repoList) {
        Log.d("BenD", "Cache: " + mJobsCache.size());
        Log.d("BenD", "Update items " + repoList.size());
        mJobsResponseList.clear();
        mJobsResponseList.addAll(repoList);
        Log.d("BenD", "Cache: " + mJobsCache.size());
        notifyDataSetChanged();
    }

    public void clearItems() {
        mJobsResponseList.clear();
    }

    public void setListener(JobsAdapterListener listener) {
        this.mListener = listener;
    }

    public interface JobsAdapterListener {

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

        @Override
        public void onClick(View v) {
            // Add Jobs Job to My Jobs
        }
    }


    public boolean filterJobs(String text){
        if(text.isEmpty()){
            Log.d("BenD","text is empty, show all jobs");
            updateItems(mJobsCache);
            Log.d("BenD", "Jobs Cache size is " + mJobsCache.size());
            notifyDataSetChanged();
            return true;
        } else {
            ArrayList<JobsItemViewModel> jobList = new ArrayList<>();
            text = text.toLowerCase();
            for(JobsItemViewModel job : mJobsCache){
                if(job.jobNumber.get().toLowerCase().contains(text)
                    || job.address.get().toLowerCase().contains(text)
                    || job.clientName.get().toLowerCase().contains(text)
                    || job.type.get().toLowerCase().contains(text)) {
                    jobList.add(job);
                }
            }
            Log.d("BenD","list made: " + jobList.size());
            if(jobList.isEmpty()){
                Log.d("BenD","jobList is empty");
                clearItems();
                return false;
            } else {
                updateItems(jobList);
                notifyDataSetChanged();
                return true;
            }
        }
//        notifyDataSetChanged();
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
            for (Integer intItem : selectedItems) {
                mJobsResponseList.remove(intItem);
                Log.d("BenD",intItem.toString());
                Log.d("BenD", mJobsResponseList.get(intItem).getJobNumber());
                Log.d("BenD",mJobsViewModel.toString());
                mJobsViewModel.deleteJob(mJobsResponseList.get(intItem).getJobNumber());
            }
            notifyDataSetChanged();
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