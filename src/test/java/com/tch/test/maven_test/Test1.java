package com.tch.test.maven_test;

import java.util.Arrays;

import org.glassfish.jersey.internal.util.Tokenizer;

public class Test1 {

	public static void main(String[] args) {
		String[] tokens = Tokenizer.tokenize("com.tch.test.**", Tokenizer.COMMON_DELIMITERS);
		System.out.println(Arrays.toString(tokens));
	}
	
}
