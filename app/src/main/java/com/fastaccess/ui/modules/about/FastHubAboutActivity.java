package com.fastaccess.ui.modules.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.fastaccess.BuildConfig;
import com.fastaccess.R;
import com.fastaccess.data.dao.model.Release;
import com.fastaccess.helper.ActivityHelper;
import com.fastaccess.helper.AppHelper;
import com.fastaccess.helper.BundleConstant;
import com.fastaccess.helper.PrefGetter;
import com.fastaccess.ui.modules.changelog.ChangelogBottomSheetDialog;
import com.fastaccess.ui.modules.repos.RepoPagerActivity;
import com.fastaccess.ui.modules.repos.issues.create.CreateIssueActivity;
import com.fastaccess.ui.modules.user.UserPagerActivity;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.Disposable;

/**
 * Created by danielstone on 12 Mar 2017, 1:57 AM
 */
public class FastHubAboutActivity extends MaterialAboutActivity {

    private View malRecyclerview;
    private Disposable disposable;


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        int themeMode = PrefGetter.getThemeType(getApplicationContext());
        if (themeMode == PrefGetter.LIGHT) {
            setTheme(R.style.AppTheme_AboutActivity_Light);
        } else if (themeMode == PrefGetter.DARK) {
            setTheme(R.style.AppTheme_AboutActivity_Dark);
        }
        super.onCreate(savedInstanceState);
        malRecyclerview = findViewById(R.id.mal_recyclerview);
    }

    @Override protected MaterialAboutList getMaterialAboutList(Context context) {
        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();
        buildApp(context, appCardBuilder);
        MaterialAboutCard.Builder miscCardBuilder = new MaterialAboutCard.Builder();
        buildMisc(context, miscCardBuilder);
        MaterialAboutCard.Builder authorCardBuilder = new MaterialAboutCard.Builder();
        buildAuthor(context, authorCardBuilder);
        MaterialAboutCard.Builder logoAuthor = new MaterialAboutCard.Builder();
        buildLogo(context, logoAuthor);
        return new MaterialAboutList(appCardBuilder.build(), miscCardBuilder.build(), authorCardBuilder.build(), logoAuthor.build());
    }

    @Override protected CharSequence getActivityTitle() {
        return getString(R.string.app_name);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == BundleConstant.REQUEST_CODE) {
            Toasty.success(this, getString(R.string.thank_you_for_feedback), Toast.LENGTH_SHORT).show();
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;//override
    }

    @Override protected void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroy();
    }

    private void buildLogo(Context context, MaterialAboutCard.Builder logoAuthor) {
        logoAuthor.title(getString(R.string.logo_designer, "Kevin Aguilar"));
        logoAuthor.addItem(new MaterialAboutActionItem.Builder()
                .text(R.string.google_plus)
                .icon(ContextCompat.getDrawable(context, R.drawable.ic_profile))
                .setOnClickListener(b -> ActivityHelper.startCustomTab(this, "https://plus.google.com/+KevinAguilarC"))
                .build())
                .addItem(new MaterialAboutActionItem.Builder()
                        .text(R.string.twitter)
                        .icon(ContextCompat.getDrawable(context, R.drawable.ic_profile))
                        .setOnClickListener(b -> ActivityHelper.startCustomTab(this, "https://twitter.com/kevttob"))
                        .build())
                .addItem(new MaterialAboutActionItem.Builder()
                        .text(R.string.website)
                        .icon(ContextCompat.getDrawable(context, R.drawable.ic_brower))
                        .setOnClickListener(b -> ActivityHelper.startCustomTab(this, "https://www.221pixels.com/"))
                        .build());
    }

    private void buildAuthor(Context context, MaterialAboutCard.Builder authorCardBuilder) {
        authorCardBuilder.title(R.string.author);
        authorCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Kosh Sergani")
                .subText("k0shk0sh")
                .icon(ContextCompat.getDrawable(context, R.drawable.ic_profile))
                .setOnClickListener(b -> UserPagerActivity.startActivity(context, "k0shk0sh"))
                .build())
                .addItem(new MaterialAboutActionItem.Builder()
                        .text(R.string.fork_github)
                        .icon(ContextCompat.getDrawable(context, R.drawable.ic_github))
                        .setOnClickListener(b -> startActivity(RepoPagerActivity.createIntent(this, "FastHub", "k0shk0sh")))
                        .build())
                .addItem(ConvenienceBuilder.createEmailItem(context, ContextCompat.getDrawable(context, R.drawable.ic_email),
                        getString(R.string.send_email), true, getString(R.string.email_address), getString(R.string.question_concerning_fasthub)));
    }

    private void buildMisc(Context context, MaterialAboutCard.Builder miscCardBuilder) {
        miscCardBuilder.title(R.string.about)
                .addItem(new MaterialAboutActionItem.Builder()
                        .text(R.string.changelog)
                        .icon(ContextCompat.getDrawable(context, R.drawable.ic_track_changes))
                        .setOnClickListener(b -> new ChangelogBottomSheetDialog().show(getSupportFragmentManager(), "ChangelogBottomSheetDialog"))
                        .build())
                .addItem(new MaterialAboutActionItem.Builder()
                        .text(R.string.join_slack)
                        .icon(ContextCompat.getDrawable(context, R.drawable.ic_slack))
                        .setOnClickListener(b -> ActivityHelper.startCustomTab(this, "http://rebrand.ly/fasthub"))
                        .build())
                .addItem(new MaterialAboutActionItem.Builder()
                        .text(R.string.open_source_libs)
                        .icon(ContextCompat.getDrawable(context, R.drawable.ic_github))
                        .setOnClickListener(b -> new LibsBuilder()
                                .withActivityStyle(AppHelper.isNightMode(getResources()) ? Libs.ActivityStyle.DARK : Libs.ActivityStyle.LIGHT)
                                .withAutoDetect(true)
                                .withAboutIconShown(true)
                                .withAboutVersionShown(true)
                                .start(this))
                        .build());
    }

    private void buildApp(Context context, MaterialAboutCard.Builder appCardBuilder) {
        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text(getString(R.string.version))
                .icon(ContextCompat.getDrawable(context, R.drawable.ic_update))
                .subText(BuildConfig.VERSION_NAME)
                .setOnClickListener(b -> disposable = Release.get("FastHub", "k0shk0sh")
                        .subscribe(releases -> {
                            if (releases != null && !releases.isEmpty()) {
                                if (releases.get(0).getTagName().contains(BuildConfig.VERSION_NAME))
                                    Toasty.success(context, getString(R.string.up_to_date)).show();
                                else
                                    Toasty.warning(context, getString(R.string.new_version)).show();
                            }
                        }))
                .build())
                .addItem(ConvenienceBuilder.createRateActionItem(context, ContextCompat.getDrawable(context, R.drawable.ic_star_filled),
                        getString(R.string.rate_app), null))
                .addItem(new MaterialAboutActionItem.Builder()
                        .text(R.string.report_issue)
                        .subText(R.string.report_issue_here)
                        .icon(ContextCompat.getDrawable(context, R.drawable.ic_bug))
                        .setOnClickListener(b -> CreateIssueActivity.startForResult(this, CreateIssueActivity.startForResult(this), malRecyclerview))
                        .build());
    }
}
