package com.cyanelix.railwatch.darwin.client;

import com.thalesgroup.rtti._2016_02_16.ldb.ObjectFactory;

public abstract class DarwinRequest<T> {
    protected static final ObjectFactory objectFactory = new ObjectFactory();

    public abstract T getSoapRequest();
}
