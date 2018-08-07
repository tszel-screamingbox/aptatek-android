package com.aptatek.aptatek.injection.module;

import com.aptatek.aptatek.data.mapper.CubeDataMapper;
import com.aptatek.aptatek.data.mapper.ReminderDayMapper;
import com.aptatek.aptatek.data.mapper.ReminderMapper;
import com.aptatek.aptatek.domain.base.Mapper;
import com.aptatek.aptatek.domain.model.CubeData;
import com.aptatek.aptatek.domain.model.Reminder;
import com.aptatek.aptatek.domain.model.ReminderDay;
import com.aptatek.aptatek.injection.qualifier.ClassKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class DataMapperModule {

    @Binds
    @IntoMap
    @ClassKey(CubeData.class)
    public abstract Mapper<?, ?> bindCubeDataMapper(CubeDataMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(ReminderDay.class)
    public abstract Mapper<?, ?> bindReminderDayMapper(ReminderDayMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(Reminder.class)
    public abstract Mapper<?, ?> bindReminderDataMapper(ReminderMapper mapper);

}
