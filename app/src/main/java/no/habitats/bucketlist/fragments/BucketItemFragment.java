package no.habitats.bucketlist.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import no.habitats.bucketlist.ColorChanger;
import no.habitats.bucketlist.R;
import no.habitats.bucketlist.listeners.BucketListFragmentListener;
import no.habitats.bucketlist.listeners.ColorChangeListener;
import no.habitats.bucketlist.models.BucketListItem;

public class BucketItemFragment extends Fragment implements ColorChangeListener {

  private static final String BUCKET_ITEM_ID = "bucketItemId";
  public static final String TAG = BucketItemFragment.class.getSimpleName();

  private BucketListFragmentListener mListener;
  private BucketListItem bucketItem;
  private SeekBar colorPicker;

  public static BucketItemFragment newInstance(BucketListItem bucketItem) {
    BucketItemFragment fragment = new BucketItemFragment();
    Bundle args = new Bundle();
    args.putString(BUCKET_ITEM_ID, BucketListItem.toJson(bucketItem));
    fragment.setArguments(args);
    return fragment;
  }

  public BucketItemFragment() {
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    if (getArguments() != null) {
      bucketItem = BucketListItem.fromJson(getArguments().getString(BUCKET_ITEM_ID));
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_bucket_item, container, false);
    view.setOnTouchListener(new View.OnTouchListener() {
      public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });

    TextView author = (TextView) view.findViewById(R.id.tv_bucket_author);
    final TextView title = (TextView) view.findViewById(R.id.tv_bucket_title);
    final TextView description = (TextView) view.findViewById(R.id.tv_bucket_description);
    TextView modified = (TextView) view.findViewById(R.id.tv_bucket_modified);
    TextView created = (TextView) view.findViewById(R.id.tv_bucket_created);

    colorPicker = (SeekBar) view.findViewById(R.id.color_seekbar);
    colorPicker.setOnSeekBarChangeListener(ColorChanger.getChanger());
    ColorChanger.getChanger().addListener(this);
    colorPicker.getProgressDrawable().mutate();
    colorPicker.setEnabled(!bucketItem.isCompleted());
    colorPicker.setProgress(ColorChanger.getChanger().toProgress(bucketItem.getCoverColor()));

    final CheckBox completed = (CheckBox) view.findViewById(R.id.cb_completed);
    completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        bucketItem.setCompleted(isChecked);
        colorPicker.setEnabled(!isChecked);
      }
    });
    Button save = (Button) view.findViewById(R.id.b_save);
    save.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        bucketItem.setDescription(description.getText().toString());
        bucketItem.setTitle(title.getText().toString());
        bucketItem.setModified();
        bucketItem.setCompleted(completed.isChecked());
        bucketItem.setCoverColor(ColorChanger.getChanger().getColor());
        mListener.update(bucketItem);
        getActivity().getFragmentManager().popBackStack();
      }
    });

    Button delete = (Button) view.findViewById(R.id.b_delete);
    delete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mListener.delete(bucketItem);
        getActivity().getFragmentManager().popBackStack();
      }
    });

    author.setText(bucketItem.getOwner());
    title.setText(bucketItem.getTitle());
    modified.setText(bucketItem.getPrettyModified());
    created.setText(bucketItem.getPrettyCreated());
    description.setText(bucketItem.getDescription());
    completed.setChecked(bucketItem.isCompleted());

    return view;
  }


  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    try {
      mListener = (BucketListFragmentListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(
          activity.toString() + " must implement " + BucketListFragmentListener.class.getSimpleName());
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
    mListener = null;
  }

  @Override
  public void colorChanged(int color) {
    colorPicker.getThumb().mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    colorPicker.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

  }
}
