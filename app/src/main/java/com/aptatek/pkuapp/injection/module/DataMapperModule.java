package com.aptatek.pkuapp.injection.module;

import com.aptatek.pkuapp.data.mapper.CubeDataMapper;
import com.aptatek.pkuapp.data.mapper.ReminderDayMapper;
import com.aptatek.pkuapp.data.mapper.ReminderMapper;
import com.aptatek.pkuapp.domain.base.Mapper;
import com.aptatek.pkuapp.domain.model.CubeData;
import com.aptatek.pkuapp.domain.model.Reminder;
import com.aptatek.pkuapp.domain.model.ReminderDay;
import com.aptatek.pkuapp.injection.qualifier.ClassKey;

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
