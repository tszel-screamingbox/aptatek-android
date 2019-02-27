package com.aptatek.pkulab.device.bluetooth.scanner;

import com.aptatek.pkulab.data.generator.FakeReaderDeviceGenerator;
import com.aptatek.pkulab.domain.error.DeviceDiscoveryError;
import com.aptatek.pkulab.domain.interactor.countdown.Countdown;
import com.aptatek.pkulab.domain.manager.reader.BluetoothScanner;
import com.aptatek.pkulab.domain.model.reader.ReaderDevice;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import ix.Ix;

public class MockBluetootScanner implements BluetoothScanner {

    private static final int MOCK_SCAN_PERIOD_MS = 6000;

    private final FakeReaderDeviceGenerator deviceGenerator;

    private final FlowableProcessor<Boolean> scanningProcessor = BehaviorProcessor.createDefault(false);
    private final FlowableProcessor<Set<ReaderDevice>> readerDevicesProcessor = PublishProcessor.create();

    public MockBluetootScanner(final FakeReaderDeviceGenerator deviceGenerator) {
        this.deviceGenerator = deviceGenerator;
    }

    @Override
    public Completable startScan() {
        return simulateScanning(true)
                .doOnComplete(() -> simulateDiscovery().subscribe(readerDevicesProcessor::onNext));
    }

    @Override
    public Completable stopScan() {
        return simulateScanning(false);
    }

    private Completable simulateScanning(final boolean scanning) {
        return Completable.complete()
                .delay(1, TimeUnit.SECONDS)
                .andThen(Completable.fromAction(() -> scanningProcessor.onNext(scanning)));
    }

    private Flowable<Set<ReaderDevice>> simulateDiscovery() {
        return Flowable.fromCallable(deviceGenerator::generateReaderDevices)
                .map(list -> Ix.from(list).toSet())
                .repeatWhen(objectFlowable -> objectFlowable.flatMap(ignore -> Countdown.countdown(MOCK_SCAN_PERIOD_MS, tick -> tick <= MOCK_SCAN_PERIOD_MS, tick -> tick)))
                .takeUntil(scanningProcessor.filter(scanning -> scanning));
    }

    @Override
    public Flowable<Boolean> isScanning() {
        return scanningProcessor;
    }

    @Override
    public Flowable<Set<ReaderDevice>> getDiscoveredDevices() {
        return readerDevicesProcessor;
    }

    @Override
    public Flowable<DeviceDiscoveryError> getDiscoveryErrors() {
        return Flowable.empty(); // no errors for now
    }
}
