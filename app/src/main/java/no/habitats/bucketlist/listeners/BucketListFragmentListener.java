package no.habitats.bucketlist.listeners;

import no.habitats.bucketlist.models.BucketListItem;

/**
 * Created by Patrick on 27.07.2014.
 */
public interface BucketListFragmentListener {

  void enterCreateItem();

  void enterBucketItem(BucketListItem item);

  void update(BucketListItem bucketItem);

  void delete(BucketListItem bucketItem);
}
