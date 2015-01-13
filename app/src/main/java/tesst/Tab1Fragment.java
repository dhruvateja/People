package tesst;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.people.R;

/**
 * Created by Svaad on 13-01-2015.
 */
public class Tab1Fragment extends Fragment {
    TextView txt;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout,container,false);
       txt = (TextView)rootView.findViewById(R.id.dummyTxt);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txt.setText("Hiiii Tab1 TEXT");

    }
}
