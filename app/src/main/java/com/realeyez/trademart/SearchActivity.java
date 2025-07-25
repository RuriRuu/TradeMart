package com.realeyez.trademart;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.tabs.TabLayout;
import com.realeyez.trademart.gui.components.search.JobListingResultFragment;
import com.realeyez.trademart.gui.components.search.PostResultFragment;
import com.realeyez.trademart.gui.components.search.ServiceResultFragment;
import com.realeyez.trademart.gui.components.search.UserResultFragment;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.search.MediaSearchResult;
import com.realeyez.trademart.search.UserSearchResult;
import com.realeyez.trademart.user.User;
import com.realeyez.trademart.util.CacheFile;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SearchActivity extends AppCompatActivity {

    private ImageButton backButton;
    private EditText searchField;
    private ImageButton submitButton;

    private TabLayout tabLayout;

    private FragmentManager fragman;

    private PostResultFragment postFrag;
    private ServiceResultFragment serviceFrag;
    private JobListingResultFragment jobFrag;
    private UserResultFragment userFrag;

    private String prevSearch;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initComponents();
    }

    private void initComponents(){
        prevSearch = "";

        backButton = findViewById(R.id.search_back_button);
        searchField = findViewById(R.id.search_query_field);
        submitButton = findViewById(R.id.search_submit_button);

        tabLayout = findViewById(R.id.search_result_toolbar);

        fragman = getSupportFragmentManager();

        postFrag = new PostResultFragment();
        serviceFrag = new ServiceResultFragment();
        jobFrag = new JobListingResultFragment();
        userFrag = new UserResultFragment();

        fragman.beginTransaction()
            .add(R.id.search_content_panel, postFrag)
            .add(R.id.search_content_panel, serviceFrag)
            .add(R.id.search_content_panel, jobFrag)
            .add(R.id.search_content_panel, userFrag)
            .setReorderingAllowed(true)
            .commit();

        showFrag(postFrag);

        addOnClickListeners();
    }

    private ArrayList<UserSearchResult> requestUserSearch(String query) {
        ArrayList<UserSearchResult> results = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>();
        params.put("query", query);
        try {
            Response response = RequestUtil.sendGetRequest("/search/user", params);
            JSONObject json = response.getContentJson();
            if(json.getString("status").equals("failed")){
                String errorMessage = json.getString("message");
                runOnUiThread(() -> Dialogs.showErrorDialog(errorMessage, this));
                return null;
            }
            JSONArray resultsJson = json.getJSONObject("data").getJSONArray("results");
            for (int i = 0; i < resultsJson.length(); i++) {
                JSONObject resultJson = resultsJson.getJSONObject(i);
                String result = resultJson.getString("result");
                int id = resultJson.getInt("id");
                double relPoints = resultJson.getDouble("relevance");
                JSONObject entity = resultJson.getJSONObject("entity");
                Uri profilePicture = requestProfilePicture(entity.getInt("id"));
                // JSONArray mediaIdsJson = resultJson.getJSONArray("media_ids");
                // ArrayList<Integer> mediaIds = new ArrayList<>();
                // for (int j = 0; j < mediaIdsJson.length(); j++)
                //     mediaIds.add(mediaIdsJson.getInt(j));
                results.add(new UserSearchResult.Builder()
                        .setId(id)
                        .setTerm(result)
                        .setRelPoints(relPoints)
                        .setEntity(entity)
                        .setProfilePictureUri(profilePicture)
                        .build());
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            runOnUiThread(() -> Dialogs.showErrorDialog("Unable to search", this));
            return null;
        }
        return results;
    }

    private ArrayList<MediaSearchResult> requestPostSearch(String query){
        return requestMediaSearch(query, "/search/post");
    }

    private ArrayList<MediaSearchResult> requestServiceSearch(String query){
        return requestMediaSearch(query, "/search/service");
    }

    private ArrayList<MediaSearchResult> requestJobSearch(String query){
        return requestMediaSearch(query, "/search/job");
    }

    private ArrayList<MediaSearchResult> requestMediaSearch(String query, String path) {
        ArrayList<MediaSearchResult> results = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>();
        params.put("query", query);
        try {
            Response response = RequestUtil.sendGetRequest(path, params);
            JSONObject json = response.getContentJson();
            if(json.getString("status").equals("failed")){
                String errorMessage = json.getString("message");
                runOnUiThread(() -> Dialogs.showErrorDialog(errorMessage, this));
                return null;
            }
            JSONArray resultsJson = json.getJSONObject("data").getJSONArray("results");
            for (int i = 0; i < resultsJson.length(); i++) {
                JSONObject resultJson = resultsJson.getJSONObject(i);
                String result = resultJson.getString("result");
                int id = resultJson.getInt("id");
                double relPoints = resultJson.getDouble("relevance");

                JSONObject entity = resultJson.getJSONObject("entity");
                JSONObject userJson = entity.getJSONObject("user");
                JSONArray mediaIdsJson = resultJson.getJSONArray("media_ids");
                ArrayList<Integer> mediaIds = new ArrayList<>();
                for (int j = 0; j < mediaIdsJson.length(); j++)
                    mediaIds.add(mediaIdsJson.getInt(j));

                Uri profilePicture = requestProfilePicture(userJson.getInt("id"));
                Uri thumbnailUri = null;
                if(mediaIds.size() > 0)
                    thumbnailUri = requestThumbnail(mediaIds.get(0));

                results.add(new MediaSearchResult.Builder()
                        .setId(id)
                        .setTerm(result)
                        .setRelPoints(relPoints)
                        .setEntity(entity)
                        .setUser(new User.UserBuilder().fromJSON(userJson))
                        .setMediaIds(mediaIds)
                        .setProfilePictureUri(profilePicture)
                        .setThumbnailUri(thumbnailUri)
                        .build());
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
            runOnUiThread(() -> Dialogs.showErrorDialog("Unable to search", this));
            return null;
        }
        return results;
    }

    private Uri requestProfilePicture(int userId){
        try {
            String path = new StringBuilder()
                .append("/user/")
                .append(userId)
                .append("/avatar")
                .toString();
            Response response = RequestUtil.sendGetRequest(path);
            String filename = response.getContentDispositionField("filename");
            byte[] profilePictureData = response.getContentBytes();

            CacheFile cache = CacheFile.cache(getCacheDir(), filename, profilePictureData);

            return Uri.fromFile(cache.getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Uri requestThumbnail(int mediaId){
        try {
            String path = new StringBuilder()
                .append("/media/thumbnail/")
                .append(mediaId)
                .toString();
            Response response = RequestUtil.sendGetRequest(path);
            String filename = response.getContentDispositionField("filename");
            byte[] thumbnailData = response.getContentBytes();

            CacheFile cache = CacheFile.cache(getCacheDir(), filename, thumbnailData);

            return Uri.fromFile(cache.getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showServiceResults(String query){
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            ArrayList<MediaSearchResult> results = requestServiceSearch(query);
            runOnUiThread(() -> serviceFrag.loadResults(results));
        });
    }

    private void showJobResults(String query){
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            ArrayList<MediaSearchResult> results = requestJobSearch(query);
            runOnUiThread(() -> jobFrag.loadResults(results));
        });
    }

    private void showPostResults(String query){
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            ArrayList<MediaSearchResult> results = requestPostSearch(query);
            runOnUiThread(() -> postFrag.loadResults(results));
        });
    }

    private void showUserResults(String query){
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            ArrayList<UserSearchResult> results = requestUserSearch(query);
            runOnUiThread(() -> userFrag.loadResults(results));
        });
    }

    private void hideFrags(FragmentTransaction transaction, Fragment fragToShow){
        if(!fragToShow.equals(postFrag)){
            transaction.hide(postFrag);
        }
        if(!fragToShow.equals(serviceFrag)){
            transaction.hide(serviceFrag);
        }
        if(!fragToShow.equals(jobFrag)){
            transaction.hide(jobFrag);
        }
        if(!fragToShow.equals(userFrag)){
            transaction.hide(userFrag);
        }
    }

    private void showFrag(Fragment frag){
        FragmentTransaction transaction = fragman.beginTransaction();
        hideFrags(transaction, frag);
        transaction
            .show(frag)
            .setReorderingAllowed(true)
            .commit();
    }

    private void onTabSelectedAction(TabLayout.Tab tab) {
        if(tab.getPosition() == 0){
            showFrag(postFrag);
            return;
        }
        if(tab.getPosition() == 1){
            showFrag(serviceFrag);
            return;
        }
        if(tab.getPosition() == 2){
            showFrag(jobFrag);
            return;
        }
        if(tab.getPosition() == 3){
            showFrag(userFrag);
            return;
        } else {
            showFrag(serviceFrag);
        }
    }

    private void addOnClickListeners(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabSelectedAction(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        userFrag.setOnRefreshEvent(() -> {
            if(prevSearch.isEmpty()){
                return;
            }
            searchField.setText(prevSearch);
            showUserResults(prevSearch);
        });
        postFrag.setOnRefreshEvent(() -> {
            if(prevSearch.isEmpty()){
                return;
            }
            searchField.setText(prevSearch);
            showPostResults(prevSearch);
        });
        jobFrag.setOnRefreshEvent(() -> {
            if(prevSearch.isEmpty()){
                return;
            }
            searchField.setText(prevSearch);
            showJobResults(prevSearch);
        });
        serviceFrag.setOnRefreshEvent(() -> {
            if(prevSearch.isEmpty()){
                return;
            }
            searchField.setText(prevSearch);
            showServiceResults(prevSearch);
        });

        submitButton.setOnClickListener(view -> {
            String query = searchField.getText().toString();
            if(query.isEmpty()){
                return;
            }
            showPostResults(query);
            showUserResults(query);
            showServiceResults(query);
            showJobResults(query);
            prevSearch = query;
        });

        backButton.setOnClickListener(view -> {
            finish();
        });
    }
    
}
