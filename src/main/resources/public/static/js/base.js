/**
 * 打开对话框
 * @param dlg   对话框节点id
 * @param title 对话框标题
 */
function openAddDialog(dlgId,title) {
    //$("#dlg").dialog("open").dialog("setTitle","机会数据添加");
    $("#"+dlgId).dialog("open").dialog("setTitle",title);
}

/**
 * 关闭对话框
 * @param dlgId 对话框节点id
 */
function closeDialog(dlgId){
    $("#"+dlgId).dialog("close");
}

/**
 * 添加或更新数据
 * @param saveUrl   添加数据地址
 * @param updateUrl 更新数据地址
 * @param dlgId     对话框id
 * @param search    多条件搜索方法名
 * @param clearDate 清除表单方法名
 */
function saveOrUpdate(saveUrl,updateUrl,dlgId,search,clearDate) {
    var url = ctx+saveUrl;
    if(!(isEmpty($("input[name='id']").val()))){
        url = ctx+updateUrl;
    }
    console.log("每次");
    $("#fm").form("submit",{
        url:url,
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        success:function (data) {
            data =JSON.parse(data);
            if(data.code==200) {
                closeDialog(dlgId)
                //重新加载数据
                search();
                clearDate();
            }else{
                $.messager.alert("来自crm",data.msg,"error");
            }
        }
    })
}

/**
 *
 * @param dataGridId    表格id
 * @param formId        表单id
 * @param dlgId         对话框id
 * @param title         对话框标题
 */
function openModifyDialog(dataGridId,formId,dlgId,title) {
    var rows=$("#"+dataGridId).datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择待修改的数据!","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","暂不支持批量修改!","error");
        return;
    }

    $("#"+formId).form("load",rows[0]);
    openAddDialog(dlgId,title);
}

/**
 * 删除用户数据
 * @param dg    表格id
 * @param url   删除地址
 * @param searchUsers   重新加载list
 */
function deleteRecode(dg,url,search) {
    var rows=$("#"+dg).datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("来自crm","请选择待删除的数据!","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("来自crm","暂不支持批量删除!","error");
        return;
    }
    $.messager.confirm("来自crm","确定删除选中的记录?",function (r) {
        if(r){
            $.ajax({
                type:"post",
                url:ctx+url,
                data:{
                    id:rows[0].id
                },
                dataType:"json",
                success:function (data) {
                    if(data.code==200){
                        search();
                    }else{
                        $.messager.alert("来自crm",data.msg,"error");
                    }
                }
            })

        }
    })

}


