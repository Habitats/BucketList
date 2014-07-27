package no.habitats.bucketlist.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Collections;
import java.util.List;

import no.habitats.bucketlist.C;
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

    if (convertView == null) {
      view = mInflater.inflate(resource, parent, false);
    } else {
      view = convertView;
    }

    TextView author = (TextView) view.findViewById(R.id.tv_bucket_author);
    TextView title = (TextView) view.findViewById(R.id.tv_bucket_title);
    View coverPhoto = view.findViewById(R.id.iv_bucket_background);
    TextView description = (TextView) view.findViewById(R.id.tv_bucket_description);
    TextView modified = (TextView) view.findViewById(R.id.tv_bucket_modified);
    TextView created = (TextView) view.findViewById(R.id.tv_bucket_created);

    BucketListItem bucket = getItem(position);
    author.setText(bucket.getOwner());
    title.setText(bucket.getTitle());
    modified.setText(bucket.getPrettyModified());
    created.setText(bucket.getPrettyCreated());
    description.setText(bucket.getDescription());
    coverPhoto.setBackgroundColor(bucket.getCoverColor());

    return view;
  }

  public void fetchNew() {
    ParseQuery<ParseObject> query = ParseQuery.getQuery("BucketList");
    query.whereEqualTo(C.OWNER, ParseUser.getCurrentUser());
    ((Activity)getContext()).setProgressBarIndeterminateVisibility(true);
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> parseObjects, ParseException e) {
        ((Activity)getContext()).setProgressBarIndeterminateVisibility(false);
        if (e == null) {
          List<BucketListItem> list = BucketListItem.fromParseObjects(parseObjects);
          Collections.sort(list);
          clear();
          addAll(list);
          notifyDataSetChanged();
        }
      }
    });
  }
}
