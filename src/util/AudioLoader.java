package util;

import java.applet.Applet;
import java.applet.AudioClip;

/**
 * Classe qui permet de lire les fichiers audios.
 * @author Alexandre Hua
 * @version 04-05-2015
 */
public class AudioLoader {
	/**
	 * Méthode qui permet de lire le fichier audio
	 * @param path la location du fichier audio
	 * @return le clip d'audio
	 */
	public AudioClip loadAudio(String path) {
		AudioClip clip = null;
		try {
			clip = Applet.newAudioClip(getClass().getClassLoader().getResource(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clip;
	}
}
