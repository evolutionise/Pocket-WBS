package com.example.pocket_wbs;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.os.Build;

public class MenuActivity extends ActionBarActivity {
	
	public final static String EXTRA_MESSAGE = "com.example.pocket_wbs.MESSAGE";
	String WBSName = "New WBS";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Remove title bar
	    //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_menu, container,
					false);
			return rootView;
		}
	}
	
	//On click method to create new WBS
	
	public void newWBS(){
		
		Intent intent = new Intent(this, GUImain.class);
		intent.putExtra(EXTRA_MESSAGE, WBSName);
		startActivity(intent);
		
	}
	
	
	//Alert dialog to get name for new WBS
	
	public void nameWBS(View view)
	{	

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle(" ");

        // Setting Dialog Message
        alertDialog.setMessage("Enter Name:");
        final EditText input = new EditText(MenuActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
          input.setLayoutParams(lp);
          alertDialog.setView(input);


        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.pocketwbsicon2);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        // Write your code here to execute after dialog
                    	WBSName = input.getText().toString();
                    	if(WBSName.equals("")){
                    		toastMessage("Please Enter a Valid Name");
                    	}
                    	else
                    		newWBS();


                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        // closed
        
        // Showing Alert Message
        alertDialog.show();

	}
	
    public void toastMessage(String message){
    	Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
