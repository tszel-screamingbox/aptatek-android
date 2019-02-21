package com.aptatek.pkulab.injection.module;

import com.aptatek.pkulab.device.bluetooth.mapper.CartridgeInfoMapper;
import com.aptatek.pkulab.device.bluetooth.mapper.ErrorMapper;
import com.aptatek.pkulab.device.bluetooth.mapper.TestProgressMapper;
import com.aptatek.pkulab.device.bluetooth.mapper.WorkflowStateMapper;
import com.aptatek.pkulab.device.bluetooth.model.CartridgeIdResponse;
import com.aptatek.pkulab.device.bluetooth.model.ErrorResponse;
import com.aptatek.pkulab.device.bluetooth.model.ResultResponse;
import com.aptatek.pkulab.device.bluetooth.model.TestProgressResponse;
import com.aptatek.pkulab.device.bluetooth.model.WorkflowStateResponse;
import com.aptatek.pkulab.domain.base.Mapper;
import com.aptatek.pkulab.injection.qualifier.ClassKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface DeviceMapperModule {

    @Binds
    @IntoMap
    @ClassKey(CartridgeIdResponse.class)
    Mapper<?, ?> bindCartridgeDeviceMapper(CartridgeInfoMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(ResultResponse.class)
    Mapper<?, ?> bindResultResponseMapper(com.aptatek.pkulab.device.bluetooth.mapper.TestResultMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(TestProgressResponse.class)
    Mapper<?, ?> bindTestProgressResponseMapper(TestProgressMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(ErrorResponse.class)
    Mapper<?, ?> bindErrorMapper(ErrorMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(WorkflowStateResponse.class)
    Mapper<?, ?> bindWorkflowStateResponseMapper(WorkflowStateMapper mapper);


}
