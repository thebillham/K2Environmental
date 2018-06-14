package nz.co.k2.k2e.ui.jobs.wfmjobs;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nz.co.k2.k2e.databinding.ItemWfmEmptyViewBinding;
import nz.co.k2.k2e.databinding.ItemWfmViewBinding;
import nz.co.k2.k2e.ui.base.BaseViewHolder;

public class WfmAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;

    public static final int VIEW_TYPE_NORMAL = 1;

    private List<WfmItemViewModel> mWfmResponseList;

    private final List<WfmItemViewModel> mWfmCache;

    private WfmAdapterListener mListener;

    WfmViewModel mWfmViewModel;

    public WfmAdapter(List<WfmItemViewModel> mWfmResponseList, WfmViewModel wfmViewModel) {
        this.mWfmResponseList = mWfmResponseList;
        this.mWfmCache = new ArrayList<>();
        mWfmViewModel = wfmViewModel;
    }

    @Override
    public int getItemCount() {
        if (!mWfmResponseList.isEmpty()) {
            return mWfmResponseList.size();
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!mWfmResponseList.isEmpty()) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                ItemWfmViewBinding wfmViewBinding = ItemWfmViewBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new WfmViewHolder(wfmViewBinding);
            case VIEW_TYPE_EMPTY:
            default:
                ItemWfmEmptyViewBinding emptyViewBinding = ItemWfmEmptyViewBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new EmptyViewHolder(emptyViewBinding);
        }
    }

    public void addItems(List<WfmItemViewModel> repoList) {
        Log.d("BenD", "Additems " + repoList.size());
        mWfmResponseList.clear();
        mWfmResponseList.addAll(repoList);
        mWfmCache.clear();
        mWfmCache.addAll(repoList);
        Log.d("BenD", "Cache size is " + mWfmCache.size());
        notifyDataSetChanged();
    }

    public void updateItems(List<WfmItemViewModel> repoList) {
        Log.d("BenD", "Update items " + repoList.size());
        mWfmResponseList.clear();
        mWfmResponseList.addAll(repoList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        mWfmResponseList.clear();
    }

    public void setListener(WfmAdapterListener listener) {
        this.mListener = listener;
    }

    public interface WfmAdapterListener {
        void onItemClick(String jobNumber);
        void onRetryClick();
    }

    public class EmptyViewHolder extends BaseViewHolder implements WfmEmptyItemViewModel.WfmEmptyItemViewModelListener {

        private final ItemWfmEmptyViewBinding mBinding;

        public EmptyViewHolder(ItemWfmEmptyViewBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            WfmEmptyItemViewModel emptyItemViewModel = new WfmEmptyItemViewModel(this);
            mBinding.setViewModel(emptyItemViewModel);
        }

        @Override
        public void onRetryClick() {
            mListener.onRetryClick();
        }
    }

    public class WfmViewHolder extends BaseViewHolder implements View.OnClickListener {

        private final ItemWfmViewBinding mBinding;

        public WfmViewHolder(ItemWfmViewBinding binding) {
            super(binding.getRoot());
            binding.getRoot().setOnClickListener(this);
            this.mBinding = binding;
        }

        @Override
        public void onBind(int position) {
            final WfmItemViewModel mWfmItemViewModel = mWfmResponseList.get(position);
            mBinding.setViewModel(mWfmItemViewModel);

            // Immediate Binding
            // When a variable or observable changes, the binding will be scheduled to change before
            // the next frame. There are times, however, when binding must be executed immediately.
            // To force execution, use the executePendingBindings() method.
            mBinding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            Log.d("BenD", String.valueOf(getAdapterPosition()));
            String jobNumber = mWfmResponseList.get(getAdapterPosition()).jobNumber.get();
            if (mWfmViewModel.addNewJobToList(jobNumber)) {
                mListener.onItemClick(jobNumber);
            }
        }
    }


    public boolean filterJobs(String text){
        if(text.isEmpty()){
            Log.d("BenD","text is empty, show all jobs");
            updateItems(mWfmCache);
            Log.d("BenD", "Wfm Cache size is " + mWfmCache.size());
            notifyDataSetChanged();
            return true;
        } else {
            ArrayList<WfmItemViewModel> jobList = new ArrayList<>();
            text = text.toLowerCase();
            for(WfmItemViewModel job : mWfmCache){
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
                notifyDataSetChanged();
                return false;
            } else {
                updateItems(jobList);
                notifyDataSetChanged();
                return true;
            }
        }
//        notifyDataSetChanged();
    }
}