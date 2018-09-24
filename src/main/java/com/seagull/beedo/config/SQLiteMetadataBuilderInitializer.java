/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.config;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
import org.hibernate.engine.jdbc.dialect.internal.DialectResolverSet;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guosheng.huang
 * @version $id:SQLiteMetadataBuilderInitializer.java, v 0.1 2018年08月12日 19:11 guosheng.huang Exp $
 */
public class SQLiteMetadataBuilderInitializer  extends IdentityColumnSupportImpl {


    private final static Logger logger = LoggerFactory.getLogger(SQLiteMetadataBuilderInitializer.class);

    public void contribute(MetadataBuilder metadataBuilder, StandardServiceRegistry serviceRegistry) {
        DialectResolver dialectResolver = serviceRegistry.getService(DialectResolver.class);

        if (!(dialectResolver instanceof DialectResolverSet)) {
            logger.warn("DialectResolver '%s' is not an instance of DialectResolverSet, not registering SQLiteDialect",
                    dialectResolver);
            return;
        }

        ((DialectResolverSet) dialectResolver).addResolver(resolver);
    }

    static private final SQLiteDialect dialect = new SQLiteDialect();

    static private final DialectResolver resolver = (DialectResolver) info -> {
        if (info.getDatabaseName().equals("SQLite")) {
            return dialect;
        }

        return null;
    };

}
