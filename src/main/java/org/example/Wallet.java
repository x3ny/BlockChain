package org.example;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
    public PublicKey publicKey;
    public PrivateKey privateKey;

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
}
