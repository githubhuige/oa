package com.huawei.oa.view.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class HomeAction extends ActionSupport {

	public String top() {

		return "top";
	}

	public String bottom() {

		return "bottom";
	}

	public String right() {

		return "right";
	}

	public String left() {

		return "left";
	}

	public String index() {

		return "index";
	}
}
