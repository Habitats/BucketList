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

import no.habitats.bucketlist.BucketListApplication;
import no.habitats.bucketlist.C;
import no.habitats.bucketlist.R;
import no.habitats.bucketlist.fragments.BucketItemFragment;
import no.habitats.bucketlist.fragments.BucketListFragment;
import no.habitats.bucketlist.fragments.LoginFragment;
import no.habitats.bucketlist.fragments.NewBucketListItemFragment;
import no.habitats.bucketlist.fragments.SignUpFragment;
import no.habitats.bucketlist.listeners.BucketListFragmentListener;
import no.habitats.bucketlist.listeners.LoginFragmentListener;
import no.habitats.bucketlist.listeners.NewBucketListItemListener;
import no.habitats.bucketlist.listeners.SignUpFragmentListener;
import no.habitats.bucketlist.models.BucketListItem;


public class MainActivity extends Activity
    implements LoginFragmentListener, BucketListFragmentListener, SignUpFragmentListener, NewBucketListItemListener {

  private BucketListFragment bucketListFragment;
  private LoginFragment loginFragment;
  private SignUpFragment signupFragment;

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
    } else if (id == R.id.action_add) {
      enterCreateItem();
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
    getFragmentManager().beginTransaction().replace(R.id.container, signupFragment).addToBackStack(null).commit();
  }

  @Override
  public void onSignupComplete() {
    getFragmentManager().beginTransaction().replace(R.id.container, bucketListFragment).commit();
  }

  @Override
  public void enterCreateItem() {
    getFragmentManager().beginTransaction()
        .add(R.id.container, NewBucketListItemFragment.newInstance(), NewBucketListItemFragment.TAG)
        .addToBackStack(null).commit();
  }

  @Override
  public void enterBucketItem(BucketListItem item) {
    getFragmentManager().beginTransaction().add(R.id.container, BucketItemFragment.newInstance(item))
        .addToBackStack(null).commit();
  }


  @Override
  public void update(final BucketListItem bucketItem) {
    bucketListFragment.update(bucketItem);
  }

  @Override
  protected void onStart() {
    super.onStart();
    BucketListApplication.getApplication().setActiveActivity(this);
  }

  @Override
  public boolean onNavigateUp() {
    onBackPressed();
    return super.onNavigateUp();
  }

  @Override
  public void onBackPressed() {
    if (getFragmentManager().getBackStackEntryCount() > 0) {
      getFragmentManager().popBackStack();
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public void createBucketItem(String title, String description, int color) {
    final BucketListItem bucketListItem = new BucketListItem(title, description).setCoverColor(color);
    ParseObject item = bucketListItem.toParseObject();
    setProgressBarIndeterminateVisibility(true);

    item.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        setProgressBarIndeterminateVisibility(false);
        if (e == null) {
          Toast.makeText(MainActivity.this, "Added to bucketlist", Toast.LENGTH_LONG).show();
          bucketListFragment.fetchNew();
        } else {
          Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
      }
    });

  }
}
