package no.habitats.bucketlist.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import no.habitats.bucketlist.R;
import no.habitats.bucketlist.listeners.LoginFragmentListener;

public class LoginFragment extends Fragment {

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
    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        login(username, password);
      }
    });
    return rootView;
  }

  private void login(String username, String password) {

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

}
