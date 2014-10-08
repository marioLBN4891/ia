package ai.smarthome.fragment;

import ai.smarthome.R;
import ai.smarthome.R.array;
import ai.smarthome.R.id;
import ai.smarthome.R.layout;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MeteoFragment extends Fragment {
	
    public static final String ARG_OBJECT_NUMBER = "posizione";

    public MeteoFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
    	int i = getArguments().getInt(ARG_OBJECT_NUMBER);
    	View rootView = inflater.inflate(R.layout.fragment_meteo, container, false);
        String object = getResources().getStringArray(R.array.opzioni_array)[i];

        TextView textview = (TextView) rootView.findViewById(R.id.textFragment);
        // int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()), "drawable", getActivity().getPackageName());
        // ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
        getActivity().setTitle(object);
        textview.setText(object);
        return rootView;
    }
}
