package com.realeyez.trademart.gui.components.job;

import com.realeyez.trademart.R;
import com.realeyez.trademart.gui.components.job.event.OnJobItemClickedListener;
import com.realeyez.trademart.job.JobItem;
import com.realeyez.trademart.job.JobItemMixed;
import com.realeyez.trademart.job.JobTransactionType;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class JobItemPanelMixed extends ConstraintLayout {

    private CircleImageView profileImage;
    private TextView usernameLabel;
    private TextView titleLabel;
    private TextView typeLabel;

    private JobItemMixed jobItem;
    
    private OnJobItemClickedListener onJobItemClickedListener;

    public JobItemPanelMixed(@NonNull Context context) {
        super(context);
    }

    public JobItemPanelMixed(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public JobItemPanelMixed(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JobItemPanelMixed(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        initComponents();
    }

    private void initComponents(){
        profileImage = findViewById(R.id.jobitemmixed_profile_view);
        usernameLabel = findViewById(R.id.jobitemmixed_username_view);
        titleLabel = findViewById(R.id.jobitemmixed_job_title_view);
        typeLabel = findViewById(R.id.jobitemmixed_job_type_view);

        setOnClickListener(view -> {
            if(onJobItemClickedListener != null)
                onJobItemClickedListener.onJobItemClicked(jobItem);
        });
    }

    private void loadData(JobItemMixed jobItem){
        Logger.log("laying out ", LogLevel.INFO);
        this.jobItem = jobItem;
        if(jobItem.getProfilePictureUri() != null)
            profileImage.setImageURI(jobItem.getProfilePictureUri());
        else
            profileImage.setImageDrawable(getResources().getDrawable(R.drawable.profile_picture_placeholder, null));
        usernameLabel.setText(jobItem.getUsername());
        titleLabel.setText(jobItem.getTitle());
        typeLabel.setText(jobItem.getType() == JobTransactionType.APPLICATION ? "Job" : "Recruitment");
    }

    public static JobItemPanelMixed inflate(LayoutInflater inflater, JobItemMixed item){
        JobItemPanelMixed view = (JobItemPanelMixed) inflater.inflate(R.layout.layout_job_item_mixed, null);
        view.loadData(item);
        return view;
    }

    public void setOnJobItemClickedListener(OnJobItemClickedListener onJobItemClickedListener){
        this.onJobItemClickedListener = onJobItemClickedListener;
    }

}
