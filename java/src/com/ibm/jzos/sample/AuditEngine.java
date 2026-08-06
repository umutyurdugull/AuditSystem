package com.ibm.jzos.sample;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.ibm.jzos.FileFactory;


public class AuditEngine {
    private static final String DB2_URL = "jdbc:db2://204.90.115.200:5040/ZXPDB2";
    private static final String DB2_USER = "Z88116";
    private static final String DB2_PASSWORD = "RHA95XVB";
    private static final String COBOL_OUTPUT_DS = "//'Z88116.BANK.OUTPUT'";

    public static void main(String[] args) {

        try {
            List<TransactionRecord> records = readCobolOutputDataset();
            System.out.println(records.toString());
            System.out.println("---");
            insertIntoDb2();

        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }

    }

    private static List<TransactionRecord> readCobolOutputDataset() throws IOException {
        List<TransactionRecord> records = new ArrayList<>();
        try(BufferedReader reader = FileFactory.newBufferedReader(COBOL_OUTPUT_DS)){
            String line;
            while((line = reader.readLine()) != null){
                if(line.length() > 50){
                    String accountId = line.substring(0, 8).trim();
                    String lastName = line.substring(10, 30).trim();
                    String amountStr = line.substring(32, 44).trim().replace(",", "");
                    String txType = line.substring(46, 48).trim();
                    String status = line.substring(50, Math.min(line.length(), 60)).trim();
                    double amount = Double.parseDouble(amountStr);
                    records.add(new TransactionRecord(accountId,lastName,amount,txType,status));
                }
            }
        }
        return records;
    }
    private static void insertIntoDb2() throws SQLException{


        // TO TRY DB2, it's a dummy code i'll rewrite it when I need it.
        String query = "SELECT * FROM Z88116.BANK_TRANSACTIONS";
        try(Connection connection = DriverManager.getConnection(DB2_URL,DB2_USER, DB2_PASSWORD)){
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet results = statement.executeQuery();
            while (results.next()){
                String accId    = results.getString("ACCOUNT_ID");
                String custName = results.getString("CUSTOMER_NAME");
                double amount = results.getDouble("TX_AMOUNT");
                String txType = results.getString("TX_TYPE");
                Date txDate     = results.getDate("TX_DATE");
                String status = results.getString("STATUS");

                System.out.println("Account: " + accId +
                        " | Customer: " + custName +
                        " | Amount: " + amount +
                        " | Type: " + txType +
                        " | Date: " + txDate +
                        " | Status: " + status);
                System.out.println(custName);
            }

        }
    }
}