package com.tung.mysmartwatch.utils.otto;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BusProvider {
    private static BusProvider busProvider;
    public static BusProvider getInstance() {
        if (busProvider == null) {
            synchronized (BusProvider.class) {
                if (busProvider == null) {
                    busProvider = new BusProvider();
                }
            }
        }
        return busProvider;
    }

    private Bus bus = new Bus(ThreadEnforcer.MAIN);

    public Bus getBus() {
        return bus;
    }
}
