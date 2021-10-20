package org.mahefa.service.application.javafx.animation;

import javafx.animation.AnimationTimer;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnimationAppServiceImpl implements AnimationAppService {

    ConcurrentHashMap<String, AnimationTimer> concurrentHashMap = new ConcurrentHashMap<>();

    @Override
    public void add(String key, AnimationTimer animationTimer) {
        concurrentHashMap.put(key, animationTimer);
    }

    @Override
    public void remove(String... keys) {
        if(keys == null || keys.length == 0 || concurrentHashMap.size() == 0) {
            return;
        }

        for(int k = 0; k < keys.length; k++) {
            AnimationTimer animationTimer = concurrentHashMap.get(keys[k]);
            animationTimer.stop();
            concurrentHashMap.remove(keys[k], animationTimer);
        }
    }
}
