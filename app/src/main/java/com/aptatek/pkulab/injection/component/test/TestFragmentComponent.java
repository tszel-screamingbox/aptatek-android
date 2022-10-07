package com.aptatek.pkulab.injection.component.test;

import com.aptatek.pkulab.injection.module.FragmentModule;
import com.aptatek.pkulab.injection.module.test.TestModule;
import com.aptatek.pkulab.view.test.breakfoil.BreakFoilFragment;
import com.aptatek.pkulab.view.test.canceltest.CancelTestFragment;
import com.aptatek.pkulab.view.test.collectblood.CollectBloodFragment;
import com.aptatek.pkulab.view.test.connectitall.ConnectItAllFragment;
import com.aptatek.pkulab.view.test.mixsample.MixSampleFragment;
import com.aptatek.pkulab.view.test.pokefingertip.PokeFingertipFragment;
import com.aptatek.pkulab.view.test.testcomplete.TestCompleteFragment;
import com.aptatek.pkulab.view.test.testing.TestingFragment;
import com.aptatek.pkulab.view.test.turnreaderon.TurnReaderOnTestFragment;
import com.aptatek.pkulab.view.test.wetting.WettingFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, TestModule.class})
public interface TestFragmentComponent {

    void inject(CancelTestFragment fragment);

    void inject(BreakFoilFragment fragment);

    void inject(PokeFingertipFragment pokeFingertipFragment);

    void inject(CollectBloodFragment collectBloodFragment);

    void inject(MixSampleFragment mixSampleFragment);

    void inject(WettingFragment wettingFragment);

    void inject(TurnReaderOnTestFragment turnReaderOnFragment);

    void inject(ConnectItAllFragment connectItAllFragment);

    void inject(TestingFragment testingFragment);

    void inject(TestCompleteFragment testCompleteFragment);
}
