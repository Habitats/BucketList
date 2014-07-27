package no.habitats.bucketlist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import no.habitats.bucketlist.R;
import no.habitats.bucketlist.listeners.LoginFragmentListener;

public class LoginFragment extends Fragment {

  private static final String TAG = LoginFragment.class.getSimpleName();
  private LoginFragmentListener mListener;

  public static LoginFragment newInstance() {
    LoginFragment fragment = new LoginFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public LoginFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      // get arguments
    }

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_login, container, false);

    final EditText usernameField = (EditText) rootView.findViewById(R.id.et_username);
    final EditText passwordField = (EditText) rootView.findViewById(R.id.et_password);

    Button loginButton = (Button) rootView.findViewById(R.id.b_login);
    Button signupButton = (Button) rootView.findViewById(R.id.b_signup);

    LoginOnClickListener loginOnClickListener = new LoginOnClickListener(usernameField, passwordField);
    loginButton.setOnClickListener(loginOnClickListener);
    signupButton.setOnClickListener(loginOnClickListener);

    return rootView;
  }

  private void login(String username, String password) {

    getActivity().setProgressBarIndeterminateVisibility(true);
    ParseUser.logInInBackground(username, password, new LogInCallback() {
      @Override
      public void done(ParseUser parseUser, ParseException e) {
        getActivity().setProgressBarIndeterminateVisibility(false);
        if (e == null) {
          mListener.onLoginSuccess(parseUser);
        } else {
          Log.e(TAG, e.getMessage());
          Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
      }
    });
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (LoginFragmentListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  private class LoginOnClickListener implements View.OnClickListener {

    private final EditText usernameField;
    private final EditText passwordField;

    public LoginOnClickListener(EditText usernameField, EditText passwordField) {
      this.usernameField = usernameField;
      this.passwordField = passwordField;
    }

    @Override
    public void onClick(View v) {
      String username = usernameField.getText().toString().trim();
      String password = passwordField.getText().toString().trim();
      if (v.getId() == R.id.b_login) {
        if (username.isEmpty() || password.isEmpty()) {
          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
          builder.setMessage("Fill in all the fields")//
              .setTitle("Field Error")//
              .setPositiveButton(android.R.string.ok, null)//
              .create()//
              .show();// ;
        } else {
          login(username, password);
        }
      } else if (v.getId() == R.id.b_signup) {
        signup(username, password);
      }
    }
  }

  private void signup(String username, String password) {
    mListener.onStartSignup(username, password);
  }
}
