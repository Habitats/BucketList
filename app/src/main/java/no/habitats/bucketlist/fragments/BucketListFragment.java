package no.habitats.bucketlist.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import no.habitats.bucketlist.R;
import no.habitats.bucketlist.adapters.BucketListAdapter;
import no.habitats.bucketlist.listeners.BuckerListFragmentListener;


public class BucketListFragment extends Fragment {

  private BuckerListFragmentListener mListener;
  private BucketListAdapter bucketListAdapter;

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
    View rootView = inflater.inflate(R.layout.fragment_bucket_list, container, false);

    bucketListAdapter = new BucketListAdapter(getActivity());
    ListView bucketList = (ListView) rootView.findViewById(R.id.bucket_list);
    bucketList.setAdapter(bucketListAdapter);

    final Button addToBucketListButton = (Button) rootView.findViewById(R.id.b_add_to_bucket_list);
    addToBucketListButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mListener.addToBucketList();
      }
    });

    fetchNew();

    return rootView;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (BuckerListFragmentListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(
          activity.toString() + " must implement " + BuckerListFragmentListener.class.getSimpleName());
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }


  public void fetchNew() {
    bucketListAdapter.fetchNew();
  }
}
