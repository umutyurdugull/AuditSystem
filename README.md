# Mainframe Audit System

A hybrid enterprise application demonstrating end-to-end z/OS integration.

## Workflow
1. **COBOL Batch (`BANKTX.cbl`)**: Processes transaction datasets (`Z88116.BANK.INPUT`) and flags high-value debit operations in `Z88116.BANK.OUTPUT`.
2. **Java Engine (`BankAuditEngine.java`)**: Reads output via JZOS, inserts records into Db2 table `Z88116.BANK_TRANSACTIONS`, and triggers JCL alerts.