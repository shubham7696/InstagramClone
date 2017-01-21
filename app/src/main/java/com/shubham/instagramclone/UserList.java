package com.shubham.instagramclone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import static com.shubham.instagramclone.R.id.username;

public class UserList extends AppCompatActivity
{

    ArrayList<String> username;
    ArrayAdapter arrayAdapter;


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.userlist,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.share )

        {

            Intent  i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i,1);

        }
        else if (id == R.id.logout)
        {

            ParseUser.logOut();
            System.exit(0);

            /*Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data !=null )
        {
            Uri selectedImage = data.getData();

            try
            {
                Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
/*
                ImageView imageView = (ImageView)findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmapImage);*/

                Log.i("AppInfo","Image Recevied");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmapImage.compress(Bitmap.CompressFormat.JPEG,50,stream);

                byte[] byteArray = stream.toByteArray();

                ParseFile file = new ParseFile("image.jpg",byteArray);

                ParseObject object = new ParseObject("images");
                object.put("username",ParseUser.getCurrentUser().getUsername());
                object.put("image",file);

               /* ParseACL parseACL = new ParseACL();
                parseACL.setPublicReadAccess(true);
                object.setACL(parseACL);*/


                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if(e == null)
                        {

                            Toast.makeText(getApplication().getBaseContext(),"Your Image has been posted",Toast.LENGTH_LONG).show();
                            Log.i("Upload","Success");
                        }
                        else
                        {
                            Toast.makeText(getApplication().getBaseContext(),"There was an error - please try again",Toast.LENGTH_LONG).show();
                            Log.i("Upload","Failure");
                        }

                    }
                });

            }
            catch (IOException e)
            {
                e.printStackTrace();
                Toast.makeText(getApplication().getBaseContext(),"There was an error - please try again",Toast.LENGTH_LONG).show();

            }
        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        username = new ArrayList<String>();


        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,username);



        final ListView userList = (ListView)findViewById(R.id.userList);


        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                Intent i = new Intent(getApplicationContext(),UserFeed.class);
                i.putExtra("username",username.get(position));
                startActivity(i);

            }
        });

        ParseQuery<ParseUser> query =  ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.addAscendingOrder("username");

        query.findInBackground(new FindCallback<ParseUser>()
        {
            @Override
            public void done(List<ParseUser> list, ParseException e)
            {

                if( e == null)
                {
                    if(list.size()>0)
                    {
                        for(ParseUser user : list)
                        {

                            username.add(user.getUsername());
                        }
                        userList.setAdapter(arrayAdapter);
                    }
                }

            }
        });
    }

}
