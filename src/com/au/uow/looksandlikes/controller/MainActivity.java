package com.au.uow.looksandlikes.controller;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.au.uow.looksandlikes.NavRowAdapter;
import com.au.uow.looksandlikes.R;
import com.au.uow.looksandlikes.controller.fragment.RatingFragment;
import com.au.uow.looksandlikes.controller.fragment.UserProfileFragment;
import com.au.uow.looksandlikes.controller.fragment.ViewRatingsFragment;
import com.parse.ParseUser;

public class MainActivity extends Activity {

	private DrawerLayout drawerLayout;
	private ListView listView;

	private ActionBarDrawerToggle drawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RatingFragment ratingFragment = new RatingFragment();
        fragmentTransaction.replace(R.id.mainContent, ratingFragment);

        fragmentTransaction.commit();

		// linking the views with the models
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		listView = (ListView) findViewById(R.id.drawerList);

		listView.setAdapter(new NavRowAdapter(this));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectItem(position);
			}
		});

		// drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
		// R.drawable.ic_drawer, R.string.drawer_open,
		// R.string.drawer_open);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_open) {
			@Override
			public void onDrawerOpened(View drawerView) {
				// Activates when the drawer is being open
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				// Activates when the drawer is being close
				super.onDrawerClosed(drawerView);
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
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
		if (id == R.id.action_logout) {
			ParseUser.logOut();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		// handles the changes on the screen size and the state
		drawerToggle.onConfigurationChanged(newConfig);
	}

	private void selectItem(int position) {
		Intent intent;
		Fragment fragment = null;
		FragmentManager fragmentManager = null;
		switch (position) {
		case 0:
			/*intent = new Intent(this, UserProfileActivity.class);
			startActivity(intent);*/
            fragment = new UserProfileFragment();
            drawerLayout.closeDrawer(listView);
            fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainContent, fragment).commit();
			break;
			
		case 1:
			fragment = new RatingFragment();
			drawerLayout.closeDrawer(listView);
			fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.mainContent, fragment).commit();
			break;
			
		case 2:
			fragment = new ViewRatingsFragment();
			drawerLayout.closeDrawer(listView);
			fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.mainContent, fragment).commit();
			break;
			
		case 3:
			// intent = new Intent(this, NewLookActivity.class);
			intent = new Intent(this, NewLookActivity.class);
			startActivity(intent);
			break;
		}
	}
}
