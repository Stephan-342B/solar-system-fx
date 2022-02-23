package org.mahefa.data;

import javafx.animation.AnimationTimer;

/**
 *
 */
public class UserData {

    private CelestialBody celestialBody;
    private AnimationTimer rotateAnimation;

    public UserData(CelestialBody celestialBody) {
        this.celestialBody = celestialBody;
    }

    public CelestialBody getCelestialBody() {
        return celestialBody;
    }

    public void setCelestialBody(CelestialBody celestialBody) {
        this.celestialBody = celestialBody;
    }

    public AnimationTimer getRotateAnimation() {
        return rotateAnimation;
    }

    public void setRotateAnimation(AnimationTimer rotateAnimation) {
        this.rotateAnimation = rotateAnimation;
    }
}
