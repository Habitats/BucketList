package no.habitats.bucketlist.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import no.habitats.bucketlist.C;
import no.habitats.bucketlist.R;
import no.habitats.bucketlist.fragments.BucketListFragment;
import no.habitats.bucketlist.fragments.LoginFragment;
import no.habitats.bucketlist.fragments.NewBucketListItemFragment;
import no.habitats.bucketlist.fragments.SignUpFragment;
import no.habitats.bucketlist.listeners.BuckerListFragmentListener;
import no.habitats.bucketlist.listeners.LoginFragmentListener;
import no.habitats.bucketlist.listeners.NewBucketListItemListener;
import no.habitats.bucketlist.listeners.SignUpFragmentListener;
import no.habitats.bucketlist.models.BucketListItem;


public class MainActivity extends Activity
    implements LoginFragmentListener, BuckerListFragmentListener, SignUpFragmentListener, NewBucketListItemListener {

  private BucketListFragment bucketListFragment;
  private LoginFragment loginFragment;
  private SignUpFragment signupFragment;
  private NewBucketListItemFragment newBucketItemFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    Crashlytics.start(this);
    setContentView(R.layout.activity_main);

    initializeFragments();
    if (savedInstanceState == null) {
      if (ParseUser.getCurrentUser() == null) {
        getFragmentManager().beginTransaction().add(R.id.container, loginFragment).commit();
      } else {
        getFragmentManager().beginTransaction().add(R.id.container, bucketListFragment).commit();
      }
    }
  }

  private void initializeFragments() {
    loginFragment = LoginFragment.newInstance();
    bucketListFragment = BucketListFragment.newInstance();
    signupFragment = SignUpFragment.newInstance();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_logout) {
      ParseUser.logOut();
      getFragmentManager().beginTransaction().replace(R.id.container, loginFragment).commit();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onLoginSuccess(ParseUser parseUser) {
    getFragmentManager().beginTransaction().replace(R.id.container, bucketListFragment).commit();
  }

  @Override
  public void onLoginFailed() {
  }

  @Override
  public void onStartSignup(String username, String password) {
    Bundle bundle = new Bundle();
    bundle.putString(C.USERNAME, username);
    bundle.putString(C.PASSWORD, password);
    signupFragment.setArguments(bundle);
    getFragmentManager().beginTransaction().replace(R.id.container, signupFragment).commit();
  }

  @Override
  public void onSignupComplete() {
    getFragmentManager().beginTransaction().replace(R.id.container, bucketListFragment).commit();
  }

  @Override
  public void addToBucketList() {
    newBucketItemFragment = NewBucketListItemFragment.newInstance();
    getFragmentManager().beginTransaction().add(R.id.container, newBucketItemFragment).commit();
  }

  @Override
  public void createNewBucketListItem(String title, String description, int color) {
    BucketListItem bucketListItem = new BucketListItem(title, description).setCoverColor(color);
    ParseObject item = bucketListItem.toParseObject();
    setProgressBarIndeterminateVisibility(true);

    item.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        setProgressBarIndeterminateVisibility(false);
        if (e == null) {
          Toast.makeText(MainActivity.this, "Added to bucketlist", Toast.LENGTH_LONG).show();
          getFragmentManager().beginTransaction().remove(newBucketItemFragment).commitAllowingStateLoss();
          bucketListFragment.fetchNew();
        } else {
          Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
      }
    });

  }
}
