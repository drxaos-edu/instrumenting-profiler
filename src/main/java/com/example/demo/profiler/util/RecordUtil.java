package com.example.demo.profiler.util;

import com.example.demo.profiler.records.Call;
import com.example.demo.profiler.records.Record;
import lombok.Data;

import java.util.*;

public class RecordUtil {

    @Data
    public static class QueryTop implements Comparable<QueryTop> {
        private long time = 0;
        private long count = 0;

        public int compareTo(QueryTop o) {
            return Long.valueOf(o.time).compareTo(time);
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        list.sort(Comparator.comparing(o -> (o.getValue())));

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static String dump(Record record, long lowThreshold) {
        return "Record: " + record.getUrl() + " " + record.getStart() + "-" + record.getEnd();
    }

    public static String dumpTODO(Record record, long lowThreshold) {
        ArrayList<Call> calls = record.getCalls();

        long now = System.currentTimeMillis();

        // Схлопываем все начала и концы вызовов

        Map<Integer, Call> map = new HashMap<>();

        for (Iterator<Call> iterator = calls.iterator(); iterator.hasNext(); ) {
            Call next = iterator.next();
            if (next.getStart() != null) {
                map.put(next.getDepth(), next); // запоминаем начало
            } else {
                map.get(next.getDepth() + 1).end(next.getEnd()); // приклеиваем к началу конец
                iterator.remove();
            }
        }

        // у вызовов будет end=null, если размер дампа превышен, фиксим это

        boolean veryLongTrace = false;
        if (calls.size() > 25000) {
            veryLongTrace = true;
            calls.forEach(call -> {
                if (call.getEnd() == null) {
                    call.end(now);
                }
            });
        }

        // Топ SQL-запросов по сумме времени выполнения

//TODO
//        int sqls = 0;
//        Map<String, QueryTop> top = new HashMap<>();
//        for (Call call : calls) {
//            if (!call.getCls().equals("DB")) { // у запросов вместо класса строка "DB"
//                continue;
//            }
//            sqls++;
//            long t = call.getEnd() - call.getStart();
//            if (t > 0) {
//                String sql = call.getMth();
//                QueryTop e = top.computeIfAbsent(sql, k -> new QueryTop());
//                e.setTime(e.getTime() + t);
//                e.setCount(e.getCount() + 1);
//            }
//        }
//
//        top = sortByValue(top); // сортируем по убыванию времени

        // Удаляем все слишком быстрые вызовы

        calls.removeIf(next -> !next.getCls().equals("DB") && next.getEnd() - next.getStart() < lowThreshold);

        // Дампим в строку все что получилось

        StringBuilder sb = new StringBuilder();
        int traceLength = calls.size();

        long fullTime = (record.getEnd() != null && record.getStart() != null) ? (record.getEnd() - record.getStart()) : -1;
        sb.append("\n\n### ").append(record.getUrl()).append(" ### [").append(traceLength).append("] ").append(fullTime).append("ms\n");
        if (veryLongTrace) {
            sb.append("!!! Very long trace\n");
        }
        for (Call call : calls) {
            for (int i = 0; i < call.getDepth(); i++) {
                sb.append(">");
            }
            String mth = call.getMth();
            String cls = call.getCls();

//TODO
//            if ("DB".equals(cls)) {
//                mth = QueryUtil.stripSqlData(mth);
//            }

            sb.append(cls).append("::").append(mth).append(" - ").append((call.getEnd() != null ? call.getEnd() : now) - (call.getStart() != null ? call.getStart() : now)).append("ms (").append(call.getStart()).append("-").append(call.getEnd()).append(")\n");
        }

//TODO
//        if (top.size() > 0) {
//            sb.append("### ").append("SQL").append(" ### [").append(sqls).append("]\n");
//            int n = 0;
//            for (Map.Entry<String, QueryTop> e : top.entrySet()) {
//                String sql = QueryUtil.stripSqlData(e.getKey());
//
//                sb.append(e.getValue().getTime()).append("ms [").append(e.getValue().getCount()).append("] ").append(sql).append("\n");
//                if (++n >= 10) {
//                    break;
//                }
//            }
//        }

        sb.append("----------\n\n\n");

        return sb.toString();
    }
}
