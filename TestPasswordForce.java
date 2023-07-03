import java.io.IOException;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HexFormat;

public class TestPasswordForce extends Thread {

    private String hash;
    private String salt;
    private int min;
    private int max;

    public TestPasswordForce(String hash, String salt, int min, int max) {
        this.hash = hash;
        this.salt = salt;
        this.min = min;
        this.max = max;
    }

    
    
    @Override
    public void run() {
        public static String forcePwd()
     {
            
            String pwd = "";
            // char ch[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            // 'm', 'n', 'o', 'p', 'q', 'r', 's',
            // 't', 'u', 'v', 'w', 'x', 'y', 'z' };
            for (int a = this.min; a < this.max; a++) {// ch.length
                System.out.println((char) ('a' + a));
                for (int b = 0; b < 26; b++) {
                    for (int c = 0; c < 26; c++) {
                        for (int d = 0; d < 26; d++) {
                            for (int e = 0; e < 26; e++) {
                                for (int f = 0; f < 26; f++) {
                                    // pwd = new String(ch, a, 1) + new String(ch, b, 1) + new String(ch, c, 1)
                                    // + new String(ch, d, 1) + new String(ch, e, 1) + new String(ch, f, 1);
                                    
                                    pwd = "" + (char) ('a' + a) + (char) ('a' + b) + (char) ('a' + c)
                                    + (char) ('a' + d) + (char) ('a' + e) + (char) ('a' + f);
                                    
                                    String myHash = hashPass(pwd, salt);
                                    if (myHash.equals(hash)) {
                                        System.out.println("Match Found" + pwd);
                                        
                                    }
                                    
                                }
                            }
                        }
                    }
                }
            }
            if (pwd == "") {
                
            }
            return pwd;
        }
    }

    private static String hashPass(String password, String salt) throws IOException, NoSuchAlgorithmException {
        HexFormat hexFormat = HexFormat.of();
        byte[] saltBytes = hexFormat.parseHex(salt);
        byte[] pwdBytes = password.getBytes();

        byte[] saltPwdToHash = Arrays.copyOf(saltBytes, saltBytes.length + pwdBytes.length);
        System.arraycopy(pwdBytes, 0, saltPwdToHash, saltBytes.length, pwdBytes.length);

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] digest = md.digest(saltPwdToHash);

        return hexFormat.formatHex(digest);
    }
}
