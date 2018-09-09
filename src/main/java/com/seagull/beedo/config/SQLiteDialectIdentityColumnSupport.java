/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.config;

import org.hibernate.dialect.identity.IdentityColumnSupportImpl;

/**
 * @author guosheng.huang
 * @version $id:SQLiteDialectIdentityColumnSupport.java, v 0.1 2018年08月12日 19:11 tao.hu Exp $
 */
public class SQLiteDialectIdentityColumnSupport extends IdentityColumnSupportImpl {

    public SQLiteDialectIdentityColumnSupport(SQLiteDialect sqLiteDialect) {

    }

    @Override
    public boolean supportsIdentityColumns() {
        return true;
    }

  /*
	public boolean supportsInsertSelectIdentity() {
    return true; // As specified in NHibernate dialect
  }
  */

    @Override
    public boolean hasDataTypeInIdentityColumn() {
        // As specified in NHibernate dialect
        // FIXME true
        return false;
    }

  /*
	public String appendIdentitySelectToInsert(String insertString) {
    return new StringBuffer(insertString.length()+30). // As specified in NHibernate dialect
      append(insertString).
      append("; ").append(getIdentitySelectString()).
      toString();
  }
  */

    @Override
    public String getIdentitySelectString(String table, String column, int type) {
        return "select last_insert_rowid()";
    }

    @Override
    public String getIdentityColumnString(int type) {
        // return "integer primary key autoincrement";
        // FIXME "autoincrement"
        return "integer";
    }

}
