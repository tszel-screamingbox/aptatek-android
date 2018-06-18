package com.aptatek.aptatek.presenter.test.remindersettings;

import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.view.settings.ReminderSettingsActivityPresenter;
import com.aptatek.aptatek.view.settings.ReminderSettingsActivityView;
import com.aptatek.aptatek.view.settings.ReminderSettingsAdapterItem;
import com.aptatek.aptatek.view.settings.RemindersAdapterItem;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Random;

import static org.mockito.Mockito.verify;

public class ReminderSettingsActivityPresenterTest {

    @Mock
    private ResourceInteractor resourceInteractor;

    @Mock
    ReminderSettingsActivityView view;

    private ReminderSettingsActivityPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        presenter = new ReminderSettingsActivityPresenter(resourceInteractor);
        presenter.attachView(view);
    }

    @Test
    public void testAddAlreadyHasReminder() {
        ReminderSettingsAdapterItem reminderSettingsAdapterItem = new ReminderSettingsAdapterItem("Friday");
        presenter.addNewReminder(reminderSettingsAdapterItem, 10, 30);
        presenter.addNewReminder(reminderSettingsAdapterItem, 10, 30);
        verify(view).alreadyHasReminderError();
    }

    @Test
    public void testAddReminder() {
        ReminderSettingsAdapterItem reminderSettingsAdapterItem = new ReminderSettingsAdapterItem("Friday");
        presenter.addNewReminder(reminderSettingsAdapterItem, 10, 30);
        verify(view).addReminder(reminderSettingsAdapterItem);
    }

    @Test
    public void testModifyReminder() {
        RemindersAdapterItem remindersAdapterItem = new RemindersAdapterItem("10:30am", true, 10, 30);
        ReminderSettingsAdapterItem reminderSettingsAdapterItem = new ReminderSettingsAdapterItem("Friday");
        reminderSettingsAdapterItem.getReminders().add(remindersAdapterItem);
        presenter.modifyReminder(reminderSettingsAdapterItem, remindersAdapterItem, 7, 30);
        verify(view).modifyReminder(reminderSettingsAdapterItem);
    }

    @Test
    public void testModifyAlreadyHasReminder() {
        RemindersAdapterItem remindersAdapterItem = new RemindersAdapterItem("10:30am", true, 10, 30);
        ReminderSettingsAdapterItem reminderSettingsAdapterItem = new ReminderSettingsAdapterItem("Friday");
        reminderSettingsAdapterItem.getReminders().add(remindersAdapterItem);
        presenter.modifyReminder(reminderSettingsAdapterItem, remindersAdapterItem, 10, 30);
        verify(view).alreadyHasReminderError();
    }

    @Test
    public void testChangeActiveState() {
        boolean state = new Random().nextBoolean();
        ReminderSettingsAdapterItem item = new ReminderSettingsAdapterItem("Friday");
        presenter.changeActiveState(item, state);
        verify(view).changeActiveState(item);
    }
}
