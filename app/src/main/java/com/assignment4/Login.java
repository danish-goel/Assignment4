package com.assignment4;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Danish Goel on 09-Apr-15.
 */
public class Login extends ActionBarActivity implements View.OnClickListener
{
        EditText username,password;
    String user,userpassword;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
            /*--------------------------------Start Of Oncreate----------------------------------------------------------*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }
    public class LoginTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Constants.postService.Login(user,userpassword);
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            Intent i=new Intent("com.assignment.homepage");
            startActivity(i);
            super.onPostExecute(result);
        }

    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.submit)
        {
            user=username.getText().toString();
            userpassword=password.getText().toString();
            new LoginTask().execute();
        }
    }
}
