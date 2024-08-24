package com.openelements.hedera.base.mirrornode;

import java.util.List;

public interface Page<T> {


    int getNumber();

    int getSize();

    List<T> getData();

    boolean hasNext();

    Page<T> next();

    Page<T> first();

    boolean isFirst();
}
