package nz.co.k2.k2e.ui.jobs.wfmjobs;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

import nz.co.k2.k2e.R;

public class WfmItemViewModel {
    public final ObservableField<String> jobNumber = new ObservableField<>();

    public final ObservableField<String> address = new ObservableField<>();

    public final ObservableField<String> clientName = new ObservableField<>();

    public final ObservableField<String> type = new ObservableField<>();

    public final ObservableInt typeIcon = new ObservableInt(type);

    public final ObservableField<String> state = new ObservableField<>();

    public WfmItemViewModel(String jobNumber, String address, String clientName, String type, String state) {
        if (jobNumber == null) { this.jobNumber.set("No job number"); } else { this.jobNumber.set(jobNumber); }
        if (address == null) { this.address.set("No address specified"); } else { this.address.set(address); }
        if (clientName == null) { this.clientName.set("No client specified"); } else { this.clientName.set(clientName); }
        if (type == null) { this.type.set("Other"); } else { this.type.set(type); }
        if (type == null){
            this.typeIcon.set(R.drawable.ic_my_jobs);
        } else if (type.toLowerCase().contains("asbestos")){
            this.typeIcon.set(R.drawable.ic_job_asbestos);
        } else if (type.toLowerCase().contains("meth")){
            this.typeIcon.set(R.drawable.ic_job_meth);
        } else if (type.toLowerCase().contains("noise")) {
            this.typeIcon.set(R.drawable.ic_job_noise);
        } else if (type.toLowerCase().contains("bio")){
            this.typeIcon.set(R.drawable.ic_job_bio);
        } else if (type.toLowerCase().contains("stack")){
            this.typeIcon.set(R.drawable.ic_job_stack);
        } else {
            this.typeIcon.set(R.drawable.ic_my_jobs);
        }
        this.state.set(state);
//        Log.d("BenD", type + ": " + String.valueOf(typeIcon.get()));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Drawable getIcon(Context context){
        return context.getDrawable(typeIcon.get());
    }
}
