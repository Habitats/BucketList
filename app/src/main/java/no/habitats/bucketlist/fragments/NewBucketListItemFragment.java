package no.habitats.bucketlist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import no.habitats.bucketlist.ColorChanger;
import no.habitats.bucketlist.R;
import no.habitats.bucketlist.listeners.ColorChangeListener;
import no.habitats.bucketlist.listeners.NewBucketListItemListener;

public class NewBucketListItemFragment extends Fragment implements ColorChangeListener {

  private static final String TAG = NewBucketListItemFragment.class.getSimpleName();
  private NewBucketListItemListener mListener;
  private View background;
  private View rootView;

  public static NewBucketListItemFragment newInstance() {
    NewBucketListItemFragment fragment = new NewBucketListItemFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public NewBucketListItemFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      // get arguments
    }

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_new_item, container, false);

    final EditText titleField = (EditText) rootView.findViewById(R.id.et_title);
    final EditText descriptionField = (EditText) rootView.findViewById(R.id.et_description);
    background = rootView.findViewById(R.id.background);
    SeekBar colorPicker = (SeekBar) rootView.findViewById(R.id.color_seekbar);
    colorPicker.setOnSeekBarChangeListener(ColorChanger.getChanger());
    ColorChanger.getChanger().addListener(this);
    colorPicker.getProgressDrawable().mutate();
    colorPicker.setProgress((int) (Math.random() * colorPicker.getMax()));

    Button NewBucketListItemButton = (Button) rootView.findViewById(R.id.b_create_item);

    NewBucketListItemOnClickListener listener = new NewBucketListItemOnClickListener(titleField, descriptionField);
    NewBucketListItemButton.setOnClickListener(listener);

    return rootView;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (NewBucketListItemListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void colorChanged(int color) {
    SeekBar colorSeeker = (SeekBar) rootView.findViewById(R.id.color_seekbar);

    colorSeeker.getThumb().mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    colorSeeker.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
  }

  private class NewBucketListItemOnClickListener implements View.OnClickListener {

    private final EditText titleField;
    private final EditText descriptionField;

    public NewBucketListItemOnClickListener(EditText titleField, EditText descriptionField) {
      this.titleField = titleField;
      this.descriptionField = descriptionField;
    }

    @Override
    public void onClick(View v) {
      String title = titleField.getText().toString().trim();
      String description = descriptionField.getText().toString().trim();

      if (title.isEmpty()) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("You need a title!")//
            .setTitle("Error")//
            .setPositiveButton(android.R.string.ok, null)//
            .create()//
            .show();// ;
      } else {
        mListener.createNewBucketListItem(title, description, ColorChanger.getChanger().getColor());
      }
    }
  }
}
