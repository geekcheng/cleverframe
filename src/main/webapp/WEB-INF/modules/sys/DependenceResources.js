/**
 * 页面Js对象定义
 */
var pageJs = function (globalPath) {
    // 当前pageJs对象
    var _this = this;
    // 根据字典类别查询字典地址
    var findDictTypeUrl = globalPath.mvcPath + "/core/dict/findDictByType.json?dictType=";
    // 分页查询系统资源
    var findByPageUrl = globalPath.mvcPath + "/sys/resources/findByPage.json";
    // 查询一个页面资源的所有依赖资源(不需要分页)
    var findDependenceResourcesUrl = globalPath.mvcPath + "/sys/resources/findDependenceResources.json";
    // 为页面资源增加一个依赖资源
    var addDependenceResourcesUrl = globalPath.mvcPath + "/sys/resources/addDependenceResources.json";
    // 为页面资源删除一个依赖资源
    var deleteDependenceResourcesUrl = globalPath.mvcPath + "/sys/resources/deleteDependenceResources.json";
    // 查询资源依赖树(查询系统所有资源:只分两级，页面资源和后台资源)
    var findResourcesTreeUrl = globalPath.mvcPath + "/sys/resources/findResourcesTree.json";

    // 资源类型 枚举
    var resourcesTypeArray = null;
    // 查询表单
    var searchForm = $("#searchForm");
    // 查询表单 - 资源类型
    var searchResourcesType = $("#searchResourcesType");
    // 资源标题 - 资源类型
    var searchTitle = $("#searchTitle");
    // 资源标题 - 资源类型
    var searchResourcesUrl = $("#searchResourcesUrl");
    // 权限标识 - 资源类型
    var searchPermission = $("#searchPermission");

    // 页面资源表格
    var dataTable_1 = $("#dataTable_1");
    // 页面资源表格 - 查询
    var dataTableButtonsSearch_1 = $("#dataTableButtonsSearch_1");

    // 依赖资源表格
    var dataTable_2 = $("#dataTable_2");
    // 依赖资源表格 - 刷新
    var dataTableButtonsReload_2 = $("#dataTableButtonsReload_2");
    // 依赖资源表格 - 新增依赖
    var dataTableButtonsAdd_2 = $("#dataTableButtonsAdd_2");
    // 依赖资源表格 - 删除依赖
    var dataTableButtonsDel_2 = $("#dataTableButtonsDel_2");
    // 选中页面资源
    var selectPageResourcesText = $("#selectPageResourcesText");
    // 选中页面资源
    var selectPageResources = null;

    // 新增表单
    var addForm = $("#addForm");
    // 新增表单 - 资源ID
    var addResourcesId = $("#addResourcesId");
    // 新增表单 - 资源标题
    var addTitle = $("#addTitle");
    // 新增表单 - 资源URL
    var addResourcesUrl = $("#addResourcesUrl");
    // 新增表单 - 依赖资源
    var addDependenceResourcesId = $("#addDependenceResourcesId");

    // 新增对话框
    var addDialog = $("#addDialog");
    // 新增对话框 - 保存
    var addDialogButtonsSave = $("#addDialogButtonsSave");
    // 新增对话框 - 取消
    var addDialogButtonsCancel = $("#addDialogButtonsCancel");

    /**
     * 页面初始化方法
     */
    this.init = function () {
        _this.addDialogInit();
        _this.dataBind();
        _this.eventBind();
    };

    /**
     * 页面数据初始化
     */
    this.dataBind = function () {
        //noinspection JSUnusedLocalSymbols
        dataTable_1.datagrid({
            url: findByPageUrl,
            idField: 'id',
            fit: true,
            fitColumns: false,
            striped: true,
            rownumbers: true,
            singleSelect: true,
            nowrap: true,
            pagination: true,
            loadMsg: "正在加载，请稍候...",
            toolbar: "#dataTableButtons_1",
            pageSize: 20,
            pageList: [10, 20, 30, 50, 100, 150],
            onSelect: function (index, row) {
                _this.setDependenceResources(row);
            },
            onBeforeLoad: function (param) {
                // 增加查询参数
                var paramArray = searchForm.serializeArray();
                $(paramArray).each(function () {
                    if (param[this.name]) {
                        if ($.isArray(param[this.name])) {
                            param[this.name].push(this.value);
                        } else {
                            param[this.name] = [param[this.name], this.value];
                        }
                    } else {
                        param[this.name] = this.value;
                    }
                });
            }
        });

        //noinspection JSUnusedLocalSymbols
        dataTable_2.datagrid({
            idField: 'id',
            fit: true,
            fitColumns: false,
            striped: true,
            rownumbers: true,
            singleSelect: true,
            nowrap: true,
            pagination: false,
            loadMsg: "正在加载，请稍候...",
            toolbar: "#dataTableButtons_2",
            pageSize: 30,
            pageList: [10, 20, 30, 50, 100, 150]
        });

        $.ajax({
            type: "POST", dataType: "JSON", data: {}, async: false,
            url: findDictTypeUrl + encodeURIComponent("资源类型") + "&hasSelectAll=false",
            success: function (data) {
                resourcesTypeArray = data;
            }
        });

    };

    /**
     * 界面事件绑定方法
     */
    this.eventBind = function () {
        // 数据显示表格 查询
        dataTableButtonsSearch_1.click(function () {
            dataTable_1.datagrid('load');
        });

        // 刷新
        dataTableButtonsReload_2.click(function () {
            if (selectPageResources != null) {
                _this.setDependenceResources(selectPageResources);
            }
        });

        // 新增依赖
        dataTableButtonsAdd_2.click(function () {
            if (selectPageResources == null) {
                $.messager.alert("提示", "请选择要增加依赖的页面资源！", "info");
                return;
            }
            addDialog.dialog("open");
            addResourcesId.val(selectPageResources.id);
            addTitle.textbox("setValue", selectPageResources.title);
            addResourcesUrl.textbox("setValue", selectPageResources.resourcesUrl);
        });

        // 删除依赖
        dataTableButtonsDel_2.click(function () {
            _this.deleteDependenceResources();
        });

        // 保存
        addDialogButtonsSave.click(function () {
            _this.addDependenceResources();
        });

        // 取消
        addDialogButtonsCancel.click(function () {
            addDialog.dialog("close");
            addForm.form('reset');
        });
    };

    // ---------------------------------------------------------------------------------------------------------
    // 查询依赖资源
    this.setDependenceResources = function (resources) {
        selectPageResources = resources;
        selectPageResourcesText.text(resources.title + "[" + resources.resourcesUrl + "]");
        dataTable_2.datagrid("loading");
        //noinspection JSUnusedLocalSymbols
        $.ajax({
            type: "POST", dataType: "JSON", data: {"id": resources.id}, async: true, url: findDependenceResourcesUrl,
            success: function (data) {
                if (data.success) {
                    dataTable_2.datagrid("loadData", data.result);
                }
            },
            complete: function (xhr, ts) {
                dataTable_2.datagrid("loaded");
            }
        });
    };

    // 删除依赖
    this.deleteDependenceResources = function () {
        var row = dataTable_2.datagrid('getSelected');
        if (row == null || selectPageResources == null) {
            $.messager.alert("提示", "请选择要删除的依赖资源！", "info");
            return;
        }
        $.messager.confirm("确认删除", "您确定删除资源依赖关系?<br/>页面资源:" + selectPageResources.title + "<br/>依赖资源:" + row.title, function (r) {
            if (r) {
                $.post(deleteDependenceResourcesUrl, {"resourcesId": selectPageResources.id, "dependenceResourcesId": row.id}, function (data) {
                    if (data.success) {
                        // 删除成功
                        $.messager.show({title: '提示', msg: data.successMessage, timeout: 5000, showType: 'slide'});
                        _this.setDependenceResources(selectPageResources);
                    } else {
                        // 删除失败
                    }
                }, "json");
            }
        });
    };

    // 新增对话框 初始化
    this.addDialogInit = function () {
        addDialog.dialog({
            title: "新增资源依赖关系",
            closed: true,
            minimizable: false,
            maximizable: false,
            resizable: false,
            // minWidth: 830,
            // minHeight: 330,
            modal: true,
            buttons: "#addDialogButtons"
        });

        addTitle.textbox({
            required: true,
            editable: false
        });
        addResourcesUrl.textbox({
            required: true,
            editable: false
        });
        addDependenceResourcesId.combogrid({
            // editable: false,
            required: true,
            panelWidth: 500,
            loadMsg: "正在加载，请稍候...",
            // value: '',
            idField: 'id',
            textField: 'title',
            mode: "remote",
            url: findByPageUrl,
            columns: [[
                {field: 'id', title: '编号', width: 60, hidden: true},
                {field: 'title', title: '资源标题', width: 100},
                {field: 'resourcesUrl', title: '资源URL', width: 120},
                {field: 'permission', title: '权限标识', width: 120},
                {field: 'resourcesType', title: '资源类型', width: 120},
                {field: 'description', title: '资源说明', width: 120, hidden: true}
            ]],
            onHidePanel: function () {
                var value = addDependenceResourcesId.combogrid("getValue");

                console.log(value);
            },
            onBeforeLoad: function (param) {
                // 资源类型（1:Web页面URL地址, 2:后台请求URL地址, 3:Web页面UI资源）
                param.resourcesType = "";
                param.title = param.q;
                param.resourcesUrl = "";
                param.permission = "";
            }
        });
    };

    // 增加资源依赖关系
    this.addDependenceResources = function () {
        addForm.form("submit", {
            url: addDependenceResourcesUrl,
            success: function (data) {
                data = $.parseJSON(data);
                if (data.success) {
                    // 保存成功
                    addDialog.dialog('close');
                    $.messager.show({title: '提示', msg: data.successMessage, timeout: 5000, showType: 'slide'});
                    _this.setDependenceResources(selectPageResources);
                } else {
                    // 保存失败
                }
            }
        });
    };

    // 格式化
    //noinspection JSUnusedGlobalSymbols,JSUnusedLocalSymbols
    this.resourcesTypeFormatter = function (value, rowData, rowIndex) {
        var result = "";
        $(resourcesTypeArray).each(function (index, data) {
            if (data.value == value) {
                result = data.text;
                return false;
            }
        });
        return result == "" ? value : result;
    };
};

/**
 * 页面Js对象
 */
var pageJsObject = null;
/**
 * 页面Js执行入口
 */
$(document).ready(function () {
    if (typeof(globalPath) == "undefined") {
        alert("系统全局路径对象未定义(globalPath)");
    } else {
        pageJsObject = new pageJs(globalPath);
        pageJsObject.init();
    }
});