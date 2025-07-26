package com.realeyez.trademart.gui.components.job;

import com.realeyez.trademart.R;
import com.realeyez.trademart.job.JobItem;
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

public class JobItemPanel extends ConstraintLayout {

    private CircleImageView profileImage;
    private TextView usernameLabel;
    private TextView titleLabel;

    private JobItem jobItem;

    public JobItemPanel(@NonNull Context context) {
        super(context);
    }

    public JobItemPanel(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public JobItemPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JobItemPanel(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        initComponents();
    }

    private void initComponents(){
        profileImage = findViewById(R.id.jobitem_profile_view);
        usernameLabel = findViewById(R.id.jobitem_username_view);
        titleLabel = findViewById(R.id.jobitem_job_title_view);
    }

    private void loadData(JobItem jobItem){
        Logger.log("laying out ", LogLevel.INFO);
        this.jobItem = jobItem;
        if(jobItem.getProfilePictureUri() != null)
            profileImage.setImageURI(jobItem.getProfilePictureUri());
        else
            profileImage.setImageDrawable(getResources().getDrawable(R.drawable.profile_picture_placeholder, null));
        usernameLabel.setText(jobItem.getUsername());
        titleLabel.setText(jobItem.getTitle());
    }

    public static JobItemPanel inflate(LayoutInflater inflater, JobItem item){
        JobItemPanel view = (JobItemPanel) inflater.inflate(R.layout.layout_job_item, null);
        view.loadData(item);
        return view;
    }

}
