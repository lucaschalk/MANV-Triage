package de.uniluebeck.imis.triage.utilities;

import com.google.glass.logging.FormattingLogger;
import com.google.glass.logging.FormattingLoggers;
import com.google.glass.logging.Log;
import com.google.glass.voice.VoiceCommand;
import com.google.glass.voice.VoiceConfig;
import com.google.glass.voice.VoiceInputHelper;
import com.google.glass.voice.VoiceListener;

public class MyVoiceListener implements VoiceListener {
    protected final VoiceConfig voiceConfig;
    private VoiceInputHelper mVoiceInputHelper;
    private VoiceConfig mVoiceConfig;

    public MyVoiceListener(VoiceConfig voiceConfig) {
        this.voiceConfig = voiceConfig;
        Log.i("VoiceActivity", "Recognized text: not yet");

    }


    @Override
    public VoiceConfig onVoiceCommand(VoiceCommand vc) {
        String recognizedStr = vc.getLiteral();
        Log.i("VoiceActivity", "Recognized text: "+recognizedStr);

        return null;
    }

    @Override
    public FormattingLogger getLogger() {
        return FormattingLoggers.getContextLogger();
    }

    @Override
    public boolean isRunning() {
        return true;
    }

    @Override
    public boolean onResampledAudioData(byte[] arg0, int arg1, int arg2) {
        return false;
    }

    public boolean onVoiceAmplitudeChanged(double arg0) {
        return false;
    }

    @Override
    public void onVoiceConfigChanged(VoiceConfig arg0, boolean arg1) {

    }
}