package com.selflearning.englishcourses.controller.web.admin.template;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/template/basic-ui")
public class BasicUIController {

	@GetMapping("/colors")
	public String colors(Model model) {
		model.addAttribute("colors", true);
		return "admin/template/basic-ui/colors";
	}

	@GetMapping("/typography")
	public String typography(Model model) {
		model.addAttribute("typography", true);
		return "admin/template/basic-ui/typography";
	}
	
	@GetMapping("/panels")
	public String panels(Model model) {
		model.addAttribute("panels", true);
		return "admin/template/basic-ui/panels";
	}
	
	@GetMapping("/buttons")
	public String buttons(Model model) {
		model.addAttribute("buttons", true);
		return "admin/template/basic-ui/buttons";
	}
	
	@GetMapping("/tabs")
	public String tabs(Model model) {
		model.addAttribute("tabs", true);
		return "admin/template/basic-ui/tabs";
	}
	
	@GetMapping("/alerts-tooltips")
	public String alertsTooltips(Model model) {
		model.addAttribute("alertsTooltips", true);
		return "admin/template/basic-ui/alerts-tooltips";
	}
	
	@GetMapping("/badges-progress")
	public String badgesProgress(Model model) {
		model.addAttribute("badgesProgress", true);
		return "admin/template/basic-ui/badges-progress";
	}
	
	@GetMapping("/lists")
	public String list(Model model) {
		model.addAttribute("lists", true);
		return "admin/template/basic-ui/lists";
	}
	
	@GetMapping("/cards")
	public String card(Model model) {
		model.addAttribute("cards", true);
		return "admin/template/basic-ui/cards";
	}

	@ModelAttribute("basicUI")
	public boolean collapseIn() {
		return true;
	}
}
