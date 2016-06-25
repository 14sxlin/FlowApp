package readwrite;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import tool.MyLogger;
import tool.WavFileFilter;

public class MusicList implements ResourcePath {
	public HashMap< String, String> fileMap;
	public ArrayList<String> musicList;
	public String[] strMusicList;
	
	public MusicList(String directory) throws UnsupportedEncodingException {
		musicList=new ArrayList<String>(9);
		fileMap=new HashMap<String, String>(9);
		getMusic(directory);
	}
	
	public void getMusic(String musicDirectory) throws UnsupportedEncodingException
	{	
		for(int i =1;i<=9;i++)
			musicList.add("ALARM"+i+".WAV");
		try {
			File[] files=null;
			File f=new File(decode(musicDirectory));
			if(f.isDirectory())
				files=f.listFiles(new WavFileFilter());
			for(File fi:files)
			{
				String path=fi.getAbsolutePath();
				String name=path.substring(path.lastIndexOf("\\")+1);
				musicList.add(name);
				fileMap.put(name, fi.getAbsolutePath());
			}
		
				strMusicList=new String[musicList.size()];
				musicList.toArray(strMusicList);
		} catch (NullPointerException e) {
			MyLogger.warn(getClass(), e.getMessage() +" 没有外部音乐文件");
			e.printStackTrace();
		}			
	}

	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		return URLDecoder.decode(path, "utf-8");
	}

}
