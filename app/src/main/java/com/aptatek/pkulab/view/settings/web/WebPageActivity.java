package com.aptatek.pkulab.view.settings.web;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aptatek.pkulab.BuildConfig;
import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.ReportIssue;
import com.aptatek.pkulab.domain.model.ReportIssueType;
import com.aptatek.pkulab.injection.component.ActivityComponent;
import com.aptatek.pkulab.injection.module.chart.ChartModule;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.main.weekly.csv.Attachment;
import com.aptatek.pkulab.view.settings.reminder.ReminderSettingsPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class WebPageActivity extends BaseActivity<WebPageView, WebPagePresenter> implements WebPageView, ReportIssueDialog.ReportIssueDialogCallback {

    private static final int SEND_ATTACHMENT_REQUEST_CODE = 9921;
    private static final String ISSUE_PICKER_DIALOG_TAG = "aptatek.issue.pickor.dialog.tag";

    @Inject
    WebPagePresenter presenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.webView)
    WebView webView;

    @Arg
    String title;

    @Arg
    String url;

    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        getActivityComponent().plus(new ChartModule()).inject(this);
    }

    @NonNull
    @Override
    public WebPagePresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStarter.fill(this, savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        webView.setWebViewClient(new WebViewClient());
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    @OnClick(R.id.btnReport)
    public void onReportButtonClicked() {
        ReportIssueDialog.create(this).show(getSupportFragmentManager(), ISSUE_PICKER_DIALOG_TAG);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onIssueTypeSelected(final ReportIssueType reportIssueType) {
        presenter.generateAttachment(reportIssueType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getSupportFragmentManager().findFragmentByTag(ISSUE_PICKER_DIALOG_TAG) != null) {
            ((ReportIssueDialog) getSupportFragmentManager().findFragmentByTag(ISSUE_PICKER_DIALOG_TAG)).dismiss();
        }
    }

    @Override
    public void sendIssueEmail(final ReportIssue reportIssue) {
        final ArrayList<Uri> attachments = new ArrayList<>();

        for (final Attachment attachment : reportIssue.getAttachments()) {
            attachments.add(FileProvider.getUriForFile(
                    this,
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
