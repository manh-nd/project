package com.selflearning.englishcourses.controller.web.admin.template;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/template/tables")
public class TableController {

	@GetMapping("/table-basic")
	public String basicTables() {
		return "admin/template/tables/table-basic";
	}

	@GetMapping("/datatables")
	public String dataTables() {
		return "admin/template/tables/datatables";
	}

	@ModelAttribute("tables")
	public boolean collapseIn() {
		return true;
	}

}
