package com.revolut.task;

import com.revolut.task.model.Account;
import com.revolut.task.exception.AccountNotFoundException;
import org.h2.tools.RunScript;

import java.io.FileReader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AccountRepository {

    //Using array as datastore for the sake of simplicity
    private List<Account> accounts = new ArrayList<>();
    private Connection connection;

    public AccountRepository() {
        String url = "jdbc:h2:mem:";
        try {
            this.connection = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(AccountRepository.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        try {
            URL path = getClass().getClassLoader().getResource("init.sql");
            RunScript.execute(this.connection, new FileReader(path.getFile()));
        } catch (Exception ex) {
            Logger lgr = Logger.getLogger(AccountRepository.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public Account getAccount(String id) throws SQLException {
        //return accounts.stream().filter(a -> a.getId().equals(id)).findFirst().orElseThrow(AccountNotFoundException::new);
        Statement st = this.connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM accounts WHERE number = " + id);
        rs.stream()
    }
    
    public Account createAccount() {
        Account acc = new Account();
        accounts.add(acc);
        return acc;
    }

    public Account deleteAccount(String id) {
        Account acc = accounts.stream().filter(a -> a.getId().equals(id)).findFirst().orElseThrow(AccountNotFoundException::new);
        accounts.remove(acc);
        return acc;
    }

    public Account updateAccount(String id, BigDecimal newAmount) {
        Account acc = accounts.stream().filter(a -> a.getId().equals(id)).findFirst().orElseThrow(AccountNotFoundException::new);
        acc.setBalance(newAmount);
        return acc;
    }
}