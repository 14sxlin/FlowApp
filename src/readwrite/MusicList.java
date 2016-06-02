package readwrite;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import tool.WavFileFilter;

public class MusicList implements ResourcePath {
	public HashMap< String, File> hashMap;
	public ArrayList<String> musicList;
	public String[] strMusicList;
	public MusicList(String path) throws UnsupportedEncodingException {
		musicList=new ArrayList<String>();
		hashMap=new HashMap<String, File>();
		getMusic(path);
	}
	
	public void getMusic(String musicDirectory) throws UnsupportedEncodingException
	{	
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
				hashMap.put(name, fi);
			}
		
				strMusicList=new String[musicList.size()];
				//String[] a=new String[0];这种形式是不会报错的 会分配一个空间
				//试图输出的元素的话会报下标越界
				musicList.toArray(strMusicList);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			System.out.println("没有音乐文件");
			e.printStackTrace();
		}			
	}

	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		return URLDecoder.decode(path, "utf-8");
	}

}
