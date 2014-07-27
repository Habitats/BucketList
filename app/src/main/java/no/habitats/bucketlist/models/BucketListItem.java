package no.habitats.bucketlist.models;

import org.joda.time.DateTime;

/**
 * Created by Patrick on 27.07.2014.
 */
public class BucketListItem {

  private final String id;
  private final DateTime created;
  private final DateTime modified;
  private  boolean completed;

  public BucketListItem(String id, DateTime created, DateTime modified) {
    this.id = id;
    this.created = created;
    this.modified = modified;
  }
}
