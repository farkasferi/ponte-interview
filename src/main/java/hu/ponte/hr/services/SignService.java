package hu.ponte.hr.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Service
public class SignService {

    @Value("${ponte.private-key.location}")
    private String filename;

    public String sign(byte[] data) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");

            PrivateKey privateKey = getPrivateKey();
            signature.initSign(privateKey);

            signature.update(data);
            byte[] digitalSignature = signature.sign();

            return Base64.getEncoder().encodeToString(digitalSignature);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Server error! Failed to sign image.");
        }
    }

    private PrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(ResourceUtils.getFile(filename).toPath());

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
}
