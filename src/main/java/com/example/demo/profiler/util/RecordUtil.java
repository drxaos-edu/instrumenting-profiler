package com.example.demo.profiler.util;

import com.example.demo.profiler.records.Call;
import com.example.demo.profiler.records.Record;
import com.example.demo.profiler.records.Top;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RecordUtil {

    public String dump(Record record, long lowThreshold) {
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

        // Топ SQL-запросов по сумме времени выполнения

        int sqls = 0;
        Map<String, Top> top = new HashMap<>();
        for (Call call : calls) {
            if (!call.getCls().equals("DB")) { // у запросов вместо класса строка "DB"
                continue;
            }
            sqls++;
            long t = call.getEnd() - call.getStart();
            if (t > 0) {
                String sql = call.getMth();
                Top e = top.computeIfAbsent(sql, k -> new Top());
                e.setTime(e.getTime() + t);
                e.setCount(e.getCount() + 1);
            }
        }

        top = MapUtil.sortByValue(top); // сортируем по убыванию времени

        // Удаляем все слишком быстрые вызовы

        calls.removeIf(next -> next.getEnd() - next.getStart() < lowThreshold);

        // Дампим в строку все что получилось

        StringBuilder sb = new StringBuilder();
        int traceLength = calls.size();

        long fullTime = (record.getEnd() != null && record.getStart() != null) ? (record.getEnd() - record.getStart()) : -1;
        sb.append("\n\n### ").append(record.getUrl()).append(" ### [").append(traceLength).append("] ").append(fullTime).append("ms\n");
        if (traceLength > 25000) {
            sb.append("!!! Very long trace\n");
        }
        for (Call call : calls) {
            for (int i = 0; i < call.getDepth(); i++) {
                sb.append(">");
            }
            String mth = call.getMth();
            String cls = call.getCls();
            if ("DB".equals(cls)) {
                mth = SqlUtil.stripSqlData(mth);
            }
            sb.append(cls).append("::").append(mth).append(" - ").append((call.getEnd() != null ? call.getEnd() : now) - (call.getStart() != null ? call.getStart() : now)).append("ms (").append(call.getStart()).append("-").append(call.getEnd()).append(")\n");
        }
        if (top.size() > 0) {
            sb.append("### ").append("SQL").append(" ### [").append(sqls).append("]\n");
            int n = 0;
            for (Map.Entry<String, Top> e : top.entrySet()) {
                String sql = SqlUtil.stripSqlData(e.getKey());

                sb.append(e.getValue().getTime()).append("ms [").append(e.getValue().getCount()).append("] ").append(sql).append("\n");
                if (++n >= 10) {
                    break;
                }
            }
        }
        sb.append("----------\n\n\n");

        return sb.toString();
    }
}
