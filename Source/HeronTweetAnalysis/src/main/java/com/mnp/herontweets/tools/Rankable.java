package com.mnp.herontweets.tools;

import java.util.List;

public interface Rankable extends Comparable<Rankable> {

    Object getObject();

    long getCount();
    
    List<Object> getFields();

}
