package com.aptatek.pkuapp.domain.interactor.parentalgate;

import com.aptatek.pkuapp.domain.model.AgeCheckModel;
import com.aptatek.pkuapp.domain.model.AgeCheckResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;

import static org.mockito.Mockito.when;

public class ParentalGateInteractorTest {

    private static final String TEST_STRING = "hello";
    private ParentalGateInteractor parentalGateInteractor;

    @Mock
    BirthDateFormatter birthDateFormatter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        parentalGateInteractor = new ParentalGateInteractor(birthDateFormatter);
    }

    @Test
    public void testFormatBirthday() throws Exception {
        final long birthDate = System.currentTimeMillis();
        when(birthDateFormatter.formatBirthDate(birthDate)).thenReturn(TEST_STRING);

        parentalGateInteractor.formatBirthDate(birthDate)
                .test()
                .assertComplete()
                .assertValue(TEST_STRING);
    }

    private long getTimestampForAge(final int age) {
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, (age + 1) * -1);
        calendar.add(Calendar.MONTH, 3);

        return calendar.getTimeInMillis();
    }

    @Test
    public void testVerify() throws Exception {
        parentalGateInteractor.verify(AgeCheckModel.builder()
                .setAge(21)
                .setBirthDate(getTimestampForAge(21))
                .setBirthDateFormatted("")
                .build()
        ).test()
        .assertNoErrors()
        .assertValue(AgeCheckResult.VALID_AGE)
        .assertComplete();
    }

    @Test
    public void testVerifyUnderAge() throws Exception {
        parentalGateInteractor.verify(AgeCheckModel.builder()
                .setAge(12)
                .setBirthDate(System.currentTimeMillis())
                .setBirthDateFormatted("")
                .build()
        ).test()
        .assertNoErrors()
        .assertValue(AgeCheckResult.NOT_OLD_ENOUGH)
        .assertComplete();
    }

    @Test
    public void testVerifyAgeMismatch() throws Exception {
        parentalGateInteractor.verify(AgeCheckModel.builder()
                .setAge(20)
                .setBirthDate(getTimestampForAge(23))
                .setBirthDateFormatted("")
                .build()
        ).test()
        .assertNoErrors()
        .assertValue(AgeCheckResult.AGE_NOT_MATCH)
        .assertComplete();
    }

    @Test
    public void testVerifyAgeFutureBornThrowsException() throws Exception {
        parentalGateInteractor.verify(AgeCheckModel.builder()
                .setAge(21)
                .setBirthDate(System.currentTimeMillis() + 1000 * 60 * 60)
                .setBirthDateFormatted("")
                .build()
        ).test()
        .assertError(throwable -> throwable instanceof IllegalArgumentException);
    }

}
