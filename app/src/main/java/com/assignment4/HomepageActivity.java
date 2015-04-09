package com.assignment4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomepageActivity extends ActionBarActivity implements OnClickListener
{
    Button newstory;
    ProgressDialog pd;
    Context context=HomepageActivity.this;

    /*-----------Drawer items---------*/
    RecyclerView mRightNavigationDrawerRecyclerView;
	/*---------------------------*/
    List<Files> files=new ArrayList<Files>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout swipeLayout;
    RadioGroup rg;

    String filePath, responseString;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /*--------------------------------Start Of Oncreate----------------------------------------------------------*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        /*--------------------------------Tooolbar----------------------------------------------------------*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setTitle("Posting Wall");
        setSupportActionBar(toolbar);

        newstory=(Button)findViewById(R.id.newstorybutton);
        newstory.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CustomPostAdapter(files,this);
        mRecyclerView.setAdapter(mAdapter);

        new BgTask().execute();

    /*------------------------Swipe Refresh Layout------------------------------------------------------------------*/


        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                new RefreshTask().execute();
            }
        });
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    /*--------------------------------End Of Oncreate----------------------------------------------------------*/

    }


    /*---------------------------------------Bg Task---------------------------------------------------*/
    public class BgTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            initPD();
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Log.d("result",Constants.postService.getFiles().toString());
//            files.addAll(Constants.postService.getFiles());
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            mAdapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
            try{pd.setCancelable(true);}catch(Exception e){}
            try{pd.dismiss();}catch(Exception e){}
            super.onPostExecute(result);
        }

    }
    /*----------------------------------Refresh Task--------------------------------------------------------*/

    public class RefreshTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            swipeLayout.setRefreshing(true);
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Log.d("result",Constants.postService.getFiles().toString());
//            files.addAll(Constants.postService.getFiles());
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            mAdapter.notifyDataSetChanged();
            swipeLayout.setRefreshing(false);
            super.onPostExecute(result);
        }

    }
    /*----------------------------------Initializing Progress Dialog--------------------------------------------------------*/
    public void initPD()
    {
        pd=new ProgressDialog(context);
        pd.setTitle("Please Wait...");
        pd.setMessage("Fetching Data");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
    }
    /*----------------------------------OnClick Listener--------------------------------------------------------*/
    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.newstorybutton)
        {
            showFileChooser();
        }

    }

    private static final int FILE_SELECT_CODE = 0;

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    // Get the path
                    String path = uri.getPath();//FileUtils.getPath(this, uri);
                    filePath = path;
                    new BgTask().execute();
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    /*----------------------------------**END***--------------------------------------------------------*/

}