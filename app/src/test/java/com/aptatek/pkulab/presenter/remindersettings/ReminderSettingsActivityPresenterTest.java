package com.aptatek.pkulab.presenter.remindersettings;

import com.aptatek.pkulab.domain.interactor.ResourceInteractor;
import com.aptatek.pkulab.domain.interactor.remindersettings.ReminderInteractor;
import com.aptatek.pkulab.view.settings.reminder.adapter.ReminderSettingsAdapterItem;
import com.aptatek.pkulab.view.settings.reminder.ReminderSettingsPresenter;
import com.aptatek.pkulab.view.settings.reminder.ReminderSettingsView;
import com.aptatek.pkulab.view.settings.reminder.adapter.RemindersAdapterItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;

public class ReminderSettingsActivityPresenterTest {

    @Mock
    private ResourceInteractor resourceInteractor;

    @Mock
    private ReminderInteractor reminderInteractor;

    @Mock
    ReminderSettingsView view;

    private ReminderSettingsPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new ReminderSettingsPresenter(resourceInteractor, reminderInteractor);
        presenter.attachView(view);
    }

    @Test
    public void testModifyAlreadyHasReminder() {

        RemindersAdapterItem remindersAdapterItem = RemindersAdapterItem.builder()
                .setId("test")
                .setTime("10:30am")
                .setActive(true)
                .setHour(10)
                .setMinute(30)
                .build();

        ReminderSettingsAdapterItem reminderSettingsAdapterItem = ReminderSettingsAdapterItem.builder()
                .setWeekDay(5)
                .setNameOfDay("Friday")
                .setActive(true)
                .setReminders(new ArrayList<>())
                .build();

        reminderSettingsAdapterItem.getReminders().add(remindersAdapterItem);
        presenter.modifyReminder(new ArrayList<>(), reminderSettingsAdapterItem, remindersAdapterItem, 10, 30);
        verify(view).showAlreadyHasReminderError();
    }
}
