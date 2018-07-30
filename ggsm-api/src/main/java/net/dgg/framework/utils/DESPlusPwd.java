package net.dgg.framework.utils;

import net.dgg.framework.PTConst;
import net.dgg.framework.utils.security.DESPlus;

public class DESPlusPwd {
	public static void main(String[] args){
		System.out.println(new DESPlus(PTConst.PWD_KEY).encrypt("123456"));
		System.out.println(new DESPlus(PTConst.PWD_KEY).decrypt("28ff71b89ddadd1079fb76a84eb6d7aa"));
		
	}

}
