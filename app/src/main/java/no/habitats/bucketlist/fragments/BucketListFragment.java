package no.habitats.bucketlist.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.habitats.bucketlist.R;
import no.habitats.bucketlist.listeners.BuckerListFragmentListener;


public class BucketListFragment extends Fragment {

  private BuckerListFragmentListener mListener;

  public static BucketListFragment newInstance() {
    BucketListFragment fragment = new BucketListFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public BucketListFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      // set arguments
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_bucket_list, container, false);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (BuckerListFragmentListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString() + " must implement " + BuckerListFragmentListener.class.getSimpleName());
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }


}
