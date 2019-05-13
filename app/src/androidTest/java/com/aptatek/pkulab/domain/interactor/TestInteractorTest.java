package com.aptatek.pkulab.domain.interactor;

import android.app.Application;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.aptatek.pkulab.domain.interactor.test.TestInteractor;
import com.aptatek.pkulab.injection.component.DaggerAndroidTestComponent;
import com.aptatek.pkulab.injection.module.ApplicationModule;
import com.aptatek.pkulab.view.test.TestScreens;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * @test.layer Domain / Interactor
 * @test.feature Test screens
 * @test.type Instrumented unit tests
 */
@RunWith(AndroidJUnit4.class)
public class TestInteractorTest {

    @Inject
    TestInteractor testInteractor;

    /**
     * Setting up the required modules and initialize reminder days in database.
     */
    @Before
    public void setUp() throws Exception {
        DaggerAndroidTestComponent.builder()
                .applicationModule(new ApplicationModule(((Application) InstrumentationRegistry.getTargetContext().getApplicationContext())))
                .build()
                .inject(this);
    }

    @Test
    public void testDefaultStatus() {
        testInteractor.getLastScreen().test()
                .assertError(throwable -> throwable instanceof IllegalStateException);
    }

    @Test
    public void testValueSet() {
        testInteractor.setLastScreen(TestScreens.COLLECT_BLOOD)
                .andThen(testInteractor.getLastScreen())
                .test()
                .assertValue(TestScreens.COLLECT_BLOOD);
    }

}
