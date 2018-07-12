package nz.co.k2.k2e.ui.samples.asbestos.bulk;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.NumberPicker;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Named;

import nz.co.k2.k2e.BR;
import nz.co.k2.k2e.R;
import nz.co.k2.k2e.databinding.FragmentSampleAsbestosBulkBinding;
import nz.co.k2.k2e.ui.base.BaseFragment;
import nz.co.k2.k2e.ui.jobs.jobmain.JobViewModel;

public class AsbestosBulkSampleFragment extends BaseFragment<FragmentSampleAsbestosBulkBinding, AsbestosBulkSampleViewModel> {
    FragmentSampleAsbestosBulkBinding fragmentSampleAsbestosBulkBinding;
    @Inject
    @Named("AsbestosBulkSampleFragment")
    ViewModelProvider.Factory mViewModelFactory;
    @Inject
    AsbestosBulkSampleViewModel asbestosBulkSampleViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sample_asbestos_bulk;
    }

    @Override
    public AsbestosBulkSampleViewModel getViewModel() {
        asbestosBulkSampleViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(AsbestosBulkSampleViewModel.class);
        return asbestosBulkSampleViewModel;
    }

    NumberPicker numberPicker;
//    String[] asbestosMaterials = getResources().getStringArray(R.array.asbestos_materials);

//
//    // newInstance constructor for creating fragment with arguments
//    public static AsbestosBulkSampleFragment newInstance(int page, String title) {
//        AsbestosBulkSampleFragment asbestosBulkSampleFragment = new AsbestosBulkSampleFragment();
//        Bundle args = new Bundle();
//        args.putInt("someInt", page);
//        args.putString("someTitle", title);
//        asbestosBulkSampleFragment.setArguments(args);
//        return asbestosBulkSampleFragment;
//    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uuid = getArguments().getString("sample_uuid", "");
        asbestosBulkSampleViewModel.sampleUuid.set(uuid);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Toast.makeText(getActivity(),"Number is now " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();
            }
        });

        String[] asbestosMaterials = getResources().getStringArray(R.array.asbestos_materials);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, asbestosMaterials);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                view.findViewById(R.id.material);
        textView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentSampleAsbestosBulkBinding = getViewDataBinding();
    }
}
