package nz.co.k2.k2e.ui.jobs.wfmjobs;

import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.data.model.db.WfmJob;
import nz.co.k2.k2e.databinding.FragmentWfmBinding;
import nz.co.k2.k2e.ui.base.BaseFragment;

// TODO Make this a pop out fragment or activity, not linked with NavDrawer
public class WfmFragment extends BaseFragment<FragmentWfmBinding, WfmViewModel>
        implements WfmNavigator, WfmAdapter.WfmAdapterListener {

    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;
    FragmentWfmBinding mFragmentWfmBinding;
    @Inject
    @Named("WfmLinearLayout")
    LinearLayoutManager mLayoutManager;
    @Inject
    WfmAdapter mWfmAdapter;
    @Inject
    @Named("WfmFragment")
    ViewModelProvider.Factory mViewModelFactory;
    @Inject
    WfmViewModel mWfmViewModel;

    public static WfmFragment newInstance() {
        Bundle args = new Bundle();
        WfmFragment fragment = new WfmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_wfm;
    }

    @Override
    public WfmViewModel getViewModel() {
        return mWfmViewModel;
    }

    @Override
    public void handleError(Throwable throwable) {
        // handle error
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWfmViewModel.setNavigator(this);
        mWfmAdapter.setListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = super.onCreateView(inflater, container, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.wfmSwipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWfmJobList(true);
            }
        });

        searchView = view.findViewById(R.id.wfmSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Boolean nonEmptyList = mWfmAdapter.filterJobs(query);
                searchView.clearFocus();
                hideKeyboard();

                if (!nonEmptyList) {
                    // Do Wfm API call to the job number entered
                    Log.d("BenD", "Searching WFM for " + query);
//                    swipeRefreshLayout.setRefreshing(true);
                    mWfmViewModel.getDataManager().getWfmApiCall(query)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(__ -> swipeRefreshLayout.setRefreshing(true))
                            .doFinally(() -> swipeRefreshLayout.setRefreshing(false))
                            .subscribe(new SingleObserver<List<WfmJob>>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    getCompositeDisposable().add(d);
                                }

                                @Override
                                public void onSuccess(List<WfmJob> wfmJobs) {
                                    if (wfmJobs.get(0).getStatus().equals("ERROR")) {
                                        Toast.makeText(getActivity(),"Job not found on WorkflowMax", Toast.LENGTH_SHORT).show();
                                        mWfmAdapter.filterJobs("");
                                    } else {
                                        Log.d("BenD", "Saving all jobs from specific query");
                                        mWfmViewModel.addWfmItemsToList(mWfmViewModel.getViewModelList(wfmJobs));
                                        mWfmViewModel.saveAllWfmJobs(wfmJobs);
                                        mWfmAdapter.addItems(mWfmViewModel.getWfmItemViewModels());
                                        mWfmAdapter.filterJobs(query);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                }
                            });
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Boolean nonEmptyList = mWfmAdapter.filterJobs(newText);
                mWfmAdapter.filteredList = nonEmptyList;
                if (!nonEmptyList) {
                    //
                }
                return true;
            }
        });

        getWfmJobList(false);
        return view;
    }

    public void getWfmJobList(Boolean forceRefresh){
        mWfmViewModel.getWfmList(forceRefresh)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(__ -> swipeRefreshLayout.setRefreshing(true))
            .doFinally(() -> swipeRefreshLayout.setRefreshing(false))
            .subscribe(new SingleObserver<List<WfmJob>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    getCompositeDisposable().add(d);
                }

                @Override
                public void onSuccess(List<WfmJob> wfmJobs) {
                    if (wfmJobs.isEmpty()) {
                        Toast.makeText(getActivity(),"No jobs found on WorkflowMax", Toast.LENGTH_SHORT).show();
                    } else {
                        mWfmViewModel.addWfmItemsToList(mWfmViewModel.getViewModelList(wfmJobs));
                        mWfmViewModel.saveAllWfmJobs(wfmJobs);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }
            });

    }

    @Override
    public void onItemClick(String jobNumber) {
        Toast.makeText(getActivity(),jobNumber + " was added to your jobs.", Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onJobLoaded(){
        Toast.makeText(getActivity(), "You have already loaded this job.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRetryClick() {
        mWfmViewModel.getWfmList(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentWfmBinding = getViewDataBinding();
        setUp();
        subscribeToLiveData();
    }

    private void setUp() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFragmentWfmBinding.WFMRecycler.setLayoutManager(mLayoutManager);
        mFragmentWfmBinding.WFMRecycler.setItemAnimator(new DefaultItemAnimator());
        mFragmentWfmBinding.WFMRecycler.setAdapter(mWfmAdapter);
    }

    private void subscribeToLiveData() {
        mWfmViewModel.getWfmRepos().observe(this,
                wfmItemViewModels -> mWfmViewModel.addWfmItemsToList(wfmItemViewModels));
    }


    public void onResume()
    {
        super.onResume();
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
    }
}
