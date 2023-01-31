package com.aptatek.pkulab.domain.manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

import com.aptatek.pkulab.injection.qualifier.ApplicationContext;

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

    private final FingerprintManagerCompat fingerprintManagerCompat;
    private KeyStore keyStore;

    @Inject
    FingerprintManager(@ApplicationContext final Context context) {
        this.fingerprintManagerCompat = FingerprintManagerCompat.from(context);
    }

    public boolean hasEnrolledFingerprints() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && fingerprintManagerCompat.hasEnrolledFingerprints();
    }

    public boolean isFingerprintHadrwareDetected() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && fingerprintManagerCompat.isHardwareDetected();
    }

    public void authenticate(final FingerprintManagerCompat.AuthenticationCallback callback, final CancellationSignal cancellationSignal) {
        generateKey();
        final FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(generateCipher());
        fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, callback, null);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            final KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            final KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationValidityDurationSeconds(-1)
                    .build();
            keyGenerator.init(spec);
            keyGenerator.generateKey();
        } catch (KeyStoreException | IOException | CertificateException |
                 InvalidAlgorithmParameterException | NoSuchAlgorithmException |
                 NoSuchProviderException e) {
            throw new RuntimeException("Failed to generate new key", e);
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private Cipher generateCipher() {
        try {
            final Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            final SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher;
        } catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException |
                 NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}
