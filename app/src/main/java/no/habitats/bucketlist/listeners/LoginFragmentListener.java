package no.habitats.bucketlist.listeners;

import com.parse.ParseUser;

/**
 * Created by Patrick on 27.07.2014.
 */
public interface LoginFragmentListener {

  public void onLoginSuccess(ParseUser parseUser);

  public void onLoginFailed();

  public void onStartSignup(String username, String password);
}
