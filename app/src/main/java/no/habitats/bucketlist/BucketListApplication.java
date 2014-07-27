package no.habitats.bucketlist;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Patrick on 27.07.2014.
 */
public class BucketListApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Parse.initialize(this, "ioAXEmBdKceJ9DKmQszmx3NgFU01dFyYfgNZnotn", "ZCY4nYCCf7VoRjt5Q6CMU4y8fA3eepSAJffxa3kP");
    ParseObject testObject = new ParseObject("BucketList");
    testObject.put("description","sex i vannet");
    testObject.saveInBackground();
  }
}
