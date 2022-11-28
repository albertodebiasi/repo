package salt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class hashing {
	
	  public static void main(String[] args) throws Exception {
	        String password = "1234";
			String algorithm = "MD5";
			byte[] salt = "98F67C40D9074294E25D7B76F3145D9B".getBytes("utf-8");
			System.out.println("hash= " + generateHash(password, algorithm, salt));
			
	  }

	  public static String generateHash(String data, String algorithm, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		  MessageDigest digest =  MessageDigest.getInstance(algorithm);
		  				digest.reset();
		  				digest.update(salt);
		  
		  byte[] hash = digest.digest(data.getBytes());
		  return bytesToStringHex(hash);
		  
	  }

	  private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	  public static String bytesToStringHex(byte[] bytes) {
	  
		    char[] hexChars = new char[bytes.length * 2];
		   
		    for (int j = 0; j < bytes.length; j++) {
		    		
		    	int v = bytes[j] & 0xFF;
		    	
			      hexChars[j * 2] = hexArray[v >>> 4];
			      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
      
		    }
		    
	   return new String(hexChars);
    
	  }
	public static byte[] createSalt(){
		byte[] bytes = new byte[16];
		SecureRandom random = new SecureRandom();
		random.nextBytes(bytes);
		return bytes;
	}
}