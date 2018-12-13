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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

  FragmentTransaction fragmentTransaction;
  TextView txtView3, txtFullName, txtEmail;
  NavigationView navigationView;
  private Voter voterDetails;
  SimpleDateFormat simpleDateFormat;
  String format;

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

    View headerView = navigationView.getHeaderView(0);
    txtEmail = headerView.findViewById(R.id.txtEmail);
    txtFullName = headerView.findViewById(R.id.txtFullName);
    txtEmail.setText(voterDetails.getVote_email());
    txtFullName.setText(voterDetails.getVote_LastName() + ", " + voterDetails.getVote_FirstName());

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

      } else if (intentFragment.equals("nav_groups")) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new GroupsFragment());
        fragmentTransaction.commit();
        txtView3.setVisibility(View.GONE);

      } else if (intentFragment.equals("nav_course")) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new CourseFragment());
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

//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.main, menu);
//    return true;
//  }
//
//  @Override
//  public boolean onOptionsItemSelected(MenuItem item) {
//    // Handle action bar item clicks here. The action bar will
//    // automatically handle clicks on the Home/Up button, so long
//    // as you specify a parent activity in AndroidManifest.xml.
//    int id = item.getItemId();
//
//    //noinspection SimplifiableIfStatement
//    if (id == R.id.action_settings) {
//      return true;
//    }
//
//    return super.onOptionsItemSelected(item);
//  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_camera) {
      Calendar c = Calendar.getInstance();

      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));

      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      df.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));
      SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
      df1.setTimeZone(TimeZone.getTimeZone("Asia/Manila"));

      String formattedDate = df.format(c.getTime());
      String formattedDate1 = df1.format(c.getTime());

      Date startDate = null;
      Date endDate = null;
      try {
        startDate = simpleDateFormat.parse(formattedDate);
        endDate = simpleDateFormat.parse(formattedDate1+" 23:59:59");

        long difference= endDate.getTime()-startDate.getTime();
        long seconds = difference / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        if (minutes <= 1440 && minutes > 0) {
          Log.d("log_tag","Hours: "+hours+" Minutes: "+minutes);
          Log.d("log_tag","Start: "+formattedDate);
          Log.d("log_tag","End: "+formattedDate1+" 23:59:00");
          fragmentTransaction = getSupportFragmentManager().beginTransaction();
          fragmentTransaction.replace(R.id.fragment_container, new ElectionFragment());
          fragmentTransaction.commit();
          txtView3.setVisibility(View.GONE);
        } else {
          Toast.makeText(MainActivity.this, "Can't vote for a while."
                  , Toast.LENGTH_SHORT).show();
          Log.d("log_tag","Hours: "+hours+" Minutes: "+minutes);
          Log.d("log_tag","Start: "+formattedDate);
          Log.d("log_tag","End: "+formattedDate1+" 23:00");
        }
      } catch (ParseException e) {
        e.printStackTrace();
      }

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

    } else if (id == R.id.nav_groups) {
    fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.fragment_container, new GroupsFragment());
    fragmentTransaction.commit();
    txtView3.setVisibility(View.GONE);

    } else if (id == R.id.nav_course) {
      fragmentTransaction = getSupportFragmentManager().beginTransaction();
      fragmentTransaction.replace(R.id.fragment_container, new CourseFragment());
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
