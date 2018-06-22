package com.aptatek.aptatek.presenter.test.remindersettings;

import com.aptatek.aptatek.domain.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.remindersettings.ReminderInteractor;
import com.aptatek.aptatek.view.settings.reminder.ReminderSettingsAdapterItem;
import com.aptatek.aptatek.view.settings.reminder.ReminderSettingsPresenter;
import com.aptatek.aptatek.view.settings.reminder.ReminderSettingsView;
import com.aptatek.aptatek.view.settings.reminder.RemindersAdapterItem;

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
        RemindersAdapterItem remindersAdapterItem = new RemindersAdapterItem("test", "10:30am", true, 10, 30);
        ReminderSettingsAdapterItem reminderSettingsAdapterItem = new ReminderSettingsAdapterItem(5, "Friday", true, new ArrayList<>());
        reminderSettingsAdapterItem.getReminders().add(remindersAdapterItem);
        presenter.modifyReminder(reminderSettingsAdapterItem, remindersAdapterItem, 10, 30);
        verify(view).showAlreadyHasReminderError();
    }
}
