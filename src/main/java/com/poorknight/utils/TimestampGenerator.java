package com.poorknight.utils;

import java.sql.Timestamp;


public class TimestampGenerator {

	public Timestamp currentTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}
}
