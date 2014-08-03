package no.habitats.bucketlist.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import no.habitats.bucketlist.BucketListApplication;
import no.habitats.bucketlist.C;
import no.habitats.bucketlist.R;
import no.habitats.bucketlist.models.BucketListItem;

/**
 * Created by Patrick on 27.07.2014.
 */
public class BucketListAdapter extends BaseAdapter {

  private static final String TAG = BucketListAdapter.class.getSimpleName();
  private final LayoutInflater mInflater;
  private View coverPhoto;
  private BucketListItem bucket;
  private List<BucketListItem> items;

  public BucketListAdapter(Activity activity) {
    mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    items = new CopyOnWriteArrayList<BucketListItem>();
  }

  @Override
  public int getCount() {
    return items.size();
  }

  @Override
  public BucketListItem getItem(int position) {
    return items.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
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
    coverPhoto = view.findViewById(R.id.iv_bucket_background);
    TextView description = (TextView) view.findViewById(R.id.tv_bucket_description);
    TextView modified = (TextView) view.findViewById(R.id.tv_bucket_modified);
    TextView created = (TextView) view.findViewById(R.id.tv_bucket_created);

    bucket = getItem(position);
    author.setText(bucket.getOwner());
    title.setText(bucket.getTitle());
    modified.setText(bucket.getPrettyModified());
    created.setText(bucket.getPrettyCreated());
    description.setText(bucket.getDescription());

    if (description.getText().toString().trim().length() <= 0) {
      description.setVisibility(View.GONE);
    }

    setCoverColor();

    return view;
  }

  private void setCoverColor() {
    if (bucket.isCompleted()) {
      coverPhoto.setBackgroundColor(Color.LTGRAY);
    } else {
      coverPhoto.setBackgroundColor(bucket.getCoverColor());
    }
  }

  public void update(final BucketListItem updatedItem) {
    // update locally
    localUpdate(updatedItem);

    // then update the server
    serverUpdate(updatedItem);

  }

  private void serverUpdate(final BucketListItem updatedItem) {
    ParseObject parseObject = updatedItem.toParseObject();
    BucketListApplication.getApplication().setLoading(true);

    // save to server
    parseObject.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        BucketListApplication.getApplication().setLoading(false);
        if (e == null) {
          // fetch saved instance from the server
          fetchNew(updatedItem);
        } else {
          BucketListApplication.getApplication().displayToast(e.getMessage());
        }
      }
    });
  }

  private void localUpdate(BucketListItem updatedItem) {
    get(updatedItem).update(updatedItem);

    update();
  }

  public void update() {
    Collections.sort(items);
    notifyDataSetChanged();
  }

  public void fetchNew() {
    ParseQuery<ParseObject> query = ParseQuery.getQuery(C.TABLE_BUCKET_LIST);
    query.whereEqualTo(C.OWNER, ParseUser.getCurrentUser());
    BucketListApplication.getApplication().setLoading(true);
    query.findInBackground(new FindCallback<ParseObject>() {
      @Override
      public void done(List<ParseObject> parseObjects, ParseException e) {
        BucketListApplication.getApplication().setLoading(false);
        if (e == null) {
          items = BucketListItem.fromParseObjects(parseObjects);

          update();
        }
      }
    });
  }

  public void fetchNew(final BucketListItem item) {
    ParseQuery<ParseObject> query = ParseQuery.getQuery(C.TABLE_BUCKET_LIST);
    query.whereEqualTo(C.OWNER, ParseUser.getCurrentUser());
    BucketListApplication.getApplication().setLoading(true);
    query.getInBackground(item.getId(), new GetCallback<ParseObject>() {

      @Override
      public void done(ParseObject parseObject, ParseException e) {
        BucketListApplication.getApplication().setLoading(false);
        if (e == null) {
          BucketListItem updatedItem = BucketListItem.fromParseObject(parseObject);
          BucketListItem old = get(updatedItem);
          old.update(updatedItem);

          update();
        }
      }
    });
  }

  private BucketListItem get(BucketListItem other) {
    for (BucketListItem item : items) {
      if (item.getId().equalsIgnoreCase(other.getId())) {
        return item;
      }
    }
    return null;
  }

  public void add(BucketListItem item) {
    items.add(item);
    update();
  }

  public void delete(final BucketListItem bucketItem) {
    items.remove(bucketItem);
    update();

    ParseObject object = bucketItem.toParseObject();
    object.deleteInBackground(new DeleteCallback() {
      @Override
      public void done(ParseException e) {
        fetchNew();
        BucketListApplication.getApplication()
            .displayToast("\"" + bucketItem.getTitle() + "\" was deleted from the server.");
      }
    });
  }
}
