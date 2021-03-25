/* Creating User inside the PDB 
   Login as sys before running the commands */

show pdbs
ALTER SESSION SET container = SPMSDB_PDB1;
CREATE USER admin IDENTIFIED BY zT7_P53Ia1_A;
GRANT CONNECT, RESOURCE, DBA TO admin;
GRANT CREATE SESSION TO admin;
GRANT UNLIMITED TABLESPACE TO admin;

/* 

Connecting to CDB
SPMSDB_iad1mp.sub12140609560.vcnsrpublic.oraclevcn.com
sys as sysdba/<password>

Connecting to PDB
SPMSDB_PDB1.sub12140609560.vcnsrpublic.oraclevcn.com
admin/<password>

 */
