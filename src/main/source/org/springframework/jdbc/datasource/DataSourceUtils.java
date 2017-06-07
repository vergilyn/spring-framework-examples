//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

public abstract class DataSourceUtils {
    public static final int CONNECTION_SYNCHRONIZATION_ORDER = 1000;
    private static final Log logger = LogFactory.getLog(DataSourceUtils.class);

    public DataSourceUtils() {
    }

    public static Connection getConnection(DataSource dataSource) throws CannotGetJdbcConnectionException {
        try {
            return doGetConnection(dataSource);
        } catch (SQLException var2) {
            throw new CannotGetJdbcConnectionException("Could not get JDBC Connection", var2);
        }
    }

    public static Connection doGetConnection(DataSource dataSource) throws SQLException {
        Assert.notNull(dataSource, "No DataSource specified");
        ConnectionHolder conHolder = (ConnectionHolder)TransactionSynchronizationManager.getResource(dataSource);
        if(conHolder == null || !conHolder.hasConnection() && !conHolder.isSynchronizedWithTransaction()) {
            logger.debug("Fetching JDBC Connection from DataSource");
            Connection con = dataSource.getConnection();
            if(TransactionSynchronizationManager.isSynchronizationActive()) {
                logger.debug("Registering transaction synchronization for JDBC Connection");
                ConnectionHolder holderToUse = conHolder;
                if(conHolder == null) {
                    holderToUse = new ConnectionHolder(con);
                } else {
                    conHolder.setConnection(con);
                }

                holderToUse.requested();
                TransactionSynchronizationManager.registerSynchronization(new DataSourceUtils.ConnectionSynchronization(holderToUse, dataSource));
                holderToUse.setSynchronizedWithTransaction(true);
                if(holderToUse != conHolder) {
                    TransactionSynchronizationManager.bindResource(dataSource, holderToUse);
                }
            }
            System.out.println("datasource cur: " + con);
            return con;
        } else {
            conHolder.requested();
            if(!conHolder.hasConnection()) {
                logger.debug("Fetching resumed JDBC Connection from DataSource");
                conHolder.setConnection(dataSource.getConnection());
            }

            System.out.println("datasource new: " + conHolder.getConnection());

            return conHolder.getConnection();
        }
    }

    public static Integer prepareConnectionForTransaction(Connection con, TransactionDefinition definition) throws SQLException {
        Assert.notNull(con, "No Connection specified");
        if(definition != null && definition.isReadOnly()) {
            Object exToCheck;
            try {
                if(logger.isDebugEnabled()) {
                    logger.debug("Setting JDBC Connection [" + con + "] read-only");
                }

                con.setReadOnly(true);
            } catch (SQLException var4) {
                for(exToCheck = var4; exToCheck != null; exToCheck = ((Throwable)exToCheck).getCause()) {
                    if(exToCheck.getClass().getSimpleName().contains("Timeout")) {
                        throw var4;
                    }
                }

                logger.debug("Could not set JDBC Connection read-only", var4);
            } catch (RuntimeException var5) {
                for(exToCheck = var5; exToCheck != null; exToCheck = ((Throwable)exToCheck).getCause()) {
                    if(exToCheck.getClass().getSimpleName().contains("Timeout")) {
                        throw var5;
                    }
                }

                logger.debug("Could not set JDBC Connection read-only", var5);
            }
        }

        Integer previousIsolationLevel = null;
        if(definition != null && definition.getIsolationLevel() != -1) {
            if(logger.isDebugEnabled()) {
                logger.debug("Changing isolation level of JDBC Connection [" + con + "] to " + definition.getIsolationLevel());
            }

            int currentIsolation = con.getTransactionIsolation();
            if(currentIsolation != definition.getIsolationLevel()) {
                previousIsolationLevel = Integer.valueOf(currentIsolation);
                con.setTransactionIsolation(definition.getIsolationLevel());
            }
        }

        return previousIsolationLevel;
    }

    public static void resetConnectionAfterTransaction(Connection con, Integer previousIsolationLevel) {
        Assert.notNull(con, "No Connection specified");

        try {
            if(previousIsolationLevel != null) {
                if(logger.isDebugEnabled()) {
                    logger.debug("Resetting isolation level of JDBC Connection [" + con + "] to " + previousIsolationLevel);
                }

                con.setTransactionIsolation(previousIsolationLevel.intValue());
            }

            if(con.isReadOnly()) {
                if(logger.isDebugEnabled()) {
                    logger.debug("Resetting read-only flag of JDBC Connection [" + con + "]");
                }

                con.setReadOnly(false);
            }
        } catch (Throwable var3) {
            logger.debug("Could not reset JDBC Connection after transaction", var3);
        }

    }

    public static boolean isConnectionTransactional(Connection con, DataSource dataSource) {
        if(dataSource == null) {
            return false;
        } else {
            ConnectionHolder conHolder = (ConnectionHolder)TransactionSynchronizationManager.getResource(dataSource);
            return conHolder != null && connectionEquals(conHolder, con);
        }
    }

    public static void applyTransactionTimeout(Statement stmt, DataSource dataSource) throws SQLException {
        applyTimeout(stmt, dataSource, -1);
    }

    public static void applyTimeout(Statement stmt, DataSource dataSource, int timeout) throws SQLException {
        Assert.notNull(stmt, "No Statement specified");
        Assert.notNull(dataSource, "No DataSource specified");
        ConnectionHolder holder = (ConnectionHolder)TransactionSynchronizationManager.getResource(dataSource);
        if(holder != null && holder.hasTimeout()) {
            stmt.setQueryTimeout(holder.getTimeToLiveInSeconds());
        } else if(timeout >= 0) {
            stmt.setQueryTimeout(timeout);
        }

    }

    public static void releaseConnection(Connection con, DataSource dataSource) {
        try {
            doReleaseConnection(con, dataSource);
        } catch (SQLException var3) {
            logger.debug("Could not close JDBC Connection", var3);
        } catch (Throwable var4) {
            logger.debug("Unexpected exception on closing JDBC Connection", var4);
        }

    }

    public static void doReleaseConnection(Connection con, DataSource dataSource) throws SQLException {
        if(con != null) {
            if(dataSource != null) {
                ConnectionHolder conHolder = (ConnectionHolder)TransactionSynchronizationManager.getResource(dataSource);
                if(conHolder != null && connectionEquals(conHolder, con)) {
                    conHolder.released();
                    return;
                }
            }

            logger.debug("Returning JDBC Connection to DataSource");
            doCloseConnection(con, dataSource);
        }
    }

    public static void doCloseConnection(Connection con, DataSource dataSource) throws SQLException {
        if(!(dataSource instanceof SmartDataSource) || ((SmartDataSource)dataSource).shouldClose(con)) {
            con.close();
        }

    }

    private static boolean connectionEquals(ConnectionHolder conHolder, Connection passedInCon) {
        if(!conHolder.hasConnection()) {
            return false;
        } else {
            Connection heldCon = conHolder.getConnection();
            return heldCon == passedInCon || heldCon.equals(passedInCon) || getTargetConnection(heldCon).equals(passedInCon);
        }
    }

    public static Connection getTargetConnection(Connection con) {
        Connection conToUse;
        for(conToUse = con; conToUse instanceof ConnectionProxy; conToUse = ((ConnectionProxy)conToUse).getTargetConnection()) {
            ;
        }

        return conToUse;
    }

    private static int getConnectionSynchronizationOrder(DataSource dataSource) {
        int order = 1000;

        for(DataSource currDs = dataSource; currDs instanceof DelegatingDataSource; currDs = ((DelegatingDataSource)currDs).getTargetDataSource()) {
            --order;
        }

        return order;
    }

    private static class ConnectionSynchronization extends TransactionSynchronizationAdapter {
        private final ConnectionHolder connectionHolder;
        private final DataSource dataSource;
        private int order;
        private boolean holderActive = true;

        public ConnectionSynchronization(ConnectionHolder connectionHolder, DataSource dataSource) {
            this.connectionHolder = connectionHolder;
            this.dataSource = dataSource;
            this.order = DataSourceUtils.getConnectionSynchronizationOrder(dataSource);
        }

        public int getOrder() {
            return this.order;
        }

        public void suspend() {
            if(this.holderActive) {
                TransactionSynchronizationManager.unbindResource(this.dataSource);
                if(this.connectionHolder.hasConnection() && !this.connectionHolder.isOpen()) {
                    DataSourceUtils.releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
                    this.connectionHolder.setConnection((Connection)null);
                }
            }

        }

        public void resume() {
            if(this.holderActive) {
                TransactionSynchronizationManager.bindResource(this.dataSource, this.connectionHolder);
            }

        }

        public void beforeCompletion() {
            if(!this.connectionHolder.isOpen()) {
                TransactionSynchronizationManager.unbindResource(this.dataSource);
                this.holderActive = false;
                if(this.connectionHolder.hasConnection()) {
                    DataSourceUtils.releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
                }
            }

        }

        public void afterCompletion(int status) {
            if(this.holderActive) {
                TransactionSynchronizationManager.unbindResourceIfPossible(this.dataSource);
                this.holderActive = false;
                if(this.connectionHolder.hasConnection()) {
                    DataSourceUtils.releaseConnection(this.connectionHolder.getConnection(), this.dataSource);
                    this.connectionHolder.setConnection((Connection)null);
                }
            }

            this.connectionHolder.reset();
        }
    }
}
