package com.example.demo.profiler.records;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class Call {
    private String cls;
    private String mth;
    private Long start;
    private Long end;
    private int depth;

    public Call cls(String cls) {
        this.cls = cls;
        return this;
    }

    public Call mth(String mth) {
        this.mth = mth;
        return this;
    }

    public Call start(Long start) {
        this.start = start;
        return this;
    }

    public Call end(Long end) {
        this.end = end;
        return this;
    }

    public Call depth(int depth) {
        this.depth = depth;
        return this;
    }
}
