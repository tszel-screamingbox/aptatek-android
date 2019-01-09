package com.aptatek.pkulab.injection.module;

import com.aptatek.pkulab.data.mapper.TestResultMapper;
import com.aptatek.pkulab.data.mapper.ReminderDayMapper;
import com.aptatek.pkulab.data.mapper.ReminderMapper;
import com.aptatek.pkulab.device.bluetooth.mapper.CartridgeInfoMapper;
import com.aptatek.pkulab.device.bluetooth.mapper.ErrorMapper;
import com.aptatek.pkulab.device.bluetooth.mapper.WorkflowStateMapper;
import com.aptatek.pkulab.device.bluetooth.model.CartridgeIdResponse;
import com.aptatek.pkulab.device.bluetooth.model.ErrorResponse;
import com.aptatek.pkulab.device.bluetooth.model.ResultResponse;
import com.aptatek.pkulab.device.bluetooth.model.WorkflowStateResponse;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.domain.model.reader.CartridgeInfo;
import com.aptatek.pkulab.domain.model.reader.TestResult;
import com.aptatek.pkulab.domain.model.Reminder;
import com.aptatek.pkulab.domain.model.ReminderDay;
import com.aptatek.pkulab.injection.qualifier.ClassKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class DataMapperModule {

    @Binds
    @IntoMap
    @ClassKey(TestResult.class)
    public abstract Mapper<?, ?> bindTestResultMapper(TestResultMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(ReminderDay.class)
    public abstract Mapper<?, ?> bindReminderDayMapper(ReminderDayMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(Reminder.class)
    public abstract Mapper<?, ?> bindReminderDataMapper(ReminderMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(CartridgeIdResponse.class)
    public abstract Mapper<?, ?> bindCartridgeDeviceMapper(CartridgeInfoMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(ResultResponse.class)
    public abstract Mapper<?, ?> bindResultResponseMapper(com.aptatek.pkulab.device.bluetooth.mapper.TestResultMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(ErrorResponse.class)
    public abstract Mapper<?, ?> bindErrorMapper(ErrorMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(WorkflowStateResponse.class)
    public abstract Mapper<?, ?> bindWorkflowStateResponseMapper(WorkflowStateMapper mapper);


}
