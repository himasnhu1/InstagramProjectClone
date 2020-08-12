package com.example.instagramproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class userListActivity extends AppCompatActivity {

    public void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getPhoto();

            }


        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_share, menu);

        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

//        item.getItemId()== R.id.currentuser

        switch (item.getItemId()){

            case R.id.itemshare:

                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {

                    getPhoto();
                }

                return true;

            case R.id.logut:

                ParseUser.logOut();
               Intent intent = new Intent(userListActivity.this,MainActivity.class);
               startActivity(intent);

                return true;

            default:

            return false;

        }
    }



        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

                Uri selectedImage;
                selectedImage = data.getData();

                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                    Log.i("Photo", "Received");

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    byte[] byteArray = stream.toByteArray();

                    ParseFile file = new ParseFile("image.png", byteArray);

                    ParseObject object = new ParseObject("Image");

                    object.put("image", file);

                    object.put("username", ParseUser.getCurrentUser().getUsername());

                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                                Toast.makeText(userListActivity.this, "Image Shared!", Toast.LENGTH_SHORT).show();

                            } else {

                                Toast.makeText(userListActivity.this, "Image could not be shared - please try again later.", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }


                    @Override
                    protected void onCreate (Bundle savedInstanceState){
                        super.onCreate(savedInstanceState);
                        setContentView(R.layout.activity_user_list);

                        final ListView listView = (ListView) findViewById(R.id.listview);

                        final ArrayList<String> userarrayList = new ArrayList<String>();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(getApplicationContext(),UserFeedActivity.class);
                                intent.putExtra("username",userarrayList.get(i));
                                startActivity(intent);
                            }
                        });

                        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userarrayList);

                        ParseQuery<ParseUser> query = ParseUser.getQuery();

                        query.whereNotEqualTo("usename", ParseUser.getCurrentUser().getUsername());

                        query.addAscendingOrder("usename");

                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {

                                if (e == null) {

                                    if (objects.size() > 0) {

                                        for (ParseUser user : objects) {

                                            userarrayList.add(user.getUsername());

                                        }
                                        listView.setAdapter(arrayAdapter);

                                    }
                                } else {

                                    e.printStackTrace();

                                }
                            }
                        });

                    }



    }