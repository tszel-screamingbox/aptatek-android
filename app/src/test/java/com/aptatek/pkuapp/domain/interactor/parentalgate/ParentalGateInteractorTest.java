package com.aptatek.pkuapp.domain.interactor.parentalgate;

import com.aptatek.pkuapp.domain.model.AgeCheckModel;
import com.aptatek.pkuapp.domain.model.AgeCheckResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;

import static org.mockito.Mockito.when;

/**
 * @test.layer Domain / Interactor
 * @test.feature ParentalGate
 * @test.type Unit tests
 */
public class ParentalGateInteractorTest {

    private static final String TEST_STRING = "hello";
    private ParentalGateInteractor parentalGateInteractor;

    @Mock
    BirthDateFormatter birthDateFormatter;

    /**
     * Setting up the required presenter
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        parentalGateInteractor = new ParentalGateInteractor(birthDateFormatter);
    }

    /**
     * Testing birthday format.
     *
     * @test.expected The stream completely emit without any error.
     */
    @Test
    public void testFormatBirthday() {
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

    /**
     * Testing age verification.
     *
     * @test.expected The stream returns with "VALID_AGE" result, without any error.
     */
    @Test
    public void testVerify() {
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

    /**
     * Testing underage verification.
     *
     * @test.expected The stream returns with "NOT_OLD_ENOUGH" result, without any error.
     */
    @Test
    public void testVerifyUnderAge() {
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

    /**
     * Testing age verification.
     *
     * @test.expected The stream returns with "AGE_NOT_MATCH" result, without any error.
     */
    @Test
    public void testVerifyAgeMismatch() {
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

    /**
     * Testing age verification with invalid, future date.
     *
     * @test.expected The stream returns with IllegalArgumentException.
     */
    @Test
    public void testVerifyAgeFutureBornThrowsException() {
        parentalGateInteractor.verify(AgeCheckModel.builder()
                .setAge(21)
                .setBirthDate(System.currentTimeMillis() + 1000 * 60 * 60)
                .setBirthDateFormatted("")
                .build()
        ).test()
                .assertError(throwable -> throwable instanceof IllegalArgumentException);
    }

}
