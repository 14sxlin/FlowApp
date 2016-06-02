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
		// TODO Auto-generated constructor stub
		musicList=new ArrayList<String>();
		hashMap=new HashMap<String, File>();
		getMusic(path);
	}
	
@Deprecated
	public void getMusicFile(int number) throws UnsupportedEncodingException
	{
		musicList=new ArrayList<String>();
		String temp;
		for(int i=0;i<number;i++)
		{
			temp=decode(ResourcePath.JARPATH)+i+".wav";
			musicList.add(temp);
		}
		strMusicList=new String[musicList.size()];
		musicList.toArray(strMusicList);
	}
	public void getMusic(String jarPath) throws UnsupportedEncodingException
	{	
		try {
			File[] files=null;
			File f=new File(decode(jarPath));
			if(f.isDirectory())
				files=f.listFiles(new WavFileFilter());
			for(File fi:files)
			{
				String path=fi.getAbsolutePath();
				String name=path.substring(path.lastIndexOf("\\")+1);
				musicList.add(name);
				hashMap.put(name, fi);
			}
		
				strMusicList=new String[musicList.size()];//String[] a=new String[0];������ʽ�ǲ��ᱨ��� �����һ���ռ�
																			//��ͼ�����Ԫ�صĻ��ᱨ�±�Խ��
				musicList.toArray(strMusicList);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
//System.out.println("û�������ļ�");
		}			
	}
//	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		GetMusicFile m=new GetMusicFile();
//		new PlayMusic(m.musicList.get(0),false).play();
//	}

	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return URLDecoder.decode(path, "utf-8");
	}

}
