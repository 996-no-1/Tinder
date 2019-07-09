package voicekit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

/**
 * Voicekit Recorder implementation
 * 
 * @author DesmondCobb
 * @version 20190709.1101
 *
 */
public class Recorder {

	private boolean isRunning = false;
	private TargetDataLine targetDataLine = null;

	/**
	 * Get Recorder output file format
	 * 
	 * @return
	 */
	public AudioFormat getAudioFormat() {
		float sampleRate = 16000F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	/**
	 * Start recording
	 * 
	 * @param filePath
	 * @return status
	 */
	public boolean start(String filePath) {
		boolean status = false;
		if (isRunning) {
			// Is running, don't do anything
		} else {
			captureVoice(filePath);
			isRunning = true;
			status = true;
		}
		return status;
	}

	/**
	 * Stop recording
	 * 
	 * @return status
	 */
	public boolean stop() {
		boolean status = false;
		if (!isRunning) {
			// Not running, don't do anything
		} else {
			targetDataLine.stop();
			targetDataLine.close();
			isRunning = false;
			status = true;
		}
		return status;
	}

	/**
	 * Start voice capturing thread
	 * 
	 * @param filePath
	 */
	private void captureVoice(String filePath) {
		try {
			AudioFormat audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
			RecordingThread rt = new RecordingThread(filePath, targetDataLine, audioFormat);
			// Run in background
			Thread recordThread = new Thread(rt);
			recordThread.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

}
