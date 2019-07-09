package voicekit;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;

/**
 * Voicekit Recorder background thread
 * 
 * @author DesmondCobb
 * @version 20190709.1104
 *
 */
public class RecordingThread implements Runnable {

	private File audioFile = null;
	private TargetDataLine targetDataLine = null;
	private AudioFormat audioFormat = null;

	/**
	 * Constructor
	 * 
	 * @param filePath
	 * @param targetDataLine
	 * @param audioFormat
	 */
	public RecordingThread(String filePath, TargetDataLine targetDataLine, AudioFormat audioFormat) {
		this.audioFile = new File(filePath);
		this.targetDataLine = targetDataLine;
		this.audioFormat = audioFormat;
	}

	@Override
	public void run() {
		AudioFileFormat.Type fileType = null;
		fileType = AudioFileFormat.Type.WAVE;
		try {
			targetDataLine.open(audioFormat);
			targetDataLine.start();
			AudioSystem.write(new AudioInputStream(targetDataLine), fileType, audioFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
