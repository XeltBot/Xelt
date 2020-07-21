package me.theditor.xelt.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import me.theditor.xelt.Xelt;

public class MySQL {

	private Xelt xelt;
    private Connection connnection;
    private String host, username, password, database, port;
    
    public MySQL() {
    	xelt = Xelt.getInstance();
    }

    public void connect(String host, String username, String password, String database, String port){
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.port = port;

        try{
            synchronized (this){final Properties prop=new Properties();
                Class.forName("com.mysql.cj.jdbc.Driver");
                prop.setProperty("user", this.username);
                prop.setProperty("password", this.password);
                prop.setProperty("autoReconnect","true");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, prop));
                xelt.getLogger().info("Connected to MySQL");
                setup();
            }
        }catch (SQLException | ClassNotFoundException e){
        	xelt.getLogger().fatal("Failed to connect to MySQL");
            e.printStackTrace();
        }
    }
    
    private void setup() {
    	if(!this.tableExists("Guilds")) {
    		this.createTable("Guilds", "ID varchar(255) NOT NULL PRIMARY KEY, Prefix varchar(255)");
    	} else {
    		String[] columns = {"ID", "Prefix"};
    		String[] types = {" varchar(255) NOT NULL PRIMARY KEY", " varchar(255)"};
    		for(int i=0; i <columns.length; i++) {
    			if(!columnExists(columns[i], "Guilds")) {
    				try {
    					PreparedStatement statement = this.getConnection().prepareStatement("ALTER TABLE Guilds ADD " + columns[i] + types[i]);
    					this.update(statement);
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    	}
    	if(!this.tableExists("Currency")) {
    		this.createTable("Currency", "ID varchar(255) NOT NULL PRIMARY KEY, Balance BigInt");
    	} else {
    		String[] columns = {"ID", "Balance"};
    		String[] types = {" varchar(255) NOT NULL PRIMARY KEY", " BigInt"};
    		for(int i=0; i <columns.length; i++) {
    			if(!columnExists(columns[i], "Currency")) {
    				try {
    					PreparedStatement statement = this.getConnection().prepareStatement("ALTER TABLE Currency ADD " + columns[i] + types[i]);
    					this.update(statement);
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    	}
    }

    /**
     * @param update
     *
     * Only to be used for quick query updates which do not require query parsing
     */
    @Deprecated
    public int update(String update){
    	if(!this.isConnected()) {
    		this.connect(this.host, this.username, this.password, this.database, this.port);
    	}
    	try{
            PreparedStatement statement = getConnection().prepareStatement(update);
            return statement.executeUpdate();
        }catch (SQLException e){
        	try{
                PreparedStatement statement = getConnection().prepareStatement(update);
                return statement.executeUpdate();
            }catch (SQLException e1){
            	Xelt.getInstance().getLogger().warn("MySQL update error");
                return 0;
            }
        }
    }
    
    /**
     * 
     * @param statement
     * 
     * For sending query updates
     */
    public int update(PreparedStatement statement){
    	if(!this.isConnected()) {
    		this.connect(this.host, this.username, this.password, this.database, this.port);
    	}
        try {
			return statement.executeUpdate();
		} catch (SQLException e) {
			try {
				return statement.executeUpdate();
			} catch (SQLException e1) {
	        	Xelt.getInstance().getLogger().warn("MySQL update error");
				return 0;
			}
		}
    }

    /**
     * @param query
     *
     * Only to be used for quick queries which do not require query parsing
     */
    @Deprecated
    public ResultSet query(String query){
    	if(!this.isConnected()) {
    		this.connect(this.host, this.username, this.password, this.database, this.port);
    	}
    	try{
            PreparedStatement statement = getConnection().prepareStatement(query);
            return statement.executeQuery();
        }catch (SQLException e){
        	Xelt.getInstance().getLogger().warn("MySQL query error");
        	try{
                PreparedStatement statement = getConnection().prepareStatement(query);
                return statement.executeQuery();
            }catch (SQLException e1){
            	Xelt.getInstance().getLogger().warn("MySQL query error");
                return null;
            }
        }
    }
    
    /**
     * 
     * @param statement
     * 
     * For sending queries
     */
    public ResultSet query(PreparedStatement statement){
    	if(!this.isConnected()) {
    		this.connect(this.host, this.username, this.password, this.database, this.port);
    	}
        try {
			return statement.executeQuery();
		} catch (SQLException e) {
			try {
				return statement.executeQuery();
			} catch (SQLException e1) {
	        	Xelt.getInstance().getLogger().warn("MySQL query error");
				return null;
			}
		}
    }

    public void createTable(String table, String column){
        try {
            PreparedStatement statement = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " (" + column + ");");
            this.update(statement);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean tableExists(String table){
        try{
            DatabaseMetaData databaseMetaData = getConnection().getMetaData();
            ResultSet rs = databaseMetaData.getTables(null, null, table, null);
            if(rs.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    public boolean columnExists(String column, String table) {
        try {
            DatabaseMetaData md = getConnection().getMetaData();
            ResultSet rs = md.getColumns(null, null, table, column);
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean guildExists(String id) {
    	try{
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM Guilds WHERE ID=?");
            statement.setString(1, id);
            ResultSet rs = this.query(statement);
            if(rs.next()){
                return true;
            }
            return false;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect(){
        this.connnection = null;
    }

    public void setConnection(Connection connnection){
        this.connnection = connnection;
    }

    public Connection getConnection(){
        return connnection;
    }

    public boolean isConnected() {
        try {
            if (getConnection() != null && !getConnection().isClosed())
                return true;
            else
                return false;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}
