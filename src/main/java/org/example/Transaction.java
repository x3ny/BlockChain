package org.example;

import java.security.*;
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

    public boolean processTransaction() throws NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
        if(!verifySignature()){
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        //Gather transaction inputs (Make sure they are unspent)

        for(TransactionInput input : inputs){
            input.UTXO = Main.UTXOs.get(input.transactionOutputId);
        }

        //check if the transaction is valid

        if(getInputsValue() < Main.minimumTransaction){
            System.out.println("#Transaction input value too low");
            return false;
        }

        //generate transaction outputs:

        float leftOver = getInputsValue() - value;
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.recipient, value, this.transactionId));
        outputs.add(new TransactionOutput(this.sender, leftOver, this.transactionId));

        for(TransactionOutput output : outputs){
            Main.UTXOs.put(output.id,output);
        }

        //remove transaction inputs from UTXO lists as spent:

        for(TransactionInput input : inputs){
            if(input.UTXO == null){
                continue;
            }
            Main.UTXOs.remove(input.UTXO.id);

        }

        return true;

    }

    public float getInputsValue(){
        float total = 0;
        for(TransactionInput input : inputs){
            if(input.UTXO == null){continue;}
            total += input.UTXO.value;
        }
        return total;
    }

    public float getOutputsValue(){
        float total = 0;
        for(TransactionOutput output : outputs){
            total += output.value;
        }
        return total;
    }



}
