package org.thoughtcrime.securesms.sound;

import android.app.Application;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.thoughtcrime.securesms.components.voice.VoiceNotePlaybackController;
import org.thoughtcrime.securesms.components.voice.VoiceNotePlaybackParameters;
import org.thoughtcrime.securesms.components.voice.VoiceNotePlaybackService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, application = Application.class)
public class SoundControlTest {
  private AudioAttributes             mediaAudioAttributes;
  private AudioAttributes             callAudioAttributes;
  private SimpleExoPlayer             player;
  private VoiceNotePlaybackController testSubject;

  @Before
  public void setup() {
    MediaSessionCompat mediaSessionCompat = Mockito.mock(MediaSessionCompat.class);
    VoiceNotePlaybackParameters playbackParameters = new VoiceNotePlaybackParameters(mediaSessionCompat);
    mediaAudioAttributes = new AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_MUSIC).setUsage(C.USAGE_MEDIA).build();
    callAudioAttributes = new AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_SPEECH).setUsage(C.USAGE_VOICE_COMMUNICATION).build();
    player = Mockito.mock(SimpleExoPlayer.class);
    testSubject = new VoiceNotePlaybackController(player, playbackParameters);
  }

  @Test
  public void testGivenStreamIsMedia_whenOnCommandForVoice_thenExpectStreamToSwitchToVoiceAndContinuePlayback() {
    // GIVEN
    when(player.getAudioAttributes()).thenReturn(mediaAudioAttributes);

    String command = VoiceNotePlaybackService.ACTION_SET_AUDIO_STREAM;
    Bundle extras = new Bundle();
    extras.putInt(VoiceNotePlaybackService.ACTION_SET_AUDIO_STREAM, AudioManager.STREAM_VOICE_CALL);
    AudioAttributes expected = callAudioAttributes;

    // WHEN
    testSubject.onCommand(player, command, extras, null);

    // THEN
    verify(player).setPlayWhenReady(false);
    verify(player).setAudioAttributes(expected, false);
    verify(player).setPlayWhenReady(true);
  }

  @Test
  public void testGivenStreamIsVoice_whenOnCommandForMedia_thenExpectStreamToSwitchToMediaAndPausePlayback() {
    // GIVEN
    when(player.getAudioAttributes()).thenReturn(callAudioAttributes);

    String command = VoiceNotePlaybackService.ACTION_SET_AUDIO_STREAM;
    Bundle extras = new Bundle();
    extras.putInt(VoiceNotePlaybackService.ACTION_SET_AUDIO_STREAM, AudioManager.STREAM_MUSIC);
    AudioAttributes expected = mediaAudioAttributes;

    // WHEN
    testSubject.onCommand(player, command, extras, null);

    // THEN
    verify(player).setPlayWhenReady(false);
    verify(player).setAudioAttributes(expected, true);
    verify(player, never()).setPlayWhenReady(true);
  }

  @Test
  public void givenStreamIsVoice_whenIOnCommandForVoice_thenIExpectNoChange() {
    // GIVEN
    when(player.getAudioAttributes()).thenReturn(callAudioAttributes);

    String command = VoiceNotePlaybackService.ACTION_SET_AUDIO_STREAM;
    Bundle extras = new Bundle();
    extras.putInt(VoiceNotePlaybackService.ACTION_SET_AUDIO_STREAM, AudioManager.STREAM_VOICE_CALL);

    // WHEN
    testSubject.onCommand(player, command, extras, null);

    // THEN
    verify(player, never()).setPlayWhenReady(anyBoolean());
    verify(player, never()).setAudioAttributes(any(), eq(false));
  }

  @Test
  public void givenStreamIsMedia_whenIOnCommandForMedia_thenIExpectNoChange() {
    // GIVEN
    when(player.getAudioAttributes()).thenReturn(mediaAudioAttributes);

    String command = VoiceNotePlaybackService.ACTION_SET_AUDIO_STREAM;
    Bundle extras = new Bundle();
    extras.putInt(VoiceNotePlaybackService.ACTION_SET_AUDIO_STREAM, AudioManager.STREAM_MUSIC);

    // WHEN
    testSubject.onCommand(player, command, extras, null);

    // THEN
    verify(player, never()).setPlayWhenReady(anyBoolean());
    verify(player, never()).setAudioAttributes(any(), anyBoolean());
  }

}
