package com.poorknight.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;


@Data
@SessionScoped
public class LatestSearch implements Serializable {

	private static final long serialVersionUID = -2834922932694653938L;

	@Setter(value = AccessLevel.PACKAGE)
	private String latestSearch;
}
