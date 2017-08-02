package edu.monash.fit4039.gym_fitness_workout_tracker.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    private TextView circleImageTitle;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        circleImageTitle = (TextView) v.findViewById(R.id.license_circleImageView_title);
        circleImageTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(R.string.circle_image_view_url);
            }
        });

        return v;
    }

    // Open a URL in a browser
    private void openUrl(int id) {
        final String url = getResources().getString(id);
        final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

}
