package com.aptatek.pkuapp.injection.component.test;

import com.aptatek.pkuapp.injection.module.FragmentModule;
import com.aptatek.pkuapp.injection.module.test.TestModule;
import com.aptatek.pkuapp.view.test.breakfoil.BreakFoilFragment;
import com.aptatek.pkuapp.view.test.canceltest.CancelTestFragment;
import com.aptatek.pkuapp.view.test.collectblood.CollectBloodFragment;
import com.aptatek.pkuapp.view.test.connectitall.ConnectItAllFragment;
import com.aptatek.pkuapp.view.test.mixsample.MixSampleFragment;
import com.aptatek.pkuapp.view.test.pokefingertip.PokeFingertipFragment;
import com.aptatek.pkuapp.view.test.testing.TestingFragment;
import com.aptatek.pkuapp.view.test.turnreaderon.TurnReaderOnFragment;
import com.aptatek.pkuapp.view.test.wetting.WettingFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {FragmentModule.class, TestModule.class})
public interface TestFragmentComponent {

    void inject(CancelTestFragment fragment);

    void inject(BreakFoilFragment fragment);

    void inject(PokeFingertipFragment pokeFingertipFragment);

    void inject(CollectBloodFragment collectBloodFragment);

    void inject(MixSampleFragment mixSampleFragment);

    void inject(WettingFragment wettingFragment);

    void inject(TurnReaderOnFragment turnReaderOnFragment);

    void inject(ConnectItAllFragment connectItAllFragment);

    void inject(TestingFragment testingFragment);
}
