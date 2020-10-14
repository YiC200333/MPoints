package me.yic.mpoints.utils;

import me.yic.mpoints.data.DataFormat;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerPoints {
	private final UUID uid;
	private Map<String, BigDecimal> points = new ConcurrentHashMap<>();

	public PlayerPoints(UUID uid, String sign, BigDecimal value) {
		this.uid = uid;
		this.points.put(sign,value);
	}

	public UUID getUUID() {
		return uid;
	}

	public BigDecimal getpoints(String sign) {
		return points.get(sign);
	}

	public boolean containsKey(String sign) {
		return points.containsKey(sign);
	}

	public void insert(final String sign, BigDecimal value) {
		if (value != null) {
			points.put(sign,value);
		}
	}
}
