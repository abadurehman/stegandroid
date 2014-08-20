package com.stegandroid.process;

import com.stegandroid.algorithms.AlgorithmFactory;
import com.stegandroid.algorithms.ICryptographyAlgorithm;
import com.stegandroid.algorithms.ISteganographyAlgorithm;
import com.stegandroid.configuration.Preferences;
import com.stegandroid.mp4.Mp4ChannelExtracter;
import com.stegandroid.parameters.EncodeParameters;
import com.stegandroid.tools.Utils;

public class EncodeProcess {

	public EncodeProcess() {
		
	}
	
	public boolean encode(EncodeParameters parameters) {
		byte[] contentToHide;
		boolean ret = false;
		
		if (parameters == null) {
			return false;
		}
		
		if (parameters.isUsingHideText()) {
			contentToHide = parameters.getTextToHide().getBytes();
		} else {
			contentToHide = Utils.getContentOfFileAsByteArray(parameters.getFileToHidePath());
		}
		contentToHide = processCryptography(parameters, contentToHide);
		return ret;
	}
	
	private byte[] processCryptography(EncodeParameters parameters, byte[] contentToHide) {
		ICryptographyAlgorithm algorithm;
		byte[] key;
		
		if (!Preferences.getInstance().getUseCryptography() || parameters == null || contentToHide == null) {
			return contentToHide;
		}
		key = parameters.getCryptographyKey().getBytes();
		algorithm = AlgorithmFactory.getCryptographyAlgorithmInstanceFromName(Preferences.getInstance().getCryptographyAlgorithm());
		if (algorithm != null) {
			return algorithm.encrypt(contentToHide, key);
		}
		return contentToHide;
	}
	
	private byte[] processVideoSignal(EncodeParameters parameters, byte[] contentToHide) {
		ISteganographyAlgorithm videoAlgorithm;
		Mp4ChannelExtracter extracter;
		byte[] videoSignal;
		
		extracter = new Mp4ChannelExtracter();
		videoSignal = extracter.extractH264(parameters.getSrcVideoPath());
		
		if (contentToHide != null && contentToHide.length > 0 && Preferences.getInstance().getUseVideoChannel()) {
			videoAlgorithm = AlgorithmFactory.getSteganographyAlgorithmInstanceFromName(Preferences.getInstance().getVideoAlgorithm());
			if (videoAlgorithm != null) {
				videoSignal = videoAlgorithm.encode(videoSignal, contentToHide);
			}
		}
		return videoSignal;
	}
	
	private byte[] processAudioSignal(EncodeParameters parameters, byte[] contentToHide) {
		ISteganographyAlgorithm audioAlgorithm;
		Mp4ChannelExtracter extracter;
		byte[] audioSignal;
		
		extracter = new Mp4ChannelExtracter();
		audioSignal = extracter.extractH264(parameters.getSrcVideoPath());
		
		if (contentToHide != null && contentToHide.length > 0 && Preferences.getInstance().getUseAudioChannel()) {
			audioAlgorithm = AlgorithmFactory.getSteganographyAlgorithmInstanceFromName(Preferences.getInstance().getAudioAlgorithm());
			if (audioAlgorithm != null) {
				audioSignal = audioAlgorithm.encode(audioSignal, contentToHide);
			}
		}
		return audioSignal;
	}
	
	private byte[] processMetadataSignal(EncodeParameters parameters, byte[] contentToHide) {
		ISteganographyAlgorithm metadataAlgorithm;
		Mp4ChannelExtracter extracter;
		byte[] metadataSignal;
		
		extracter = new Mp4ChannelExtracter();
		metadataSignal = extracter.extractH264(parameters.getSrcVideoPath());
		
		if (contentToHide != null && contentToHide.length > 0 && Preferences.getInstance().getUseMetadataChannel()) {
			metadataAlgorithm = AlgorithmFactory.getSteganographyAlgorithmInstanceFromName(Preferences.getInstance().getMetadataAlgorithm());
			if (metadataAlgorithm != null) {
				metadataSignal = metadataAlgorithm.encode(metadataSignal, contentToHide);
			}
		}
		return metadataSignal;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//		IAlgorithm audioAlgorithm;
//	IAlgorithm videoAlgorithm;
//	IAlgorithm metadataAlgorithm;
//	
//	// TODO: Open the video and extract the channels
//	
//	if (Configuration.getInstance().getUseAudioChannel()) {
//		audioAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getAudioAlgorithm());
//		audioAlgorithm.encode();
//	}
//	
//	if (Configuration.getInstance().getUseVideoChannel()) {
//		videoAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getVideoAlgorithm());
//		videoAlgorithm.encode();
//		
//	}
//	
//	if (Configuration.getInstance().getUseMetadataChannel()) {
//		metadataAlgorithm = AlgorithmFactory.getInstanceFromName(Configuration.getInstance().getMetadataAlgorithm());
//		metadataAlgorithm.encode();			
//	}
	
	// TODO: Save the new video
	//copyContent(pathToVideo, destinationPath);

	
//	private void copyContent(String original, String dest) {
//		FileInputStream fileInputStream;
//		FileOutputStream fileOutputStream;
//		byte [] tmp = new byte[256];
//		int readed;
//		
//		try {
//			fileInputStream = new FileInputStream(new File(original));
//			fileOutputStream = new FileOutputStream(new File(dest + "/lalala.mp4"));
//			
//			while ((readed = fileInputStream.read(tmp)) >= 0) {
//				fileOutputStream.write(tmp, 0, readed);
//			}
//			
//			fileInputStream.close();
//			fileOutputStream.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
}