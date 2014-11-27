package ai.smarthome.activity.fragmentAccesso;

import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;

import ai.smarthome.R;
import ai.smarthome.activity.MainActivity;
import ai.smarthome.database.wrapper.Utente;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginFragment extends Fragment {
	
	public static final String UTENTE = "utente";
	public LoginFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        
		LoginButton loginBtn = (LoginButton) rootView.findViewById(R.id.fb_login_button);
        loginBtn.setUserInfoChangedCallback(new UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) { 
                	String fbId = user.getId();
                    String firstName = user.getFirstName();
                    String lastName = user.getLastName();
                    
                    Utente utente= new Utente(fbId, null, null, lastName, firstName);
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    i.putExtra(UTENTE, utente);
                    startActivity(i);
                    getActivity().finish();
                }
                else  { 
                }
            }
        });
		return rootView;
	}

}
