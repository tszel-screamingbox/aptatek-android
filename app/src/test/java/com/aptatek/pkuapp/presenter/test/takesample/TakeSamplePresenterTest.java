package com.aptatek.pkuapp.presenter.test.takesample;

import android.graphics.Bitmap;

import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.domain.interactor.VideoThumbnailInteractor;
import com.aptatek.pkuapp.domain.interactor.incubation.IncubationInteractor;
import com.aptatek.pkuapp.view.test.takesample.TakeSamplePresenter;
import com.aptatek.pkuapp.view.test.takesample.TakeSampleView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for the TakeSamplePresenter class.
 *
 * @test.layer presentation
 * @test.feature Test flow - Take Sample
 * @test.type unit
 */
public class TakeSamplePresenterTest {

    private static final String TEST_STRING = "TEST";
    private static final Bitmap TEST_BITMAP = Bitmap.createBitmap(25, 25, Bitmap.Config.ARGB_8888);

    @Mock
    ResourceInteractor resourceInteractor;

    @Mock
    IncubationInteractor incubationInteractor;

    @Mock
    VideoThumbnailInteractor videoThumbnailInteractor;

    @Mock
    TakeSampleView view;

    private TakeSamplePresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt())).thenReturn(TEST_STRING);
        when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt(), ArgumentMatchers.anyVararg())).thenReturn(TEST_STRING);
        when(videoThumbnailInteractor.createThumbnailForRawVideo(ArgumentMatchers.any())).thenReturn(TEST_BITMAP);
        when(incubationInteractor.startIncubation()).thenReturn(Completable.complete());

        presenter = new TakeSamplePresenter(resourceInteractor, videoThumbnailInteractor, incubationInteractor);
        presenter.attachView(view);
    }

    /**
     * Tests the proper behavior: the initUi() method should trigger changes on the UI to display initial state.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testInitUi() throws Exception {
        presenter.initUi();

        verify(view).setCancelBigVisible(false);
        verify(view).setCircleCancelVisible(true);
        verify(view).setNavigationButtonVisible(true);
        verify(view).setNavigationButtonText(TEST_STRING);
        verify(view).setMessage(TEST_STRING);
        verify(view).setTitle(TEST_STRING);
        verify(view).showAgeSwitcherText(TEST_STRING);
        verify(view).showVideoThumbnail(TEST_BITMAP);
        verify(view).loadVideo(ArgumentMatchers.any());
    }

    /**
     * Tests the proper behavior: onChangeAge() method should trigger changes on the UI: age switcher text, video thumbnail and video should be changed.
     *
     * @test.input
     * @test.expected
     */
    @Test
    public void testOnChangeAge() throws Exception {
        presenter.onChangeAge();

        verify(view).showAgeSwitcherText(TEST_STRING);
        verify(view).showVideoThumbnail(TEST_BITMAP);
        verify(view).loadVideo(ArgumentMatchers.any());
    }

}
