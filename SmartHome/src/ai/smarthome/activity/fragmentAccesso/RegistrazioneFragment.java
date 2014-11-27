package ai.smarthome.activity.fragmentAccesso;

import ai.smarthome.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RegistrazioneFragment extends Fragment {
	
	public RegistrazioneFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_registrazione, container, false);
        
		return rootView;
	}

}
