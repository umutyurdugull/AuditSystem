       IDENTIFICATION DIVISION.
       PROGRAM-ID. BANKTX.
       AUTHOR. UMUT-YURDUGUL.

       ENVIRONMENT DIVISION. 
       INPUT-OUTPUT SECTION. 
       FILE-CONTROL.
           SELECT ACCTREC ASSIGN TO ACCTREC.
           SELECT BANKOUT ASSIGN TO BANKOUT.

       DATA DIVISION. 
       FILE SECTION. 
       FD  ACCTREC.
           COPY BANKREC.

       FD  BANKOUT.
           COPY BANKOUT.

       WORKING-STORAGE SECTION.
       01  WS-FLAGS.
           05 LASTREC           PIC X VALUE 'N'.
       01  WS-COUNTERS.
           05 TOTAL-COUNT       PIC 9(4) VALUE ZERO.
           05 FLAGGED-COUNT     PIC 9(4) VALUE ZERO.

       PROCEDURE DIVISION.
       MAIN-PROCEDURE.
           OPEN INPUT  ACCTREC.
           OPEN OUTPUT BANKOUT.
           
           PERFORM PROCESS-RECORD UNTIL LASTREC = 'Y'.
           
           CLOSE ACCTREC
                 BANKOUT.
                 
           DISPLAY "TOTAL RECORDS PROCESSED: " TOTAL-COUNT.
           DISPLAY "FLAGGED RECORDS: " FLAGGED-COUNT.
           GOBACK.

       PROCESS-RECORD.
           READ ACCTREC
               AT END MOVE 'Y' TO LASTREC
               NOT AT END
                   ADD 1 TO TOTAL-COUNT
                   MOVE ACCT-NO TO OUT-ACCT-NO
                   MOVE LAST-NAME TO OUT-LAST-NAME
                   MOVE TX-AMOUNT TO OUT-TX-AMOUNT
                   MOVE TX-TYPE TO OUT-TX-TYPE
                   IF TX-TYPE = 'DR' AND TX-AMOUNT > 10000.00
                       MOVE 'FLAGGED   ' TO OUT-STATUS-FLAG
                       ADD 1 TO FLAGGED-COUNT
                   ELSE
                       MOVE 'NORMAL    ' TO OUT-STATUS-FLAG
                   END-IF
                   WRITE BANK-OUT-RECORD
           END-READ.
