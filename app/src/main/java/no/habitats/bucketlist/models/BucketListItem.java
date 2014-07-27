package no.habitats.bucketlist.models;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import no.habitats.bucketlist.C;

/**
 * Created by Patrick on 27.07.2014.
 */
public class BucketListItem implements Comparable<BucketListItem> {

  private String id;
  private DateTime created;
  private DateTime modified;
  private boolean completed;
  private ParseUser owner;
  private String title;
  private String description;
  private int coverColor;

  public BucketListItem(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public BucketListItem(String id, String title, String description, DateTime created) {
    this(title, description);
    this.id = id;
    this.created = created;
  }

  public String getOwner() {
    return "Author: " + owner.getObjectId();
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

  public BucketListItem setOwner(ParseUser owner) {
    this.owner = owner;
    return this;
  }

  private BucketListItem setModified(DateTime dateTime) {
    this.modified = dateTime;
    return this;
  }

  public ParseObject toParseObject() {
    ParseObject parse = new ParseObject(C.NAME);
    parse.put(C.DESCRIPTION, description);
    parse.put(C.TITLE, title);
    parse.setObjectId(id);
    parse.put(C.OWNER, ParseUser.getCurrentUser());
    parse.put(C.COVER_COLOR, coverColor);

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
    String title = parseObject.getString(C.TITLE);
    String description = parseObject.getString(C.DESCRIPTION);
    String id = parseObject.getObjectId();
    DateTime created = new DateTime(parseObject.getCreatedAt());
    DateTime modified = new DateTime(parseObject.get(C.MODIFIED));
    ParseUser owner = (ParseUser) parseObject.get(C.OWNER);
    int coverColor = parseObject.getInt(C.COVER_COLOR);
    return new BucketListItem(id, title, description, created) //
        .setModified(modified) //
        .setOwner(owner) //
        .setCoverColor(coverColor);
  }


  @Override
  public int compareTo(BucketListItem another) {
    return (int) ((another.getCreated().getMillis() / 1000) - (getCreated().getMillis() / 1000));
  }

  public String getPrettyCreated() {
    return "Created: " + prettifyDate(created);
  }

  public String getPrettyModified() {
    return "Updated: " + prettifyDate(modified);
  }

  private String prettifyDate(DateTime date) {
    DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    return format.print(date);
  }

  private DateTime getCreated() {
    return created;
  }

  public BucketListItem setCoverColor(int color) {
    this.coverColor = color;
    return this;
  }

  public int getCoverColor() {
    return coverColor;
  }
}
