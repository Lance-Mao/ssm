package com.demo.controller;

import com.demo.bean.Department;
import com.demo.bean.Employee;
import com.demo.bean.Msg;
import com.demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 处理部门有关的请求
 * Created by admin on 2017/7/3.
 */
@Controller
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;




    /**
     * 返回所有的部门信息
     */
    @RequestMapping("/depts")
    @ResponseBody
    public Msg getDpets() {
        List<Department> list  = departmentService.getDepts();
        return Msg.success().add("depts", list);
    }
}
