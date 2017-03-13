package com.wrike.wtalk.caller;

import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.DisconnectCause;
import android.telecom.InCallService;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telecom.VideoProfile;
import android.util.Log;

import java.util.List;

public class CallConnectionService extends ConnectionService {

    @Override
    public Connection onCreateIncomingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        Log.d("Connection service", "onCreateIncomingConnection");
        String from = "It's me";
        if (request.getExtras().containsKey(MainActivity.extra_from)) {
            from = request.getExtras().getString(MainActivity.extra_from);
        }
        CallConnection callConnection = new CallConnection();
        callConnection.setCallerDisplayName(from, TelecomManager.PRESENTATION_ALLOWED);
        return callConnection;
    }

    @Override
    public Connection onCreateOutgoingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        Log.d("Connection service", "onCreateOutgoingConnection");
        return new CallConnection();
    }

    public static class CallConnection extends Connection {
        /**
         * Notifies this Connection that the {@link #getCallAudioState()} property has a new value.
         *
         * @param state The new connection audio state.
         */
        @Override
        public void onCallAudioStateChanged(CallAudioState state) {
            Log.d("Connection", "onCallAudioStateChanged " + state);
        }

        /**
         * Notifies this Connection of an internal state change. This method is called after the
         * state is changed.
         *
         * @param state The new state, one of the {@code STATE_*} constants.
         */
        @Override
        public void onStateChanged(int state) {
            Log.d("Connection", "onStateChanged " + stateToString(state));
            switch (state) {
                case STATE_INITIALIZING:
                    break;
                case STATE_NEW:
                    break;
                case STATE_RINGING:
                    break;
                case STATE_DIALING:
                    break;
                case STATE_PULLING_CALL:
                    break;
                case STATE_ACTIVE:
                    break;
                case STATE_HOLDING:
                    break;
                case STATE_DISCONNECTED:
                    destroy();
                    break;
                default:
                    break;
            }
        }

        /**
         * Notifies this Connection of a request to play a DTMF tone.
         *
         * @param c A DTMF character.
         */
        @Override
        public void onPlayDtmfTone(char c) {
            Log.d("Connection", "onPlayDtmfTone " + c);
        }

        /**
         * Notifies this Connection of a request to stop any currently playing DTMF tones.
         */
        @Override
        public void onStopDtmfTone() {
            Log.d("Connection", "onStopDtmfTone");
        }

        /**
         * Notifies this Connection of a request to disconnect.
         */
        @Override
        public void onDisconnect() {
            Log.d("Connection", "onDisconnect");
            setDisconnected(new DisconnectCause(DisconnectCause.LOCAL));
        }

        /**
         * Notifies this Connection of a request to disconnect a participant of the conference managed
         * by the connection.
         *
         * @param endpoint the {@link Uri} of the participant to disconnect.
         * @hide
         */
        public void onDisconnectConferenceParticipant(Uri endpoint) {
            Log.d("Connection", "onDisconnectConferenceParticipant " + endpoint);
        }

        /**
         * Notifies this Connection of a request to separate from its parent conference.
         */
        @Override
        public void onSeparate() {
            Log.d("Connection", "onSeparate");
        }

        /**
         * Notifies this Connection of a request to abort.
         */
        @Override
        public void onAbort() {
            Log.d("Connection", "onAbort");
        }

        /**
         * Notifies this Connection of a request to hold.
         */
        @Override
        public void onHold() {
            Log.d("Connection", "onHold");
        }

        /**
         * Notifies this Connection of a request to exit a hold state.
         */
        @Override
        public void onUnhold() {
            Log.d("Connection", "onUnhold");
        }

        /**
         * Notifies this Connection, which is in {@link #STATE_RINGING}, of
         * a request to accept.
         *
         * @param videoState The video state in which to answer the connection.
         */
        @Override
        public void onAnswer(int videoState) {
            Log.d("Connection service", "onAnswer " + videoState);
            setActive();
        }

        /**
         * Notifies this Connection, which is in {@link #STATE_RINGING}, of
         * a request to accept.
         */
        @Override
        public void onAnswer() {
            onAnswer(VideoProfile.STATE_AUDIO_ONLY);
        }

        /**
         * Notifies this Connection, which is in {@link #STATE_RINGING}, of
         * a request to reject.
         */
        @Override
        public void onReject() {
            Log.d("Connection", "onReject");
            setDisconnected(new DisconnectCause(DisconnectCause.REJECTED));
        }

        /**
         * Notifies this Connection, which is in {@link #STATE_RINGING}, of
         * a request to reject with a message.
         */
        @Override
        public void onReject(String replyMessage) {
            Log.d("Connection", "onReject " + replyMessage);
            setDisconnected(new DisconnectCause(DisconnectCause.REJECTED, replyMessage));
        }

        /**
         * Notifies the Connection of a request to silence the ringer.
         *
         * @hide
         */
        public void onSilence() {
            Log.d("Connection", "onSilence");
        }

        /**
         * Notifies this Connection whether the user wishes to proceed with the post-dial DTMF codes.
         */
        @Override
        public void onPostDialContinue(boolean proceed) {
            Log.d("Connection", "onPostDialContinue " + proceed);
        }

        /**
         * Notifies this Connection of a request to pull an external call to the local device.
         * <p>
         * The {@link InCallService} issues a request to pull an external call to the local device via
         * {@link Call#pullExternalCall()}.
         * <p>
         * For a Connection to be pulled, both the {@link Connection#CAPABILITY_CAN_PULL_CALL}
         * capability and {@link Connection#PROPERTY_IS_EXTERNAL_CALL} property bits must be set.
         * <p>
         * For more information on external calls, see {@link Connection#PROPERTY_IS_EXTERNAL_CALL}.
         */
        @Override
        public void onPullExternalCall() {
            Log.d("Connection", "onPullExternalCall");
        }

        /**
         * Notifies this Connection of a {@link Call} event initiated from an {@link InCallService}.
         * <p>
         * The {@link InCallService} issues a Call event via {@link Call#sendCallEvent(String, Bundle)}.
         * <p>
         * Where possible, the Connection should make an attempt to handle {@link Call} events which
         * are part of the {@code android.telecom.*} namespace.  The Connection should ignore any events
         * it does not wish to handle.  Unexpected events should be handled gracefully, as it is
         * possible that a {@link InCallService} has defined its own Call events which a Connection is
         * not aware of.
         * <p>
         * See also {@link Call#sendCallEvent(String, Bundle)}.
         *
         * @param event The call event.
         * @param extras Extras associated with the call event.
         */
        @Override
        public void onCallEvent(String event, Bundle extras) {
            Log.d("Connection", "onCallEvent " + event + " + " + extras);
        }

        /**
         * Notifies this {@link Connection} of a change to the extras made outside the
         * {@link ConnectionService}.
         * <p>
         * These extras changes can originate from Telecom itself, or from an {@link InCallService} via
         * the {@link android.telecom.Call#putExtras(Bundle)} and
         * {@link Call#removeExtras(List)}.
         *
         * @param extras The new extras bundle.
         */
        @Override
        public void onExtrasChanged(Bundle extras) {
            Log.d("Connection", "onExtrasChanged "+ extras);
        }
    }
}
