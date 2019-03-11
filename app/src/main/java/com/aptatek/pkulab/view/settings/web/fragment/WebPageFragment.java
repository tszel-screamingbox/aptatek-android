package com.aptatek.pkulab.view.settings.web.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.aptatek.pkulab.BuildConfig;
import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.ReportIssue;
import com.aptatek.pkulab.domain.model.ReportIssueType;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.util.Constants;
import com.aptatek.pkulab.view.base.BaseFragment;
import com.aptatek.pkulab.view.main.weekly.csv.Attachment;
import com.aptatek.pkulab.view.test.TestActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebPageFragment extends BaseFragment<WebPageView, WebPagePresenter> implements WebPageView, ReportIssueDialog.ReportIssueDialogCallback {

    private static final int SEND_ATTACHMENT_REQUEST_CODE = 9921;
    private static final String ISSUE_PICKER_DIALOG_TAG = "aptatek.issue.pickor.dialog.tag";

    @Inject
    WebPagePresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.btnReport)
    Button reportIssue;

    @Arg(optional = true)
    String title;

    @Arg(optional = true)
    String url;

    @Arg(optional = true)
    Boolean reportVisible = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        ActivityStarter.fill(this, savedInstanceState);
        return view;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_web;
    }

    @Override
    protected void initObjects(final View view) {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(title == null ? getString(R.string.settings_help) : title);
        getBaseActivity().setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (getBaseActivity() instanceof TestActivity) {
                ((TestActivity) getBaseActivity()).closeHelpScreen();
            } else {
                getBaseActivity().onBackPressed();
            }
        });

        webView.setWebViewClient(new WebViewClient());
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(url == null ? Constants.URL_HELP : url);

        if (reportVisible == null ? true : reportVisible) {
            reportIssue.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.plus(new ChartModule()).inject(this);
    }

    @NonNull
    @Override
    public WebPagePresenter createPresenter() {
        return presenter;
    }

    @OnClick(R.id.btnReport)
    public void onReportButtonClicked() {
        ReportIssueDialog.create(this).show(getBaseActivity().getSupportFragmentManager(), ISSUE_PICKER_DIALOG_TAG);
    }

    @Override
    public void onIssueTypeSelected(final ReportIssueType reportIssueType) {
        presenter.generateAttachment(reportIssueType);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getBaseActivity().getSupportFragmentManager().findFragmentByTag(ISSUE_PICKER_DIALOG_TAG) != null) {
            ((ReportIssueDialog) getBaseActivity().getSupportFragmentManager().findFragmentByTag(ISSUE_PICKER_DIALOG_TAG)).dismiss();
        }
    }

    @Override
    public void sendIssueEmail(final ReportIssue reportIssue) {
        final ArrayList<Uri> attachments = new ArrayList<>();

        for (final Attachment attachment : reportIssue.getAttachments()) {
            attachments.add(FileProvider.getUriForFile(
                    getBaseActivity(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    attachment.getCsvFile()));
        }

        final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{reportIssue.getTargetEmail()});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, reportIssue.getTitle());
        emailIntent.putExtra(Intent.EXTRA_TEXT, reportIssue.getDescription());
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachments);
        startActivityForResult(Intent.createChooser(emailIntent, getString(R.string.faq_report_issue_dialog_title)), SEND_ATTACHMENT_REQUEST_CODE);
    }
}
