import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentVerifierBuilderProvider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.Security;

public class Verify3 {
    private static String readFile(String file)  {
        try{
            BufferedReader reader = new BufferedReader(new FileReader (file));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");

            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            reader.close();    
            return stringBuilder.toString();
        }catch( Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean verify(String data) {

        Security.addProvider(new BouncyCastleProvider());
        try {
            String publicKeyFilePath = "public.asc"; // Path to the public key
            String signaturePath = "output.txt.sig"; // Path to the signature file
            String signedData = readFile(signaturePath);
            InputStream publicKeyStream = new FileInputStream(publicKeyFilePath);
            InputStream signatureStream = new ByteArrayInputStream(signedData.getBytes());

            PGPObjectFactory pgpFactory = new PGPObjectFactory(PGPUtil.getDecoderStream(publicKeyStream), new BcKeyFingerprintCalculator());
		    PGPSecretKeyRing keyRing = ( PGPSecretKeyRing ) pgpFactory.nextObject();
			PGPSecretKey pgpKey = keyRing.getSecretKey();
            PGPPublicKey publicKey = pgpKey.getPublicKey();

            // Create the signature object
            PGPObjectFactory pgpObjectFactory = new PGPObjectFactory(PGPUtil.getDecoderStream(signatureStream), new BcKeyFingerprintCalculator());
            PGPSignatureList signatureList = (PGPSignatureList) pgpObjectFactory.nextObject();
            PGPSignature signature = signatureList.get(0);

            signature.init(new BcPGPContentVerifierBuilderProvider(), publicKey);
            signature.update(data.getBytes());

            if(signature.verify()){
                return true;
            }
            return false;

        } catch (PGPException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}