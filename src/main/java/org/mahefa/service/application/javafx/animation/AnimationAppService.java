package org.mahefa.service.application.javafx.animation;

import javafx.animation.AnimationTimer;

public interface AnimationAppService {

    void add(String key, AnimationTimer animationTimer);
    void remove(String... keys);
}
