package com.example.slowvf.View.Chat.conversation;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.example.slowvf.R;

public class NewMessageActivity extends AppCompatActivity {

    private ConstraintLayout cardLayout;
    private AnimatorSet slideUpAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_message_received);

        Intent intent = getIntent();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Nouveau message");

        cardLayout = findViewById(R.id.card_layout);
        slideUpAnimator = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.slide_up);

        View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private final Rect r = new Rect();

            @Override
            public void onGlobalLayout() {
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) {
                    // Keyboard is visible
                    if (!slideUpAnimator.isRunning() && cardLayout.getTranslationY() == 0) {
                        slideUpAnimator.setTarget(cardLayout);
                        slideUpAnimator.start();
                    }
                } else {
                    // Keyboard is hidden
                    if (!slideUpAnimator.isRunning() && cardLayout.getTranslationY() < 0) {
                        cardLayout.setTranslationY(0);
                    }
                }
            }
        });

    }
}
