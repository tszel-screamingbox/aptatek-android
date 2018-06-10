package com.aptatek.aptatek.presenter.test.takesample;

import android.graphics.Bitmap;

import com.aptatek.aptatek.domain.interactor.ResourceInteractor;
import com.aptatek.aptatek.domain.interactor.VideoThumbnailInteractor;
import com.aptatek.aptatek.view.test.takesample.TakeSamplePresenter;
import com.aptatek.aptatek.view.test.takesample.TakeSampleView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TakeSamplePresenterTest {

    private static final String TEST_STRING = "TEST";
    private static final Bitmap TEST_BITMAP = Bitmap.createBitmap(25, 25, Bitmap.Config.ARGB_8888);

    @Mock
    ResourceInteractor resourceInteractor;

    @Mock
    VideoThumbnailInteractor videoThumbnailInteractor;

    @Mock
    TakeSampleView view;

    private TakeSamplePresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt())).thenReturn(TEST_STRING);
        Mockito.when(resourceInteractor.getStringResource(ArgumentMatchers.anyInt(), ArgumentMatchers.anyVararg())).thenReturn(TEST_STRING);
        Mockito.when(videoThumbnailInteractor.createThumbnailForRawVideo(ArgumentMatchers.any())).thenReturn(TEST_BITMAP);

        presenter = new TakeSamplePresenter(resourceInteractor, videoThumbnailInteractor);
        presenter.attachView(view);
    }

    @Test
    public void testInitUi() throws Exception {
        presenter.initUi();

        Mockito.verify(view).setCancelBigVisible(false);
        Mockito.verify(view).setCircleCancelVisible(true);
        Mockito.verify(view).setNavigationButtonVisible(true);
        Mockito.verify(view).setNavigationButtonText(TEST_STRING);
        Mockito.verify(view).setMessage(TEST_STRING);
        Mockito.verify(view).setTitle(TEST_STRING);
        Mockito.verify(view).showAgeSwitcherText(TEST_STRING);
        Mockito.verify(view).showVideoThumbnail(TEST_BITMAP);
        Mockito.verify(view).loadVideo(ArgumentMatchers.any());
    }

    @Test
    public void testOnChangeAge() throws Exception {
        presenter.onChangeAge();

        Mockito.verify(view).showAgeSwitcherText(TEST_STRING);
        Mockito.verify(view).showVideoThumbnail(TEST_BITMAP);
        Mockito.verify(view).loadVideo(ArgumentMatchers.any());
    }

}
