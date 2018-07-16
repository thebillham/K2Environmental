package nz.co.k2.k2e.ui.jobs.wfmjobs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nz.co.k2.k2e.data.model.db.jobs.BaseJob;
import nz.co.k2.k2e.databinding.ItemWfmEmptyViewBinding;
import nz.co.k2.k2e.databinding.ItemWfmViewBinding;
import nz.co.k2.k2e.ui.base.BaseViewHolder;

public class WfmAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_EMPTY = 0;

    public static final int VIEW_TYPE_NORMAL = 1;

    public boolean filteredList = false;

    private List<WfmItemViewModel> mWfmResponseList;

    private final List<WfmItemViewModel> mWfmCache;

    private WfmAdapterListener mListener;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        mWfmResponseList.clear();
        mWfmResponseList.addAll(repoList);
        mWfmCache.clear();
        mWfmCache.addAll(repoList);
        notifyDataSetChanged();
    }

    private void updateItems(List<WfmItemViewModel> repoList) {
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
        void onJobLoaded();
    }

    public class EmptyViewHolder extends BaseViewHolder implements WfmEmptyItemViewModel.WfmEmptyItemViewModelListener {

        private final ItemWfmEmptyViewBinding mBinding;

        private EmptyViewHolder(ItemWfmEmptyViewBinding binding) {
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

        private WfmViewHolder(ItemWfmViewBinding binding) {
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
            String jobNumber = mWfmResponseList.get(getAdapterPosition()).jobNumber.get();
            // TODO Have changed PHP to return full job data for all Wfm Jobs instead of only getting full data onClick.
            // Check if Job is already loaded into My Jobs. If it is not loaded, onComplete will be called
            // Note: this may cause unexpected loadJob functions if onComplete happens more than expected

            mWfmViewModel.getDataManager().getJobByJobNumber(jobNumber)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MaybeObserver<BaseJob>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onSuccess(BaseJob baseJob) {
                            mListener.onJobLoaded();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            loadJob(jobNumber);
                        }
                    });
        }
    }


    public void loadJob(String jobNumber) {
        // This function takes a jobnumber and converts the WFM Job to a new Base Job, it then
        // returns to the main job screen
        Log.d("BenD", "loadJob " + jobNumber);
        mWfmViewModel.getDataManager().getWfmJobByNumber(jobNumber)
                .subscribeOn(Schedulers.io())
                .flatMap(wfmJob -> {
                    Log.d("BenD", "Got wfm job " + wfmJob.getJobNumber());
                    return mWfmViewModel.getDataManager().pushJob(mWfmViewModel.createBaseJob(wfmJob));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        mListener.onItemClick(jobNumber);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


    public boolean filterJobs(String text){
        if(text.isEmpty()){
            updateItems(mWfmCache);
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
            if(jobList.isEmpty()){
                clearItems();
                notifyDataSetChanged();
                return false;
            } else {
                updateItems(jobList);
                notifyDataSetChanged();
                filteredList = true;
                return true;
            }
        }
//        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        compositeDisposable.dispose();
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
