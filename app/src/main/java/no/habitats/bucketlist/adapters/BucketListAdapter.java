package no.habitats.bucketlist.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Collections;
import java.util.List;

import no.habitats.bucketlist.R;
import no.habitats.bucketlist.models.BucketListItem;

/**
 * Created by Patrick on 27.07.2014.
 */
public class BucketListAdapter extends ArrayAdapter<BucketListItem> {

  private static final String TAG = BucketListAdapter.class.getSimpleName();
  private final LayoutInflater mInflater;

  public BucketListAdapter(Activity activity) {
    super(activity, 0);
    mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    return createViewFromResource(position, convertView, parent, R.layout.bucket_item);
  }

  private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
    View view;
    TextView author;
    TextView title;
    TextView description;
    ImageView coverPhoto;

    if (convertView == null) {
      view = mInflater.inflate(resource, parent, false);
    } else {
      view = convertView;
    }

    try {
      author = (TextView) view.findViewById(R.id.tv_bucket_author);
      title = (TextView) view.findViewById(R.id.tv_bucket_title);
      coverPhoto = (ImageView) view.findViewById(R.id.iv_bucket_background);
      description = (TextView) view.findViewById(R.id.tv_bucket_description);
    } catch (ClassCastException e) {
      Log.e(TAG, "You must supply a resource ID for a TextView");
      throw new IllegalStateException("ArrayAdapter requires the resource ID to be a TextView", e);
    }
    BucketListItem bucket = getItem(position);
//    author.setText("Owner: " + bucket.getOwner().getUsername());
    title.setText(bucket.getTitle());

    description.setText("Description: " + bucket.getDescription());

    return view;
  }

  public void fetchNew() {
    clear();
    ParseQuery<ParseObject> query = new ParseQuery("BucketList");
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> parseObjects, ParseException e) {
        if (e == null) {
          List<BucketListItem> list = BucketListItem.fromParseObjects(parseObjects);
          Collections.sort(list);
          addAll(list);
        }
      }
    });
    notifyDataSetChanged();
  }
}
