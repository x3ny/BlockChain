package org.example;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {

    public String transactionId; // hash of the transaction
    public PublicKey sender;
    public PublicKey recipient;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequenceNumber = 0; // rough count of how many transactions have been generated

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;

    }

    private String calculateHash() {
        sequenceNumber++;
        return StringUtil.applySha256(StringUtil.applySha256(String.valueOf(sender)) +
                StringUtil.applySha256(String.valueOf(recipient)) +
                Float.toString(value) + sequenceNumber);
    }



}
