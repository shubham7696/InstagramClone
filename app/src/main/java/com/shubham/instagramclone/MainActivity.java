package com.shubham.instagramclone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener , View.OnKeyListener
{

    EditText usernameField;
    EditText passwordField;

    TextView changeSignUpMode;

    Boolean signUpModeActive;

    Button signUpButton;


    ImageView logo;

    RelativeLayout relativeLayout;

    @Override
    public boolean onKey(View view, int keycode, KeyEvent keyEvent)
    {

        if(keycode == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN )
        {
            signUpOrLogin(view);
        }

        return false;
    }

    public void signUpOrLogin(View view)
    {

        if(signUpModeActive == true) {

            ParseUser user = new ParseUser();

            user.setUsername(String.valueOf(usernameField.getText()));
            user.setPassword(String.valueOf(passwordField.getText()));

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e)
                {
                    if (e == null)
                    {
                        Log.i("AppInfo", "Signup Successful");
                        showUserList();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else
        {
            ParseUser.logInInBackground(String.valueOf(usernameField.getText()),String.valueOf(passwordField.getText()), new LogInCallback()
            {
                @Override
                public void done(ParseUser parseUser, ParseException e)
                {

                    if(parseUser != null)
                    {
                        Log.i("AppInfo","Login Successful");
                        showUserList();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

        // New Data


    /*ParseObject score = new ParseObject("score");
        score.put("Username","tommy");
        score.put("score",200);
        score.saveInBackground(new SaveCallback()
        {
            @Override
            public void done(ParseException e)
            {
                if ( e == null)
                {
                    Log.i("SaveInBackground","success");
                }
                else
                {
                    Log.i("SaveInBackground","Failed");
                    e.printStackTrace();
                }
            }
        });
*/

        // update


       /* ParseQuery<ParseObject> query = ParseQuery.getQuery("score");
        query.getInBackground("uOFmAOD7JW", new GetCallback<ParseObject>()
        {
            @Override
            public void done(ParseObject parseObject, ParseException e)
            {
                if(e == null)
                {
                    parseObject.put("score",200);
                    parseObject.saveInBackground();
                }
            }
        });*/


        // To display all the Userlist
/*

        ParseQuery<ParseObject> query = ParseQuery.getQuery("score");


        query.whereEqualTo("Username","tommy");

        query.findInBackground(new FindCallback<ParseObject>()
        {
            @Override
            public void done(List<ParseObject> list, ParseException e)
            {
                if(e == null)
                {
                    Log.i("FindInBackground"," Reterived " + list.size() + " results ");

                    for(ParseObject object : list)
                    {
                     Log.i("findInBackgroundUser",String.valueOf(object.get("score")));
                    }
                }
            }
        });
*/


    }

    @Override
    public void onClick(View view)
    {
        if(view.getId()==R.id.changeSignUpMode)
        {
            if(signUpModeActive == true)
            {
                signUpModeActive =false;
                changeSignUpMode.setText("Sign Up");
                signUpButton.setText("Log In");

            }
            else
            {

                signUpModeActive =true;
                changeSignUpMode.setText("Log In");
                signUpButton.setText("Sign Up ");
            }
        }
        else if (view.getId() == R.id.logo || view.getId()==R.id.relativeLayout)
        {
            InputMethodManager in = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }

    }


    public void showUserList()
    {
        Intent i = new Intent(getApplicationContext(), UserList.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       Parse.initialize(new Parse.Configuration.Builder(MainActivity.this)
                .applicationId("I1Ar2KT3Rc3dIxaDp4CQlz37Ugjx8TaB8VdMBMA6")
                .clientKey("aD8QZaularzd1zTyhDtwmg87mu7CBgfYGTqB95El")
                .server("https://parseapi.back4app.com") // The trailing slash is important.


        .build()
        );

        if(ParseUser.getCurrentUser() !=null)
        {
             showUserList();
        }

        signUpModeActive =true;

        usernameField =(EditText)findViewById(R.id.username);
        passwordField = (EditText)findViewById(R.id.password);

        changeSignUpMode = (TextView)findViewById(R.id.changeSignUpMode);

        changeSignUpMode.setOnClickListener(this);

        signUpButton =(Button)findViewById(R.id.signUpBottom);

        usernameField.setOnKeyListener(this);
        passwordField.setOnClickListener(this);

        logo =(ImageView)findViewById(R.id.logo);

        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);

        logo.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);

    }


}
