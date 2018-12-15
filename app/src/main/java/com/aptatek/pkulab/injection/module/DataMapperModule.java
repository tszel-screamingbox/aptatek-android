package com.aptatek.pkulab.injection.module;

import com.aptatek.pkulab.data.mapper.TestResultMapper;
import com.aptatek.pkulab.data.mapper.ReminderDayMapper;
import com.aptatek.pkulab.data.mapper.ReminderMapper;
import com.aptatek.pkulab.domain.base.Mapper;
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
    public abstract Mapper<?, ?> bindCubeDataMapper(TestResultMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(ReminderDay.class)
    public abstract Mapper<?, ?> bindReminderDayMapper(ReminderDayMapper mapper);

    @Binds
    @IntoMap
    @ClassKey(Reminder.class)
    public abstract Mapper<?, ?> bindReminderDataMapper(ReminderMapper mapper);

}
