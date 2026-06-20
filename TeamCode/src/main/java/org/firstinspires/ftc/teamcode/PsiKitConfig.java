package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.qualcomm.ftccommon.FtcEventLoop;

import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop;
import org.psilynx.psikit.ftc.autolog.PsiKitAutoLogSettings;

public final class PsiKitConfig {
    @OnCreateEventLoop
    public static void configure(Context context, FtcEventLoop ftcEventLoop) {
        PsiKitAutoLogSettings.enabledByDefault = true;

        System.setProperty(PsiKitAutoLogSettings.PROPERTY_RLOG_PORT, "5800");
        System.setProperty(PsiKitAutoLogSettings.PROPERTY_RLOG_FOLDER, "/sdcard/FIRST/PsiKit/");
        // Optional:
        // System.setProperty(PsiKitAutoLogSettings.PROPERTY_RLOG_FILENAME, "my-run.rlog")
    }
}