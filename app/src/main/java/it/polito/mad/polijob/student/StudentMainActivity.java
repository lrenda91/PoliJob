package it.polito.mad.polijob.student;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import it.polito.mad.polijob.Utility;
import it.polito.mad.polijob.model.PoliJobDB.ParseObjectObserver;
import it.polito.mad.polijob.MainActivity;
import it.polito.mad.polijob.R;
import it.polito.mad.polijob.model.PoliJobDB;
import it.polito.mad.polijob.model.Student;
import it.polito.mad.polijob.navigation.PoliJobNavigationDrawer;

/**
 * Created by Francesco on 10/05/2015.
 */
public class StudentMainActivity extends AppCompatActivity implements OnScrollListener {

    private Student user = (Student) ParseUser.getCurrentUser().get(PoliJobDB.SPECIFIC_USER);

    private Fragment[] pages = new Fragment[7];

    private PoliJobNavigationDrawer mNavigationDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private int currentAlpha = 255;
    private int i;
    private ImageView mImageView;

    private boolean doubleBackToExitPressedOnce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getBackground().setAlpha(255);

        setUpNavigationDrawer();

        mNavigationDrawer.setOnItemClickListener(new PoliJobNavigationDrawer.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int idx = position - 1;
                if (pages[idx] == null) {
                    switch (position) {
                        case 1:
                            i = 1;
                            pages[idx] = new ProfileFragment();
                            break;
                        case 2:
                            i = 0;
                            pages[idx] = new NewsFragment();
                            break;
                        case 3:
                            i = 0;
                            pages[idx] = new SearchPositionsFragment();
                            break;
                        case 4:
                            i = 0;
                            pages[idx] = new FavoritePositionsFragment();
                            break;
                        case 5:
                            i = 0;
                            pages[idx] = new SearchCompaniesFragment();
                            break;
                        case 6:
                            i = 0;
                            pages[idx] = new FavoriteCompaniesFragment();
                            break;
                        case 7:
                            i = 0;
                            pages[idx] = new AppliedPositionsFragment();
                            break;
                    }
                }
                FragmentManager frgManager = getFragmentManager();
                frgManager.beginTransaction().replace(R.id.container, pages[idx]).commit();
                mNavigationDrawer.toggle();
            }

            @Override
            public void onFooterClick(View view) {
                PoliJobDB.logOut(new PoliJobDB.LogOutCallback() {
                    @Override
                    public void onLogOutException(ParseException pe) {
                        Intent toHomePage = new Intent(StudentMainActivity.this, MainActivity.class);
                        startActivity(toHomePage);
                        finish();
                    }
                    @Override
                    public void onLogOutSuccess() {
                        Intent toHomePage = new Intent(StudentMainActivity.this, MainActivity.class);
                        startActivity(toHomePage);
                        finish();
                    }
                });
            }

            @Override
            public void onHeaderClick(View view) {
                mImageView = (ImageView) view.findViewById(R.id.circle_view);
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(StudentMainActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo")) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 1);
                        } else if (items[item].equals("Choose from Library")) {
                            Intent intent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(
                                    Intent.createChooser(intent, "Select File"),
                                    2);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        Fragment fragment = new NewsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof ParseObjectObserver) {
            ((ParseObjectObserver<Student>) fragment).updateContent(user);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //int m = (menuID == 0) ? R.menu.menu_temp_profile_page : menuID;
        //getMenuInflater().inflate(m, menu);
        getMenuInflater().inflate(R.menu.menu_temp_profile_page, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mNavigationDrawer.toggle();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setUpNavigationDrawer() {
        String[] titles = getResources().getStringArray(R.array.student_drawer_titles);
        TypedArray ar = getResources().obtainTypedArray(R.array.student_drawer_icons);
        int[] icons = new int[ar.length()];
        for (int i = 0; i < titles.length; i++) {
            icons[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationDrawer = new PoliJobNavigationDrawer(
                titles, icons,
                mDrawerLayout
        );

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                (Toolbar) findViewById(R.id.toolbar),
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View v) {
                if (i == 1) {
                    toolbar.getBackground().setAlpha(currentAlpha);
                } else {
                    toolbar.getBackground().setAlpha(255);
                }
                super.onDrawerClosed(v);
                invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerOpened(View v) {
                if (Build.VERSION.SDK_INT >= 19) {
                    currentAlpha = toolbar.getBackground().getAlpha();
                }
                toolbar.getBackground().setAlpha(255);
                super.onDrawerOpened(v);
                invalidateOptionsMenu();
                syncState();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        if (user.getPhotoFile() != null) {
            try {
                mNavigationDrawer.setUserData(firstName, lastName,
                        Utility.getBitmap(user.getPhotoFile().getData()));
            } catch (Exception e) {
                mNavigationDrawer.setUserData(firstName, lastName, R.drawable.ic_user);
            }
        } else {
            mNavigationDrawer.setUserData(firstName, lastName, R.drawable.ic_user);
        }

    }

    @Override
    public void onScrolled(int dx, int dy) {
        //height of the header image and the toolbar
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int headerHeight = (int) getResources().getDimension(R.dimen.header_image_height);
        int toolbarHeight = (int) getResources().getDimension(R.dimen.toolbar_height);


        //difference between the heights in pixel
        float heightPx = (headerHeight - toolbarHeight);

        //value of the transparency(between 0 and 255) based on the scroll displacement
        int transparency = (int) (dy * (256 / heightPx));

        if (transparency < 256) {
            toolbar.getBackground().setAlpha(transparency);
            currentAlpha = transparency;
            System.out.println("TRA " + transparency + " dy " + dy + " px" + heightPx);

        } else {
            toolbar.getBackground().setAlpha(255);
            currentAlpha = 255;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mImageView.setImageBitmap(thumbnail);

            } else if (requestCode == 2) {
                // Let's read picked image data - its URI
                Uri pickedImage = data.getData();
                // Let's read picked image path using content resolver
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                cursor.close();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap bm = BitmapFactory.decodeFile(imagePath, options);
                // Now we need to set the GUI ImageView data with data read from the picked file.


                mImageView.setImageBitmap(bm);
                PoliJobDB.encodePhoto(requestCode, resultCode, data, pickedImage, this, new PoliJobDB.UploadedCallback() {
                    @Override
                    public void onUploadPhotoException(ParseException pe) {

                    }

                    @Override
                    public void onUploadPhotoInSuccess() {

                    }
                });

                // At the end remember to close the cursor or you will end with the RuntimeException!
            }
        }
    }
}
