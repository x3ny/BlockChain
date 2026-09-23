package org.example;
import com.google.gson.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static ArrayList<Block> blockChain = new ArrayList<Block>();
    public static int difficulty = 5;
    public static Wallet walletA;
    public static Wallet walletB;


    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException {

        //Secured by BouncyCastle
        Security.addProvider(new BouncyCastleProvider());
        walletA = new Wallet();
        walletB = new Wallet();

        walletA.generateKeyPair();
        walletB.generateKeyPair();

        System.out.println("Private and public keys");
        System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println(StringUtil.getStringFromKey(walletA.publicKey));

        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5);
        transaction.generateSignature(walletA.privateKey);

        System.out.println("Is signature verified");
        System.out.println(transaction.verifySignature());



    }

    public static Boolean isChainValid(){
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for(int i = 1; i < blockChain.size(); i++){
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i-1);

            if(!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.print("Current hashes not equal;");
                return false;
            }

            if(!previousBlock.hash.equals(currentBlock.previousHash)){
                System.out.println("Previous hashes not equal;");
                return false;
            }


            //Check if hash is solved
            if(!currentBlock.hash.substring(0,difficulty).equals(hashTarget)){
                System.out.println("This block hasn't been mined;");
                return false;
            }
        }

        return true;
    }
}