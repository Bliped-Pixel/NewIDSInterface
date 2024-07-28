import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/IDS")
public class Test extends HttpServlet{

    
    private static KeyGenerator keyGenerator;
    private static SecretKey secretKey;
    static{
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        keyGenerator.init(256); // Key size
        secretKey = keyGenerator.generateKey();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException{
        doPost(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException{

    }

    public static String encryptAES(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData =  cipher.doFinal(data.getBytes());
        return new String(encryptedData);
    }

    public static String decryptAES(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData =  cipher.doFinal(encryptedData.getBytes());
        return new String(decryptedData);
    }
    public static void main(String[] args) {
        System.out.println("\\");
    }
}
