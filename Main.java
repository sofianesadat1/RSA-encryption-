package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.crypto.*;

public class Main {

    public static File saveInFile(String text, String path) throws IOException {
        File f = new File(path + "message.txt");
        f.createNewFile();
        byte[] byte_code = text.getBytes();
        FileOutputStream fos = new FileOutputStream(path + "message.txt");
        fos.write(byte_code);
        fos.close();
        return f;
    }



    public static void savePublicKey(PublicKey key, String path) throws IOException {
        File file = new File(path + "public.key");
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(key);
    }
    public static void savePrivateKey(PrivateKey key, String path) throws IOException {
        File file = new File(path + "private.key");
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(key);
    }

    public static String readFile(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path + "message.txt");
        byte[] octet = new byte[1];
        ArrayList<String> message = new ArrayList<String>();
        while (fis.read(octet) >= 0) {
//            System.out.println(octet[0]);
            String binaryString = Integer.toBinaryString(octet[0]);
//            System.out.println(binaryString);
            message.add(binaryString);


        }
        String mot = "";
        for (int i = 0; i < message.size(); i++) {
            int charCode = Integer.parseInt(message.get(i), 2);
            String str = new Character((char) charCode).toString();
//            System.out.println(str);
            mot = mot + str;
        }
//        System.out.println(mot);
        return (mot);

    }

    public static PrivateKey readPriKey(String path) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(path + "private.key");
        ObjectInputStream ois = new ObjectInputStream(fis);
        PrivateKey key = (PrivateKey) ois.readObject();
        return (key);
    }

    public static PublicKey readPubKey(String path) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(path + "public.key");
        ObjectInputStream ois = new ObjectInputStream(fis);
        PublicKey key = (PublicKey) ois.readObject();
        return (key);
    }


    public static byte[] encrypt(String text, PublicKey key, String path) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] text_bytes = text.getBytes();
        byte[] cryptogramme_bytes;
        cryptogramme_bytes = c.doFinal(text_bytes);
        FileOutputStream fos = new FileOutputStream(path + "cryptogramme.txt");
        fos.write(cryptogramme_bytes);
        fos.close();
        return (cryptogramme_bytes);
    }

    public static byte[] decrypt(PrivateKey secretkey, byte[] cryptogramme_bytes, String path) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        Cipher c = Cipher.getInstance("RSA");
        c.init(Cipher.DECRYPT_MODE, secretkey);
        byte[] text_bytes;
        text_bytes = c.doFinal(cryptogramme_bytes);
        FileOutputStream fos = new FileOutputStream(path + "messageEnClair.txt");
        fos.write(text_bytes);
        fos.close();
        return (text_bytes);


    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, ClassNotFoundException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        String path = "desktop/";

        String text = "mot de passe : algeriealgerie";

        saveInFile(text, path);

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair key = keyGen.generateKeyPair();
        PublicKey publicKey= key.getPublic();
        PrivateKey privateKey =key.getPrivate();

        savePrivateKey(privateKey, path);
        savePublicKey(publicKey, path);

        publicKey = readPubKey(path);
        privateKey = readPriKey(path);

        byte[] code = encrypt(readFile(path), publicKey, path);

        byte[] array = decrypt(privateKey, code, path);
    }
}
