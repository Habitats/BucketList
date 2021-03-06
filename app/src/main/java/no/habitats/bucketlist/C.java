package no.habitats.bucketlist;

/**
 * Created by Patrick on 27.07.2014.
 */
public class C {

  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String DESCRIPTION = "description";
  public static final String TITLE = "title";
  public static final String NAME = "BucketList";
  public static final String OWNER = "createdBy";
  public static final String MODIFIED = "modifiedAt";
  public static final String USER = "User";

  public static final String COVER_COLOR = "coverColor";
  public static final String COMPLETED = "completed";
  public static final String TABLE_BUCKET_LIST = "BucketList";

  public enum Sort {
    MODIFIED, CREATED, COLOR;

    public Sort next() {
      return values()[(ordinal() + 1) % 3];
    }
  }
}
