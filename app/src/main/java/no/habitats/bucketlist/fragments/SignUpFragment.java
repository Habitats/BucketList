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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import no.habitats.bucketlist.C;
import no.habitats.bucketlist.R;
import no.habitats.bucketlist.listeners.SignUpFragmentListener;

public class SignUpFragment extends Fragment {

  private static final String TAG = SignUpFragment.class.getSimpleName();
  private SignUpFragmentListener mListener;
  private String username;
  private String password;

  public static SignUpFragment newInstance() {
    SignUpFragment fragment = new SignUpFragment();

    return fragment;
  }

  public SignUpFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      Bundle bundle = this.getArguments();
      username = bundle.getString(C.USERNAME);
      password = bundle.getString(C.PASSWORD);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

    final EditText usernameField = (EditText) rootView.findViewById(R.id.et_username);
    final EditText passwordField = (EditText) rootView.findViewById(R.id.et_password);
    final EditText emailField = (EditText) rootView.findViewById(R.id.et_email);

    usernameField.setText(username);
    passwordField.setText(password);

    Button SignUpButton = (Button) rootView.findViewById(R.id.b_signup);
    SignUpButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String email = emailField.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
          builder.setMessage("Fill in all the fields!")//
              .setTitle("Error") //
              .setPositiveButton(android.R.string.ok, null)//
              .create()//
              .show();// ;
        } else {
          signUp(username, password, email);
        }
      }
    });
    return rootView;
  }

  private void signUp(String username, String password, String email) {
    ParseUser user = new ParseUser();
    user.setUsername(username);
    user.setPassword(password);
    user.setEmail(email);

    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if(e == null) {
          mListener.onSignupComplete();
        }else{
          Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
          Log.e(TAG, e.getMessage());
        }
      }
    });

  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (SignUpFragmentListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

}
