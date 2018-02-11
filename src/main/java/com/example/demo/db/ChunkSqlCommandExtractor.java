/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package com.example.demo.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.internal.util.StringHelper;
import org.hibernate.tool.hbm2ddl.ImportScriptException;
import org.hibernate.tool.hbm2ddl.ImportSqlCommandExtractor;

public class ChunkSqlCommandExtractor implements ImportSqlCommandExtractor {
    @Override
    public String[] extractCommands(Reader reader) {
        BufferedReader bufferedReader = new BufferedReader(reader);
        List<String> statementList = new LinkedList<String>();
        try {
            StringBuilder cmd = new StringBuilder();
            for (String sql = bufferedReader.readLine(); sql != null; sql = bufferedReader.readLine()) {
                String trimmedSql = sql.trim();
                if (trimmedSql.startsWith("--next-chunk")) {
                    if (cmd.length() > 0) {
                        statementList.add(cmd.toString());
                    }
                    cmd.setLength(0);
                } else if (!StringHelper.isEmpty(trimmedSql) && !isComment(trimmedSql)) {
                    cmd.append(trimmedSql).append("\n");
                }
            }
            if (cmd.length() > 0) {
                statementList.add(cmd.toString());
            }
            return statementList.toArray(new String[statementList.size()]);
        } catch (IOException e) {
            throw new ImportScriptException("Error during import script parsing.", e);
        }
    }

    private boolean isComment(final String line) {
        return line.startsWith("--") || line.startsWith("//") || line.startsWith("/*");
    }
}
