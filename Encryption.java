import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;

class Encryption
{
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 12;
    private static final int KEY_LENGTH = 256;

    private static SecretKey generateKey(String Password, byte Salt[]) throws Exception
    {
        PBEKeySpec Spec = new PBEKeySpec(
                Password.toCharArray(),
                Salt,
                65536,
                KEY_LENGTH
        );

        SecretKeyFactory Factory =
                SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte KeyBytes[] = Factory.generateSecret(Spec).getEncoded();

        return new SecretKeySpec(KeyBytes, "AES");
    }

    public static void encryptFile(
            String InputFile,
            String OutputFile,
            String Password) throws Exception
    {
        SecureRandom Random = new SecureRandom();

        byte Salt[] = new byte[SALT_LENGTH];
        byte IV[] = new byte[IV_LENGTH];

        Random.nextBytes(Salt);
        Random.nextBytes(IV);

        SecretKey Key = generateKey(Password, Salt);

        Cipher CipherObj = Cipher.getInstance("AES/GCM/NoPadding");

        GCMParameterSpec GCM =
                new GCMParameterSpec(128, IV);

        CipherObj.init(
                Cipher.ENCRYPT_MODE,
                Key,
                GCM
        );

        FileInputStream fiobj =
                new FileInputStream(InputFile);

        FileOutputStream foobj =
                new FileOutputStream(OutputFile);

        // Store Salt and IV at beginning of encrypted file
        foobj.write(Salt);
        foobj.write(IV);

        CipherOutputStream coobj =
                new CipherOutputStream(foobj, CipherObj);

        byte Buffer[] = new byte[1024];

        int iRet = 0;

        while((iRet = fiobj.read(Buffer)) != -1)
        {
            coobj.write(Buffer, 0, iRet);
        }

        fiobj.close();
        coobj.close();
    }

    public static void decryptFile(
            String InputFile,
            String OutputFile,
            String Password) throws Exception
    {
        FileInputStream fiobj =
                new FileInputStream(InputFile);

        byte Salt[] = new byte[SALT_LENGTH];
        byte IV[] = new byte[IV_LENGTH];

        int SaltRead = fiobj.read(Salt);

        if(SaltRead != SALT_LENGTH)
        {
            fiobj.close();
            throw new IOException("Invalid encrypted file");
        }

        int IVRead = fiobj.read(IV);

        if(IVRead != IV_LENGTH)
        {
            fiobj.close();
            throw new IOException("Invalid encrypted file");
        }

        SecretKey Key = generateKey(Password, Salt);

        Cipher CipherObj =
                Cipher.getInstance("AES/GCM/NoPadding");

        GCMParameterSpec GCM =
                new GCMParameterSpec(128, IV);

        CipherObj.init(
                Cipher.DECRYPT_MODE,
                Key,
                GCM
        );

        CipherInputStream ciobj =
                new CipherInputStream(fiobj, CipherObj);

        FileOutputStream foobj =
                new FileOutputStream(OutputFile);

        byte Buffer[] = new byte[1024];

        int iRet = 0;

        while((iRet = ciobj.read(Buffer)) != -1)
        {
            foobj.write(Buffer, 0, iRet);
        }

        ciobj.close();
        foobj.close();
    }
}