package com.giftanimation;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.giftanimation.animationLib.CommonClass;
import com.giftanimation.animationLib.AnimationManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<AnimationManager> activeConfettiManagers = new ArrayList<>();
    protected int goldDark, goldMed, gold, goldLight;
    protected int[] colors;
    ViewGroup container;

    ImageView imageView;

    ArrayList<Drawable> arrDrawables = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = (ViewGroup) findViewById(R.id.container);

         imageView=(ImageView)findViewById(R.id.iv);

        /*Resources res = getResources();
        goldDark = res.getColor(R.color.blue);
        goldMed = res.getColor(R.color.yellow);
        gold = res.getColor(R.color.pink);
        goldLight = res.getColor(R.color.green);
        colors = new int[]{goldDark, goldMed, gold, goldLight};*/


        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(bitmap);
                /*if (imageView.getDrawable() != null) {
                    mDrawable = imageView.getDrawable();
                    System.out.println("Drwable : " + mDrawable);
                } else {
                    System.out.println("Drwable is null");
                }*/

                createDrawableArray(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.get()
                .load("https://www.punchey.com/images/gift-green.png")
                .into(target);



    }

    public AnimationManager generateStream() {

        Drawable[] arrDrawableMain = new Drawable[arrDrawables.size()];
        arrDrawableMain = arrDrawables.toArray(arrDrawableMain);
        return CommonClass.rainingConfetti(container, arrDrawableMain)
                .stream(3000);
    }

    public void startAnimation(View v) {
        activeConfettiManagers.add(generateStream());
    }


    private void createDrawableArray(Bitmap bitmap) {

        /*
        0xFFFF0000 = Opaque red -
        0xFF00FF00 = Opaque green -
        0xFF0000FF = Opaque blue -
        0xFFFFFFFF = Opaque white -
        0xFF000000 = Opaque black
        0xFF777777 = Opaque medium gray -
        0x7FFF0000 = 50% transparent red -
        0xffff00ff = Magenta
        0xffffff00 = yellow
        0xffBF5FFF = violate
        */

        Drawable mDrawable1 = new BitmapDrawable(getResources(), bitmap);
        mDrawable1.setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY); //red
        arrDrawables.add(mDrawable1);

        Drawable mDrawable2 = new BitmapDrawable(getResources(), bitmap);
        mDrawable2.setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY); // green
        arrDrawables.add(mDrawable2);

        Drawable mDrawable3 = new BitmapDrawable(getResources(), bitmap);
        mDrawable3.setColorFilter(0xFF4EDFD7, PorterDuff.Mode.MULTIPLY); //blue
        arrDrawables.add(mDrawable3);

        Drawable mDrawable4 = new BitmapDrawable(getResources(), bitmap);
        mDrawable4.setColorFilter(0xFFFF6600, PorterDuff.Mode.MULTIPLY); //orange
        arrDrawables.add(mDrawable4);

        Drawable mDrawable5 = new BitmapDrawable(getResources(), bitmap);
        mDrawable5.setColorFilter(0xffffff00, PorterDuff.Mode.MULTIPLY); //yellow
        arrDrawables.add(mDrawable5);

        Drawable mDrawable6 = new BitmapDrawable(getResources(), bitmap);
        mDrawable6.setColorFilter(0xffBF5FFF, PorterDuff.Mode.MULTIPLY); //violate
        arrDrawables.add(mDrawable6);

    }

}
