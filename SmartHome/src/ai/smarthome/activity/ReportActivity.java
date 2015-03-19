package ai.smarthome.activity;

import java.util.ArrayList;

import ai.smarthome.R;
import ai.smarthome.database.DatabaseHelper;
import ai.smarthome.database.wrapper.Report;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ReportActivity extends Activity {

	private SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new DatabaseHelper(this).getWritableDatabase();
		
		setContentView(R.layout.activity_report);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ArrayList<Report> lista = Report.getLista(db);
		
		TableLayout tableLayout = (TableLayout) findViewById(R.id.tableReport);
	    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
	    lp.setMargins(10, 2, 10, 3);
	    
	    int indice = 1;
	    for (Report r: lista) {
	    	TextView textView1 = new TextView(this);
		    textView1.setText(String.valueOf(indice++));
		    textView1.setTextColor(Color.BLUE);
		    textView1.setTextSize(16);
		    textView1.setLayoutParams(lp);
		    
		    TextView textView2 = new TextView(this);
		    textView2.setText(r.getAzione());
		    textView2.setTextColor(Color.BLACK);
		    textView2.setTextSize(16);
		    textView2.setLayoutParams(lp);
		        
		    TableRow riga = new TableRow(this);
		    riga.setLayoutParams(lp);
		    riga.addView(textView1);
		    riga.addView(textView2);
		    tableLayout.addView(riga, 0);
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_action_report, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.mail:
			sendMailReport();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}
	
	@Override
    public void onBackPressed() {
		finish();
    }

	private void sendMailReport() {
		new AlertDialog.Builder(this).setTitle("Report")
        .setMessage("Salvare Report?")
        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	
            }
        }).setNegativeButton("Annulla", null).show();
	}
}
