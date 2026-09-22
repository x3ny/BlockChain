package org.example;
import com.google.gson.*;

import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static ArrayList<Block> blockChain = new ArrayList<Block>();

    public static int difficulty = 5;


    public static void main(String[] args) {

        blockChain.add(new Block("Hi i am the first block", "0"));
        System.out.println("Trying to mine block 1...");
        blockChain.get(0).mineBlock(difficulty);


        blockChain.add(new Block("Hi i am the second block", blockChain.get(blockChain.size()-1).hash));
        System.out.println("Trying to mine block 2...");
        blockChain.get(1).mineBlock(difficulty);

        blockChain.add(new Block("Hi i am the third block", blockChain.get(blockChain.size()-1).hash ));
        System.out.println("Trying to mine block 3...");
        blockChain.get(2).mineBlock(difficulty);

        blockChain.add(new Block("The forth block is here", blockChain.get(blockChain.size()-1).hash));
        System.out.println("Trying to mine block 4...");
        blockChain.get(3).mineBlock(difficulty);

        System.out.println("\nBlockchain is Valid: " +  isChainValid());

        String blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockChainJson);




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