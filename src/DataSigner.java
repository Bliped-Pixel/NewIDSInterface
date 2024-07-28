import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPObjectFactory;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
public class DataSigner {
	private static PGPSignatureGenerator createSignatureGenerator(PGPSecretKey secretKey) throws PGPException {
		JcePBESecretKeyDecryptorBuilder decryptorBuilder = new JcePBESecretKeyDecryptorBuilder();
		PBESecretKeyDecryptor decryptor = decryptorBuilder.build("Pass@123".toCharArray());
		PGPSignatureGenerator signatureGenerator = new PGPSignatureGenerator(
                					    new BcPGPContentSignerBuilder(secretKey.getPublicKey()
						       						        				   .getAlgorithm(), 
					      						                      PGPUtil.SHA256));

		signatureGenerator.init(PGPSignature.BINARY_DOCUMENT,
		        	        secretKey.extractPrivateKey(decryptor));

		return signatureGenerator;
	}
	
	private static byte[] signData(PGPSignatureGenerator signatureGenerator, String data) throws Exception{
		byte[] dataAsBytes = data.getBytes("UTF-8");
		signatureGenerator.update(dataAsBytes);
		PGPSignature signature = signatureGenerator.generate();

	    ByteArrayOutputStream signatureOut = new ByteArrayOutputStream();
        try (ArmoredOutputStream armoredOut = new ArmoredOutputStream(signatureOut)) {
           		signature.encode(armoredOut);
			}
			return signatureOut.toByteArray();
	}
	public static void main(String[] args) throws Exception{
		DataSigner.sign("same data");
	}

	static void sign(String data) throws Exception {

		Security.addProvider(new BouncyCastleProvider());
		String privateKeyFilePath = "private.asc"; 
		PGPSecretKeyRing keyRing;

		InputStream inputStream = new BufferedInputStream(new FileInputStream(privateKeyFilePath)); // read private key file
		PGPObjectFactory pgpFactory = new PGPObjectFactory(PGPUtil.getDecoderStream(inputStream),
															new BcKeyFingerprintCalculator());  // decode it with in pgp format
		Object obj = pgpFactory.nextObject();
		keyRing = ( PGPSecretKeyRing ) obj;
		PGPSecretKey pgpKey = keyRing.getSecretKey();
		
		PGPSignatureGenerator signatureGenerator = createSignatureGenerator(pgpKey);
		byte[] signature = signData(signatureGenerator, data);
		for(byte out: signature)
			System.out.print(String.format("%02X", out));
			try (FileOutputStream fos = new FileOutputStream("output.txt.sig")){	// save output in output.txt.sig
				fos.write(signature);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}

