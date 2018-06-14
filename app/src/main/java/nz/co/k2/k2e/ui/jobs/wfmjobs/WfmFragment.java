package nz.co.k2.k2e.ui.jobs.wfmjobs;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.reactivestreams.Subscriber;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.databinding.FragmentWfmBinding;

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
    public void onItemClick(String jobNumber) {
        // Card has been clicked
        getActivity().getSupportFragmentManager().popBackStackImmediate();
        Toast.makeText(getContext(), jobNumber + " has been added to your jobs.", Toast.LENGTH_LONG).show();
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
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.wfmSwipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                mWfmViewModel.loadWfmItems(true).
                        subscribe()
            }
        });
        searchView = (SearchView) view.findViewById(R.id.wfmSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Boolean nonEmptyList = mWfmAdapter.filterJobs(query);
                searchView.clearFocus();
                if (!nonEmptyList) {
                    // Do Wfm API call to the job number entered
                    Log.d("BenD", "Searching WFM for " + query);
                    mWfmViewModel.getWfmJobByNumber(query);
                    mWfmAdapter.filterJobs(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Boolean nonEmptyList = mWfmAdapter.filterJobs(newText);
                if (!nonEmptyList) {
                    //
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onRetryClick() {
        //
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
        Log.d("BenD","Wfm fragment set up " + mFragmentWfmBinding.WFMRecycler.getLayoutManager().toString());
        mFragmentWfmBinding.WFMRecycler.setItemAnimator(new DefaultItemAnimator());
        mFragmentWfmBinding.WFMRecycler.setAdapter(mWfmAdapter);
    }

    private void subscribeToLiveData() {
        mWfmViewModel.getWfmRepos().observe(this,
                new Observer<List<WfmItemViewModel>>() {
                    @Override
                    public void onChanged(@Nullable List<WfmItemViewModel> wfmItemViewModels) {
                        mWfmViewModel.addWfmItemsToList(wfmItemViewModels);
                    }
                });
    }


    public void onResume()
    {
        super.onResume();
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
    }
}
