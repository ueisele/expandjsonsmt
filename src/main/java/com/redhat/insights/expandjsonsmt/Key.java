package com.redhat.insights.expandjsonsmt;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

public class Key {

    private final List<String> path;

    public Key() {
        this(emptyList());
    }

    public Key(String key) {
        this(singletonList(key));
    }

    public Key(final List<String> path) {
        this.path = requireNonNull(path);
    }

    public Key childKey(final String child) {
        final List<String> copy = new ArrayList<>(path);
        copy.add(child);
        return new Key(copy);
    }

    public String getLastElement() {
        if (isRoot()) {
            return null;
        }
        return path.get(path.size() - 1);
    }

    public String getPath() {
        if (isRoot()) {
            return null;
        }
        return String.join(".", path);
    }

    public String getCamelCasePath() {
        if (isRoot()) {
            return null;
        }
        return path.stream().map(StringUtils::capitalize).collect(Collectors.joining(""));
    }

    public boolean isRoot() {
        return path.isEmpty();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Key)) return false;
        final Key key = (Key) o;
        return path.equals(key.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return getPath();
    }

}
