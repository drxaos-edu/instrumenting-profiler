package com.example.demo.profiler.records;

public class Profiler {

    // для непараллельного кода в контейнере сервлетов подойдет ThreadLocal
    // для паралельного понадобится Map с id вызова
    static ThreadLocal<Record> records = new ThreadLocal<>();

    public static void beginRecord(String url) {
        records.set(new Record(url));
    }

    public static void url(String url) {
        Record record = records.get();
        if (record != null) {
            record.setUrl(url);
        }
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

    public static void endCall(String cls, String mth) {
        Record rec = records.get();
        if (rec == null || rec.getCalls().size() > 50100) {
            return;
        }

        int depth = rec.getCalls().size() > 0 ? rec.getCalls().get(rec.getCalls().size() - 1).getDepth() : 0;

        rec.addCall(new Call()
                .cls(cls)
                .mth(mth)
                .end(System.currentTimeMillis())
                .depth(depth - 1)
        );
    }
}
