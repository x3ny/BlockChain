package org.example;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public PublicKey publicKey;
    public PrivateKey privateKey;

    public HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

    public Wallet(PublicKey publicKey, PrivateKey privateKey){
        this.publicKey = publicKey;
        this.privateKey = privateKey;

    }

    public void generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        try{
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec espec = new ECGenParameterSpec("prime192v1");

            //Initialize the key generator and generate a KeyPair
            keyGen.initialize(espec, random);  //256 bytes provides an acceptable security level
            KeyPair keyPair = keyGen.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        }
        catch (Exception e){
            throw new RuntimeException();
        }

    }

    // Generates and returns a new transaction from this wallet.

    public float getBalance(){
        float total = 0;
        for(Map.Entry<String, TransactionOutput> item : Main.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();

            if(UTXO.isMine(publicKey)){  // if coins belong to me
                UTXOs.put(UTXO.id, UTXO); // add it to our list of unsoent transactions
                total += UTXO.value;
            }
        }

        return total;
    }

    // Generates and returns a new transaction from this wallet
    public Transaction sendFunds(PublicKey _recipient, float value) throws NoSuchAlgorithmException, InvalidKeyException {
        if(getBalance() < value){
            System.out.println("#Not enough funds to send transaction. Transaction discarded. ");
            return null;
        }
        // Create an arrayList of inputs

        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0;
        for(Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            total+= UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if(total > value){break;}
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput input : inputs){
            UTXOs.remove(input.transactionOutputId);
        }

        return newTransaction;
    }
}
