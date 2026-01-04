package com.dave.Main.State;

import org.springframework.context.ApplicationEvent;

public class StateChangeEvent extends ApplicationEvent {

    public StateChangeEvent(Object source) {
        super(source);
    }

}
