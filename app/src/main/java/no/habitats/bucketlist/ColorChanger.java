package no.habitats.bucketlist;

import android.graphics.Color;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

import no.habitats.bucketlist.listeners.ColorChangeListener;


/**
 * Created by Patrick on 26.06.2014.
 */
public class ColorChanger implements SeekBar.OnSeekBarChangeListener {

  private static ColorChanger instance;
  private int color;
  private final List<ColorChangeListener> listeners;
  private int progress;

  public static ColorChanger getChanger() {
    if (instance == null) {
      instance = new ColorChanger();
    }
    return instance;
  }

  private ColorChanger() {
    listeners = new ArrayList<ColorChangeListener>();
  }

  public int getColor() {
    return color;
  }

  private void setBackgroundColor(int color) {
    this.color = color;
    update();
  }

  public void addListener(ColorChangeListener listener) {
    listeners.add(listener);
  }

  private void update() {
    for (ColorChangeListener listener : listeners) {
      listener.colorChanged(color);
    }
  }

  private int toHsv(double i) {
    float hue = (float) ((i / 100.) * 360.);
    float value = 0.73f;
    float sat = 0.95f;

    float hsv[] = {hue, sat, value};

    return Color.HSVToColor(hsv);
  }


  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    this.progress = progress;
    setBackgroundColor(toHsv(progress));
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
  }

  public int toProgress(int coverColor) {
    float hsv[] = new float[3];
    Color.colorToHSV(coverColor, hsv);
    return (int) (hsv[0] * 100 / 360);
  }
}
