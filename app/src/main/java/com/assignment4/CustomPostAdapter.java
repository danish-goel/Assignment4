package com.assignment4;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomPostAdapter extends RecyclerView.Adapter<CustomPostAdapter.ViewHolder>
{
    private List<Files> files;
    Context context;
    ProgressDialog pd;
//    List<Integer> colors=new ArrayList<Integer>(
//            Arrays.asList(R.color.greenishblue,R.color.purple,R.color.red,R.color.blue,R.color.green,R.color.limegreen,R.color.orange));

    List<String> colors=new ArrayList<String>(
            Arrays.asList("#009688","#9C27B0","#F44336","#8BC34A","#CDDC39","#FF9800","#9E9E9E"));


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        //public TextView mTextView;
        public CardView cardView;
        public ViewHolder(CardView v)
        {
            super(v);
            cardView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomPostAdapter(List<Files> files, Context context)
    {
        this.files = files;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view, parent, false);

            // set the view's size, margins, paddings and layout parameters
            //...
            ViewHolder vh = new ViewHolder((CardView) v);
            return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
            Log.d("postition", "post" + position);
            ((LinearLayout) holder.cardView.findViewById(R.id.header)).setBackgroundColor(Color.parseColor(getCardColor(position)));
            ((TextView) holder.cardView.findViewById(R.id.name)).setText(files.get(position).getName());
            ((Button) holder.cardView.findViewById(R.id.download)).setOnClickListener(new DownloadClickListener(position));
            ((Button) holder.cardView.findViewById(R.id.delete)).setOnClickListener(new DeleteClickListener(position));
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return files.size();
    }

    class DownloadClickListener implements View.OnClickListener
    {

        int index;

        public DownloadClickListener(int position) {
            this.index = position;
        }

        @Override
        public void onClick(View v)
        {
            new DownloadTask().execute();
        }
    }

    class DeleteClickListener implements View.OnClickListener
    {

        int index;

        public DeleteClickListener(int position) {
            this.index = position;
        }

        @Override
        public void onClick(View v)
        {
            new DownloadTask().execute();
        }
    }

    public class DownloadTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            initPD();
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {

            URL u = null;
            try {

                String root = System.getenv("EXTERNAL_STORAGE");
                new DefaultHttpClient().execute(new HttpGet("http://192.168.49.6/Assignment4/video/getfile/"))
                        .getEntity().writeTo(
                        new FileOutputStream(new File(root,"file.pdf")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            try{pd.setCancelable(true);}catch(Exception e){}
            try{pd.dismiss();}catch(Exception e){}
            super.onPostExecute(result);
        }

    }

    public class DeleteTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            initPD();
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
              Constants.postService.DeleteFile(0);
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            try{pd.setCancelable(true);}catch(Exception e){}
            try{pd.dismiss();}catch(Exception e){}
            super.onPostExecute(result);
        }

    }

    public String getCardColor(int position)
    {
        int i=0;
        i=position%colors.size();
        Log.d("color",i+" "+colors.get(i));
        return colors.get(i);
    }

    public void initPD()
    {
        pd=new ProgressDialog(context);
        pd.setTitle("Please Wait...");
        pd.setMessage("Downloading File");
        pd.setIndeterminate(true);
        pd.setCancelable(false);
    }

}
