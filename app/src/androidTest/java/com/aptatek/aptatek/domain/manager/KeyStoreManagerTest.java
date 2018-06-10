package com.aptatek.aptatek.domain.manager;

import android.support.test.runner.AndroidJUnit4;

import com.aptatek.aptatek.data.PinCode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class KeyStoreManagerTest {


    private KeyStoreManager keyStoreManager;
    private PinCode originalPin = new PinCode("secretPsw".toCharArray());


    @Before
    public void setUp() {
        keyStoreManager = new KeyStoreManager(getContext());
    }

    @Test
    public void testEncryptAndDecrypt() {
        final String encryptedPin = keyStoreManager.encrypt(originalPin);
        final PinCode decryptedPin = keyStoreManager.decrypt(encryptedPin);
        assertTrue(originalPin.isTheSame(decryptedPin));
    }


    @Test
    public void testAliasExists() {
        keyStoreManager.encrypt(originalPin);
        assertTrue(keyStoreManager.aliasExists());
    }


    @Test
    public void testDeleteKeyStore() {
        keyStoreManager.encrypt(originalPin);
        keyStoreManager.deleteKeyStore();
        assertTrue(!keyStoreManager.aliasExists());
    }
}