package com.aptatek.aptatek.domain.manager;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.inject.Inject;
import javax.security.auth.x500.X500Principal;

import timber.log.Timber;

public class KeyStoreManager {

    private static final String ALIAS = "user_key";

    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";


    private KeyStore keyStore;
    private Context context;

    @Inject
    public KeyStoreManager(@ApplicationContext final Context context) {
        this.context = context;
        initKeyStore();
    }

    private void initKeyStore() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load AndroidKeyStore", e);
        }
    }

    public boolean aliasExists() {
        try {
            return keyStore.containsAlias(ALIAS);
        } catch (KeyStoreException e) {
            Timber.e("Error while checking keystore aliases %s", e.getMessage());
            return false;
        }
    }

    public void deleteKeyStore() {
        try {
            keyStore.deleteEntry(ALIAS);
        } catch (KeyStoreException e) {
            Timber.e("Error while deleting keystore aliases %s", e.getMessage());
        }
    }

    public String encrypt(PinCode pinCode) {
        try {
            createNewKeys();
            final KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(ALIAS, null);
            final RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

            final Cipher cipher = Cipher.getInstance(TRANSFORMATION, "AndroidOpenSSL");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);
            cipherOutputStream.write(pinCode.getBytes());
            cipherOutputStream.close();
            final byte[] vals = outputStream.toByteArray();
            return Base64.encodeToString(vals, Base64.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException("Error during encryption", e);
        }
    }

    public PinCode decrypt(String encryptedData) {
        try {
            final KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(ALIAS, null);
            final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

            final CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(encryptedData, Base64.DEFAULT)), cipher);
            final ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            final byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i);
            }
            cipherInputStream.close();
            return new PinCode(bytes);
        } catch (Exception e) {
            Timber.e("Error during decrypting %s", e.getMessage());
        }
        return null;
    }

    private void createNewKeys() {
        try {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 10);
            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(ALIAS)
                    .setKeySize(2048)
                    .setSubject(new X500Principal("CN=Aptatek, O=Apatetek Android"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
            generator.initialize(spec);
            generator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Error while creating new keys", e);
        }
    }
}
