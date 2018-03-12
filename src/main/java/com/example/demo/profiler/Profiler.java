package com.example.demo.profiler;

import com.example.demo.profiler.records.Call;
import com.example.demo.profiler.records.Record;

public class Profiler {

    static ThreadLocal<Record> records = new ThreadLocal<>();

    public static void beginRecord(String url) {
        records.set(new Record(url));
    }

    public static Record finishRecord() {
        Record record = records.get();
        records.set(null);
        if (record != null) {
            record.end();
        }
        return record;
    }

    public static void startCall(String cls, String mth) {
        Record rec = records.get();
        if (rec == null || rec.getCalls().size() > 50100) {
            return;
        }

        int depth = rec.getCalls().size() > 0 ? rec.getCalls().get(rec.getCalls().size() - 1).getDepth() : 0;

        rec.addCall(new Call()
                .cls(cls)
                .mth(mth)
                .start(System.currentTimeMillis())
                .depth(depth + 1)
        );
    }

    public static void endCall(String c, String m) {
        Record rec = records.get();
        if (rec == null || rec.getCalls().size() > 50100) {
            return;
        }

        int depth = rec.getCalls().size() > 0 ? rec.getCalls().get(rec.getCalls().size() - 1).getDepth() : 0;

        rec.addCall(new Call()
                .cls(c)
                .mth(m)
                .end(System.currentTimeMillis())
                .depth(depth - 1)
        );
    }
}
