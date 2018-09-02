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
 * Tests for the ParentalGateInteractor class
 *
 * @test.layer domain
 * @test.feature ParentalGate
 * @test.type unit
 */
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

    /**
     * Tests the proper behavior: the formatBirthDate(long) method should call the the BirthDateFormatter class to return a properly formatted String representation of the birthday and completes without error.
     *
     * @test.input birthday as long
     * @test.expected formatted String
     */
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

    /**
     * Tests whether the verify(AgeCheckModel) method completes and emits an AgeCheckResult.VALID_AGE object when the time elapsed between today and the provided birthdate equals the provided age.
     *
     * @test.input birthday = today minus 21 years and 3 months, age = 21
     * @test.expected VALID_AGE
     */
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

    /**
     * Tests whether the verify(AgeCheckModel) method completes and emits a AgeCheckResult.NOT_OLD_ENOUGH object when the provided age is under 13
     *
     * @test.input age = 12
     * @test.expected NOT_OLD_ENOUGH
     */
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

    /**
     * Tests whether the verify(AgeCheckModel) method completes and emits a AgeCheckResult.NOT_NOT_MATCH object when the provided age doesn't match the elapsed years since the given birthdate
     *
     * @test.input age = 20, birthDate = today minus 23 years and 3 months
     * @test.expected NOT_OLD_ENOUGH
     */
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

    /**
     * Tests whether the verify(AgeCheckModel) method signals error when the provided birthDate is in the future.
     *
     * @test.input birthDate = now + 1 hour
     * @test.expected exception
     */
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
