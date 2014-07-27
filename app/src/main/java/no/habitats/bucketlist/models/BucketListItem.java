package no.habitats.bucketlist.models;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 27.07.2014.
 */
public class BucketListItem implements Comparable<BucketListItem> {

  private static final String DESCRIPTION = "description";
  private static final String TITLE = "title";
  private static final String NAME = "BucketList";

  private String id;
  private DateTime created;
  private DateTime modified;
  private boolean completed;
  private ParseUser owner;
  private String title;
  private String description;

  public BucketListItem(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public BucketListItem(String id, String title, String description, DateTime created) {
    this(title, description);
    this.id = id;
    this.created = created;
  }

  public ParseUser getOwner() {
    return owner;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public static BucketListItem random() {
    String title = Integer.toString((int) (Math.random() * 100)) + " title";
    String description = "epic item";
    return new BucketListItem(title, description);
  }

  private static ParseUser randomUser() {
    ParseUser user = new ParseUser();
    user.setUsername("bob");
    return user;
  }

  private BucketListItem setModified(DateTime dateTime) {
    this.modified = dateTime;
    return this;
  }

  private BucketListItem setCreated(DateTime dateTime) {
    this.created = dateTime;
    return this;
  }

  public ParseObject toParseObject() {
    ParseObject parse = new ParseObject(NAME);
    parse.put(DESCRIPTION, description);
    parse.put(TITLE, title);
    parse.setObjectId(id);

    return parse;
  }

  public BucketListItem setDescription(String description) {
    this.description = description;
    return this;
  }

  public static List<BucketListItem> fromParseObjects(List<ParseObject> parseObjects) {
    List<BucketListItem> bucketListItems = new ArrayList<BucketListItem>();
    for (ParseObject parseObject : parseObjects) {
      bucketListItems.add(BucketListItem.fromParseObject(parseObject));
    }
    return bucketListItems;
  }

  public static BucketListItem fromParseObject(ParseObject parseObject) {
    String title = parseObject.getString(TITLE);
    String description = parseObject.getString(DESCRIPTION);
    String id = parseObject.getObjectId();
    DateTime created = new DateTime(parseObject.getCreatedAt());
    return new BucketListItem(id, title, description, created);
  }

  @Override
  public int compareTo(BucketListItem another) {
    return (int) ((another.getCreated().getMillis() / 1000) - (getCreated().getMillis() / 1000));
  }

  private DateTime getCreated() {
    return created;
  }
}
