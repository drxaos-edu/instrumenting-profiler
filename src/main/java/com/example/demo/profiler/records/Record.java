package com.example.demo.profiler.records;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Record {
    private String url;
    private ArrayList<Call> calls = new ArrayList<Call>();
    private Long start, end;

    public Record(String url) {
        this.url = url;
        this.start = System.currentTimeMillis();
    }

    public void addCall(Call call) {
        calls.add(call);
    }

    public void end() {
        this.end = System.currentTimeMillis();
    }
}
