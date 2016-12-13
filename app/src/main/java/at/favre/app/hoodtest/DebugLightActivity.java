package at.favre.app.hoodtest;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.favre.lib.hood.defaults.DefaultActions;
import at.favre.lib.hood.defaults.DefaultConfigActions;
import at.favre.lib.hood.defaults.DefaultProperties;
import at.favre.lib.hood.extended.PopHoodActivity;
import at.favre.lib.hood.page.Config;
import at.favre.lib.hood.page.DebugPage;
import at.favre.lib.hood.page.Page;
import at.favre.lib.hood.page.entries.ActionEntry;
import at.favre.lib.hood.page.entries.ConfigBoolEntry;
import at.favre.lib.hood.page.entries.ConfigSpinnerEntry;
import at.favre.lib.hood.page.values.SpinnerElement;
import at.favre.lib.hood.util.HoodUtil;

public class DebugLightActivity extends PopHoodActivity {
    private static final String TAG = DebugLightActivity.class.getName();

    @NonNull
    @Override
    public Page getPageData(@NonNull DebugPage page) {
        page.add(DefaultProperties.createAppVersionInfo(at.favre.lib.hood.BuildConfig.class, true));
        page.add(DefaultProperties.createSignatureHashInfo(this));

        page.add(DefaultProperties.createBasicDeviceInfo(true));
        page.add(DefaultProperties.createDetailedDeviceInfo(this));

        page.add(new ConfigBoolEntry(DefaultConfigActions.getBoolSharedPreferencesConfigAction(getPreferences(MODE_PRIVATE), "KEY_TEST", false)));
        page.add(new ConfigBoolEntry(DefaultConfigActions.getBoolSharedPreferencesConfigAction(getPreferences(MODE_PRIVATE), "KEY_TEST2", false)));
        page.add(new ConfigBoolEntry(DefaultConfigActions.getBoolSharedPreferencesConfigAction(getPreferences(MODE_PRIVATE), "KEY_TEST3", "a debug feature", false)));

        page.add(new ConfigSpinnerEntry(DefaultConfigActions.getDefaultSharedPrefBackedSpinnerAction(null, getPreferences(MODE_PRIVATE), "W_BACKEND_KEY", null, getBackendElements())));

        page.add(DefaultProperties.createSectionTelephonyManger(this));

        page.addTitle("Misc Actions");
        page.addAction(DefaultActions.getAppInfoAction(this));
        page.addAction(DefaultActions.getCrashAction(), DefaultActions.getUninstallAction(this));
        page.addAction(DefaultActions.getKillProcessAction(this), DefaultActions.getClearAppDataAction(this));
        page.addAction(HoodUtil.getConditionally(DefaultActions.getKillProcessAction(this), at.favre.lib.hood.BuildConfig.DEBUG));

        page.add(DefaultProperties.createSectionRuntimePermissions(this, true));

        page.addTitle("System Features");
        Map<CharSequence, String> systemFeatureMap = new HashMap<>();
        systemFeatureMap.put("hasHce", "android.hardware.nfc.hce");
        systemFeatureMap.put("hasCamera", "android.hardware.camera");
        systemFeatureMap.put("hasWebview", "android.software.webview");
        page.addAction(new ActionEntry.Action("Test Loading", new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setEnabled(false);
                getDebugView().setProgressBarVisible(true);
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setEnabled(true);
                        getDebugView().setProgressBarVisible(false);
                    }
                }, 3000);
            }
        }));
        page.add(DefaultProperties.createSystemFeatureInfo(this, systemFeatureMap));

        page.add(DefaultProperties.createConnectivityStatusInfo(this, true));

        return page;
    }

    private List<SpinnerElement> getBackendElements() {
        List<SpinnerElement> elements = new ArrayList<>();
        elements.add(new SpinnerElement.Default("1", "dev.backend.com"));
        elements.add(new SpinnerElement.Default("2", "dev2.backend.com"));
        elements.add(new SpinnerElement.Default("3", "dev3.backend.com"));
        elements.add(new SpinnerElement.Default("4", "stage.backend.com"));
        elements.add(new SpinnerElement.Default("5", "prod.backend.com"));
        return elements;
    }

    @NonNull
    @Override
    public Config getConfig() {
        return new Config.Builder().setLogTag(TAG).setAutoLog(false).build();
    }
}
