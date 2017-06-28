package com.yanghao123.doodlepad;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yanghao123.stickerPad.DoodleSticker;
import com.yanghao123.stickerPad.DrawableSticker;
import com.yanghao123.stickerPad.StickerPad;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private StickerPad mStickerPad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStickerPad = (StickerPad) findViewById(R.id.doodle_container);

        findViewById(R.id.add_drawable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDrawable();
            }
        });

        findViewById(R.id.add_doodle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDoodle();
            }
        });
    }

    private void addDrawable() {
        Random random = new Random();
        int x = random.nextInt(mStickerPad.getWidth());
        int y = random.nextInt(mStickerPad.getHeight());
        Drawable drawable = getResources().getDrawable(R.drawable.test);

        DrawableSticker element = new DrawableSticker(getResources(), x, y, drawable);
        mStickerPad.addElement(element);
    }

    private void addDoodle() {
        DoodleSticker sticker = new DoodleSticker(getResources(), 0, 0, mStickerPad.getWidth(), mStickerPad.getHeight());
        mStickerPad.addElement(sticker);
    }
}
