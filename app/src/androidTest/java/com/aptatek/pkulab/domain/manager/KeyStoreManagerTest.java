package com.aptatek.pkulab.domain.manager;

import androidx.test.runner.AndroidJUnit4;

import com.aptatek.pkulab.data.PinCode;
import com.aptatek.pkulab.domain.manager.keystore.KeyStoreError;
import com.aptatek.pkulab.domain.manager.keystore.KeyStoreManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.InstrumentationRegistry.getContext;
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
    public void testEncryptAndDecrypt() throws KeyStoreError {
        final String encryptedPin = keyStoreManager.encrypt(originalPin);
        final PinCode decryptedPin = keyStoreManager.decrypt(encryptedPin);
        assertTrue(originalPin.equals(decryptedPin));
    }


    @Test
    public void testAliasExists() throws KeyStoreError {
        keyStoreManager.encrypt(originalPin);
        assertTrue(keyStoreManager.aliasExists());
    }


    @Test
    public void testDeleteKeyStore() throws KeyStoreError {
        keyStoreManager.encrypt(originalPin);
        keyStoreManager.deleteKeyStore();
        assertTrue(!keyStoreManager.aliasExists());
    }
}