package com.selflearning.englishcourses.controller.web.admin.template;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/template/forms")
public class FormController {

	@GetMapping("/form-basic")
	public String formBasic(Model model) {
		model.addAttribute("formBasic", true);
		return "admin/template/forms/form-basic";
	}

	@GetMapping("/form-advanced")
	public String formAdvanced(Model model) {
		model.addAttribute("formAdvanced", true);
		return "admin/template/forms/form-advanced";
	}

	@GetMapping("/form-input-masks")
	public String formInputMasks(Model model) {
		model.addAttribute("formInputMasks", true);
		return "admin/template/forms/form-input-masks";
	}

	@GetMapping("/form-validation")
	public String formValidation(Model model) {
		model.addAttribute("formValidation", true);
		return "admin/template/forms/form-validation";
	}

	@GetMapping("/text-editors")
	public String textEditors(Model model) {
		model.addAttribute("textEditors", true);
		return "admin/template/forms/text-editors";
	}

	@ModelAttribute("forms")
	public boolean getActive() {
		return true;
	}

}
