package com.ibm.jzos.sample;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.ibm.jzos.ZFile;
import com.ibm.jzos.ZFileException;
import com.ibm.jzos.FileFactory;
import com.ibm.jzos.MvsJobSubmitter;
import com.ibm.jzos.PdsDirectory;
import com.ibm.jzos.ZFileConstants;



public class AuditEngine {
    private static final String DB2_URL = "jdbc:db2://204.90.115.200:5040/ZXPDB2";
    private static final String DB2_USER = "Z88116";
    private static final String DB_PASSWORD = "";
    private static final String COBOL_OUTPUT_DS = "//'Z88116.BANK.OUTPUT'";

    public static void main(String[] args) {

       try {
           List<TransactionRecord> records = readCobolOutputDataset();

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
}