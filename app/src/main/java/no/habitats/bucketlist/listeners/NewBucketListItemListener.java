package no.habitats.bucketlist.listeners;

/**
 * Created by Patrick on 27.07.2014.
 */
public interface NewBucketListItemListener {

  void createBucketItem(String title, String description, int color);
}
