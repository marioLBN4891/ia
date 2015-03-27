package ai.smarthome.activity.fragmentAccesso;

import ai.smarthome.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class RegistrazioneFragment extends Fragment {
	
	public RegistrazioneFragment() {
		
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		String indirizzoRegistrazione = "http://193.204.187.73:81/mondo/new/;jsessionid=0084B51015FF0CCF936BDEC609EC9D40";
		
		View rootView = inflater.inflate(R.layout.fragment_registrazione, container, false);
        
		WebView myWebView = (WebView) rootView.findViewById(R.id.webview);
        myWebView.loadUrl(indirizzoRegistrazione);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        
		return rootView;
	}

}
