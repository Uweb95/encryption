package de.uweb95;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

public class Encryption {
    /**
     * @param inputFile
     * @param password
     * @throws Exception
     */
    public void encryptFile(File inputFile, String password) throws Exception {
        File encryptedFile = new File(inputFile.getName() + ".enc");

        if (encryptedFile.exists()) {
            System.out.println("File " + encryptedFile.getName() + " already exists.");
            System.exit(1);
        }

        encrypt(inputFile, encryptedFile, password);
        System.out.println("File " + encryptedFile.getName() + " successfully encrypted.");
    }

    /**
     * @param inputFile
     * @param password
     * @throws Exception
     */
    public void decryptFile(File inputFile, String password) throws Exception {
        File encryptedFile = new File(inputFile.getName());
        File decryptedFile = new File(inputFile.getName().replace(".enc", ""));

        if (decryptedFile.exists()) {
            System.out.println("File " + decryptedFile.getName() + " already exists.");
            System.exit(1);
        }

        decrypt(encryptedFile, decryptedFile, password);
        System.out.println("File " + decryptedFile.getName() + " successfully decrypted.");
    }


    /**
     * @param inputFile
     * @param outputFile
     * @param password
     * @throws Exception
     */
    private void encrypt(File inputFile, File outputFile, String password) throws Exception {
        runEncryption(Cipher.ENCRYPT_MODE, inputFile, outputFile, password);
    }

    /**
     * @param inputFile
     * @param outputFile
     * @param password
     * @throws Exception
     */
    private void decrypt(File inputFile, File outputFile, String password) throws Exception {
        runEncryption(Cipher.DECRYPT_MODE, inputFile, outputFile, password);
    }

    /**
     * @param cipherMode
     * @param inputFile
     * @param outputFile
     * @param password
     * @throws Exception
     */
    private static void runEncryption(int cipherMode, File inputFile, File outputFile, String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] key = digest.digest(password.getBytes("UTF-8"));
        key = Arrays.copyOf(key, 16);

        Key secretKey = new SecretKeySpec(key, "AES");

        byte[] ivBytes = new byte[16];
        Arrays.fill(ivBytes, (byte) 0x00);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(cipherMode, secretKey, ivSpec);

        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            outputStream.write(outputBytes);
        } catch (Exception e) {
            System.out.println("Seems like the password is wrong.");
            outputFile.delete();
            System.exit(1);
        }
    }
}
