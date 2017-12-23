package models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordGenerator {

    /**
     * this method creates a encripted password
     * @param passwordToEncypt
     * @param salt
     * @return
     */
    public static String getSHA512Password(String passwordToEncypt, byte[] salt)
    {
        String generatedPassword = null;

        try
        {
            //tells what algorithm we are using
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            //converts password into a string of bytes
            byte[] bytes = md.digest(passwordToEncypt.getBytes());

            StringBuilder sb = new StringBuilder();

            //for each byte in our array of bytes convert them back to a string
            for(int i = 0; i < bytes.length; i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }

        return generatedPassword;
    }

    /**
     * generates the salt
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstanceStrong();

        byte[] salt = new byte[16];
        sr.nextBytes(salt);

        return salt;
    }
}
