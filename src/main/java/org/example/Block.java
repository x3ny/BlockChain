package org.example;

import java.security.*;
import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); // The data is gonna be a simple message
    private String data;
    private long timeStamp;
    private int nonce;

    public Block(String previousHash){
        this.previousHash = previousHash;
        this.timeStamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }

    public String calculateHash(){

        String calculatedHash = StringUtil.applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data);

        return calculatedHash;
    }

    public void mineBlock(int difficulty){

        merkleRoot = StringUtil.getMerkleRoot(transactions);

        String target = new String(new char[difficulty]).replace('\0', '0');
        while(!hash.substring(0, difficulty).equals(target)){
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined! :" + hash);
    }

    // Add transactions to this block

    public boolean addTransaction(Transaction transaction) throws NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
        //process transaction and check if valid, unless block is genesis block then ignore
        if(transaction == null) return false;

        if((previousHash != "0")){
          if((!transaction.processTransaction())){
              System.out.println("Transaction failed to process. Discarded. ");
              return false;
          }
        }
        transactions.add(transaction);
        System.out.println("Transaction successfully added to block.");
        return true;
    }
}
