package org.example;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class StringUtil {


    //Applies ECDSA Signature and returns the result ( as bytes ) .
    public static byte[] applyECDASig(PrivateKey privateKey, String input){
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes(StandardCharsets.UTF_8);
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException | InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    //Verifies a String signature

    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(data.getBytes());
        return ecdsaVerify.verify(signature);


    }

    public static String getStringFromKey(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }



    //Applies sha256 to a string and returns the result.

    public static String applySha256(String input){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();

            for(int i=0; i<hash.length; i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }



}
