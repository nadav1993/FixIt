package com.hadarfem.fixit.extensions;

/**
 * Created by Tzach & Nadav on 2/6/2018.
 */

public class AnimationHandler {
    public Animation getAnimation(int oldIndex, int newIndex, int indexCount) {
        if (oldIndex == 0 && newIndex == indexCount - 1) {
            return Animation.EnterFromLeft;
        } else if (newIndex == 0 && oldIndex == indexCount - 1) {
            return Animation.EnterFromRight;
        } else {
            if (newIndex > oldIndex) {
                return Animation.EnterFromRight;
            } else if (newIndex < oldIndex) {
                return Animation.EnterFromLeft;
            } else {
                return Animation.None;
            }
        }
    }

    public enum Animation {
        EnterFromLeft,
        EnterFromRight,
        None
    }
}
