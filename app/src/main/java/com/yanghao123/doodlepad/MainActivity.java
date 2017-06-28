package com.yanghao123.doodlepad;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yanghao123.doodlepad.doodle.DoodleContainer;
import com.yanghao123.doodlepad.doodle.StickerElement;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private DoodleContainer mDoodleContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDoodleContainer = (DoodleContainer) findViewById(R.id.doodle_container);

        findViewById(R.id.add_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTest();
            }
        });
    }

    private void addTest() {
        Random random = new Random();
        int x = random.nextInt(mDoodleContainer.getWidth());
        int y = random.nextInt(mDoodleContainer.getHeight());
        Drawable drawable = getResources().getDrawable(R.drawable.test);
        StickerElement element = new StickerElement(getResources(), x, y, drawable);
        mDoodleContainer.addElement(element);
    }
}
