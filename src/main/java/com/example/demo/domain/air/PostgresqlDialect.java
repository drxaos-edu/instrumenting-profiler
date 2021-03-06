package com.example.demo.domain.air;

import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class PostgresqlDialect extends PostgreSQL94Dialect {
    public PostgresqlDialect() {
        super();
        registerFunction("split_part", new StandardSQLFunction("split_part", StandardBasicTypes.STRING));
    }
}
