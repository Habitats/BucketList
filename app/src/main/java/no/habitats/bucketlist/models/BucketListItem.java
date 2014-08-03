package no.habitats.bucketlist.models;

import com.google.gson.Gson;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import no.habitats.bucketlist.BucketListApplication;
import no.habitats.bucketlist.C;

/**
 * Created by Patrick on 27.07.2014.
 */
public class BucketListItem implements Comparable<BucketListItem> {

  private String id;
  private long created;
  private long modified;
  private boolean completed;
  private String ownerId;
  private String title;
  private String description;
  private int coverColor;

  public BucketListItem(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public BucketListItem(String id, String title, String description, DateTime created) {
    this(title, description);
    this.created = created.getMillis();
    this.id = id;
  }

  public String getOwner() {
    return "Author: " + ownerId;
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
    this.ownerId = owner.getObjectId();
    return this;
  }

  private BucketListItem setModified(DateTime dateTime) {
    this.modified = dateTime.getMillis();
    return this;
  }

  public ParseObject toParseObject() {
    ParseObject parse = new ParseObject(C.NAME);
    parse.put(C.DESCRIPTION, description);
    parse.put(C.TITLE, title);
    parse.setObjectId(id);
    parse.put(C.OWNER, ParseUser.getCurrentUser());
    parse.put(C.COVER_COLOR, coverColor);
    parse.put(C.COMPLETED, completed);

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
    boolean completed = parseObject.getBoolean(C.COMPLETED);
    int coverColor = parseObject.getInt(C.COVER_COLOR);
    return new BucketListItem(id, title, description, created) //
        .setModified(modified) //
        .setOwner(owner) //
        .setCompleted(completed)//
        .setCoverColor(coverColor);
  }

  public BucketListItem setCompleted(boolean completed) {
    this.completed = completed;
    return this;
  }


  @Override
  public int compareTo(BucketListItem another) {
    if (BucketListApplication.getApplication().getSortBy() == C.SORT_MODIFIED) {
      return (int) ((another.getModified().getMillis() / 1000) - (getModified().getMillis() / 1000));
    } else if (BucketListApplication.getApplication().getSortBy() == C.SORT_CREATED) {
      return (int) ((another.getCreated().getMillis() / 1000) - (getCreated().getMillis() / 1000));
    }
    return 0;
  }

  public String getPrettyCreated() {
    return "Created: " + prettifyDate(getCreated());
  }

  public String getPrettyModified() {
    return "Updated: " + prettifyDate(getModified());
  }

  private String prettifyDate(DateTime date) {
    DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    return format.print(date);
  }

  private DateTime getCreated() {
    return new DateTime(created);
  }

  public BucketListItem setCoverColor(int color) {
    this.coverColor = color;
    return this;
  }

  public int getCoverColor() {
    return coverColor;
  }

  public void toggleComplete() {
    completed = !completed;
  }

  public boolean isCompleted() {
    return completed;
  }

  public String getId() {
    return id;
  }

  public DateTime getModified() {
    return new DateTime(modified);
  }

  public void update(BucketListItem updatedItem) {
    this.title = updatedItem.getTitle();
    this.completed = updatedItem.isCompleted();
    this.coverColor = updatedItem.getCoverColor();
    this.description = updatedItem.getDescription();
    this.modified = updatedItem.getModified().getMillis();
  }

  public static BucketListItem fromJson(String json) {
    return new Gson().fromJson(json, BucketListItem.class);
  }

  public static String toJson(BucketListItem item) {
    return new Gson().toJson(item, BucketListItem.class);
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setModified() {
    modified = new DateTime().getMillis();
  }
}
