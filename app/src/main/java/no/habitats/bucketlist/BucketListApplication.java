package no.habitats.bucketlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

import no.habitats.bucketlist.activities.MainActivity;

/**
 * Created by Patrick on 27.07.2014.
 */
public class BucketListApplication extends Application {

  private static BucketListApplication instance;
  private boolean loading;
  private Activity activity;
  private EditText input;
  private String sortBy = C.SORT_MODIFIED;

  public static BucketListApplication getApplication() {
    if (instance != null) {
      return instance;
    } else {
      throw new IllegalArgumentException("instance is null");
    }
  }

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    Parse.initialize(this, "ioAXEmBdKceJ9DKmQszmx3NgFU01dFyYfgNZnotn", "ZCY4nYCCf7VoRjt5Q6CMU4y8fA3eepSAJffxa3kP");

  }

  public void setActiveActivity(Activity activity) {
    this.activity = activity;
    setLoading(getLoading());
  }


  public boolean getLoading() {
    return loading;
  }

  public void setLoading(final boolean loading) {
    this.loading = loading;
    if (activity != null) {
      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          activity.setProgressBarIndeterminateVisibility(loading);
        }
      });
    }
  }

  // DISPLAY

  public void displayInputDialog(final String title, final String desc,
                                 final DialogInterface.OnClickListener listener) {
    if (activity == null) {
      return;
    }
    // empty the input field from the last dialog
    input.setText("");
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        input = new EditText(activity);
        builder.setView(input);
        builder.setTitle(title) //
            .setMessage(desc)//
            .setPositiveButton(android.R.string.yes, listener) //
            .create() //
            .show();
      }
    });
  }

  public void displayToast(final String toast) {
    if (activity != null) {
      activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          Toast.makeText(activity, toast, Toast.LENGTH_LONG).show();
        }
      });
    }
  }

  public void displayErrorDialog(final String title, final String message) {
    if (activity == null) {
      return;
    }
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)//
            .setTitle(title) //
            .setPositiveButton(android.R.string.ok, null)//
            .create()//
            .show();//
      }
    });
  }

  public void displayPromptDialog(final String title, final String question,
                                  final DialogInterface.OnClickListener dialogClickListener) {
    if (activity == null) {
      return;
    }
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title) //
            .setMessage(question)//
            .setPositiveButton(android.R.string.yes, dialogClickListener)//
            .setNegativeButton(android.R.string.no, dialogClickListener)//
            .create() //
            .show();
      }
    });
  }

  public String getSortBy() {
    return sortBy;
  }

  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  }
}
