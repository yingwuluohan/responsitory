package com.common.utils.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * 网络工具
 * <P>File name : NetUtil.java </P>
 */
@SuppressWarnings("rawtypes")
public class NetUtil {

	/**
	 * 是否为windows或Linux
	 * NetUtil.isLinux()<BR>
	 * @return
	 */
	public static boolean isLinux() {
		String osName="";
		boolean isLinux = false;
		try{
			osName = System.getProperty("os.name").toLowerCase();
		} catch (Error error) {
			error.printStackTrace();
		}
		if (osName.indexOf("win") != -1) {
			isLinux = false;
		} else {
			isLinux = true;
		}
		return isLinux;
	}
	
	/**
	 * 拿到本机的IP地址
	 * NetUtil.getIp()<BR>
	 * <P>Author : zouzhihua </P>  
	 * <P>Date : 2013-4-7 </P>
	 * @return
	 */
	public static String getIp(){
		String ipAddres = "";
		if (isLinux()) {
			//根据网卡取本机配置的IP
			Enumeration netInterfaces = null;
			try {
				netInterfaces = NetworkInterface.getNetworkInterfaces();
			} catch (SocketException e) {
				e.printStackTrace();
			}
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
			    NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();  
			    //System.out.println(ni.getName());    
			    ip = (InetAddress) ni.getInetAddresses().nextElement();
			    if (!ip.isSiteLocalAddress()
			            && !ip.isLoopbackAddress()
			            && ip.getHostAddress().indexOf(":") == -1) {
			        //System.out.println("Linux-本机的ip=" + ip.getHostAddress());
			        ipAddres = ip.getHostAddress();
			        break;
			    }  else {
			        ip = null;    
			    }
			}  
		} else {
			try {
				InetAddress inet = InetAddress.getLocalHost();
				//System.out.println("windows-本机的ip=" + inet.getHostAddress()); 
				ipAddres = inet.getHostAddress();
			} catch (UnknownHostException ex) {
				ex.printStackTrace();
			}
		}
		return ipAddres;
	}
	
	public static void main(String[] args) {
        System.out.println(getIp());
	}

}
