<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2017/7/2
  Time: 13:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>员工列表</title>

    <%
        pageContext.setAttribute("APP_PATH", request.getContextPath());
    %>

    <%--
         web路径
         不以/开始的相对路径，找资源，以当前资源的路径为基准，经常容易出问题
         以/开始的相对路径，找资源，以服务器的路径为标准
         http://localhost:8080/ssm-crud
    --%>
    <script type="text/javascript" src="${APP_PATH}/static/js/jquery-3.2.1/jquery-3.2.1.min.js"></script>
    <link href="${APP_PATH}/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="${APP_PATH}/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
</head>
<body>
<%--搭建显示页面--%>
<div class="container">
    <%--标题--%>
    <div class="row">
        <div class="col-md-12">
            <h1>SSM-CRUD</h1>
        </div>
    </div>
    <%--按钮--%>
    <div class="row">
        <div class="col-md-4 col-md-offset-8">
            <button class="btn btn-primary">新增</button>
            <button class="btn btn-danger">删除</button>
        </div>
    </div>
    <%--表格数据--%>
    <div class="row">
        <div class="col-md-12">
            <table class="table table-hover" id="emps_table">
                <thead>
                <tr>
                    <th>#</th>
                    <th>empName</th>
                    <th>gender</th>
                    <th>email</th>
                    <th>deptName</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
        </div>
    </div>
    <%--分页--%>
    <div class="row">
        <%--分页文字信息--%>
        <div class="col-md-6" id="page_info_area">
        </div>
        <%--分页条信息--%>
        <div class="col-md-6" id="page_nav_area">
            <nav aria-label="Page navigation">
                <ul class="pagination">


                </ul>
            </nav>
        </div>
    </div>
</div>
<script type="text/javascript">
    //页面加载完成以后，直接去发送一个ajax请求，要到分页数据

    $(function () {
        to_page(pn);
    })

    function to_page(pn) {
        $.ajax({
            url: "${APP_PATH}/emps", <!--请求的url地址-->
            data: "pn=" + pn, <!--请求要带的数据-->
            type: "GET", <!--请求的类型-->
            success: function (result) {     <!--请求成功的回调函数-->
                //解析 显示员工数据
                build_emps_table(result);
                //解析 显示分页信息
                build_page_info(result);
                //解析 显示分页条数据
                build_page_nav(result);
            }

        })
    }
    $(function () {
        $.ajax({
            url: "${APP_PATH}/emps", <!--请求的url地址-->
            data: "pn=1", <!--请求要带的数据-->
            type: "GET", <!--请求的类型-->
            success: function (result) {     <!--请求成功的回调函数-->
                //解析 显示员工数据
                build_emps_table(result);
                //解析 显示分页信息
                build_page_info(result);
                //解析 显示分页条数据
                build_page_nav(result);
            }

        })
    });
    function build_emps_table(result) {
        //清空table表格
        $("#emps_table tbody").empty();
        var emps = result.extend.pageInfo.list;
        $.each(emps, function (index, item) {  //要遍历的元素，每次遍历的回调函数
            var empIdTd = $("<td></td>").append(item.empId);
            var empNameTd = $("<td></td>").append(item.empName);
            var genderTd = $("<td></td>").append(item.gender == 'M' ? "男" : "女");
            var emailTd = $("<td></td>").append(item.email);
            var deptName = $("<td></td>").append(item.department.deptName);
            var editBtn = $("<button></button>").addClass("btn btn-info btn-sm").append($("<span></span>").addClass("glyphicon glyphicon-pencil")).append("编辑")
            var delBtn = $("<button></button>").addClass("btn btn-danger btn-sm").append($("<span></span>").addClass("glyphicon glyphicon-trash")).append("删除")
            var btnTd = $("<td></td>").append(editBtn).append(" ").append(delBtn);
            //append方法执行完成以后还是返回原来的元素
            $("<tr></tr>").append(empIdTd)
                .append(empNameTd)
                .append(genderTd)
                .append(emailTd)
                .append(deptName)
                .append(btnTd)
                .appendTo("#emps_table tbody");
        })

    }

    //解析显示分页信息
    function build_page_info(result) {
        $("#page_info_area").empty();
        $("#page_info_area").append("当前" + result.extend.pageInfo.pageNum + "页，" +
            "总" + result.extend.pageInfo.pages + "页数，" +
            "总" + result.extend.pageInfo.total + "条记录");
    }
    //解析显示分页条
    function build_page_nav(result) {
        //page_nav_area
        $("#page_nav_area").empty();
        var ul = $("<ul></ul>").addClass("pagination")

        var firstPageLi = $("<li></li>").append($("<a></a>").append("首页").attr("href", "#"));
        var prePageLi = $("<li></li>").append($("<a></a>").append("&laquo;"));
        if (result.extend.pageInfo.hasPreviousPage == false) {
            firstPageLi.addClass("disabled");
            prePageLi.addClass("disabled");
        }else {
            //为元素添加点击翻页事件
            firstPageLi.click(function () {
                to_page(1);
            })
            prePageLi.click(function () {
                to_page(result.extend.pageInfo.pageNum - 1);
            })
        }

        var nextPageLi = $("<li></li>").append($("<a></a>").append("&raquo;"));
        var lastPageLi = $("<li></li>").append($("<a></a>").append("末页").attr("href", "#"));
        if (result.extend.pageInfo.hasNextPage == false) {
            nextPageLi.addClass("disabled");
            lastPageLi.addClass("disabled");
        }else {
            nextPageLi.click(function () {
                to_page(result.extend.pageInfo.pageNum + 1);
            })
            lastPageLi.click(function () {
                to_page(result.extend.pageInfo.pages);
            })
        }

        //添加首页和前一页的提示
        ul.append(firstPageLi).append(prePageLi)

        $.each(result.extend.pageInfo.navigatepageNums, function (index, item) {
            //遍历，添加页码提示
            var numLi = $("<li></li>").append($("<a></a>").append(item));
            if (result.extend.pageInfo.pageNum == item) {
                numLi.addClass("active");
            }
            numLi.click(function () {
                to_page(item);
            })
            ul.append(numLi)
        })

        //添加下一页和末页提示
        ul.append(nextPageLi).append(lastPageLi);

        //把ul加入到nav
        var navEle = $("<nav></nav>").append(ul);
        navEle.appendTo("#page_nav_area");
    }

</script>
</body>
</html>
