package no.habitats.bucketlist.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import no.habitats.bucketlist.R;
import no.habitats.bucketlist.adapters.BucketListAdapter;
import no.habitats.bucketlist.listeners.BucketListFragmentListener;
import no.habitats.bucketlist.models.BucketListItem;


public class BucketListFragment extends Fragment
    implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

public static final String TAG = BucketListFragment.class.getSimpleName();
  private BucketListFragmentListener mListener;
  private BucketListAdapter bucketListAdapter;
  private ListView bucketList;
  private List<BucketListItem> bucketListItems;

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
    bucketList = (ListView) rootView.findViewById(R.id.bucket_list);
    bucketList.setAdapter(bucketListAdapter);

    bucketList.setOnItemClickListener(this);
    bucketList.setOnItemLongClickListener(this);

    fetchNew();
    return rootView;
  }

  private void completeBucketItem(final BucketListItem item) {
    item.toggleComplete();
    mListener.update(item);
  }

  public void fetchNew(BucketListItem item) {
    bucketListAdapter.fetchNew(item);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (BucketListFragmentListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(
          activity.toString() + " must implement " + BucketListFragmentListener.class.getSimpleName());
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

  private void enterBucket(BucketListItem bucketItem) {
    mListener.enterBucketItem(bucketItem);
  }

  // ITEM CLICK EVENTS
  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    enterBucket(bucketListAdapter.getItem(position));
  }


  @Override
  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
    completeBucketItem(bucketListAdapter.getItem(position));
    return true;
  }

  public void update(final BucketListItem bucketItem) {
    bucketListAdapter.update(bucketItem);
  }
}
