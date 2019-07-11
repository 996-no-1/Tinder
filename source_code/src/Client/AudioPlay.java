package Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * 用于播放wav音频文件
 * 
 * @author Wang Zhichao
 * @version 1.0
 */
public class AudioPlay {
	/**
	 * 输入文件的地址播放相关的文件
	 * 
	 * @param filePath
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 */
	public static void playAudio(String filePath) throws IOException, UnsupportedAudioFileException {
		try {
			File file = new File(filePath);
			InputStream inputStream = new FileInputStream(file);
			AudioStream as = new AudioStream(inputStream);
			AudioPlayer.player.start(as);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		try {
			AudioPlay.playAudio("C:/Users/12284/Desktop/msn_wav/online.wav");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
}
