package com.example.demo.profiler.util;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.*;
import net.ttddyy.dsproxy.QueryInfo;

import java.util.List;

public class QueryUtil {

    public static String queryToCall(List<QueryInfo> queryInfoList) {
        StringBuilder sb = new StringBuilder();
        for (QueryInfo queryInfo : queryInfoList) {
            if (sb.length() > 0) {
                sb.append(" || ");
            }
            sb.append(queryInfo.getQuery());
        }
        return sb.toString();
    }

    /**
     * Заменяем все данные в запросах на X и Z
     */
    public static String stripSqlData(String sql) {
        try {
            final StringBuilder buffer = new StringBuilder();

            final ExpressionDeParser expressionDeParser = new ExpressionDeParser() {

                @Override
                public void visit(StringValue stringValue) {
                    stringValue.setValue("Z");
                }

            };
            final SelectDeParser selectParser = new SelectDeParser(expressionDeParser, buffer);
            expressionDeParser.setBuffer(buffer);
            expressionDeParser.setSelectVisitor(selectParser);

            Statement statement = CCJSqlParserUtil.parse(sql);
            statement.accept(new StatementVisitorAdapter() {
                @Override
                public void visit(Select select) {
                    select.getSelectBody().accept(selectParser);
                }

                @Override
                public void visit(Delete delete) {
                    DeleteDeParser deleteParser = new DeleteDeParser(expressionDeParser, buffer);
                    deleteParser.deParse(delete);
                }

                @Override
                public void visit(Update update) {
                    UpdateDeParser updateDeParser = new UpdateDeParser(expressionDeParser, selectParser, buffer);
                    updateDeParser.deParse(update);
                }

                @Override
                public void visit(Insert insert) {
                    InsertDeParser insertDeParser = new InsertDeParser(expressionDeParser, selectParser, buffer);
                    insertDeParser.deParse(insert);
                }
            });

            sql = statement.toString();

        } catch (JSQLParserException ignore) {
            // мы хотя бы попытались
        }

        return sql.replaceAll("[0-9]+", "X");
    }

}
