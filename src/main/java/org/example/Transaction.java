package org.example;

import java.security.*;
import java.util.ArrayList;

public class Transaction {

    public String transactionId; // hash of the transaction
    public PublicKey sender;
    public PublicKey recipient;
    public float value;
    public byte[] signature;

    //public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    //public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequenceNumber = 0; // rough count of how many transactions have been generated

    public Transaction(PublicKey from, PublicKey to, float value) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        //this.inputs = inputs;

    }

    private String calculateHash() {
        sequenceNumber++;
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                StringUtil.getStringFromKey(recipient) + value + sequenceNumber
        );
    }


    //Signs all the data we dont wish to be tampered with
    public void generateSignature(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
        signature = StringUtil.applyECDASig(privateKey, data);
    }

    public boolean verifySignature() throws NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + value;
        return StringUtil.verifyECDSASig(sender, data, signature);
    }



}
