package no.habitats.bucketlist;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Patrick on 03.08.2014.
 */
public class BucketItemGestureDetector
    implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnTouchListener,
               GestureDetector.OnGestureListener {

  private GestureDetector detector;

  public BucketItemGestureDetector(Context context) {
    detector = new GestureDetector(context, this);
  }

  // SIMPLE CLICK EVENTS

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
    return false;
  }

  // SIMPLE TOUCH EVENTS

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    return detector.onTouchEvent(event);
  }

  // GESTURE EVENTS

  @Override
  public boolean onDown(MotionEvent e) {
    return false;
  }

  @Override
  public void onShowPress(MotionEvent e) {
  }

  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    return false;
  }

  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    return false;
  }

  @Override
  public void onLongPress(MotionEvent e) {
  }

  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    return false;
  }
}
