package edu.monash.fit4039.gym_fitness_workout_tracker.activity;


import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.fragment.AboutFragment;
import edu.monash.fit4039.gym_fitness_workout_tracker.fragment.ExerciseFragment;
import edu.monash.fit4039.gym_fitness_workout_tracker.fragment.HomeFragment;
import edu.monash.fit4039.gym_fitness_workout_tracker.fragment.ProfileFragment;
import edu.monash.fit4039.gym_fitness_workout_tracker.fragment.RoutineFragment;
import edu.monash.fit4039.gym_fitness_workout_tracker.fragment.SettingFragment;
import edu.monash.fit4039.gym_fitness_workout_tracker.fragment.TimerFragment;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Profile;


public class MainActivity extends BaseActivity {

    // Unique Identifier for receiving activity result
    private static final int PICK_IMAGE = 100;
    // Instance variable
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private NavigationView navigationView;
    private DatabaseReference profileReference;
    private ValueEventListener profileListener;
    private ImageView pickImage;
    private StorageReference filePath;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String userKey = getUid();

        // Get reference to users node
        profileReference = FirebaseDatabase.getInstance().getReference().child("users").child(userKey);
        filePath = FirebaseStorage.getInstance().getReference().child("photos").child(getUid());

        // Get reference from activity
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
        setTitle("GFWT");


        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Switch fragment by navigation drawer
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.nav_home) {
                    displayView(new HomeFragment(), menuItem);
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_account) {
                    displayView(new ProfileFragment(), menuItem);
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_exercise) {
                    displayView(new ExerciseFragment(), menuItem);
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_setting) {
                    displayView(new SettingFragment(), menuItem);
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_timer) {
                    displayView(new TimerFragment(), menuItem);
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_routine) {
                    displayView(new RoutineFragment(), menuItem);
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_map) {
                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                    drawerLayout.closeDrawers();
                    return true;
                } else if (menuItem.getItemId() == R.id.nav_about) {
                    displayView(new AboutFragment(), menuItem);
                    return true;
                } else {
                    menuItem.setChecked(true);
                    drawerLayout.closeDrawers();
                    return false;
                }
            }
        });

    }

    // Initialize navigation header
    // https://stackoverflow.com/questions/33999407/how-to-set-text-to-view-from-drawer-header-layout-in-navigation-drawer-without-i
    private void initializeNavHeader(Profile profile) {
        // Get reference from the navigation view header
        View header = navigationView.getHeaderView(0);
        TextView weight = (TextView) header.findViewById(R.id.nav_weightText);
        TextView BMI = (TextView) header.findViewById(R.id.nav_BMIText);
        TextView BMR = (TextView) header.findViewById(R.id.nav_BMRText);
        TextView email = (TextView) header.findViewById(R.id.nav_emailText);
        pickImage = (ImageView) header.findViewById(R.id.profile_pick);
        // Set the text
        weight.setText(profile.formatWeight());
        BMI.setText(profile.formatBMI());
        BMR.setText(profile.formatBMR());
        email.setText(profile.getName());

        // Download the image and set up imageView
        // https://github.com/firebase/FirebaseUI-Android/tree/master/storage
        // https://stackoverflow.com/questions/41758087/glide-not-updating-image-android-of-same-url
        if (imageUri == null) {
            Glide.with(MainActivity.this).using(new FirebaseImageLoader())
                    .load(filePath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.profile)
                    .into(pickImage);
        }


        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    // Open gallery and pick image
    // https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            pickImage.setImageURI(imageUri);
            // Upload image to the Firebase storage
            // https://firebase.google.com/docs/storage/android/upload-files
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    feedback("Upload photo done.");
                }
            });

        }
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    // Transfer the fragment
    private void displayView(Fragment fragment, MenuItem menuItem) {
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Read the data from Firebase reference
        profileListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                initializeNavHeader(profile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        profileReference.addValueEventListener(profileListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (profileListener != null) {
            profileReference.removeEventListener(profileListener);
        }
    }

}