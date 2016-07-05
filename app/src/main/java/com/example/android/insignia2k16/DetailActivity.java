package com.example.android.insignia2k16;

import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    ImageView mImageView;
    TextView mTextView;
    ImageView mFab;
    Button mRegister_button;
    Firebase firebase;
    Firebase child;
    LinearLayout mLinearLayout;
    Button submit,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getBackground().setAlpha(0);
        setSupportActionBar(toolbar);

        firebase=new Firebase("https://insignia-master.firebaseio.com/insignia");
        child=firebase.child("event1");
        mImageView = (ImageView) findViewById(R.id.detail_imageView);
        mFab = (ImageView) findViewById(R.id.detail_fab);
        mTextView = (TextView) findViewById(R.id.detail_textView);
        mRegister_button = (Button)findViewById(R.id.detail_register_button);
        mRegister_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registration();
            }
        });
        int position = getIntent().getIntExtra("p",0);
        mImageView.setImageResource(Constants.mEvents_posters[position]);
        mTextView.setText(Constants.mEvents_names[position]);
        Bitmap bitmap = getReducedBitmap(position);
        chooseColor(bitmap);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularReveal(mImageView);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void registration(){
        final Dialog dialog=new Dialog(DetailActivity.this);
        dialog.setContentView(R.layout.registration_layout);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialog.show();

        submit=(Button)dialog.findViewById(R.id.btn_submit);
        cancel=(Button)dialog.findViewById(R.id.btn_cancel);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("xxxxxxxxxxx",((EditText)dialog.findViewById(R.id.e_name)).getText().toString()+"hello");
                if (((EditText) dialog.findViewById(R.id.e_name)).getText().toString().length() > 2 &&
                        ((EditText) dialog.findViewById(R.id.e_mail)).getText().toString().endsWith("@gmail.com") &&
                        ((EditText) dialog.findViewById(R.id.e_mobile)).getText().toString().length() == 10 &&
                        ((EditText) dialog.findViewById(R.id.e_college)).getText().toString().length() > 2) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    String s = "Name: " + ((EditText) dialog.findViewById(R.id.e_name)).getText().toString()
                            + " Email: " + ((EditText) dialog.findViewById(R.id.e_mail)).getText().toString()
                            + " Phone: " + ((EditText) dialog.findViewById(R.id.e_mobile)).getText().toString()
                            + " College: " + ((EditText) dialog.findViewById(R.id.e_college)).getText().toString();
                    map.put("contestant", s);
                    child.push().setValue(map);
                    dialog.cancel();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    private void circularReveal(View selectedView) {
        View myView = selectedView;

        // get the center for the clipping circle
        int cx = myView.getRight() -30;
        int cy = myView.getBottom() -60;

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

        // create the animator for this view (the start radius is zero)
        Animator anim =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius).setDuration(500);
        }
        anim.start();


    }

    private Bitmap getReducedBitmap(int albumArtResId) {
        // reduce image size in memory to avoid memory errors
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 8;
        return BitmapFactory.decodeResource(getResources(), Constants.mEvents_posters[albumArtResId], options);
    }

    private void chooseColor(Bitmap b) {

        Palette.generateAsync(b, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch!=null){
                    mTextView.setBackgroundColor(swatch.getRgb());
                    mTextView.setTextColor(swatch.getTitleTextColor());
                    mRegister_button.setBackgroundColor(swatch.getRgb());
                }

            }
        });
    }
}
