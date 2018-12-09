package psuva.com.ph.psuvotingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

  FragmentTransaction fragmentTransaction;
  TextView txtView3;
  NavigationView navigationView;
  private Voter voterDetails;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    txtView3 = findViewById(R.id.textView3);

    navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);

    voterDetails = (Voter) getIntent().getSerializableExtra("voterDetails");

    if (voterDetails.getVote_IdNumber().equals("1") && voterDetails.getVote_LastName().equals("Admin")) {
      hideItemForAdmin();
    } else {
      hideItemForVoter();
    }


    loadFragmentFromActivity();
  }

  private void hideItemForAdmin() {
    navigationView = (NavigationView) findViewById(R.id.nav_view);
    Menu nav_Menu = navigationView.getMenu();
    nav_Menu.findItem(R.id.nav_camera).setVisible(false);
  }

  private void hideItemForVoter() {
    navigationView = (NavigationView) findViewById(R.id.nav_view);
    Menu nav_Menu = navigationView.getMenu();
    nav_Menu.findItem(R.id.nav_gallery).setVisible(false);
    nav_Menu.findItem(R.id.nav_slideshow).setVisible(false);
    nav_Menu.findItem(R.id.nav_slideshow).setVisible(false);
    nav_Menu.findItem(R.id.nav_result).setVisible(false);
  }

  private void loadFragmentFromActivity() {
    String intentFragment;
    if(getIntent().hasExtra("frgToLoad")) {
      intentFragment = getIntent().getExtras().getString("frgToLoad");
      if(intentFragment.equals("nav_gallery")) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new PartyListFragment());
        fragmentTransaction.commit();
        txtView3.setVisibility(View.GONE);

      } else if(intentFragment.equals("nav_camera")) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new ElectionFragment());
        fragmentTransaction.commit();
        txtView3.setVisibility(View.GONE);

      } else if(intentFragment.equals("nav_slideshow")) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new VoterFragment());
        fragmentTransaction.commit();
        txtView3.setVisibility(View.GONE);

      } else if (intentFragment.equals("nav_result")) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new ResultFragment());
        fragmentTransaction.commit();
        txtView3.setVisibility(View.GONE);

      } else if (intentFragment.equals("nav_manage")) {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
      }
    }
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_camera) {
      fragmentTransaction = getSupportFragmentManager().beginTransaction();
      fragmentTransaction.replace(R.id.fragment_container, new ElectionFragment());
      fragmentTransaction.commit();
      txtView3.setVisibility(View.GONE);

    } else if (id == R.id.nav_gallery) {
      fragmentTransaction = getSupportFragmentManager().beginTransaction();
      fragmentTransaction.replace(R.id.fragment_container, new PartyListFragment());
      fragmentTransaction.commit();
      txtView3.setVisibility(View.GONE);

    } else if (id == R.id.nav_slideshow) {
      fragmentTransaction = getSupportFragmentManager().beginTransaction();
      fragmentTransaction.replace(R.id.fragment_container, new VoterFragment());
      fragmentTransaction.commit();
      txtView3.setVisibility(View.GONE);

    } else if (id == R.id.nav_result) {
      fragmentTransaction = getSupportFragmentManager().beginTransaction();
      fragmentTransaction.replace(R.id.fragment_container, new ResultFragment());
      fragmentTransaction.commit();
      txtView3.setVisibility(View.GONE);

    } else if (id == R.id.nav_manage) {
      Intent i = new Intent(MainActivity.this, LoginActivity.class);
      finish();
      startActivity(i);

    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }
}
