package com.daniel.app.global.sphere.Utils;


import com.daniel.app.global.sphere.models.BaseEntity;
import com.daniel.app.global.sphere.models.FeedItem;

import java.util.Comparator;

import java.util.Comparator;

public class CreatedAtComparator<T extends BaseEntity> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        if (o1.getCreatedAt() == null && o2.getCreatedAt() == null) {
            return 0;
        }
        if (o1.getCreatedAt() == null) {
            return 1; // nulls go last
        }
        if (o2.getCreatedAt() == null) {
            return -1; // nulls go last
        }
        return o2.getCreatedAt().compareTo(o1.getCreatedAt()); // newest first
    }
}

