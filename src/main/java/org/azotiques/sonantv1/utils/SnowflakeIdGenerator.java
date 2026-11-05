package org.azotiques.sonantv1.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class SnowflakeIdGenerator implements IdentifierGenerator {

    private static final Snowflake SNOWFLAKE = new Snowflake(1);

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return SNOWFLAKE.nextId();
    }
}
