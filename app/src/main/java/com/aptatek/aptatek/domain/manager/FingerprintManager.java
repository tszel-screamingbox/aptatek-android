package com.aptatek.aptatek.domain.manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.inject.Inject;


public class FingerprintManager {

    private static final String KEY_NAME = "encryption_key";

    private final FingerprintManagerCompat fingerprintManager;
    private KeyStore keyStore;

    @Inject
    FingerprintManager(@ApplicationContext Context context) {
        this.fingerprintManager = FingerprintManagerCompat.from(context);
    }

    public boolean hasEnrolledFingerprints() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && fingerprintManager.hasEnrolledFingerprints();
    }

    public boolean isFingerprintHadrwareDetected() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && fingerprintManager.isHardwareDetected();
    }

    public void authenticate(FingerprintManagerCompat.AuthenticationCallback callback, CancellationSignal cancellationSignal) {
        generateKey();
        final FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(generateCipher());
        fingerprintManager.authenticate(cryptoObject, 0, cancellationSignal, callback, null);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationValidityDurationSeconds(-1)
                    .build();
            keyGenerator.init(spec);
            keyGenerator.generateKey();
        } catch (KeyStoreException | IOException | CertificateException | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to generate new key", e);
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private Cipher generateCipher() {
        try {
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher;
        } catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}
