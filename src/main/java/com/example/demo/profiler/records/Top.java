package com.example.demo.profiler.records;

import lombok.Data;

@Data
public class Top implements Comparable<Top> {
    private long time = 0;
    private long count = 0;

    public int compareTo(Top o) {
        return Long.valueOf(o.time).compareTo(time);
    }
}
