package no.habitats.bucketlist.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import no.habitats.bucketlist.models.BucketListItem;

/**
 * Created by Patrick on 27.07.2014.
 */
public class BucketListAdapter extends ArrayAdapter<BucketListItem>{

  public BucketListAdapter(Context context, int resource) {
    super(context, resource);
  }
}
