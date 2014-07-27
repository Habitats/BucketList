package no.habitats.bucketlist.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;

import no.habitats.bucketlist.R;
import no.habitats.bucketlist.fragments.BucketListFragment;
import no.habitats.bucketlist.fragments.LoginFragment;
import no.habitats.bucketlist.listeners.BuckerListFragmentListener;
import no.habitats.bucketlist.listeners.LoginFragmentListener;


public class MainActivity extends Activity implements LoginFragmentListener, BuckerListFragmentListener {

  private BucketListFragment bucketListFragment;
  private LoginFragment loginFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Crashlytics.start(this);
    setContentView(R.layout.activity_main);

    initializeFragments();
    if (savedInstanceState == null) {
      getFragmentManager().beginTransaction().add(R.id.container, loginFragment).commit();
    }
  }


  private void initializeFragments() {
    loginFragment = LoginFragment.newInstance();
    bucketListFragment = BucketListFragment.newInstance();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onLoginSuccess() {
    getFragmentManager().beginTransaction().replace(R.id.container, bucketListFragment).commit();
  }

  @Override
  public void onLoginFailed() {

  }
}
